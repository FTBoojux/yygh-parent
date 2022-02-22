package com.boojux.yygh.hosp.controller;

import com.boojux.yygh.hosp.service.DepartmentService;
import com.boojux.yygh.hosp.service.HospitalService;
import com.boojux.yygh.hosp.service.HospitalSetService;
import com.boojux.yygh.hosp.service.ScheduleService;
import com.example.yygh.common.result.Result;
import com.example.yygh.model.hosp.Hospital;
import com.example.yygh.vo.hosp.HospitalQueryVo;
import com.example.yygh.vo.hosp.ScheduleOrderVo;
import com.example.yygh.vo.order.SignInfoVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/hosp/hospital")
public class HospApiController {
    @Autowired
    private HospitalService hospitalService;
    @Autowired
    private DepartmentService departmentService;
    @Autowired
    private ScheduleService scheduleService;
    @Autowired
    private HospitalSetService hospitalSetService;
    @GetMapping("{page}/{limit}")
    public Result index(@PathVariable Integer page,
                        @PathVariable Integer limit,
                        HospitalQueryVo hospitalQueryVo
                        ){
        Page<Hospital> hospitals = hospitalService.selectHospPage(page, limit, hospitalQueryVo);
        return Result.ok(hospitals);
    }

    @GetMapping("findByHosname/{hosname}")
    public Result findByHosname(@PathVariable String hosname){
        List<Hospital> hospitals = hospitalService.findByHosname(hosname);
        return Result.ok(hospitals);
    }

    @GetMapping("department/{hoscode}")
    public Result index(@PathVariable String hoscode){
        return Result.ok(departmentService.findDeptTree(hoscode));
    }

    @GetMapping("{hoscode}")
    public Result item(@PathVariable String hoscode){
        Map<String,Object> res = hospitalService.item(hoscode);
        return Result.ok(res);
    }

    @GetMapping("auth/getBookingScheduleRule/{page}/{limit}/{hoscode}/{depcode}")
    public Result getBookingSchedule(
            @PathVariable Integer page,
            @PathVariable Integer limit,
            @PathVariable String hoscode,
            @PathVariable String depcode
    ){
        return Result.ok(scheduleService.getBookingScheduleRule(page, limit, hoscode, depcode));
    }

    @GetMapping("auth/findScheduleList/{hoscode}/{depcode}/{workDate}")
    public Result findScheduleList(
            @PathVariable String hoscode,
            @PathVariable String depcode,
            @PathVariable String workDate
    ){
        return Result.ok(scheduleService.getDetailSchedule(hoscode, depcode, workDate));
    }

    @GetMapping("getSchedule/{scheduleId}")
    public Result getSchedule(@PathVariable String scheduleId){
        return Result.ok(scheduleService.getById(scheduleId));
    }

    @GetMapping("inner/getScheduleOrderVo/{scheduleId}")
    public ScheduleOrderVo getScheduleOrderVo(@PathVariable("scheduleId") String scheduleId){
        return scheduleService.getScheduleOrderVo(scheduleId);
    }

    @GetMapping("inner/getSignInfoVo/{hoscode}")
    public SignInfoVo getSignInfoVo(@PathVariable("hoscode") String hoscode){
        return hospitalSetService.getSignInfoVo(hoscode);
    }
}
