package com.youkeshu.hr.util;

import java.lang.reflect.Field;

/**
 * @Author: liuxing
 * @Date: 2018/11/22 9:41
 * @Description:
 */
public class UploadMetaData {

    private int order;
    private int group;
    private boolean required;
    private Field field;

    private String validate;

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public int getGroup() {
        return group;
    }

    public void setGroup(int group) {
        this.group = group;
    }

    public boolean isRequired() {
        return required;
    }

    public void setRequired(boolean required) {
        this.required = required;
    }

    public Field getField() {
        return field;
    }

    public void setField(Field field) {
        this.field = field;
    }

    public String getValidate() {
        return validate;
    }

    public void setValidate(String validate) {
        this.validate = validate;
    }
}