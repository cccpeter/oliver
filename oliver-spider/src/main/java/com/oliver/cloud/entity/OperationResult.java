package com.oliver.cloud.entity;

import com.oliver.cloud.dto.MetaData;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
/**
 * @Author: cpeter
 * @Desc: 网络请求返回的封装类
 * @Date: cretead in 2019/10/17 18:05
 * @Last Modified: by
 * @return value
 */
public class OperationResult {
    public static final String SUCCESS = "0";
    public static final String FAILURE = "-1";
    public static final String UNKNOWN = "999999";
    private String type;
    private String code;
    private String message;
    private Object data;

    public String getType() {
        return this.type;
    }

    public OperationResult setType(String var1) {
        this.type = var1;
        return this;
    }

    public String getCode() {
        return this.code;
    }

    public OperationResult setCode(String var1) {
        this.code = var1;
        return this;
    }

    public String getMessage() {
        return this.message;
    }

    public OperationResult setMessage(String var1) {
        this.message = var1;
        return this;
    }

    public Object getData() {
        return this.data;
    }

    public OperationResult setData(Object var1) {
        this.data = var1;
        return this;
    }

    public static OperationResult buildSuccessResult(String var0, Object var1) {
        return (new OperationResult(OperationResult.OPERATION_RESULT_TYPE.success, var0, var1)).setCode("0");
    }

    public static OperationResult buildSuccessResult() {
        return (new OperationResult(OperationResult.OPERATION_RESULT_TYPE.success, (String)null)).setCode("0");
    }

    public static OperationResult buildSuccessResult(String var0) {
        return (new OperationResult(OperationResult.OPERATION_RESULT_TYPE.success, var0)).setCode("0");
    }

    public static OperationResult buildSuccessResult(Object var0) {
        return (new OperationResult(OperationResult.OPERATION_RESULT_TYPE.success, "处理成功", var0)).setCode("0");
    }

    public static OperationResult buildWarningResult(String var0, Object var1) {
        return (new OperationResult(OperationResult.OPERATION_RESULT_TYPE.warning, var0, var1)).setCode("0");
    }

    public static OperationResult buildFailureResult(String var0) {
        return (new OperationResult(OperationResult.OPERATION_RESULT_TYPE.failure, var0)).setCode("-1");
    }

    public static OperationResult buildFailureResult(String var0, Object var1) {
        return (new OperationResult(OperationResult.OPERATION_RESULT_TYPE.failure, var0, var1)).setCode("-1");
    }

    public static OperationResult buildUnauthorizedResult(String var0) {
        return (new OperationResult(OperationResult.OPERATION_RESULT_TYPE.unauthorized, var0)).setCode("-1");
    }

    public static OperationResult buildUnauthorizedResult(String var0, Object var1) {
        return (new OperationResult(OperationResult.OPERATION_RESULT_TYPE.unauthorized, var0, var1)).setCode("-1");
    }

    public static OperationResult buildConfirmResult(String var0, Object var1) {
        return (new OperationResult(OperationResult.OPERATION_RESULT_TYPE.confirm, var0, var1)).setCode("0");
    }

    public static OperationResult buildConfirmResult(String var0) {
        return (new OperationResult(OperationResult.OPERATION_RESULT_TYPE.confirm, var0, (Object)null)).setCode("0");
    }

    public OperationResult(OperationResult.OPERATION_RESULT_TYPE var1, String var2) {
        this.type = var1.name();
        this.message = var2;
    }

    public OperationResult(OperationResult.OPERATION_RESULT_TYPE var1, String var2, Object var3) {
        this.type = var1.name();
        this.message = var2;
        this.data = var3;
    }

    public static enum OPERATION_RESULT_TYPE {
        /**
         * 操作成功
         */
        @MetaData(
                value = "成功",
                comments = "操作处理成功。"
        )
        success,
        @MetaData(
                value = "警告",
                comments = "用于提示业务处理基本完成，但是其中存在一些需要注意事项放在message或data中的提示信息。"
        )
        warning,
        @MetaData(
                value = "失败",
                comments = "操作处理失败。"
        )
        failure,
        @MetaData(
                value = "确认",
                comments = "本次提交中止，提示用户进行确认。"
        )
        confirm,
        @MetaData(
                value = "失败",
                comments = "操作未授权。"
        )
        unauthorized;

        private OPERATION_RESULT_TYPE() {
        }
    }
}
