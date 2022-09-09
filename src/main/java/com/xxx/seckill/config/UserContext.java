package com.xxx.seckill.config;

import com.xxx.seckill.entity.User;

public class UserContext {
    private static ThreadLocal<User> userHolder = new ThreadLocal<>();

    public static void setUser(User user){
        userHolder.set(user);
    }
    public static User getUser(){
        return userHolder.get();
    }
}
