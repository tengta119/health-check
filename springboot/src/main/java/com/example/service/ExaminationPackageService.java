package com.example.service;

import cn.hutool.core.date.DateUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONUtil;
import com.example.entity.ExaminationPackage;
import com.example.entity.PhysicalExamination;
import com.example.mapper.ExaminationPackageMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ExaminationPackageService {

    @Autowired
    private ExaminationPackageMapper examinationPackageMapper;
    @Autowired
    private PhysicalExaminationService physicalExaminationService;

    public void add(ExaminationPackage examinationPackage) {
        examinationPackageMapper.insert(examinationPackage);
    }

    public void updateById(ExaminationPackage examinationPackage) {
        examinationPackageMapper.updateById(examinationPackage);
    }

    public void deleteById(Integer id) {
        examinationPackageMapper.deleteById(id);
    }

    public void deleteBatch(List<Integer> ids) {
        for (Integer id : ids) {
            examinationPackageMapper.deleteById(id);
        }
    }

    public ExaminationPackage selectById(Integer id) {
        return examinationPackageMapper.selectById(id);
    }

    public List<ExaminationPackage> selectAll(ExaminationPackage examinationPackage) {
        return examinationPackageMapper.selectAll(examinationPackage);
    }

    public PageInfo<ExaminationPackage> selectPage(ExaminationPackage examinationPackage, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<ExaminationPackage> list = examinationPackageMapper.selectAll(examinationPackage);
        for (ExaminationPackage p : list) {
            List<PhysicalExamination> examinationList = new ArrayList<>();
            String examinations = p.getExaminations();
            JSONArray examinationIds = JSONUtil.parseArray(examinations);
            // 遍历体检项目id
            for (Object examinationId : examinationIds) {
                PhysicalExamination physicalExamination = physicalExaminationService.selectById((Integer) examinationId);// 查询普通体检的信息
                examinationList.add(physicalExamination);
            }
            p.setExaminationList(examinationList);
        }
        return PageInfo.of(list);
    }

}
