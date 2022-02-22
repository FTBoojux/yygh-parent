package com.boojux.servicemsm.service.impl;

import com.boojux.servicemsm.service.MsmService;
import com.example.yygh.vo.msm.MsmVo;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class MsmServiceImpl implements MsmService {
    @Override
    public boolean send(MsmVo msmVo) {
        //            String code = (String)msmVo.getParam().get("code");
        return StringUtils.isEmpty(msmVo.getPhone());
    }
}
