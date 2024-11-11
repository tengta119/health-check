package com.example.service;

import cn.hutool.core.date.DateUtil;
import com.example.common.enums.RoleEnum;
import com.example.entity.Account;
import com.example.entity.ExaminationOrder;
import com.example.entity.PhysicalExamination;
import com.example.exception.CustomException;
import com.example.mapper.ExaminationOrderMapper;
import com.example.utils.TokenUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class ExaminationOrderService {

    @Autowired
    private ExaminationOrderMapper examinationOrderMapper;
    @Autowired
    private PhysicalExaminationService physicalExaminationService;


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
        return PageInfo.of(list);
    }

}
