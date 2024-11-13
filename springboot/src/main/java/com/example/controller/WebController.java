package com.example.controller;

import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.example.common.Result;
import com.example.common.enums.RoleEnum;
import com.example.entity.*;
import com.example.exception.CustomException;
import com.example.mapper.ExaminationOrderMapper;
import com.example.mapper.ExaminationPackageMapper;
import com.example.mapper.PhysicalExaminationMapper;
import com.example.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.xml.crypto.Data;
import java.util.*;
import java.util.stream.Collectors;

@RestController
public class WebController {

    @Autowired
    AdminService adminService;
    @Autowired
    UserService userService;
    @Autowired
    DoctorService doctorService;

    @Autowired
    ExaminationOrderMapper examinationOrderMapper;

    @Autowired
    PhysicalExaminationMapper physicalExaminationMapper;
    @Autowired
    ExaminationPackageMapper examinationPackageMapper;

    @Autowired
    private TitleService titleService;

    @Autowired
    private OfficeService officeService;


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

    @GetMapping("/getCountData")
    public Result getCountData() {
        List<ExaminationOrder> examinationOrders = examinationOrderMapper.selectAll(null);
        //普通体检金额统计
        Integer PhysicalExaminationMoney = examinationOrders.stream()
                .filter(o -> o.getOrderType().equals("普通体检"))
                .filter(o -> o.getStatus().equals("待检查") || o.getStatus().equals("待上传报告") || o.getStatus().equals("已完成"))
                .map(ExaminationOrder::getMoney)
                .reduce(Integer::sum).orElse(0);
        //体检套餐金额统计
        Integer PhysicalExaminationPackageMoney = examinationOrders.stream()
                .filter(o -> o.getOrderType().equals("套餐体检"))
                .filter(o -> o.getStatus().equals("待检查") || o.getStatus().equals("待上传报告") || o.getStatus().equals("已完成"))
                .map(ExaminationOrder::getMoney)
                .reduce(Integer::sum).orElse(0);
        // 普通体检项目数量统计
        long physicalExaminationCount = physicalExaminationMapper.selectAll(null).size();
        // 套餐体检项目数量统计
        long examinationPackageCount = examinationPackageMapper.selectAll(null).size();

        HashMap<Object, Object> map = new HashMap<>();
        map.put("PhysicalExaminationMoney", PhysicalExaminationMoney);
        map.put("PhysicalExaminationPackageMoney", PhysicalExaminationPackageMoney);
        map.put("physicalExaminationCount", physicalExaminationCount);
        map.put("examinationPackageCount", examinationPackageCount);
        return Result.success(map);
    }

    @GetMapping("/lineData")
    public Result getLineData() {
        List<ExaminationOrder> examinationOrders = examinationOrderMapper.selectAll(null);
        //获取一个月的日期
        Date date = new Date();
        DateTime start = DateUtil.offsetDay(date, -30); //30天之前的日期、
        List<DateTime> dateTimes = DateUtil.rangeToList(start, date, DateField.DAY_OF_YEAR);
        List<String> dateStrList = dateTimes.stream().map(DateUtil::formatDate).sorted(Comparator.naturalOrder()).collect(Collectors.toList());
        ArrayList<Integer> moneyList = new ArrayList<>();
        for (String day : dateStrList) {
            // 统计当天的销售额
            Integer money = examinationOrders.stream().filter(o -> o.getCreateTime().contains(day))
                    .filter(o -> o.getStatus().equals("待检查") || o.getStatus().equals("待上传报告") || o.getStatus().equals("已完成"))
                    .map(ExaminationOrder::getMoney).reduce(Integer::sum).orElse(0);
            moneyList.add(money);
        }
        HashMap<Object, Object> map = new HashMap<>();
        map.put("dateStrList", dateStrList);
        map.put("moneyList", moneyList);
        return Result.success(map);
    }

    @GetMapping("/pieData")
    public Result getPieData() {
        //name和value
        List<Title> titleList = titleService.selectAll(null);
        List<Map<String, Object>> list = new ArrayList<>();
        for (Title title : titleList) {
            HashMap<String, Object> map = new HashMap<>();
            map.put("name", title.getName());
            Integer count = doctorService.selectByTitleId(title.getId());
            map.put("value", count);
            list.add(map);
        }
        return Result.success(list);
    }

    @GetMapping("/barData")
    public Result getBarData() {
        List<Office> officeList = officeService.selectAll(null);
        List<String> officeNames = officeList.stream().map(Office::getName).collect(Collectors.toList());
        ArrayList<Integer> list = new ArrayList<>();
        for (Office office : officeList) {
            // 统计当天的销售额
            Integer count = doctorService.selectByOfficeId(office.getId());
            list.add(count);
        }
        HashMap<Object, Object> map = new HashMap<>();
        map.put("officeList", officeNames);
        map.put("countList", list);
        return Result.success(map);
    }
}
