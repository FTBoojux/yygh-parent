package com.boojux.yygh.hosp.controller;

import com.boojux.yygh.hosp.service.DepartmentService;
import com.example.yygh.common.result.Result;
import com.example.yygh.vo.hosp.DepartmentVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("/damin/hosp/department")
//@CrossOrigin
public class DepartmentController {

    @Autowired
    private DepartmentService departmentService;

    @GetMapping("getDeptList/{hoscode}")
    public Result getDepeList(@PathVariable String hoscode){
        System.out.println(hoscode);
        List<DepartmentVo> list = departmentService.findDeptTree(hoscode);
        return Result.ok(list);
    }

}
