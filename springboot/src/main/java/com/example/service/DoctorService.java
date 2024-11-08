package com.example.service;

import cn.hutool.core.util.ObjectUtil;
import com.example.common.enums.ResultCodeEnum;
import com.example.common.enums.RoleEnum;
import com.example.entity.Account;
import com.example.entity.Doctor;
import com.example.exception.CustomException;
import com.example.mapper.DoctorMapper;
import com.example.utils.TokenUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.example.common.Constants.USER_DEFAULT_PASSWORD;

@Service
public class DoctorService {

    @Autowired
    DoctorMapper doctorMapper;

    public void add(Doctor doctor) {
        Doctor dbdoctor = doctorMapper.selectByUsername(doctor.getUsername());
        if (ObjectUtil.isNotNull(dbdoctor)) {
            throw new CustomException(ResultCodeEnum.USER_EXIST_ERROR);
        }
        if (ObjectUtil.isEmpty(doctor.getPassword())) {
            doctor.setPassword(USER_DEFAULT_PASSWORD);
        }
        if (ObjectUtil.isEmpty(doctor.getName())) {
            doctor.setName(doctor.getUsername());
        }
        doctor.setRole(RoleEnum.Doctor.name());
        doctorMapper.insert(doctor);
    }

    public PageInfo<Doctor> selcetPage(Doctor doctor, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Doctor> list =  doctorMapper.selectAll(doctor);
        return PageInfo.of(list);
    }

    public List<Doctor> selectAll(Doctor doctor) {
        return doctorMapper.selectAll(doctor);
    }

    public Doctor selectById(Integer id) {
        return doctorMapper.selectById(id);
    }

    public void updateById(Doctor doctor) {
        doctorMapper.updateById(doctor);
    }

    public void deleteById(Integer id) {
        doctorMapper.deleteById(id);
    }

    public void deleteBatch(List<Integer> ids) {
        for (Integer id : ids) {
            deleteById(id);
        }
    }

    //登录
    public Doctor login(Account account) {
        Doctor doctor = doctorMapper.selectByUsername(account.getUsername());
        if (ObjectUtil.isNull(doctor)) {
            throw new CustomException(ResultCodeEnum.USER_NOT_EXIST_ERROR);
        }
        if (!doctor.getPassword().equals(account.getPassword())) {
            throw new CustomException(ResultCodeEnum.USER_ACCOUNT_ERROR);
        }
        //生成token
        String token = TokenUtils.createToken(doctor.getId() + "-" + doctor.getRole(), doctor.getPassword());
        doctor.setToken(token);
        return doctor;
    }

    public void updatePassword(Account account) {
        Doctor dbDoctor = doctorMapper.selectByUsername(account.getUsername());
        if (ObjectUtil.isNull(dbDoctor)) {
            throw new CustomException(ResultCodeEnum.USER_NOT_EXIST_ERROR);
        }
        if (!account.getPassword().equals(dbDoctor.getPassword())) {
            throw new CustomException(ResultCodeEnum.PARAM_PASSWORD_ERROR);
        }
        dbDoctor.setPassword(account.getNewPassword());
        doctorMapper.updateById(dbDoctor);
    }
}
