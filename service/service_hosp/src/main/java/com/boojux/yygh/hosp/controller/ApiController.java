package com.boojux.yygh.hosp.controller;

import com.example.yygh.common.exception.YyghException;
import com.example.yygh.common.result.Result;
import com.example.yygh.common.result.ResultCodeEnum;
import com.boojux.yygh.hosp.helper.HttpRequestHelper;
import com.boojux.yygh.hosp.service.DepartmentService;
import com.boojux.yygh.hosp.service.HospitalService;
import com.boojux.yygh.hosp.service.HospitalSetService;
import com.boojux.yygh.hosp.service.ScheduleService;
import com.boojux.yygh.hosp.utils.MD5;
import com.example.yygh.model.hosp.Department;
import com.example.yygh.model.hosp.Hospital;
import com.example.yygh.model.hosp.Schedule;
import com.example.yygh.vo.hosp.DepartmentQueryVo;
import com.example.yygh.vo.hosp.ScheduleQueryVo;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Api(tags = "医院管理api接口")
@RestController
@RequestMapping("/api/hosp")
//@CrossOrigin
public class ApiController {
    @Autowired
    private HospitalService hospitalService;
    @Autowired
    private HospitalSetService hospitalSetService;
    @Autowired
    private DepartmentService departmentService;
    @Autowired
    private ScheduleService scheduleService;

    @PostMapping("saveHospital")
    public Result saveHosp(HttpServletRequest request){
//        获取信息
        Map<String, String[]> map = request.getParameterMap();
        Map<String, Object> paramMap = HttpRequestHelper.switchMap(map);
//        hospitalService.save(paramMap);
//        获取签名，并进行md5加密
        String hospSign = (String) paramMap.get("sign");
//        根据医院编码查询数据库，查询签名
        String hoscode = (String)paramMap.get("hoscode");
        String signKey = hospitalSetService.getSignKey(hoscode);
        System.out.println("signKey:" + signKey);
//        将查询的签名加密
        String signKeyMD5 = MD5.encrypt(signKey);
        System.out.println("signKeyND5" + signKeyMD5);
        if(!hospSign.equals(signKeyMD5)){
            throw new YyghException(ResultCodeEnum.SIGN_ERROR);
        }
//        修正传输过程中 “+” -》 “ ”
        String logoData = (String) paramMap.get("logoData");
        logoData = logoData.replaceAll(" ","+");
        paramMap.put("logoData",logoData);

        hospitalService.save(paramMap);
        return Result.ok();
    }

    @PostMapping("hospital/show")
    public Result getHospital(HttpServletRequest request){
        //        获取信息
        Map<String, String[]> map = request.getParameterMap();
        Map<String, Object> paramMap = HttpRequestHelper.switchMap(map);
//        hospitalService.save(paramMap);
//        获取签名，并进行md5加密
        String hoscode = (String)paramMap.get("hoscode");
        if(StringUtils.isEmpty(hoscode)){
            throw new YyghException(ResultCodeEnum.PARAM_ERROR);
        }
        if(!HttpRequestHelper.isSignEquals(paramMap,hospitalSetService.getSignKey(hoscode))){
            throw  new YyghException(ResultCodeEnum.SIGN_ERROR);
        }
        Hospital hospital = hospitalService.getByHoscode(hoscode);
        return Result.ok(hospital);
    }

    @PostMapping("saveDepartment")
    public Result saveDepartment(HttpServletRequest request){
        //        获取信息
        Map<String, String[]> map = request.getParameterMap();
        Map<String, Object> paramMap = HttpRequestHelper.switchMap(map);
//        hospitalService.save(paramMap);
//        获取签名，并进行md5加密
        String hoscode = (String)paramMap.get("hoscode");
        if(StringUtils.isEmpty(hoscode)){
            throw new YyghException(ResultCodeEnum.PARAM_ERROR);
        }
        if(!HttpRequestHelper.isSignEquals(paramMap,hospitalSetService.getSignKey(hoscode))){
            throw  new YyghException(ResultCodeEnum.SIGN_ERROR);
        }
        departmentService.save(paramMap);

        return Result.ok();
    }

    @PostMapping("department/list")
    public Result findDepartment(HttpServletRequest request){
        //        获取信息
        Map<String, String[]> map = request.getParameterMap();
        Map<String, Object> paramMap = HttpRequestHelper.switchMap(map);
//                医院编号
        String hoscode = (String) paramMap.get("hoscode");
        if(!HttpRequestHelper.isSignEquals(paramMap,hospitalSetService.getSignKey(hoscode))){
            throw  new YyghException(ResultCodeEnum.SIGN_ERROR);
        }

        int page = StringUtils.isEmpty(paramMap.get("page"))?1:Integer.parseInt((String) paramMap.get("page"));
        int limit = StringUtils.isEmpty(paramMap.get("limit"))?1:Integer.parseInt((String) paramMap.get("limit"));

        DepartmentQueryVo departmentQueryVo = new DepartmentQueryVo();
        departmentQueryVo.setHoscode(hoscode);

        Page<Department> pageDepartment = departmentService.findPageDepartment(page,limit,departmentQueryVo);
        return Result.ok(pageDepartment);
    }

    @PostMapping("department/remove")
    public Result removeDepartment(HttpServletRequest request){
        //        获取信息
        Map<String, String[]> map = request.getParameterMap();
        Map<String, Object> paramMap = HttpRequestHelper.switchMap(map);
//                医院编号
        String hoscode = (String) paramMap.get("hoscode");
        if(!HttpRequestHelper.isSignEquals(paramMap,hospitalSetService.getSignKey(hoscode))){
            throw  new YyghException(ResultCodeEnum.SIGN_ERROR);
        }

        String depcode = (String) paramMap.get("depcode");
        departmentService.remove(hoscode,depcode);
        return Result.ok();
    }

    @PostMapping("saveSchedule")
    public Result saveSchedule(HttpServletRequest request){
        //        获取信息
        Map<String, String[]> map = request.getParameterMap();
        Map<String, Object> paramMap = HttpRequestHelper.switchMap(map);
//                医院编号
        String hoscode = (String) paramMap.get("hoscode");
        if(!HttpRequestHelper.isSignEquals(paramMap,hospitalSetService.getSignKey(hoscode))){
            throw  new YyghException(ResultCodeEnum.SIGN_ERROR);
        }
        scheduleService.save(paramMap);
        return Result.ok();

    }

    @PostMapping("schedule/list")
    public Result findSchedule(HttpServletRequest request){
        //        获取信息
        Map<String, String[]> map = request.getParameterMap();
        Map<String, Object> paramMap = HttpRequestHelper.switchMap(map);
//                医院编号
        String hoscode = (String) paramMap.get("hoscode");
        if(!HttpRequestHelper.isSignEquals(paramMap,hospitalSetService.getSignKey(hoscode))){
            throw  new YyghException(ResultCodeEnum.SIGN_ERROR);
        }
        String depcode = (String) paramMap.get("depcode");
        int page = StringUtils.isEmpty(paramMap.get("page"))?1:Integer.parseInt((String) paramMap.get("page"));
        int limit = StringUtils.isEmpty(paramMap.get("limit"))?1:Integer.parseInt((String) paramMap.get("limit"));

        ScheduleQueryVo scheduleQueryVo = new ScheduleQueryVo();
        scheduleQueryVo.setHoscode(hoscode);
        scheduleQueryVo.setDepcode(depcode);

        Page<Schedule> pageSchedule = scheduleService.findPageSchedule(page,limit,scheduleQueryVo);
        return Result.ok(pageSchedule);
    }

    @PostMapping("schedule/remove")
    public Result removeSchedule(HttpServletRequest request){
        //        获取信息
        Map<String, String[]> map = request.getParameterMap();
        Map<String, Object> paramMap = HttpRequestHelper.switchMap(map);
//                医院编号
        String hoscode = (String) paramMap.get("hoscode");
        if(!HttpRequestHelper.isSignEquals(paramMap,hospitalSetService.getSignKey(hoscode))){
            throw  new YyghException(ResultCodeEnum.SIGN_ERROR);
        }

        String hosScheduleId = (String) paramMap.get("hosScheduleId");
        scheduleService.remove(hoscode,hosScheduleId);
        return Result.ok();
    }

}

