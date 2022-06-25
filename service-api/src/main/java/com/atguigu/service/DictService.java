package com.atguigu.service;

import com.atguigu.entity.Dict;

import java.util.List;
import java.util.Map;

public interface DictService {

    List<Map<String,Object>> findZnodes(Long id);
    /*
    * @description:根据dictCode查询dict集合
    * @author: niuzp
    * @date: 2022/6/15 19:15
    **/
    List<Dict> findDictListByParentDictCode(String parentDictCode);
    /*
     * @description:根据父节点的id查询其所有的子节点
     * @author: niuzp
     * @date: 2022/6/15 19:16
     **/
    List<Dict> findDictListByParentId(Long parentId);
}
