package com.youkeshu.hr.util;

/**
 * @Author: liuxing
 * @Date: 2018/8/15 11:13
 * @Description:
 */
public class DownloadMetadata {

    private String headerName;

    private int order;

    private String fieldName;

    private String getMethodName;

    private String valueMethod;

    private String group;

    public String getHeaderName() {
        return headerName;
    }

    public void setHeaderName(String headerName) {
        this.headerName = headerName;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getGetMethodName() {
        return getMethodName;
    }

    public void setGetMethodName(String getMethodName) {
        this.getMethodName = getMethodName;
    }

    public String getValueMethod() {
        return valueMethod;
    }

    public void setValueMethod(String valueMethod) {
        this.valueMethod = valueMethod;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }
}