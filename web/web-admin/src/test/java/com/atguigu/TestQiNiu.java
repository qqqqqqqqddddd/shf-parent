package com.atguigu;

import com.google.gson.Gson;
import com.qiniu.common.QiniuException;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import org.junit.Test;

public class TestQiNiu {

    //测试七牛云上传文件
    @Test
    public void test() {
        //构造一个带指定 Zone 对象的配置类
        //Zone.zone2()表示华南
        Configuration cfg = new Configuration(Zone.zone2());
//...其他参数参考类注释
//创建文件上传的管理器对象
        UploadManager uploadManager = new UploadManager(cfg);
//...生成上传凭证，然后准备上传
        String accessKey = "PctevFDbRonqsN3JDNpbvhPwsPtJEwFFa2QCirDB";
        String secretKey = "SCMK2aJOE0M7UdlwuqaoAQYKASHQOtsx50GKUqMv";
        String bucket = "niuzp-0310shf";
//如果是Windows情况下，格式是 D:\\qiniu\\test.png
        String localFilePath = "D:\\io\\nnn.jpg";
//默认不指定key的情况下，以文件内容的hash值作为文件名
        String key = null;
//鉴权对象
        Auth auth = Auth.create(accessKey, secretKey);
        //创建上传时候的token
        String upToken = auth.uploadToken(bucket);

        try {
            Response response = uploadManager.put(localFilePath, key, upToken);
            //解析上传成功的结果
            DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
            System.out.println(putRet.key);
            System.out.println(putRet.hash);
        } catch (QiniuException ex) {
            Response r = ex.response;
            System.err.println(r.toString());
            try {
                System.err.println(r.bodyString());
            } catch (QiniuException ex2) {
                //ignore
            }
        }
    }

    @Test
    public void test02() {

        //构造一个带指定 Zone 对象的配置类
        Configuration cfg = new Configuration(Zone.zone2());
//...其他参数参考类注释
        String accessKey = "PctevFDbRonqsN3JDNpbvhPwsPtJEwFFa2QCirDB";
        String secretKey = "SCMK2aJOE0M7UdlwuqaoAQYKASHQOtsx50GKUqMv";
        String bucket = "niuzp-0310shf";
        //文件再七牛云中的名字
        String key = "FugfA_Y918zlaEtluP5rC2-0aLeo";
        Auth auth = Auth.create(accessKey, secretKey);
        BucketManager bucketManager = new BucketManager(auth, cfg);
        try {
            //删除文件
            bucketManager.delete(bucket, key);
        } catch (QiniuException ex) {
            //如果遇到异常，说明删除失败
            System.err.println(ex.code());
            System.err.println(ex.response.toString());
        }
    }



}
