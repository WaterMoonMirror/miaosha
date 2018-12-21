package com.tkn.miaosha.service;

import com.tkn.miaosha.dao.UserDao;
import com.tkn.miaosha.domain.MiaoshaUser;
import com.tkn.miaosha.domain.User;
import com.tkn.miaosha.redis.KeyPrefix;
import com.tkn.miaosha.redis.MiaoshaUserKey;
import com.tkn.miaosha.redis.RedisService;
import com.tkn.miaosha.redis.UserKey;
import com.tkn.miaosha.result.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;

@Service
public class UserService {

    @Autowired
     UserDao userDao;
    @Autowired
    RedisService redisService;

   public User get (int id){
       User user=userDao.getUser(id);
       return user;
   }
   public boolean set(User user){
       userDao.setUser(user);
       return true;
   }
    public User redisGet (String key){
        User user=redisService.get(UserKey.getById,key,User.class);
        return user;
    }
    public boolean redisSet(String key,User user){
        redisService.set(UserKey.getById,key,user);
        return true;
    }


}
