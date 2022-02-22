package com.boojux.yygh.hosp.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.yygh.model.hosp.HospitalSet;
import com.example.yygh.vo.order.SignInfoVo;

public interface HospitalSetService extends IService<HospitalSet> {
    String getSignKey(String hoscode);

    SignInfoVo getSignInfoVo(String hoscode);
}
