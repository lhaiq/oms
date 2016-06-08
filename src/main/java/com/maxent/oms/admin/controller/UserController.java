package com.maxent.oms.admin.controller;

import com.maxent.oms.admin.model.User;
import com.maxent.oms.admin.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Created by haiquanli on 16/6/3.
 */
@RestController
@RequestMapping("/oms")
public class UserController {

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/admin/login", method = RequestMethod.POST)
    @ResponseBody
    public Object login(@RequestBody User user) {
        return userService.login(user);
    }
}
