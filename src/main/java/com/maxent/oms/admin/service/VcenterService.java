package com.maxent.oms.admin.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.maxent.oms.admin.ZabbixMethod;
import com.maxent.oms.admin.model.Host;
import com.maxent.oms.admin.model.Item;
import com.maxent.oms.admin.model.User;
import com.maxent.oms.core.model.ZabbixRequest;
import com.maxent.oms.core.service.ZabbixTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by haiquanli on 16/6/3.
 */
@Service
public class VcenterService {


    private static final String VCENTERS_TEMPLATE = "vcenter";
    private static final String HYPERVISORS_TEMPLATE = "";
    private static final String VMS_TEMPLATE = "";

    @Autowired
    private JdbcTemplate jdbcTemplate;

    //https://www.zabbix.com/documentation/3.0/
    //6092c6a8d6af6b5493cb9abac5060f04
    @Autowired
    private ZabbixTemplate zabbixTemplate;


    public List<Host> vcenters(String auth) {
        Long groupId = findByName(VCENTERS_TEMPLATE);
        String sql = "select h.* from hosts_groups g,hosts h\n" +
                "where\n" +
                "  g.hostid=h.hostid\n" +
                "  and g.groupid=? \n" +
                "and h.status!=3";

        List<Host> hosts = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Host.class), groupId);
        for (Host host : hosts) {
            host.setItems(lastValues(host.getHostid(), auth));
        }

        return hosts;
    }


    public List<Host> hypervisors(String ip, String auth) {
        Long groupId = findByName(HYPERVISORS_TEMPLATE);
        String sql = "select h.* from interface i\n" +
                "  join hosts_groups hg\n" +
                "on i.hostid=hg.hostid\n" +
                "join groups g\n" +
                "on hg.groupid=g.groupid\n" +
                "JOIN hosts h\n" +
                "  on hg.hostid=h.hostid\n" +
                "and g.groupid=?\n" +
                "and i.ip=?\n" +
                "LIMIT 0,1\n";

        List<Host> hosts = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Host.class), groupId, ip);
        for (Host host : hosts) {
            host.setItems(lastValues(host.getHostid(), auth));
        }

        return hosts;
    }


    public List<Host> vms(String ip, String auth) {
        Long groupId = findByName(VMS_TEMPLATE);
        String sql = "select h.* from interface i\n" +
                "  join hosts_groups hg\n" +
                "on i.hostid=hg.hostid\n" +
                "join groups g\n" +
                "on hg.groupid=g.groupid\n" +
                "JOIN hosts h\n" +
                "  on hg.hostid=h.hostid\n" +
                "and g.groupid=?\n" +
                "and i.ip=?\n" +
                "LIMIT 0,1\n";

        List<Host> hosts = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Host.class), groupId, ip);
        for (Host host : hosts) {
            host.setItems(lastValues(host.getHostid(), auth));
        }

        return hosts;
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


    public Long findByName(String name) {
        String sql = "select groupid from groups where name = ?";
        return jdbcTemplate.queryForObject(sql, Long.class, name);
    }


    public List<Item> lastValues(Long hostid, String auth) {
        Map params = new HashMap();
        params.put("output", ImmutableList.of("lastvalue", "name", "value_type"));
        params.put("hostids", hostid);
        params.put("sortfield", "name");
        ZabbixRequest request = new ZabbixRequest(ZabbixMethod.ITEM_GET, params, auth);
        List<Item> items = JSON.parseArray(zabbixTemplate.call(request).getJSONArray("result").toJSONString(), Item.class);
        return items;
    }


}
