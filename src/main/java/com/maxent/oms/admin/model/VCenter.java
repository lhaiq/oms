package com.maxent.oms.admin.model;

import java.util.List;
import java.util.Map;

/**
 * Created by haiquanli on 16/6/16.
 */
public class VCenter {

    private Long hostid;
    private String hostName;
    private String visibleName;
    private String ip;
    private List<Macro> macros;

    public String getHostName() {
        return hostName;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

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

    public String getVisibleName() {
        return visibleName;
    }

    public void setVisibleName(String visibleName) {
        this.visibleName = visibleName;
    }

    public void setHostid(Long hostid) {
        this.hostid = hostid;
    }

    public Long getHostid() {
        return hostid;
    }
}
