package com.think.futurecase;

/**
 * Created by borney on 1/11/18.
 */

public class CaseException extends Exception {
    private int code;

    public CaseException(String msg) {
        super(msg);
    }

    public CaseException(Exception ex) {
        super(ex);
    }

    public CaseException(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    @Override
    public String toString() {
        return "CaseException{" +
                "code=" + code +
                '}';
    }
}
