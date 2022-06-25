package com.atguigu.service;

import com.atguigu.base.BaseService;
import com.atguigu.entity.HouseImage;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface HouseImageService extends BaseService<HouseImage> {
    List<HouseImage> findHouseImageList( Long houseId,  Integer type);
}
