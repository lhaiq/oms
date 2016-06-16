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

    //登录
    @RequestMapping(value = "/admin/login", method = RequestMethod.POST)
    public Object login(@RequestBody User user) {
        return userService.login(user);
    }

    //添加用户

    //修改用户

    //用户列表

    //删除用户

    //添加user group

    //删除user group

    //修改user group

    //user group 列表

}
