package com.platform.testing.common;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.HashMap;

/**
 * @program: ResultDto
 * @description:
 * @author: zhuruiqi
 * @create: 2022-03-17 15:56
 **/

@ApiModel(value = "基础返回类", description = "基础返回类")
public class ResultDto<T> implements Serializable {
    private static final long serialVersionUID = -7472879865481412372L;

    @ApiModelProperty(value = "返回结果码 1成功 0失败", required = true, example = "1", allowableValues = "1,0")
    private Integer resultCode;

    @ApiModelProperty(value = "提示信息", example = "成功", allowableValues = "成功，失败")
    private String message = "";

    @ApiModelProperty(value = "响应结果数据")
    private T data = null;

    public Integer getResultCode() {
        return resultCode;
    }

    public static ResultDto newInstance() {
        return new ResultDto();
    }

    /**
     * 设置成功
     */

    public void setAsSuccess() {
        this.resultCode = 1;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public static ResultDto success(String message) {
        ResultDto resultDto = new ResultDto();
        resultDto.setAsSuccess();
        resultDto.setMessage(message);
        return resultDto;
    }

    public static <T> ResultDto<T> success(String message, T data) {
        ResultDto<T> resultDto = new ResultDto<>();
        resultDto.setAsSuccess();
        resultDto.setMessage(message);
        resultDto.setData(data);
        return resultDto;
    }

    /**
     * 设置失败
     */
    public void setAsFailure() {
        this.resultCode = 0;
    }

    public static <T> ResultDto<T> fail(String message) {
        ResultDto<T> resultDto = new ResultDto<>();
        resultDto.setAsFailure();
        resultDto.setMessage(message);
        return resultDto;
    }

    public static <T> ResultDto<T> fail(String message, T data) {
        ResultDto<T> resultDto = new ResultDto<>();
        resultDto.setAsFailure();
        resultDto.setMessage(message);
        resultDto.setData(data);
        return resultDto;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public static ResultDto main(String[] args) {
        return ResultDto.fail("密码不对", new HashMap<>());
    }
}
