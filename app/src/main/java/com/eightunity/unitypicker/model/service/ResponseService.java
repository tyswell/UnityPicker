package com.eightunity.unitypicker.model.service;

/**
 * Created by deksen on 8/31/16 AD.
 */
public class ResponseService<T> {

    private int code;
    private String message;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
