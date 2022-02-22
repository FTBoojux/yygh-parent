package com.boojux.yygh.hosp.controller;

import com.boojux.yygh.hosp.service.HospitalService;
import com.example.yygh.common.result.Result;
import com.example.yygh.model.hosp.Hospital;
import com.example.yygh.vo.hosp.HospitalQueryVo;
import com.example.yygh.vo.hosp.HospitalSetQueryVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/damin/hosp/hospital")
//@CrossOrigin
public class HospitalController {
    @Autowired
    private HospitalService hospitalService;
// provinceCode
    @GetMapping("list/{page}/{limit}")
    private Result listHosp(@PathVariable Integer page,
                            @PathVariable Integer limit,
                            HospitalQueryVo hospitalQueryVo
    ){
        Page pageModel = hospitalService.selectHospPage(page,limit,hospitalQueryVo);
        System.out.println("?");
        System.out.println(hospitalQueryVo);
        System.out.println(pageModel);
        return Result.ok(pageModel);
    }

    @GetMapping("updateHospitalStatus/{id}/{status}")
    public Result updateHospitalStatus(@PathVariable String id,@PathVariable Integer status){
        hospitalService.updateStatus(id,status);
        return Result.ok();
    }

    @GetMapping("showHospital/{id}")
    public Result showHospital(@PathVariable String id){
        Map<String,Object> hospital = hospitalService.getHospById(id);
        return Result.ok(hospital);
    }
}
