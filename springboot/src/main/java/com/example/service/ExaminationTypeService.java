package com.example.service;

import cn.hutool.core.date.DateUtil;
import com.example.entity.ExaminationType;
import com.example.mapper.ExaminationTypeMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ExaminationTypeService {

    @Autowired
    private ExaminationTypeMapper examinationTypeMapper;

    public void add(ExaminationType examinationType) {
        examinationTypeMapper.insert(examinationType);
    }

    public void updateById(ExaminationType examinationType) {
        examinationTypeMapper.updateById(examinationType);
    }

    public void deleteById(Integer id) {
        examinationTypeMapper.deleteById(id);
    }

    public void deleteBatch(List<Integer> ids) {
        for (Integer id : ids) {
            examinationTypeMapper.deleteById(id);
        }
    }

    public ExaminationType selectById(Integer id) {
        return examinationTypeMapper.selectById(id);
    }

    public List<ExaminationType> selectAll(ExaminationType examinationType) {
        return examinationTypeMapper.selectAll(examinationType);
    }

    public PageInfo<ExaminationType> selectPage(ExaminationType examinationType, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<ExaminationType> list = examinationTypeMapper.selectAll(examinationType);
        return PageInfo.of(list);
    }

}
