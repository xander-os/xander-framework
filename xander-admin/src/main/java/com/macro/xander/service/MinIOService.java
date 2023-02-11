package com.macro.xander.service;

import com.macro.xander.common.api.CommonResult;
import com.macro.xander.dto.MinIOUploadDto;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author     ：ZhRunXin
 * @date       ：Created in 2023/2/12 1:33
 * @email      ：zhrunxin33@gmail.com
 * @description：MinIO文件上传
 */
public interface MinIOService {
    /**
     * MinIO文件上传
     */
    CommonResult<MinIOUploadDto> upload(MultipartFile file);

    /**
     * 文件删除
     */
    CommonResult delete(String objectName);
}
