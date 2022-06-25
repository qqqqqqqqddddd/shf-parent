package com.atguigu.servvice.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.atguigu.entity.Dict;
import com.atguigu.mapper.DictMapper;
import com.atguigu.service.DictService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service(interfaceClass = DictService.class)
public class DictServiceImpl implements DictService {

    @Autowired
    private DictMapper dictMapper;

    @Override
    public List<Map<String, Object>> findZnodes(Long id) {
        List<Dict> dictList = dictMapper.findListByParentId(id);
        List<Map<String, Object>> znodes = dictList.stream()
                .map(dict -> {
                    Map<String, Object> znode = new HashMap<>();
                    //往znode中存放id
                    znode.put("id", dict.getId());
                    //往znode中存放name
                    znode.put("name", dict.getName());
                    //往znode中存放isParent
                    znode.put("isParent", dictMapper.countIsParent(dict.getId()) > 0);
                    return znode;
                })
                .collect(Collectors.toList());
        return znodes;
    }

    @Override
    public List<Dict> findDictListByParentDictCode(String parentDictCode) {
        return  dictMapper.findDictListByParentDictCode(parentDictCode);
    }

    @Override
    public List<Dict> findDictListByParentId(Long parentId) {
        return dictMapper.findListByParentId(parentId);
    }
}
