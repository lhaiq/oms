package com.maxent.oms.monitor.controller;

import com.maxent.oms.monitor.service.HostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by haiquanli on 16/6/3.
 */
@RestController
@RequestMapping("/oms")
public class HostController {

    @Autowired
    private HostService hostService;

//    /**
//     * 添加host
//     *
//     * @param auth
//     * @param addHost
//     */
//    @RequestMapping(value = "/host", method = RequestMethod.POST)
//    public void addHost(@RequestHeader("Authorization") String auth,
//                        AddHost addHost) {
//        hostService.addHost(addHost, auth);
//    }
//
//    //获取host详情
//
//    /**
//     * hosts of group
//     *
//     * @param group
//     * @param pageable
//     * @return
//     */
//    @RequestMapping(value = "/hosts/{id}", method = RequestMethod.GET)
//    public Object hostPage(@RequestParam String group, Pageable pageable) {
//
//        List<Map<String, Object>> content = hostService.hostOfGroup(group, pageable);
//        long count = hostService.hostCountOfGroup(group);
//        Page<Map<String, Object>> page = new PageImpl<>(content, pageable, count);
//        return page;
//    }
//
//    //host count of group
//    @RequestMapping(value = "/host", method = RequestMethod.GET)
//    public Object hostCount(@RequestParam List<String> groups) {
//
//        Map<String, Long> result = new HashMap<>();
//        for (String group : groups) {
//            result.put(group, hostService.hostCountOfGroup(group));
//        }
//        return result;
//    }

    //更新host

    /**
     * 删除host
     * @param auth
     * @param groupIds
     */
    @RequestMapping(value = "/host", method = RequestMethod.GET)
    public void deleteHost(@RequestHeader("Authorization") String auth,
                           @RequestParam List<Long> groupIds) {
        hostService.deleteHost(groupIds,auth);
    }


}
