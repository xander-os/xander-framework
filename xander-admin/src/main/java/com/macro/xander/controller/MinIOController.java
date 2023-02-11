package com.macro.xander.controller;

import com.macro.xander.common.api.CommonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
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

    @ApiOperation("文件上传")
    @RequestMapping(value = "upload", method = RequestMethod.POST)
    public CommonResult upload(@RequestPart("file")MultipartFile file){
        return CommonResult.success(file.getName());
    }
}
