package com.example.controller;

import com.example.common.Result;
import com.example.entity.ExaminationOrder;
import com.example.service.ExaminationOrderService;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/examinationOrder")
public class ExaminationOrderController {

    @Autowired
    private ExaminationOrderService examinationOrderService;

    /**
     * 新增
     */
    @PostMapping("/add")
    public Result add(@RequestBody ExaminationOrder examinationOrder) {
        examinationOrderService.add(examinationOrder);
        return Result.success();
    }

    /**
     * 修改
     */
    @PutMapping("/update")
    public Result update(@RequestBody ExaminationOrder examinationOrder) {
        examinationOrderService.updateById(examinationOrder);
        return Result.success();
    }

    /**
     * 单个删除
     */
    @DeleteMapping("/delete/{id}")
    public Result delete(@PathVariable Integer id) {
        examinationOrderService.deleteById(id);
        return Result.success();
    }

    /**
     * 批量删除
     */
    @DeleteMapping("/delete/batch")
    public Result delete(@RequestBody List<Integer> ids) {
        examinationOrderService.deleteBatch(ids);
        return Result.success();
    }

    /**
     * 单个查询
     */
    @GetMapping("/selectById/{id}")
    public Result selectById(@PathVariable Integer id) {
        ExaminationOrder examinationOrder = examinationOrderService.selectById(id);
        return Result.success(examinationOrder);
    }

    /**
     * 查询所有
     */
    @GetMapping("/selectAll")
    public Result selectAll(ExaminationOrder examinationOrder) {
        List<ExaminationOrder> list = examinationOrderService.selectAll(examinationOrder);
        return Result.success(list);
    }

    /**
     * 分页查询
     */
    @GetMapping("/selectPage")
    public Result selectPage(ExaminationOrder examinationOrder,
                             @RequestParam(defaultValue = "1") Integer pageNum,
                             @RequestParam(defaultValue = "10") Integer pageSize) {
        PageInfo<ExaminationOrder> pageInfo = examinationOrderService.selectPage(examinationOrder, pageNum, pageSize);
        return Result.success(pageInfo);
    }

    @GetMapping("/schedule")
    public Result selectSchedule() {
        List<ExaminationOrder> examinationOrders = examinationOrderService.selectScheduleData();
        return Result.success(examinationOrders);
    }

}
