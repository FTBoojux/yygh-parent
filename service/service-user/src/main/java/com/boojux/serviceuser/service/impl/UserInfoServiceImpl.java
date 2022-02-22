package com.boojux.serviceuser.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.boojux.serviceuser.mapper.UserInfoMapper;
import com.boojux.serviceuser.service.PatientService;
import com.boojux.serviceuser.service.UserInfoService;
import com.example.yygh.common.exception.YyghException;
import com.example.yygh.common.helper.JwtHelper;
import com.example.yygh.common.result.ResultCodeEnum;
import com.example.yygh.enums.AuthStatusEnum;
import com.example.yygh.model.user.Patient;
import com.example.yygh.model.user.UserInfo;
import com.example.yygh.vo.user.LoginVo;
import com.example.yygh.vo.user.UserAuthVo;
import com.example.yygh.vo.user.UserInfoQueryVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserInfoServiceImpl extends ServiceImpl<UserInfoMapper, UserInfo>
    implements UserInfoService {
    @Autowired
    private PatientService patientService;
    @Override
    public Map<String, Object> login(LoginVo loginVo) {
        String phone = loginVo.getPhone();
        String code = loginVo.getCode();
        if(StringUtils.isEmpty(phone) || StringUtils.isEmpty(code))
            throw new YyghException(ResultCodeEnum.PARAM_ERROR);
//        TODO 校验验证码
        QueryWrapper<UserInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("phone",phone);
        UserInfo userInfo = baseMapper.selectOne(queryWrapper);
        if(null == userInfo){
            userInfo = new UserInfo();
            userInfo.setName("");
            userInfo.setPhone(phone);
            userInfo.setStatus(1);
            this.save(userInfo);
        }
//        校验是否被禁用
        if(userInfo.getStatus() == 0){
            throw new YyghException(ResultCodeEnum.LOGIN_DISABLED_ERROR);
        }
//        记录登录
        Map<String, Object> map = new HashMap<>();
        String name = userInfo.getName();
        if(StringUtils.isEmpty(name)){
            name = userInfo.getNickName();
        }
        if(StringUtils.isEmpty(name)){
            name = userInfo.getPhone();
        }
        map.put("name",name);
        String token = JwtHelper.createToken(userInfo.getId(), name);
        map.put("token",token);
        return map;
    }

    @Override
    public void userAuth(Long userId, UserAuthVo userAuthVo) {
        UserInfo userInfo = baseMapper.selectById(userId);

        userInfo.setName(userAuthVo.getName());
        userInfo.setCertificatesType(userAuthVo.getCertificatesType());
        userInfo.setCertificatesNo(userAuthVo.getCertificatesNo());
        userInfo.setCertificatesUrl(userAuthVo.getCertificatesUrl());
        userInfo.setAuthStatus(AuthStatusEnum.AUTH_RUN.getStatus());
        baseMapper.updateById(userInfo);
    }

    @Override
    public IPage<UserInfo> selectPage(Page<UserInfo> pageParam, UserInfoQueryVo userInfoQueryVo) {
        String name = userInfoQueryVo.getKeyword();
        Integer status = userInfoQueryVo.getStatus();
        Integer authStatus = userInfoQueryVo.getAuthStatus();
        String createTimeBegin = userInfoQueryVo.getCreateTimeBegin();
        String createTimeEnd = userInfoQueryVo.getCreateTimeEnd();
        QueryWrapper<UserInfo> wrapper = new QueryWrapper<>();

        if(!StringUtils.isEmpty(name)){
            wrapper.like("name",name);
        }
        if(!StringUtils.isEmpty(status)){
            wrapper.eq("status",status);
        }
        if(!StringUtils.isEmpty(authStatus)){
            wrapper.eq("auth_status",authStatus);
        }
        if(!StringUtils.isEmpty(createTimeBegin)){
            wrapper.like("create_time_begin",createTimeBegin);
        }
        if(!StringUtils.isEmpty(createTimeEnd)){
            wrapper.like("create_time_end",createTimeEnd);
        }

        IPage<UserInfo> page = baseMapper.selectPage(pageParam, wrapper);
        page.getRecords().stream().forEach(item->{
            this.packageUserInfo(item);
        });
        return page;
    }

    @Override
    public void lock(Long userId, Integer status) {
        if(status.intValue() == 0 || status.intValue() == 1){
            UserInfo userInfo = this.getById(userId);
            userInfo.setStatus(status);
            this.updateById(userInfo);
        }
    }

    @Override
    public Map<String, Object> show(Long userId) {
        Map<String,Object> map = new HashMap<>();
        UserInfo userInfo = this.packageUserInfo(baseMapper.selectById(userId));
        map.put("userInfo",userInfo);
        List<Patient> patientList = patientService.findAllUserId(userId);
        map.put("patientList",patientList);
        return map;
    }

    @Override
    public void approval(Long userId, Integer authStatus) {
        if(authStatus.intValue() == 2 || authStatus.intValue() == 1){
            UserInfo userInfo = baseMapper.selectById(userId);
            userInfo.setAuthStatus(authStatus);
            baseMapper.updateById(userInfo);
        }
    }

    private UserInfo packageUserInfo(UserInfo userInfo) {
        userInfo.getParam().put("authStatusString",AuthStatusEnum.getStatusNameByStatus(userInfo.getAuthStatus()));
        String statusString = userInfo.getStatus().intValue()==0?"锁定":"正常";
        userInfo.getParam().put("statusString",statusString);
        return userInfo;
    }
}
