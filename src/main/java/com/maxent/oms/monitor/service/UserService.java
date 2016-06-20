package com.maxent.oms.monitor.service;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.maxent.oms.core.model.ZabbixRequest;
import com.maxent.oms.core.service.ZabbixTemplate;
import com.maxent.oms.monitor.ZabbixMethod;
import com.maxent.oms.monitor.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
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
                "userData", true);
        ZabbixRequest request = new ZabbixRequest(ZabbixMethod.USER_LOGIN, params);
        JSONObject jsonObject = zabbixTemplate.call(request);
        return jsonObject.getJSONObject("result");
    }


    public JSONObject addUser(User user,String auth){
        Long userGroupId=0L;
        Map param = new HashMap();
        param.put("alias",user.getUsername());
        param.put("passwd",user.getPassword());
        param.put("usrgrps", ImmutableList.of(ImmutableMap.of("usrgrps",userGroupId)));
        ZabbixRequest request = new ZabbixRequest(ZabbixMethod.USER_CREATE,param,auth);
        JSONObject result = zabbixTemplate.call(request);
        return result.getJSONObject("result");
    }



}
