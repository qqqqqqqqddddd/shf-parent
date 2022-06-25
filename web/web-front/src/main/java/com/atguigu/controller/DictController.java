package com.atguigu.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.entity.Dict;
import com.atguigu.result.Result;
import com.atguigu.service.DictService;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/dict")
public class DictController {

    @Reference
    private DictService dictService;

    @RequestMapping("/findDictListByParentDictCode/{DictCode}")
    public Result   findDictListByParentDictCode(@PathVariable("DictCode") String DictCode){
       //根据业务层父节点查询字节的的列表
        List<Dict> dictList = dictService.findDictListByParentDictCode(DictCode);
        return Result.ok(dictList);
    }
    @RequestMapping("/findDictListByParentId/{parentId}")
    public  Result   findDictListByParentId(@PathVariable("parentId") Long parentId){
        //根据业务层父节点id查询字节的的列表
        List<Dict> dictList = dictService.findDictListByParentId(parentId);
        return Result.ok(dictList);
    }
}
