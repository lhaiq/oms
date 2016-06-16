package com.maxent.oms.admin.service;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.maxent.oms.admin.ZabbixMethod;
import com.maxent.oms.admin.model.Host;
import com.maxent.oms.admin.model.Item;
import com.maxent.oms.admin.model.VCenter;
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
public class VMService extends HostService{


    private static final String VCENTERS_GROUP = "vcenter";
    private static final String VCENTERS_TEMPLATE = "Template Virt VMware";

    private static final String HYPERVISORS_TEMPLATE = "";
    private static final String VMS_TEMPLATE = "";


    public List<Host> vcenters(String auth) {
        Long groupId = findGroupByName(VCENTERS_GROUP);
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


    public void addVCenter(VCenter vCenter, String auth) {

        //group id
        Long groupId = findGroupByName(VCENTERS_GROUP);

        //template id
        Long templateId = findTemplateByName(VCENTERS_TEMPLATE);

        Map param = new HashMap();
        param.put("host", vCenter.getHostName());
        param.put("name", vCenter.getVisibleName());

        //interfaces
        param.put("interfaces", ImmutableList.of(ImmutableMap.of("type", 1, "main", 1, "useip", 1,
                "ip", vCenter.getIp(), "port", "10050")));

        //groups
        param.put("groups", ImmutableList.of(ImmutableMap.of("groupid", groupId)));

        //templates
        param.put("templates", ImmutableList.of(ImmutableMap.of("templateid", templateId)));

        //macros
        param.put("macros", vCenter.getMacros());

        ZabbixRequest request = new ZabbixRequest(ZabbixMethod.HOST_CREATE, param, auth);
        zabbixTemplate.call(request);
    }

    public void updateVCenter(VCenter vCenter,String auth){
        Map param = new HashMap();
        param.put("hostid",vCenter.getHostid());
        param.put("host", vCenter.getHostName());
        param.put("name", vCenter.getVisibleName());

        //macros
        param.put("macros", vCenter.getMacros());

        ZabbixRequest request = new ZabbixRequest(ZabbixMethod.HOST_UPDATE, param, auth);
        zabbixTemplate.call(request);
    }

    public List<Host> hypervisors(String ip, String auth) {
        Long groupId = findGroupByName(HYPERVISORS_TEMPLATE);
        String sql =
                "select h.* from interface i\n" +
                "join hosts_groups hg\n" +
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
        Long groupId = findGroupByName(VMS_TEMPLATE);
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


    public Long findGroupByName(String name) {
        String sql = "select groupid from groups where name = ?";
        return jdbcTemplate.queryForObject(sql, Long.class, name);
    }

    public Long findTemplateByName(String name) {
        String sql = "select hostid from hosts where name = ?";
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
