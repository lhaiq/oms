package com.maxent.oms.admin.service;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.ImmutableMap;
import com.maxent.oms.admin.ZabbixMethod;
import com.maxent.oms.admin.model.User;
import com.maxent.oms.core.model.ZabbixRequest;
import com.maxent.oms.core.service.ZabbixTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * Created by haiquanli on 16/6/3.
 */
@Service
public class UserService {


    @Autowired
    private ZabbixTemplate zabbixTemplate;


    public JSONObject login(User user) {
        Map params = ImmutableMap.of(
                "user", user.getUsername(),
                "password", user.getPassword(),
                "userData",true);
        ZabbixRequest request = new ZabbixRequest(ZabbixMethod.USER_LOGIN,params);
        JSONObject jsonObject = zabbixTemplate.call(request);
        return jsonObject.getJSONObject("result");
    }
}
