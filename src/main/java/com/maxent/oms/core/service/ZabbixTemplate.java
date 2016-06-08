package com.maxent.oms.core.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.maxent.oms.core.model.ZabbixRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * Created by haiquanli on 16/6/3.
 */
@Service
public class ZabbixTemplate {

    @Autowired
    private RestTemplate restTemplate;

    @Value("${zabbix.url}")
    private String zabbixAddress;

    public JSONObject call(ZabbixRequest request) {
        ResponseEntity<String> ret = restTemplate.postForEntity(zabbixAddress, request, String.class);
        JSONObject jo = JSON.parseObject(ret.getBody());
        if (jo.get("error") != null) {
            JSONObject err = jo.getJSONObject("error");
//            throw new ZabbixCallException(err.getString("code"), err.getString("message"), err.getString("data"));
        }
        return jo;
    }
}
