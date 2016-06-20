package com.maxent.oms.monitor.service;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.maxent.oms.core.model.ZabbixRequest;
import com.maxent.oms.core.service.ZabbixTemplate;
import com.maxent.oms.monitor.ZabbixMethod;
import com.maxent.oms.monitor.model.AddHost;
import com.maxent.oms.monitor.model.Item;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by haiquanli on 16/6/16.
 */
@Service
@CacheConfig
public class HostService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    //https://www.zabbix.com/documentation/3.0/
    //6092c6a8d6af6b5493cb9abac5060f04
    @Autowired
    private ZabbixTemplate zabbixTemplate;

    public void addHost(AddHost addHost, String auth) {
        //group id
        Long groupId = findGroupByName(addHost.getApplicationName());

        //template id
        Long templateId = findTemplateByName(addHost.getApplicationName());

        Map param = new HashMap();
        param.put("host", addHost.getHostName());

        //interfaces
        param.put("interfaces", ImmutableList.of(ImmutableMap.of("type", 1, "main", 1, "useip", 1,
                "ip", addHost.getIp(), "port", "10050")));

        //groups
        param.put("groups", ImmutableList.of(ImmutableMap.of("groupid", groupId)));

        //templates
        param.put("templates", ImmutableList.of(ImmutableMap.of("templateid", templateId)));

        ZabbixRequest request = new ZabbixRequest(ZabbixMethod.HOST_CREATE, param, auth);
        zabbixTemplate.call(request);
    }

    public Long hostCountOfGroup(String group) {
        String sql = "select count(1) from hosts h,hosts_groups hg,groups g\n" +
                "WHERE g.groupid=hg.groupid\n" +
                "and h.hostid=hg.hostid\n" +
                "and g.name =? ";
        return jdbcTemplate.queryForObject(sql, Long.class, group);

    }


    public List<Map<String, Object>> hostOfGroup(String group, Pageable pageable) {
        String sql = "select * from hosts h,hosts_groups hg,groups g\n" +
                "WHERE g.groupid=hg.groupid\n" +
                "and h.hostid=hg.hostid\n" +
                "and g.name =?\n" +
                "limit ?,?";
        return jdbcTemplate.queryForList(sql, group, pageable.getPageNumber(), pageable.getPageSize());
    }


    public void deleteHost(List<Long> hostids, String auth) {
        ZabbixRequest request = new ZabbixRequest(ZabbixMethod.HOST_DELETE, hostids, auth);
        zabbixTemplate.call(request);
    }

    @Cacheable("groups")
    public Long findGroupByName(String name) {
        String sql = "select groupid from groups where name = ?";
        return jdbcTemplate.queryForObject(sql, Long.class, name);
    }

    @Cacheable("templates")
    public Long findTemplateByName(String name) {
        String sql = "select hostid from hosts where name = ?";
        return jdbcTemplate.queryForObject(sql, Long.class, name);
    }

    public void history(List<Long> itemIds, Integer valueType, Long startTime, Long endTime, String auth) {
        Map params = new HashMap();
        params.put("output", "extend");
        params.put("history", valueType);
        params.put("itemids", itemIds);
        params.put("time_from", startTime);
        params.put("time_till", endTime);
        params.put("sortfield", "clock");
        params.put("sortorder", "DESC");
        ZabbixRequest request = new ZabbixRequest(ZabbixMethod.ITEM_GET, params, auth);
        List<Item> items = JSON.parseArray(zabbixTemplate.call(request).getJSONArray("result").toJSONString(), Item.class);
    }


    public List<Item> lastValues(Long hostid, String auth) {
        Map params = new HashMap();
        params.put("output", ImmutableList.of("lastvalue", "name", "value_type","key_"));
        params.put("hostids", hostid);
        params.put("sortfield", "name");
        ZabbixRequest request = new ZabbixRequest(ZabbixMethod.ITEM_GET, params, auth);
        List<Item> items = JSON.parseArray(zabbixTemplate.call(request).getJSONArray("result").toJSONString(), Item.class);
        return items;
    }

}
