package com.atguigu.base;

import org.springframework.ui.Model;

public class BaseController {
    private final static String PAGE_SUCCESS = "common/successPage";

    public  String successPage(Model model,String successPage){
        //将成功信息放入请求域
        model.addAttribute("successPage",successPage);
        //显示成功页面
        return PAGE_SUCCESS;

    }

}
