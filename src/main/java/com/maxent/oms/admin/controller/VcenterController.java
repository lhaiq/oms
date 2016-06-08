package com.maxent.oms.admin.controller;

import com.maxent.oms.admin.model.User;
import com.maxent.oms.admin.service.UserService;
import com.maxent.oms.admin.service.VcenterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Created by haiquanli on 16/6/3.
 */
@RestController
@RequestMapping("/oms")
public class VcenterController {

    @Autowired
    private VcenterService vcenterService;

    @RequestMapping(value = "/centers", method = RequestMethod.GET)
    @ResponseBody
    public Object centers(@RequestParam("auth") String auth) {
        return vcenterService.vcenters(auth);
    }
}
