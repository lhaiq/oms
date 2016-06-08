package com.maxent.oms.core.model;

/**
 * Created by haiquanli on 16/6/3.
 */
public class ZabbixRequest {

    private String jsonrpc="2.0";
    private String method;
    private Object params;
    private String auth;
    private int id=1;


    public ZabbixRequest(String method, Object params){
        this.method=method;
        this.params=params;
    }

    public ZabbixRequest(String method, Object params,String auth){
        this.method=method;
        this.params=params;
        this.auth=auth;
    }

    public Object getParams() {
        return params;
    }

    public void setParams(Object params) {
        this.params = params;
    }

    public String getJsonrpc() {
        return jsonrpc;
    }

    public void setJsonrpc(String jsonrpc) {
        this.jsonrpc = jsonrpc;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
