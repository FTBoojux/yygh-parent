package com.boojux.yygh.hosp.controller;
import com.boojux.yygh.hosp.service.ScheduleService;
import com.example.yygh.common.result.Result;
import com.example.yygh.model.hosp.Schedule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/damin/hosp/schedule")
//@CrossOrigin
public class ScheduleController {
    @Autowired
    private ScheduleService scheduleService;

    @GetMapping("getScheduleRule/{page}/{limit}/{hoscode}/{depcode}")
    public Result getScheduleRule(@PathVariable long page,
                                  @PathVariable long limit,
                                  @PathVariable String hoscode,
                                  @PathVariable String depcode){
        Map<String,Object> map = scheduleService.getScheduleRule(page,limit,hoscode,depcode);
        return Result.ok(map);
    }

    @GetMapping("getScheduleDetail/{hoscode}/{depcode}/{workDate}")
    public Result getScheduleDetail(@PathVariable String hoscode,
                                    @PathVariable String depcode,
                                    @PathVariable String workDate){
        List<Schedule> list = scheduleService.getDetailSchedule(hoscode,depcode,workDate);
        return Result.ok(list);
    }
}
