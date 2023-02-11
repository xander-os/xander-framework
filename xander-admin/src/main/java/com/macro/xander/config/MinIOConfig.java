package com.macro.xander.config;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.json.JSONUtil;
import com.macro.xander.dto.BucketPolicyConfigDto;
import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.SetBucketPolicyArgs;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author ：ZhRunXin
 * @date ：Created in 2023/2/9 23:00
 * @email ：zhrunxin33@gmail.com
 * @description：MinIO注入配置
 */
@Configuration
public class MinIOConfig {

    private static final Logger LOGGER = LoggerFactory.getLogger(MinIOConfig.class);

    @Value("${minio.endpoint}")
    private String ENDPOINT;
    @Value("${minio.bucketName}")
    private String BUCKET_NAME;
    @Value("${minio.accessKey}")
    private String ACCESS_KEY;
    @Value("${minio.secretKey}")
    private String SECRET_KEY;

    /**
     * MinIO初始化，并检测桶是否创建
     */
    @Bean
    public MinioClient minioClient(){
        MinioClient minioClient =MinioClient.builder()
                .endpoint(ENDPOINT)
                .credentials(ACCESS_KEY,SECRET_KEY)
                .build();
        try{
            boolean isExist = minioClient.bucketExists(BucketExistsArgs.builder().bucket(BUCKET_NAME).build());
            if (isExist) {
                LOGGER.info("存储桶已经存在！");
            } else {
                //创建存储桶并设置只读权限
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(BUCKET_NAME).build());
                BucketPolicyConfigDto bucketPolicyConfigDto = createBucketPolicyConfigDto(BUCKET_NAME);
                SetBucketPolicyArgs setBucketPolicyArgs = SetBucketPolicyArgs.builder()
                        .bucket(BUCKET_NAME)
                        .config(JSONUtil.toJsonStr(bucketPolicyConfigDto))
                        .build();
                minioClient.setBucketPolicy(setBucketPolicyArgs);
            }
        }catch (Exception e){
            e.printStackTrace();
            LOGGER.error("MinIO初始化失败: {}！", e.getMessage());
        }
        return minioClient;
    }

    /**
     * 桶访问策略
     */
    private BucketPolicyConfigDto createBucketPolicyConfigDto(String bucketName) {
        BucketPolicyConfigDto.Statement statement = BucketPolicyConfigDto.Statement.builder()
                .Effect("Allow")
                .Principal("*")
                .Action("s3:GetObject")
                .Resource("arn:aws:s3:::"+bucketName+"/*.**").build();
        return BucketPolicyConfigDto.builder()
                .Version("2012-10-17")
                .Statement(CollUtil.toList(statement))
                .build();
    }

    /**
     * 返回上传节点
     */
    public String getEndPoint(){
        return this.ENDPOINT;
    }

    /**
     * 返回桶
     */
    public String getBucket(){
        return this.BUCKET_NAME;
    }
}
