package com.shf.yygh.hosp.controller;


import com.shf.yygh.common.result.Result;
import com.shf.yygh.hosp.service.DepartmentService;
import com.shf.yygh.vo.hosp.DepartmentVo;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/admin/hosp/department")
@RestController
public class DepartmentController {
    @Autowired
    private DepartmentService departmentService;

    //    根据医院编号，查询医院所有科室列表
    @ApiOperation("查询医院所有科室列表")
    @GetMapping("getDeptList/{hoscode}")
    public Result getDeptList(@PathVariable String hoscode) {
        List<DepartmentVo> list = departmentService.findDeptTree(hoscode);
        return Result.ok(list);
    }
}
