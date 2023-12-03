package com.zhibai.ai.util;

import com.zhibai.ai.common.AiServerException;
import com.zhibai.ai.common.AiServerExceptionEnum;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.Collection;

/**
 * @Author xunbai
 * @Date 2023-05-13 20:22
 * @description
 **/
public class AssertUtil {

    public static void isTrue(boolean expression, AiServerExceptionEnum exceptionEnum, String... params) {
        if(!expression) {
            throw new AiServerException(exceptionEnum, params);
        }
    }

    /**
     * 断言参数类不为空
     */
    public static void isNotNull(Object object, String param) {
        isTrue(object != null, AiServerExceptionEnum.ARGUMENT_BLANK, param);
    }

    public static void isNotNull(Object object, AiServerExceptionEnum exceptionEnum, String... params) {
        isTrue(object != null, exceptionEnum, params);
    }

    public static void hasLength(String text, AiServerExceptionEnum exceptionEnum, String... params) {
        isTrue(StringUtils.isNotBlank(text), exceptionEnum, params);
    }

    public static void isOrNotNull(Object obj1, Object obj2, String param) {
        isTrue(obj1 != null || obj2 != null, AiServerExceptionEnum.ARGUMENT_BLANK, param);
    }

    public static void isNotEmpty(Collection collection, String param) {
        isTrue(CollectionUtils.isNotEmpty(collection), AiServerExceptionEnum.ARGUMENT_BLANK, param);
    }

    /**
     * 字符串非空断言
     */
    public static void hasLength(String text, String param) {
        isTrue(StringUtils.isNotBlank(text), AiServerExceptionEnum.ARGUMENT_BLANK, param);
    }
}
