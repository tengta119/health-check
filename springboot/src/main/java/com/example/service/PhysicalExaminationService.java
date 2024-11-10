package com.example.service;

import com.example.entity.PhysicalExamination;
import com.example.mapper.PhysicalExaminationMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PhysicalExaminationService {

    @Autowired
    private PhysicalExaminationMapper physicalExaminationMapper;

    public void add(PhysicalExamination physicalExamination) {
        physicalExaminationMapper.insert(physicalExamination);
    }

    public void updateById(PhysicalExamination physicalExamination) {
        physicalExaminationMapper.updateById(physicalExamination);
    }

    public void deleteById(Integer id) {
        physicalExaminationMapper.deleteById(id);
    }

    public void deleteBatch(List<Integer> ids) {
        for (Integer id : ids) {
            physicalExaminationMapper.deleteById(id);
        }
    }

    public PhysicalExamination selectById(Integer id) {
        return physicalExaminationMapper.selectById(id);
    }

    public List<PhysicalExamination> selectAll(PhysicalExamination physicalExamination) {
        return physicalExaminationMapper.selectAll(physicalExamination);
    }

    public PageInfo<PhysicalExamination> selectPage(PhysicalExamination physicalExamination, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<PhysicalExamination> list = physicalExaminationMapper.selectAll(physicalExamination);
        return PageInfo.of(list);
    }

}
