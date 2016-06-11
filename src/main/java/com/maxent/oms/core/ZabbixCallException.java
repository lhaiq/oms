package com.maxent.oms.core;


public class ZabbixCallException extends BusinessException {

    private static final long serialVersionUID = 1L;

    private String data;

    public ZabbixCallException(String code, String message, String data) {
        super(data, code);
        this.data = data;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }


}