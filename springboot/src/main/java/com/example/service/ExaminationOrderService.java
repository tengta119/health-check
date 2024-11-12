package com.example.service;

import cn.hutool.core.date.DateUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONUtil;
import com.example.common.enums.RoleEnum;
import com.example.entity.Account;
import com.example.entity.ExaminationOrder;
import com.example.entity.ExaminationPackage;
import com.example.entity.PhysicalExamination;
import com.example.exception.CustomException;
import com.example.mapper.ExaminationOrderMapper;
import com.example.utils.TokenUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class ExaminationOrderService {

    @Autowired
    private ExaminationOrderMapper examinationOrderMapper;
    @Autowired
    private PhysicalExaminationService physicalExaminationService;
    @Autowired
    private ExaminationPackageService examinationPackageService;


    public void add(ExaminationOrder examinationOrder) {
        examinationOrder.setCreateTime(DateUtil.now());
        Account currentUser = TokenUtils.getCurrentUser();

        examinationOrder.setUserId(currentUser.getId());

        //预约的项目
        Integer examinationId = examinationOrder.getExaminationId();
        if ("普通体检".equals(examinationOrder.getOrderType())) {

            // 判断当前是否存在相同项目待审批的订单
            ExaminationOrder order = examinationOrderMapper.selectByExaminationIdAndOrderType(examinationOrder.getReserveDate(), examinationId, "普通体检", currentUser.getId());
            if (order != null) {
                throw new CustomException("500", "您已经预约过该项目" + order.getReserveDate() + "的检查，请不要重复预约");
            }
            PhysicalExamination physicalExamination = physicalExaminationService.selectById(examinationId);
            examinationOrder.setMoney(physicalExamination.getMoney());
            examinationOrder.setDoctorId(physicalExamination.getDoctorId());
        } else {
            //套餐体检
            ExaminationOrder order = examinationOrderMapper.selectByExaminationIdAndOrderType(examinationOrder.getReserveDate(), examinationId, "套餐体检", currentUser.getId());
            if (order != null) {
                throw new CustomException("500", "您已经预约过该项目" + order.getReserveDate() + "的检查，请不要重复预约");
            }
            ExaminationPackage examinationPackage = examinationPackageService.selectById(examinationId);
            examinationOrder.setMoney(examinationPackage.getMoney());
            examinationOrder.setDoctorId(examinationPackage.getDoctorId());
        }
        Date date = new Date();
        String orderNo = DateUtil.format(date, "yyyyMMdd") + date.getTime();
        examinationOrder.setOrderNo(orderNo);
        examinationOrder.setStatus("待审批");
        examinationOrderMapper.insert(examinationOrder);
    }

    public void updateById(ExaminationOrder examinationOrder) {
        if (examinationOrder.getStatus().equals("待上传报告")) {
            examinationOrder.setCheckTime(DateUtil.now());
        }
        examinationOrderMapper.updateById(examinationOrder);
    }

    public void deleteById(Integer id) {
        examinationOrderMapper.deleteById(id);
    }

    public void deleteBatch(List<Integer> ids) {
        for (Integer id : ids) {
            examinationOrderMapper.deleteById(id);
        }
    }

    public ExaminationOrder selectById(Integer id) {
        return examinationOrderMapper.selectById(id);
    }

    public List<ExaminationOrder> selectAll(ExaminationOrder examinationOrder) {
        return examinationOrderMapper.selectAll(examinationOrder);
    }

    public PageInfo<ExaminationOrder> selectPage(ExaminationOrder examinationOrder, Integer pageNum, Integer pageSize) {
        Account currentUser = TokenUtils.getCurrentUser();
        if (currentUser != null && RoleEnum.USER.name().equals(currentUser.getRole())) {
            examinationOrder.setUserId(currentUser.getId());
        }
        if (currentUser != null && RoleEnum.DOCTOR.name().equals(currentUser.getRole())) {
            examinationOrder.setDoctorId(currentUser.getId());
        }
        PageHelper.startPage(pageNum, pageSize);
        List<ExaminationOrder> list = examinationOrderMapper.selectAll(examinationOrder);


        for (ExaminationOrder order : list) {
            // 初始化一个列表来存储每个订单的体检项目
            List<PhysicalExamination> examinationList = new ArrayList<>();

            // 检查订单类型是否为“套餐体检”
            if (order.getOrderType().equals("套餐体检")) {
                // 从订单中获取体检ID
                Integer examinationId = order.getExaminationId();

                // 使用体检ID检索体检套餐
                ExaminationPackage examinationPackage = examinationPackageService.selectById(examinationId);

                // 将套餐中的体检项目解析为JSON数组
                JSONArray examinationIds = JSONUtil.parseArray(examinationPackage.getExaminations());

                // 遍历JSON数组中的每个体检ID
                for (Object physicalExaminationId : examinationIds) {
                    // 使用ID检索体检项目并将其添加到列表中
                    PhysicalExamination physicalExamination = physicalExaminationService.selectById((Integer) physicalExaminationId);
                    examinationList.add(physicalExamination);
                }
            }

            // 将体检项目列表设置到订单中
            order.setExaminationList(examinationList);
        }

        return PageInfo.of(list);
    }

    public List<ExaminationOrder> selectScheduleData() {
        Account currentUser = TokenUtils.getCurrentUser();
        List<ExaminationOrder> examinationOrderList =  examinationOrderMapper.selectPrepareOrders(currentUser.getId());
        return examinationOrderList;
    }
}
