package com.maxent.oms.admin.service;

import com.maxent.oms.admin.ZabbixMethod;
import com.maxent.oms.core.model.ZabbixRequest;
import com.maxent.oms.core.service.ZabbixTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

/**
 * Created by haiquanli on 16/6/16.
 */
public abstract class HostService {

    @Autowired
    protected JdbcTemplate jdbcTemplate;

    //https://www.zabbix.com/documentation/3.0/
    //6092c6a8d6af6b5493cb9abac5060f04
    @Autowired
    protected ZabbixTemplate zabbixTemplate;

    public void deleteHost(List<Long> hostids,String auth){
        ZabbixRequest request = new ZabbixRequest(ZabbixMethod.HOST_DELETE,hostids,auth);
        zabbixTemplate.call(request);
    }
}
