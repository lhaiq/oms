package com.maxent.oms.admin.controller;

import com.maxent.oms.admin.service.VMService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Created by haiquanli on 16/6/3.
 */
@RestController
@RequestMapping("/oms")
public class VMController {

    @Autowired
    private VMService vmService;

    @RequestMapping(value = "/vcenters", method = RequestMethod.GET)
    @ResponseBody
    public Object vcenters(@RequestHeader("Authorization") String auth) {
        return vmService.vcenters(auth);
    }

    //添加vcenter

}
