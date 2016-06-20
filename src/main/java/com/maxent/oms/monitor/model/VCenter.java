package com.maxent.oms.monitor.model;

import java.util.List;

/**
 * Created by haiquanli on 16/6/16.
 */
public class VCenter {

    private Long hostid;
    private String name;
    private String ip;
    private List<Macro> macros;

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public void setMacros(List<Macro> macros) {
        this.macros = macros;
    }

    public List<Macro> getMacros() {
        return macros;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setHostid(Long hostid) {
        this.hostid = hostid;
    }

    public Long getHostid() {
        return hostid;
    }
}
