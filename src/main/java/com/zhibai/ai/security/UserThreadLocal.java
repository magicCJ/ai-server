package com.zhibai.ai.security;

import java.io.Serializable;

/**
 * @Author xunbai
 * @Date 2023-05-13 18:40
 * @description
 **/
public class UserThreadLocal implements Serializable {

    private static final ThreadLocal<Long> USER_ID = new ThreadLocal<>();

    public static Long getUserId(){
        return USER_ID.get();
    }

    public static void setUserId(Long userId){
        if (userId != null) {
            USER_ID.set(userId);
        }
    }

    public static void remove(){
        USER_ID.remove();
    }
}
