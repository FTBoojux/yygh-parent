package com.boojux.serviceuser.controller;

import com.alibaba.nacos.client.utils.IPUtil;
import com.boojux.serviceuser.service.UserInfoService;
import com.boojux.serviceuser.utils.IpUtil;
import com.example.yygh.common.helper.AuthContextHolder;
import com.example.yygh.common.result.Result;
import com.example.yygh.model.user.UserInfo;
import com.example.yygh.vo.user.LoginVo;
import com.example.yygh.vo.user.UserAuthVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RestController
@RequestMapping("/api/user")
public class UserInfoApiController {
    @Autowired
    private UserInfoService userInfoService;

    @PostMapping("login")
    public Result login(@RequestBody LoginVo loginVo, HttpServletRequest request){
        loginVo.setIp(IpUtil.getIpAddr(request));
        System.out.println("loginVo"+loginVo);
        Map<String,Object> info = userInfoService.login(loginVo);
        return Result.ok(info);
    }
//    用户认证
    @PostMapping("auth/userAuth")
    public Result getUserInfo(@RequestBody UserAuthVo userAuthVo, HttpServletRequest request){
        userInfoService.userAuth(AuthContextHolder.getUserId(request),userAuthVo);
        return Result.ok();
    }
//     获取用户id信息
    @GetMapping("auth/getUserInfo")
    public Result getUserInfo(HttpServletRequest request){
        Long userId = AuthContextHolder.getUserId(request);
        UserInfo userInfo = userInfoService.getById(userId);
        return Result.ok(userInfo);
    }
}
