package com.example.mapper;

import com.example.entity.Doctor;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface DoctorMapper {


    int insert(Doctor doctor);

    @Select("select * from `doctor` where username = #{username}")
    Doctor selectByUsername(String username);

    List<Doctor> selectAll(Doctor doctor);

    void updateById(Doctor doctor);

    void deleteById(Integer id);

    @Select("select * from `doctor` where id = #{id}")
    Doctor selectById(Integer id);
}
