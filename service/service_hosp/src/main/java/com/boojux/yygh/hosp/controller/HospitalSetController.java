package com.boojux.yygh.hosp.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.yygh.common.result.Result;
import com.example.yygh.common.utils.MD5;
import com.boojux.yygh.hosp.service.HospitalSetService;
import com.example.yygh.model.hosp.HospitalSet;
import com.example.yygh.vo.hosp.HospitalSetQueryVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Random;

@Api(tags = "医院设置管理")
@RestController
@RequestMapping("/damin/hosp/hospitalSet")
//@CrossOrigin
public class HospitalSetController {
    @Autowired
    private HospitalSetService hospitalSetService;
//    查询所有医院信息
    @ApiOperation(value = "获取所有医院设置")
    @GetMapping("findAll")
    public Result findAll(){
        List<HospitalSet> list = hospitalSetService.list();
        return Result.ok(list);
    }
//    根据id逻辑删除医院
    @ApiOperation("逻辑删除医院设置")
    @DeleteMapping("{id}")
    public Result removeHospital(@PathVariable("id") Long id){
        boolean b = hospitalSetService.removeById(id);
//        return Result.ok(b);
        if(b) return Result.ok();
        else return Result.fail();
    }
// 分页查询
    @PostMapping("findPageHospSet/{current}/{limit}")
    public Result findPageHospSet(
            @PathVariable long current,
            @PathVariable long limit,
            @RequestBody(required = false) HospitalSetQueryVo hospitalSetQueryVo){
        Page<HospitalSet> page = new Page<>(current,limit);
        QueryWrapper<HospitalSet> wrapper = new QueryWrapper<>();
        String hosname = hospitalSetQueryVo.getHosname();
        String hoscode = hospitalSetQueryVo.getHoscode();
        if(!StringUtils.isEmpty(hosname)) wrapper.like("hosname",hosname);
        if(!StringUtils.isEmpty(hoscode)) wrapper.eq("hoscode",hoscode);
        Page<HospitalSet> hospitalSetPage = hospitalSetService.page(page,wrapper);
        return Result.ok(hospitalSetPage);
    }

//    添加医院
    @PostMapping("saveHospitalSet")
    public Result saveHospitalSet(@RequestBody HospitalSet hospitalSet){
        hospitalSet.setStatus(1);
        Random random = new Random();
        hospitalSet.setSignKey(MD5.encrypt(System.currentTimeMillis()+""+random.nextInt(1000)));
        boolean save = hospitalSetService.save(hospitalSet);
        if(save) return Result.ok();
        else return Result.fail();
    }

//    根据id查询
    @GetMapping("getHospSet/{id}")
    public Result getHospSet(@PathVariable long id){
        HospitalSet hospitalSet = hospitalSetService.getById(id);
        return Result.ok(hospitalSet);
    }

//    修改医院设置
    @PostMapping("updateHospSet")
    public Result updateHospSet(@RequestBody HospitalSet hospitalSet){
        boolean res = hospitalSetService.updateById(hospitalSet);
        if(res) return Result.ok();
        else return Result.fail();
    }
//    批量删除
    @DeleteMapping("batchRemove")
    public Result batchRemove(@RequestBody List<Long> ids){
        hospitalSetService.removeByIds(ids);
        return Result.ok();
    }
//    8医院锁定和解锁
    @PutMapping("lockHospSet/{id}/{status}")
    public Result lockHospSet(@PathVariable long id,@PathVariable Integer status){
        HospitalSet hospitalSet = hospitalSetService.getById(id);
        hospitalSet.setStatus(status);
        hospitalSetService.updateById(hospitalSet);
        return Result.ok();
    }
//    9发送签名密钥
    @PutMapping("sendKey/{id}")
    public Result sendKey(@PathVariable long id){
        HospitalSet hospitalSet = hospitalSetService.getById(id);
        String signKey = hospitalSet.getSignKey();
        String hoscode = hospitalSet.getHoscode();
//        TODO:发送短信
        return Result.ok();

    }
}