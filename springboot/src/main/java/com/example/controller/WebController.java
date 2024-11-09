package com.example.controller;

import com.example.common.Result;
import com.example.common.enums.RoleEnum;
import com.example.entity.Account;
import com.example.entity.Admin;
import com.example.entity.User;
import com.example.exception.CustomException;
import com.example.service.AdminService;
import com.example.service.DoctorService;
import com.example.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class WebController {

    @Autowired
    AdminService adminService;
    @Autowired
    UserService userService;
    @Autowired
    DoctorService doctorService;

    @PostMapping("/login")
    public Result login(@RequestBody Account account) {
        Account accountLogin;
        if (RoleEnum.ADMIN.name().equals(account.getRole())) {
            accountLogin = adminService.login(account);
        } else if (RoleEnum.USER.name().equals(account.getRole())) {
            accountLogin = userService.login(account);
        } else if (RoleEnum.DOCTOR.name().equals(account.getRole())) {
            accountLogin = doctorService.login(account);
        } else {
            throw new CustomException("500","非法请求");
        }
        return Result.success(accountLogin);
    }

    @PostMapping("/register")
    public Result register(@RequestBody User user) {
        userService.add(user);
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
        } else if (RoleEnum.USER.name().equals(account.getRole())) {
            userService.updatePassword(account);
        } else if (RoleEnum.DOCTOR.name().equals(account.getRole())) {
            doctorService.updatePassword(account);
        } else {
            throw new CustomException("500","非法请求");
        }
        return Result.success();
    }
}
