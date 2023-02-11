package com.macro.xander.service.impl;

import com.macro.xander.common.api.CommonResult;
import com.macro.xander.config.MinIOConfig;
import com.macro.xander.dto.MinIOUploadDto;
import com.macro.xander.service.MinIOService;
import io.minio.MinioClient;
import io.minio.ObjectWriteArgs;
import io.minio.PutObjectArgs;
import io.minio.RemoveObjectArgs;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.text.SimpleDateFormat;
import java.util.Date;



/**
 * @author ：ZhRunXin
 * @date ：Created in 2023/2/12 1:41
 * @email ：zhrunxin33@gmail.com
 * @description：MinIO文件上传
 */
@Service
public class MinIOServiceImpl implements MinIOService {
    private static final Logger LOGGER = LoggerFactory.getLogger(MinIOServiceImpl.class);

    @Autowired
    private MinioClient minioClient;
    @Autowired
    private MinIOConfig minIOConfig;


    @Override
    public CommonResult<MinIOUploadDto> upload(MultipartFile file) {
        String filename = file.getOriginalFilename();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        // 设置存储对象名称
        String objectName = sdf.format(new Date()) + "/" + filename;
        // 使用putObject上传一个文件到存储桶中
        try {
        PutObjectArgs putObjectArgs = PutObjectArgs.builder()
                .bucket(minIOConfig.getBucket())
                .object(objectName)
                .contentType(file.getContentType())
                .stream(file.getInputStream(), file.getSize(), ObjectWriteArgs.MIN_MULTIPART_SIZE).build();
            minioClient.putObject(putObjectArgs);
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error("MinIO文件上传失败: {}！", e.getMessage());
            return CommonResult.failed();
        }
        LOGGER.info("文件上传成功!");
        MinIOUploadDto minioUploadDto = new MinIOUploadDto();
        minioUploadDto.setName(filename);
        minioUploadDto.setUrl(minIOConfig.getEndPoint() + "/" + minIOConfig.getBucket() + "/" + objectName);
        return CommonResult.success(minioUploadDto);
    }

    @Override
    public CommonResult delete(String objectName) {
        try {
            minioClient.removeObject(RemoveObjectArgs.builder().bucket(minIOConfig.getBucket()).object(objectName).build());
            return CommonResult.success(null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return CommonResult.failed();
    }
}
