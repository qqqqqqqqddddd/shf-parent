package com.atguigu.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.base.BaseController;

import com.atguigu.entity.House;
import com.atguigu.entity.HouseImage;
import com.atguigu.result.Result;
import com.atguigu.service.HouseImageService;
import com.atguigu.service.HouseService;
import com.atguigu.util.FileUtil;
import com.atguigu.util.QiniuUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Controller
@RequestMapping("/houseImage")
public class HouseImageController extends BaseController {

    private static final String DETAIL_ACTION = "redirect:/house/";
    private static final String PAGE_UPLOAD_SHOW = "house/upload";
    @Reference
    private HouseImageService houseImageService;
    @Reference
    private HouseService houseService;

    @GetMapping("/uploadShow/{houseId}/{type}")
    public  String uploadShow(@PathVariable("houseId") Long houseId, @PathVariable("type") Integer type, Model model){
        model.addAttribute("houseId",houseId);
        model.addAttribute("type",type);
        return  PAGE_UPLOAD_SHOW;
    }

    @ResponseBody
    @PostMapping("/upload/{houseId}/{type}")
    public Result  upload(@PathVariable("houseId") Long houseId,
                          @PathVariable("type") Integer type,
                          @RequestParam("file") MultipartFile[]  files,
                          Model model) throws IOException {
        //遍历上传的文件
        for (int i = 0; i < files.length; i++) {
            //获取客户端的文件名
            MultipartFile file=files[i];
            String originalFilename = file.getOriginalFilename();
            //获取唯一的文件名
            String uuidName = FileUtil.getUUIDName(originalFilename);
            //将文件上传到七牛云
            QiniuUtils.upload2Qiniu(file.getBytes(),uuidName);
            //获取文件在七牛云的url
            String url = QiniuUtils.getUrl(uuidName);

          //将url保存到数据库
            HouseImage houseImage = new HouseImage();
            houseImage.setHouseId(houseId);
            houseImage.setImageName(uuidName);
            houseImage.setImageUrl(url);
            houseImage.setType(type);

            houseImageService.insert(houseImage);

            model.addAttribute("houseImage1List",houseImage);
            //设置默认图片
            //判断是否有默认图片,没有就设置第一张为默认图片
            if(i == 0){
                House house = houseService.getById(houseId);
                if (house.getDefaultImageUrl() == null || "".equals(house.getDefaultImageUrl())||"null".equals(house.getDefaultImageUrl())) {
                    //设置第一张图片为默认图片
                    house.setDefaultImageUrl(url);
                    //更新数据库中的房源信息
                    houseService.update(house);
                }
            }
        }
        //返回Result.ok表示上传成功
        return  Result.ok();
    }

    @GetMapping("/delete/{houseId}/{id}")
    public String delete(@PathVariable("houseId") Long houseId,
                         @PathVariable("id") Long id) {
        //查询图片信息
        HouseImage houseImage = houseImageService.getById(id);
        //删除图片
        QiniuUtils.deleteFileFromQiniu(houseImage.getImageName());
        //数据库中删除图片
        houseImageService.delete(id);

        //修改房源的默认图片地址为null
        House house = houseService.getById(houseId);
        if (houseImage.getImageUrl().equals(house.getDefaultImageUrl())){
            //则将当前房源的默认图片地址设置为null
            house.setDefaultImageUrl("null");
            //更新数据库中的房源信息
            houseService.update(house);
        }
        //重定向
        return DETAIL_ACTION + houseId;
    }
}
