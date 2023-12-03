package com.zhibai.ai.util;

import java.util.UUID;

/**
 * @Author xunbai
 * @Date 2023-05-20 21:12
 * @description
 **/
public class UuidUtils {

    public static String getUUID(){
        return UUID.randomUUID().toString().replaceAll("-", "").replaceAll("\\.", "");
    }

}
