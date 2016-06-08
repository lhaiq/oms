package com.maxent.oms.admin.model;

import java.util.Map;

/**
 * Created by haiquanli on 16/6/5.
 */
public class AddHostRequest {

    private String host;
    private String ip;
    private Map<String,String> macros;

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Map<String, String> getMacros() {
        return macros;
    }

    public void setMacros(Map<String, String> macros) {
        this.macros = macros;
    }
}
