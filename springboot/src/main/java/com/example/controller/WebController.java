package com.example.controller;

import com.example.common.Result;
import com.example.common.enums.RoleEnum;
import com.example.entity.Account;
import com.example.entity.Admin;
import com.example.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class WebController {

    @Autowired
    AdminService adminService;

    @PostMapping("/login")
    public Result login(@RequestBody Account account) {
        Account accountLogin = new Account();
        if (RoleEnum.ADMIN.name().equals(account.getRole())) {
            accountLogin = adminService.login(account);
        }
        return Result.success(accountLogin);
    }

    @PostMapping("/register")
    public Result register(@RequestBody Account account) {
        return Result.success();
    }

    @GetMapping("/")
    public Result hello() {
        return Result.success();
    }

    @PostMapping("/updatePassword")
    public Result updatePassword(@RequestBody Account account) {
        if (RoleEnum.ADMIN.name().equals(account.getRole())) {
            adminService.updatePassword(account);
        }
        return Result.success();
    }
}
