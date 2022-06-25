package com.atguigu.mapper;

import com.atguigu.base.BaseMapper;
import com.atguigu.entity.House;
import com.atguigu.entity.bo.HouseQueryBo;
import com.atguigu.entity.vo.HouseVo;
import com.github.pagehelper.Page;

import java.util.List;

public interface HouseMapper extends BaseMapper<House> {

    /*
     * @description:根据小区id查房屋数量
     * @author: niuzp
     * @date: 2022/6/16 10:19
     **/
    Integer findCountByCommunityId(Long communityId);

    Page<HouseVo> findListPage(HouseQueryBo houseQueryBo);
}
