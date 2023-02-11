package com.macro.xander.controller;

import com.macro.xander.common.api.CommonResult;
import com.macro.xander.service.MinIOService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author ：ZhRunXin
 * @date ：Created in 2023/2/11 15:53
 * @email ：zhrunxin33@gmail.com
 * @description：MinIO对象存储管理
 */
@RestController
@Api(tags = "MinIOController",value = "MioIO对象存储管理")
@RequestMapping("/minio")
public class MinIOController {

    @Autowired
    private MinIOService minIOService;

    @ApiOperation("文件上传")
    @RequestMapping(value = "upload", method = RequestMethod.POST)
    public CommonResult upload(@RequestPart("file")MultipartFile file){
        return minIOService.upload(file);
    }

    @ApiOperation("文件删除")
    @RequestMapping(value = "delete", method = RequestMethod.POST)
    public CommonResult upload(@RequestParam("objectName")String objectName){
        return minIOService.delete(objectName);
    }

}
