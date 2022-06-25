package com.atguigu.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.base.BaseController;
import com.atguigu.entity.*;
import com.atguigu.entity.bo.HouseQueryBo;
import com.atguigu.entity.vo.HouseVo;
import com.atguigu.result.Result;
import com.atguigu.service.*;
import com.github.pagehelper.PageInfo;
import okhttp3.Response;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/house")
public class HouseController extends BaseController {

    @Reference
    private HouseService houseService;
    @Reference
    private CommunityService communityService;
    @Reference
    private HouseBrokerService houseBrokerService;
    @Reference
    private HouseImageService houseImageService;
    @Reference
    private HouseUserService houseUserService;
    @Reference
    private  UserFollowService userFollowService;

    @PostMapping("/list/{pageNum}/{pageSize}")
     public Result findListPage(@RequestBody HouseQueryBo houseQueryBo,
                                @PathVariable("pageNum") Integer pageNum,
                                @PathVariable("pageSize") Integer pageSize){
        PageInfo<HouseVo> pageInfo = houseService.findListPage(pageNum, pageSize, houseQueryBo);
        return Result.ok(pageInfo);
    }

    @GetMapping("/info/{id}")
    public  Result info(@PathVariable("id") Long id, HttpSession session){
        Map<String,Object> responseMap=new HashMap<>();
        //1.查询当前房源的信息
        House house = houseService.getById(id);
        responseMap.put("house",house);
        //2.查当前房源的小区信息
        Community community = communityService.getById(house.getCommunityId());
        responseMap.put("community",community);
        //3.查询当前房源的经纪人列表
        List<HouseBroker> houseBrokerList = houseBrokerService.findHouseBrokerListByHouseId(id);
        responseMap.put("houseBrokerList",houseBrokerList);
        //4.查询当前房源的图片列表
        List<HouseImage> houseImage1List = houseImageService.findHouseImageList(id, 1);
        responseMap.put("houseImage1List",houseImage1List);
        //5.查询当前房源的房东
        List<HouseUser> houseUserList = houseUserService.findHouseUserListByHouseId(id);
        responseMap.put("houseUserList",houseUserList);
        //查询登录状态是否关注该房源,默认false,设置备忘录todo
        UserInfo userInfo = (UserInfo) session.getAttribute("USER");
        boolean isFollow=false;
        if (userInfo != null){
            //判断是否已经关注当前房源
            //调用UserFollowService查询
            UserFollow userFollow = userFollowService.findByUserIdAndHouseId(userInfo.getId(), id);
            if (userFollow != null && userFollow.getIsDeleted()==0){
              isFollow=true;
            }
          }

        responseMap.put("isFollow",isFollow);
        return Result.ok(responseMap);
    }


}



