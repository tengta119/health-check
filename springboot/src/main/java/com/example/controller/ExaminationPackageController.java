package com.example.controller;

import com.example.common.Result;
import com.example.entity.ExaminationPackage;
import com.example.service.ExaminationPackageService;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/examinationPackage")
public class ExaminationPackageController {

    @Autowired
    private ExaminationPackageService examinationPackageService;

    /**
     * 新增
     */
    @PostMapping("/add")
    public Result add(@RequestBody ExaminationPackage examinationPackage) {
        examinationPackageService.add(examinationPackage);
        return Result.success();
    }

    /**
     * 修改
     */
    @PutMapping("/update")
    public Result update(@RequestBody ExaminationPackage examinationPackage) {
        examinationPackageService.updateById(examinationPackage);
        return Result.success();
    }

    /**
     * 单个删除
     */
    @DeleteMapping("/delete/{id}")
    public Result delete(@PathVariable Integer id) {
        examinationPackageService.deleteById(id);
        return Result.success();
    }

    /**
     * 批量删除
     */
    @DeleteMapping("/delete/batch")
    public Result delete(@RequestBody List<Integer> ids) {
        examinationPackageService.deleteBatch(ids);
        return Result.success();
    }

    /**
     * 单个查询
     */
    @GetMapping("/selectById/{id}")
    public Result selectById(@PathVariable Integer id) {
        ExaminationPackage examinationPackage = examinationPackageService.selectById(id);
        return Result.success(examinationPackage);
    }

    /**
     * 查询所有
     */
    @GetMapping("/selectAll")
    public Result selectAll(ExaminationPackage examinationPackage) {
        List<ExaminationPackage> list = examinationPackageService.selectAll(examinationPackage);
        return Result.success(list);
    }

    /**
     * 分页查询
     */
    @GetMapping("/selectPage")
    public Result selectPage(ExaminationPackage examinationPackage,
                             @RequestParam(defaultValue = "1") Integer pageNum,
                             @RequestParam(defaultValue = "10") Integer pageSize) {
        PageInfo<ExaminationPackage> pageInfo = examinationPackageService.selectPage(examinationPackage, pageNum, pageSize);
        return Result.success(pageInfo);
    }

}
