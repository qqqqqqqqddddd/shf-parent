package com.atguigu.service;

import com.atguigu.entity.UserFollow;
import com.atguigu.entity.vo.UserFollowVo;
import com.github.pagehelper.PageInfo;

public interface UserFollowService {
     /*
      * @description:根据用户id和房源id查询关注信息
      * @author: niuzp 
      * @date: 2022/6/21 14:38
      **/
    UserFollow findByUserIdAndHouseId(Long userId, Long houseId);
     /*
      * @description:更新房源的关注信息
      * @author: niuzp
      * @date: 2022/6/21 14:39
      **/
    void update(UserFollow userFollow);
    /*
     * @description:新增关注信息
     * @author: niuzp
     * @date: 2022/6/21 14:40
     **/
    void insert(UserFollow userFollow);
    /*
     * @description:分页查询关注信息
     * @author: niuzp
     * @date: 2022/6/21 16:37
     **/
    PageInfo<UserFollowVo> findListPage(Integer pageNum, Integer pageSize, Long userId);
}
