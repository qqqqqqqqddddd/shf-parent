package com.atguigu.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.entity.UserFollow;
import com.atguigu.entity.UserInfo;
import com.atguigu.entity.vo.UserFollowVo;
import com.atguigu.result.Result;
import com.atguigu.service.UserFollowService;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/userFollow")
public class UserFollowController {

     @Reference
     private UserFollowService userFollowService;

     @GetMapping("/auth/follow/{houseId}")
     public Result addFollow(@PathVariable("houseId") Long houseId, HttpSession session){
        //判断用户是否已经关注过该房源,根据用户id和房源id查询userFollow
         //获取登录的用户
         UserInfo userInfo = (UserInfo) session.getAttribute("USER");
         UserFollow userFollow = userFollowService.findByUserIdAndHouseId(userInfo.getId(), houseId);
       //如果用户之前已经关注过,更新这条数据is_deleted为0
         if(userFollow!=null){
             //说明关注过了
             userFollow.setIsDeleted(0);
         }else{
             //之前没关注过,现在就要新增一条数据
              userFollow = new UserFollow();
              userFollow.setUserId(userInfo.getId());
              userFollow.setHouseId(houseId);
              userFollowService.insert(userFollow);
         }
         return Result.ok();
     }

     @GetMapping("/auth/list/{pageNum}/{pageSize}")
     public  Result findListPage(@PathVariable("pageNum") Integer pageNum,
                                 @PathVariable("pageSize") Integer pageSize,
                                 HttpSession session){

         UserInfo userInfo = (UserInfo) session.getAttribute("USER");
         PageInfo<UserFollowVo> pageInfo = userFollowService.findListPage(pageNum,pageSize,userInfo.getId());
         return Result.ok(pageInfo);
     }
     @GetMapping("/auth/cancelFollow/{id}")
     public  Result cancelFollow(@PathVariable("id") Long id){
         //创建userFollow对象
         UserFollow userFollow =new UserFollow();
         userFollow.setId(id);
         userFollow.setIsDeleted(1);
         //调用业务层方法修改
         userFollowService.update(userFollow);

         return  Result.ok();
     }

 }
