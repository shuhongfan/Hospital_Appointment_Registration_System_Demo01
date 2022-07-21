package com.shf.yygh.hosp.api;

import com.shf.yygh.common.result.Result;
import com.shf.yygh.hosp.service.DepartmentService;
import com.shf.yygh.hosp.service.HospitalService;
import com.shf.yygh.hosp.service.HospitalSetService;
import com.shf.yygh.hosp.service.ScheduleService;
import com.shf.yygh.model.hosp.Hospital;
import com.shf.yygh.model.hosp.Schedule;
import com.shf.yygh.vo.hosp.DepartmentVo;
import com.shf.yygh.vo.hosp.HospitalQueryVo;
import com.shf.yygh.vo.hosp.ScheduleOrderVo;
import com.shf.yygh.vo.order.SignInfoVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@Api(tags = "医院管理接口")
@RestController
@RequestMapping("/api/hosp/hospital")
public class HospApiController {
    @Autowired
    private HospitalSetService hospitalSetService;

    @Autowired
    private HospitalService hospitalService;

    @Autowired
    private DepartmentService departmentService;

    @Autowired
    private ScheduleService scheduleService;

    @ApiOperation("查询医院列表")
    @GetMapping("findHospList/{page}/{limit}")
    public Result findHospList(
            @PathVariable int page,
            @PathVariable int limit, HospitalQueryVo hospitalQueryVo) {
        Page<Hospital> hospPage = hospitalService.selectHospPage(page, limit, hospitalQueryVo);
        return Result.ok(hospPage);
    }

    @ApiOperation(value = "根据医院名称获取医院列表")
    @GetMapping("findByHosname/{hosname}")
    public Result findByHosname(
            @ApiParam(name = "hosname", value = "医院名称", required = true)
            @PathVariable String hosname) {
        return Result.ok(hospitalService.findByHosname(hosname));
    }

    @ApiOperation("根据医院编号获取科室")
    @GetMapping("department/{hoscode}")
    public Result index(@PathVariable String hoscode) {
        List<DepartmentVo> list = departmentService.findDeptTree(hoscode);
        return Result.ok(list);
    }

    @ApiOperation(value = "医院预约挂号详情")
    @GetMapping("{hoscode}")
    public Result item(
            @ApiParam(name = "hoscode", value = "医院code", required = true)
            @PathVariable String hoscode) {
        return Result.ok(hospitalService.item(hoscode));
    }

    @ApiOperation("获取可预约排班数据")
    @GetMapping("auth/getBookingScheduleRule/{page}/{limit}/{hoscode}/{depcode}")
    public Result getBookingSchedule(@ApiParam(name = "page", value = "当前页码", required = true)
                                     @PathVariable Integer page,
                                     @ApiParam(name = "limit", value = "每页记录数", required = true)
                                     @PathVariable Integer limit,
                                     @ApiParam(name = "hoscode", value = "医院code", required = true)
                                     @PathVariable String hoscode,
                                     @ApiParam(name = "depcode", value = "科室code", required = true)
                                     @PathVariable String depcode
    ) {
        Map<String, Object> map = scheduleService.getBookingScheduleRule(page, limit, hoscode, depcode);
        return Result.ok(map);
    }

    @ApiOperation(value = "获取排班数据")
    @GetMapping("auth/findScheduleList/{hoscode}/{depcode}/{workDate}")
    public Result findScheduleList(
            @ApiParam(name = "hoscode", value = "医院code", required = true)
            @PathVariable String hoscode,
            @ApiParam(name = "depcode", value = "科室code", required = true)
            @PathVariable String depcode,
            @ApiParam(name = "workDate", value = "排班日期", required = true)
            @PathVariable String workDate) {
        List<Schedule> detailSchedule = scheduleService.getDetailSchedule(hoscode, depcode, workDate);
        return Result.ok(detailSchedule);
    }

    @ApiOperation(value = "根据排班id获取排班数据")
    @GetMapping("getSchedule/{scheduleId}")
    public Result getSchedule(
            @ApiParam(name = "scheduleId", value = "排班id", required = true)
            @PathVariable String scheduleId) {
        return Result.ok(scheduleService.getById(scheduleId));
    }

    @ApiOperation(value = "根据排班id获取预约下单数据")
    @GetMapping("inner/getScheduleOrderVo/{scheduleId}")
    public ScheduleOrderVo getScheduleOrderVo(
            @ApiParam(name = "scheduleId", value = "排班id", required = true)
            @PathVariable("scheduleId") String scheduleId) {
        return scheduleService.getScheduleOrderVo(scheduleId);
    }

    @ApiOperation(value = "获取医院签名信息")
    @GetMapping("inner/getSignInfoVo/{hoscode}")
    public SignInfoVo getSignInfoVo(
            @ApiParam(name = "hoscode", value = "医院code", required = true)
            @PathVariable("hoscode") String hoscode) {
        return hospitalSetService.getSignInfoVo(hoscode);
    }


}
