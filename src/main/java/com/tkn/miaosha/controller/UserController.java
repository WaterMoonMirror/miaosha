package com.tkn.miaosha.controller;

import com.tkn.miaosha.domain.User;
import com.tkn.miaosha.result.Result;
import com.tkn.miaosha.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;
    @RequestMapping("/get")
    public Result<User> get(){
        User user=userService.get(1);
        return Result.success(user);
    }
    @RequestMapping("/set")
    public Result<User> set(){

        User user=new User();
        user.setId(2);
        user.setName("456");
        userService.set(user);
        return Result.success(userService.get(2));
    }

    @RequestMapping("/redis/get")
    public Result<User> redisGet(){
        User user=userService.redisGet("1");
        return Result.success(user);
    }
    @RequestMapping("/redis/set")
    public Result<User> redisSet(){

        User user=new User();
        user.setId(2);
        user.setName("456");
        userService.redisSet("1",user);
        return Result.success(userService.redisGet("1"));
    }
}
