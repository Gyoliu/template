package com.youkeshu.hr.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @Author: liuxing
 * @Date: 2020/1/16 15:54
 * @Description:
 */
@Data
public class Result<T> implements Serializable {

    /**
     * 操作结果约定码
     */
    protected Integer code;

    /**
     * 操作结果信息
     */
    protected String message;

    /**
     * 当前时间
     */
    private Date currentTime;

    /**
     * 返回数据
     */
    private T data;

    public Result(){
        super();
        this.currentTime=new Date();
    }
    public Result(ResCode resCode){
        this.code=resCode.code;
        this.message=resCode.note;
        this.currentTime=new Date();
    }
    public Result(String message){
        this.code = 400;
        this.message=message;
        this.currentTime=new Date();
    }
    public Result(T data){
        this.code=ResCode.SUCCESS.code;
        this.message=ResCode.SUCCESS.note;
        this.currentTime=new Date();
        this.data=data;
    }
    public Result(int code,String message){
        this.code=code;
        this.message=message;
        this.currentTime=new Date();
    }

}