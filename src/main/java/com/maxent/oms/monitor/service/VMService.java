package com.maxent.oms.monitor.service;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.maxent.oms.core.model.ZabbixRequest;
import com.maxent.oms.core.service.ZabbixTemplate;
import com.maxent.oms.monitor.ZabbixMethod;
import com.maxent.oms.monitor.model.Host;
import com.maxent.oms.monitor.model.VCenter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by haiquanli on 16/6/3.
 */
@Service
public class VMService {


    private static final String VCENTERS_GROUP = "vcenter";
    private static final String VCENTERS_TEMPLATE = "Template Virt VMware";

    private static final String HYPERVISORS_GROUP = "Hypervisors";
    private static final String VMS_GROUP = "Virtual machines";


    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    //https://www.zabbix.com/documentation/3.0/
    //6092c6a8d6af6b5493cb9abac5060f04
    @Autowired
    private ZabbixTemplate zabbixTemplate;

    @Autowired
    private HostService hostService;

    public List<Host> vcenters(String auth) {
        Long groupId = hostService.findGroupByName(VCENTERS_GROUP);
        String sql = "select h.* from hosts_groups g,hosts h\n" +
                "where\n" +
                "  g.hostid=h.hostid\n" +
                "  and g.groupid=? \n" +
                "and h.status!=3";

        List<Host> hosts = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Host.class), groupId);
        for (Host host : hosts) {
            host.setItems(hostService.lastValues(host.getHostid(), auth));
        }

        return hosts;
    }


    public void addVCenter(VCenter vCenter, String auth) {

        //group id
        Long groupId = hostService.findGroupByName(VCENTERS_GROUP);

        //template id
        Long templateId = hostService.findTemplateByName(VCENTERS_TEMPLATE);

        Map param = new HashMap();
        param.put("host", vCenter.getName());

        //interfaces
        Map interfaces = new HashMap();
        interfaces.put("type",1);
        interfaces.put("main",1);
        interfaces.put("useip",1);
        interfaces.put("dns","");
        interfaces.put("ip",vCenter.getIp());
        interfaces.put("port","10050");
        param.put("interfaces", ImmutableList.of(interfaces));

        //groups
        param.put("groups", ImmutableList.of(ImmutableMap.of("groupid", groupId)));

        //templates
        param.put("templates", ImmutableList.of(ImmutableMap.of("templateid", templateId)));

        //macros
        param.put("macros", vCenter.getMacros());

        ZabbixRequest request = new ZabbixRequest(ZabbixMethod.HOST_CREATE, param, auth);
        zabbixTemplate.call(request);
    }

    public void updateVCenter(VCenter vCenter, String auth) {
        Map param = new HashMap();
        param.put("hostid", vCenter.getHostid());
        param.put("host", vCenter.getName());

        //macros
        param.put("macros", vCenter.getMacros());

        ZabbixRequest request = new ZabbixRequest(ZabbixMethod.HOST_UPDATE, param, auth);
        zabbixTemplate.call(request);
    }

    public Long hypervisorCount(String interfaceIp, String ip, String auth) {
        Long groupId = hostService.findGroupByName(HYPERVISORS_GROUP);
        Map<String, ?> param = ImmutableMap.of("groupId", groupId, "interfaceIp", interfaceIp);
        StringBuffer sql = new StringBuffer();
        sql.append("select count(1) from interface i\n" +
                "join hosts_groups hg\n" +
                "on i.hostid=hg.hostid\n" +
                "join groups g\n" +
                "on hg.groupid=g.groupid\n" +
                "JOIN hosts h\n" +
                "  on hg.hostid=h.hostid\n" +
                "and g.groupid=:groupId\n");
        if (!StringUtils.isEmpty(interfaceIp)) {
            sql.append("and i.ip=:interfaceIp\n");
        }

        if (!StringUtils.isEmpty(ip)) {
            sql.append("and h.name like '" + ip + "%' \n");
        }


        return namedParameterJdbcTemplate.queryForObject(sql.toString(), param, Long.class);
    }

    public List<Host> hypervisors(String interfaceIp, String ip, String auth, Pageable pageable) {
        Long groupId = hostService.findGroupByName(HYPERVISORS_GROUP);
        Map<String, ?> param = ImmutableMap.of("groupId", groupId, "interfaceIp", interfaceIp, "number"
                , pageable.getPageNumber(), "size", pageable.getPageSize());
        StringBuffer sql = new StringBuffer();
        sql.append("select h.* from interface i\n" +
                "join hosts_groups hg\n" +
                "on i.hostid=hg.hostid\n" +
                "join groups g\n" +
                "on hg.groupid=g.groupid\n" +
                "JOIN hosts h\n" +
                "  on hg.hostid=h.hostid\n" +
                "and g.groupid=:groupId\n");
        if (!StringUtils.isEmpty(interfaceIp)) {
            sql.append("and i.ip=:interfaceIp\n");
        }

        if (!StringUtils.isEmpty(ip)) {
            sql.append("and h.name like '" + ip + "%' \n");
        }

        sql.append("limit :number,:size\n");


        List<Host> hosts = namedParameterJdbcTemplate.query(sql.toString(), param, new BeanPropertyRowMapper<>(Host.class));
        for (Host host : hosts) {
            host.setItems(hostService.lastValues(host.getHostid(), auth));
        }

        return hosts;
    }

    public Long vmCount(String interfaceIp, String ip, String auth) {
        Long groupId = hostService.findGroupByName(VMS_GROUP);
        Map<String, ?> param = ImmutableMap.of("groupId", groupId, "interfaceIp", interfaceIp);
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT count(1)\n" +
                "FROM interface i\n" +
                "  JOIN hosts_groups hg\n" +
                "    ON i.hostid = hg.hostid\n" +
                "  JOIN groups g\n" +
                "    ON hg.groupid = g.groupid\n" +
                "  JOIN hosts h\n" +
                "    ON hg.hostid = h.hostid\n" +
                "       AND g.groupid =:groupId\n");
        if (!StringUtils.isEmpty(interfaceIp)) {
            sql.append("and i.ip=:interfaceIp\n");
        }

        if (!StringUtils.isEmpty(ip)) {
            sql.append("and h.name like '" + ip + "%' \n");
        }


        return namedParameterJdbcTemplate.queryForObject(sql.toString(), param, Long.class);
    }

    public List<Host> vms(String interfaceIp, String ip, String auth, Pageable pageable) {
        Long groupId = hostService.findGroupByName(VMS_GROUP);

        Map<String, ?> param = ImmutableMap.of("groupId", groupId, "interfaceIp", interfaceIp, "number"
                , pageable.getPageNumber(), "size", pageable.getPageSize());
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT h.*\n" +
                "FROM interface i\n" +
                "  JOIN hosts_groups hg\n" +
                "    ON i.hostid = hg.hostid\n" +
                "  JOIN groups g\n" +
                "    ON hg.groupid = g.groupid\n" +
                "  JOIN hosts h\n" +
                "    ON hg.hostid = h.hostid\n" +
                "       AND g.groupid =:groupId\n");
        if (!StringUtils.isEmpty(interfaceIp)) {
            sql.append("and i.ip=:interfaceIp\n");
        }

        if (!StringUtils.isEmpty(ip)) {
            sql.append("and h.name like '" + ip + "%' \n");
        }

        sql.append("limit :number,:size\n");


        List<Host> hosts = namedParameterJdbcTemplate.query(sql.toString(), param, new BeanPropertyRowMapper<>(Host.class));
        for (Host host : hosts) {
            host.setItems(hostService.lastValues(host.getHostid(), auth));
        }

        return hosts;
    }


}
