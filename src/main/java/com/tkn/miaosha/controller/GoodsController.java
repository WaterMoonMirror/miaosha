package com.tkn.miaosha.controller;

import com.tkn.miaosha.domain.MiaoshaUser;
import com.tkn.miaosha.redis.RedisService;
import com.tkn.miaosha.service.MiaoshaUserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletResponse;

@RequestMapping("/goods")
@Controller
public class GoodsController {

    @Autowired
    RedisService redisService;
    @Autowired
    MiaoshaUserService userService;

    @RequestMapping("/to_list")
    public String toGood(Model model,
                         MiaoshaUser user) {
        model.addAttribute("user", user);
        return "goods_list";
    }
}
