package com.atguigu.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.base.BaseController;
import com.atguigu.entity.Dict;
import com.atguigu.result.Result;
import com.atguigu.service.DictService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/dict")
public class DictController extends BaseController {

    @Reference
    private DictService dictService;

    @GetMapping("/findZnodes")
    @ResponseBody
    public Result findZNodes(@RequestParam(value = "id",defaultValue = "0") Long id){
        List<Map<String, Object>> zNodes = dictService.findZnodes(id);
        return Result.ok(zNodes);
    }

    @RequestMapping("/findDictListByParentId/{parentId}")
    @ResponseBody
    public  Result findDictListByParentId(@PathVariable("parentId") Long parentId){
        List<Dict> dictList = dictService.findDictListByParentId(parentId);
        return  Result.ok(dictList);
    }

}
