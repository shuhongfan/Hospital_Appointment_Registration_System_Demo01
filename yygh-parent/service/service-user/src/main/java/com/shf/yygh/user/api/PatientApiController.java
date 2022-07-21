package com.shf.yygh.user.api;

import com.shf.yygh.common.result.Result;
import com.shf.yygh.common.utils.AuthContextHolder;
import com.shf.yygh.model.user.Patient;
import com.shf.yygh.user.service.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/api/user/patient")
public class PatientApiController {

    @Autowired
    private PatientService patientService;

    //    用户就诊人列表
    @GetMapping("auth/findAll")
    public Result findAll(HttpServletRequest request) {
//        获取当前登录用户id
        Long userId = AuthContextHolder.getUserId(request);
        List<Patient> patientList = patientService.findAllUserId(userId);
        return Result.ok(patientList);
    }

    //    添加就诊人
    @PostMapping("auth/save")
    public Result savePatient(@RequestBody Patient patient, HttpServletRequest request) {
//        获取当前登录用户id
        Long userId = AuthContextHolder.getUserId(request);
        patient.setUserId(userId);
        patientService.save(patient);
        return Result.ok();
    }

    //    根据id获取就诊人信息
    @GetMapping("auth/get/{id}")
    public Result getPatient(@PathVariable long id) {
        Patient patient = patientService.getPatientId(id);
        return Result.ok(patient);
    }

    //    修改就诊人
    @PostMapping("auth/update")
    public Result updatePatient(@RequestBody Patient patient) {
        patientService.updateById(patient);
        return Result.ok();
    }

    //    删除就诊人
    @DeleteMapping("auth/remove/{id}")
    public Result removePatient(@PathVariable long id) {
        patientService.removeById(id);
        return Result.ok();
    }

    //根据就诊人id获取就诊人信息
    @GetMapping("inner/get/{id}")
    public Patient getPatientOrder(@PathVariable Long id) {
        Patient patient = patientService.getPatientId(id);
        return patient;
    }
}
