package com.atguigu.controller;

import ch.qos.logback.core.joran.util.beans.BeanUtil;
import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.entity.UserInfo;
import com.atguigu.entity.bo.LoginBo;
import com.atguigu.entity.bo.RegisterBo;
import com.atguigu.result.Result;
import com.atguigu.result.ResultCodeEnum;
import com.atguigu.service.UserInfoService;
import com.atguigu.util.MD5;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/userInfo")
public class UserInfoController {

    @Reference
    private UserInfoService userInfoService;

    @GetMapping("/sendCode/{phone}")
    public Result sendCode(@PathVariable("phone") String phone, HttpSession session){
        //判断当前手机号是否已经注册
        // 模拟阿里云短信发送验证码
         String code = "1111";
         //使用session保存验证码
        session.setAttribute("CODE",code);
        return Result.ok();
    }

    @PostMapping("/register")
    public  Result  register(@RequestBody RegisterBo registerBo, HttpSession session){
       //校验验证码是否正确
        String code = (String) session.getAttribute("CODE");
        if(!registerBo.getCode().equalsIgnoreCase(code)){
            return Result.build(null,ResultCodeEnum.CODE_ERROR);
        }
        //校验手机号是否已经注册
        UserInfo userInfo = userInfoService.findByPhone(registerBo.getPhone());
        if(userInfo!=null){
            return Result.build(null,ResultCodeEnum.CODE_ERROR);
        }
        //调用业务层处理
        userInfo = new UserInfo();
        //将registerBo赋值给userInfo
            BeanUtils.copyProperties(registerBo,userInfo);
        //设置status和加密
        userInfo.setStatus(1);
        userInfo.setPassword(MD5.encrypt(userInfo.getPassword()));
       //保存用户信息
        userInfoService.insert(userInfo);
        return Result.ok();
    }

     @PostMapping("/login")
    public Result login(@RequestBody  LoginBo loginBo,HttpSession session){
       //根据手机号查询用户,判断是否正确
         UserInfo userInfo = userInfoService.findByPhone(loginBo.getPhone());
         if(userInfo == null){
             return Result.build(null,ResultCodeEnum.PHONE_REGISTER_ERROR);
         }
         //判断账号是否被锁定
         if(userInfo.getStatus() == 0){
             return Result.build(null,ResultCodeEnum.ACCOUNT_LOCK_ERROR);
         }
       //判断密码是否正确
         if (userInfo.getPassword().equals(MD5.encrypt(loginBo.getPassword()))){
             return Result.build(null,ResultCodeEnum.PASSWORD_ERROR);
         }
       //登录成功,用户信息存储到session中
          session.setAttribute("USER",userInfo);
         //将信息给前端页面回显
         Map<String ,Object> responseMapping = new HashMap();
         responseMapping.put("nickName",userInfo.getNickName());
         responseMapping.put("phone",userInfo.getPhone());
         return Result.ok(responseMapping);
     }
//  退出登录
     @GetMapping("/logout")
     public Result logout(HttpSession session){
      //销毁session
      session.invalidate();
      return Result.ok();
     }

}
