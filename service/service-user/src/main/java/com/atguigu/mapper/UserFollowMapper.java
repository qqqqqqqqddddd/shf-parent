package com.atguigu.mapper;

import com.atguigu.entity.UserFollow;
import com.atguigu.entity.vo.UserFollowVo;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import org.apache.ibatis.annotations.Param;

public interface UserFollowMapper {
     Page<UserFollowVo> findListPage(@Param("userId") Long userId);


    UserFollow findByUserIdAndHouseId(@Param("userId") Long userId, @Param("houseId")  Long houseId);

    void update(UserFollow userFollow);

    void insert(UserFollow userFollow);
}
