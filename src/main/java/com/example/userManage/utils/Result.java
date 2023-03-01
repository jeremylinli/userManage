package com.example.userManage.utils;

import lombok.Data;

@Data
public class Result<T> {
    private Integer status;

    private String message;

    private T data;

    public Result(){}

    public static <T> Result<T> build(T body, Integer status, String message) {
        Result<T> result = new Result<T>();
        if (body != null) {
            result.setData(body);
        }
        result.setStatus(status);
        result.setMessage(message);
        return result;
    }

    public static<T> Result<T> ok(){
        return Result.ok(null);
    }

    public static<T> Result<T> ok(T data){
        return build(data,0,"获取用户列表数据成功");
    }

    public static<T> Result<T> fail(){
        return Result.fail(null);
    }

    public static<T> Result<T> fail(T data){
        return build(data, 1,"获取用户列表数据失败");
    }

}
