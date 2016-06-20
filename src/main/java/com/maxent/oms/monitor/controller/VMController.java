package com.maxent.oms.monitor.controller;

import com.google.common.collect.ImmutableList;
import com.maxent.oms.monitor.model.Host;
import com.maxent.oms.monitor.model.VCenter;
import com.maxent.oms.monitor.service.HostService;
import com.maxent.oms.monitor.service.VMService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by haiquanli on 16/6/3.
 */
@RestController
@RequestMapping("/oms")
public class VMController {

    @Autowired
    private VMService vmService;

    @Autowired
    private HostService hostService;

    /**
     * vcenter列表
     *
     * @param auth
     * @return
     */
    @RequestMapping(value = "/vcenters", method = RequestMethod.GET)
    public Object vcenters(@RequestHeader("Authorization") String auth) {
        return vmService.vcenters(auth);
    }

    /**
     * 添加vcenter
     *
     * @param auth
     * @param vCenter
     */
    @RequestMapping(value = "/vcenter", method = RequestMethod.POST)
    public void addVcenter(@RequestHeader("Authorization") String auth,
                           @RequestBody VCenter vCenter) {
        vmService.addVCenter(vCenter, auth);
    }

    /**
     * 编辑vcenter
     *
     * @param auth
     * @param vCenter
     */
    @RequestMapping(value = "/vcenter/{hostid}", method = RequestMethod.PUT)
    public void updateVcenter(@RequestHeader("Authorization") String auth,
                              @PathVariable Long hostid,
                              @RequestBody VCenter vCenter) {
        vCenter.setHostid(hostid);
        vmService.updateVCenter(vCenter, auth);
    }

    /**
     * vcenter详情
     *
     * @param auth
     * @param hostid
     */
    @RequestMapping(value = "/vcenter/{hostid}", method = RequestMethod.GET)
    public void vcenter(@RequestHeader("Authorization") String auth,
                        @PathVariable Long hostid) {
//        vmService.addVCenter(vCenter, auth);
    }

    /**
     * 删除vcenter
     *
     * @param auth
     * @param hostid
     */
    @RequestMapping(value = "/vcenter/{hostid}", method = RequestMethod.DELETE)
    public void deleteVcenter(@RequestHeader("Authorization") String auth,
                              @PathVariable Long hostid) {
        hostService.deleteHost(ImmutableList.of(hostid),auth);

    }

    /**
     * hypervisors列表
     * @param auth
     * @param interfaceIp
     * @param ip
     * @param pageable
     * @return
     */
    @RequestMapping(value = "/hypervisors", method = RequestMethod.GET)
    public Object vcenters(@RequestHeader("Authorization") String auth,
                           @RequestParam(required = false) String interfaceIp,
                           @RequestParam(required = false) String ip,
                           Pageable pageable) {
        List<Host> hosts = vmService.hypervisors(interfaceIp,ip, auth, pageable);
        Long count = vmService.hypervisorCount(interfaceIp,ip,auth);
        Page<Host> page = new PageImpl<>(hosts,pageable,count);
        return page;
    }

    @RequestMapping(value = "/vms", method = RequestMethod.GET)
    public Object vms(@RequestHeader("Authorization") String auth,
                           @RequestParam(required = false) String interfaceIp,
                           @RequestParam(required = false) String ip,
                           Pageable pageable) {
        List<Host> hosts = vmService.vms(interfaceIp, ip, auth, pageable);
        Long count = vmService.vmCount(interfaceIp, ip, auth);
        Page<Host> page = new PageImpl<>(hosts,pageable,count);
        return page;
    }


}
