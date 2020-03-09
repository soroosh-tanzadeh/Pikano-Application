package com.ternobo.pikano.RESTobjects;

public class Result {

    public boolean result;
    public Object[] data;

    public boolean isResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }

    public Object[] getData() {
        return data;
    }

    public void setData(Object[] data) {
        this.data = data;
    }
}