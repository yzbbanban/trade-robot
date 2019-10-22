package com.yzb.lingo.domain;

import java.util.List;

/**
 * Created by brander on 2019/10/21
 */
public class ResultJson<T> {

    /**
     * code : 1
     * msg : successful
     * time : 1571659222
     * data : []
     */

    private int code;
    private String msg;
    private String time;
    private List<T> data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "ResultJson{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                ", time='" + time + '\'' +
                ", data=" + data +
                '}';
    }
}
