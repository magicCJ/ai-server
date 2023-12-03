package com.zhibai.ai.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Author xunbai
 * @Date 2023-05-13 17:04
 * @description
 **/
@Getter
@AllArgsConstructor
public enum AiServerExceptionEnum {

    /** ************** 系统异常 *************** */
    SYSTEM_ERROR("9000", "系统异常"),
    DB_DATA_SAVE_ERROR("9001", "数据库保存失败"),
    DB_DATA_UPDATE_ERROR("9002", "数据库更像失败"),
    DB_DATA_DELETE_ERROR("9003", "数据库删除失败"),
    DB_DATA_QUERY_ERROR("9004", "数据库查询失败"),
    ARGUMENT_BLANK("9005", "请求参数{0}不能为空"),
    VALIDATION_ERROR("9006", "参数校验失败"),

    Third_FAILED("9007", "三方调用失败"),

    DATE_NOT_EXISTS("9008", "数据不存在"),


    /** ************** 系统异常 *************** */

    /** ************** 业务异常 *************** */
    SECURITY_ERROR("1000", "登录已过期请重新登录!"),
    SCENE_NOT_EXISTS("1001", "场景不存在"),
    AVAILABLE_FREQUENCY_DEFICIENCY("1002", "可用次数不足/暂无会员/会员已过期!"),
    PROHIBITED_WORDS("1003", "违禁提问，请重新输入"),

    UPLOAD_FAILED("1004", "上传图片到discord失败"),

    IMG_CREATE_FAILED("1005", "很抱歉，似乎有一些问题导致了您的绘图失败。如果该问题持续出现，请联系客服。"),
    IMG_DESCRIBE_FAILED("1006", "很抱歉，似乎有一些问题导致了您的解析失败。如果该问题持续出现，请联系客服。"),
    IMG_TOO_LARGE("1007", "图片过大，请重新上传"),
    ;

    /** ************** 业务异常 *************** */

    ;

    private final String code;

    private final String message;
}
