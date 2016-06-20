package com.maxent.oms.monitor.model;

/**
 * Created by haiquanli on 16/6/5.
 */
public class Item {

    private Long itemid;
    private String name;
    private String lastvalue;

    public Long getItemid() {
        return itemid;
    }

    public void setItemid(Long itemid) {
        this.itemid = itemid;
    }

    public String getLastvalue() {
        return lastvalue;
    }

    public void setLastvalue(String lastvalue) {
        this.lastvalue = lastvalue;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
