package com.tkn.miaosha.service;

import com.tkn.miaosha.dao.MiaoshaUserDao;
import com.tkn.miaosha.domain.MiaoshaUser;
import com.tkn.miaosha.exception.GlobalException;
import com.tkn.miaosha.redis.MiaoshaUserKey;
import com.tkn.miaosha.redis.RedisService;
import com.tkn.miaosha.result.CodeMsg;
import com.tkn.miaosha.util.MD5Util;
import com.tkn.miaosha.util.UUIDUtil;
import com.tkn.miaosha.vo.LoginVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

@Service
public class MiaoshaUserService {

    public static final String COOKI_NAME_TOKEN = "token";

    @Autowired
    MiaoshaUserDao miaoshaUserDao;

    @Autowired
    RedisService redisService;

    public MiaoshaUser getById(long id) {
        return miaoshaUserDao.getById(id);
    }

    public boolean login(HttpServletResponse response, LoginVo loginVo) {
        if (loginVo == null) {
            return false;
        }
        String mobile = loginVo.getMobile();
        String formPass = loginVo.getPassword();
        // 验证手机号是否存在
        MiaoshaUser miaoshaUser = getById(Long.parseLong(mobile));
        if (miaoshaUser == null) {
            throw new GlobalException(CodeMsg.MOBILE_NOT_EXIST);
        }
        // 验证手机号
        String password = miaoshaUser.getPassword();
        String salt = miaoshaUser.getSalt();
        String dbPass = MD5Util.formPassToDBPass(formPass, salt);
        if (!dbPass.equals(password)) {
            throw new GlobalException(CodeMsg.PASSWORD_ERROR);
        }
        // 生成token
        String token = UUIDUtil.uuid();
        addCooke(response, token, miaoshaUser);
        return true;
    }

    /**
     * 根据token获取
     *
     * @param response
     * @param token
     */
    public MiaoshaUser getByToken(HttpServletResponse response, String token) {
        if (token == null) {
            return null;
        }
        MiaoshaUser user = redisService.get(MiaoshaUserKey.token, token, MiaoshaUser.class);
        // 延长时间
        if (user != null) {
            addCooke(response, token, user);

        }
        return user;

    }

    /**
     * 添加cooke
     *
     * @param response
     * @param token
     * @param user
     */
    private void addCooke(HttpServletResponse response, String token, MiaoshaUser user) {
        redisService.set(MiaoshaUserKey.token, token, user);
        Cookie cookie = new Cookie(COOKI_NAME_TOKEN, token);
        cookie.setMaxAge(MiaoshaUserKey.token.expireSeconds());
        cookie.setPath("/");
        response.addCookie(cookie);
    }

}
