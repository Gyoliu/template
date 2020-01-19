package com.youkeshu.hr.dto;

public enum ResCode {
	
    SUCCESS(200,"操作成功"),
    FAILE(500,"操作失败"),
    no_login(501,"没有登录，请登录"),
    ERR(500,"操作错误"),
    UnAuthorization(401,"账号或密码错误"),
    Forbidden(403,"拒绝访问，请联系服务台设置具体权限！"),
    Bad_Request(400,"请求错误");

    public String note;
    public Integer code;

    private ResCode(Integer code, String note) {
        this.note = note;
        this.code = code;
    }
    
}
