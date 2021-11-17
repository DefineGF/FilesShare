package com.example.mywhitejotter.bean;

public class Result {
    private int code;
    private String describe;

    public Result(int code) {
        this.code = code;
    }

    public Result(int code, String describe) {
        this.code = code;
        this.describe = describe;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }
}
