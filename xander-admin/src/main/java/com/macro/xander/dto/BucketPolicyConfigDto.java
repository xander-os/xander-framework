package com.macro.xander.dto;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @author ：ZhRunXin
 * @date ：Created in 2023/2/9 23:03
 * @email ：zhrunxin33@gmail.com
 * @description：MinIO Bucket访问策略配置
 */
@Builder
@Data
@EqualsAndHashCode(callSuper = false)
public class BucketPolicyConfigDto {

    private String Version;
    private List<Statement> Statement;

    @Data
    @EqualsAndHashCode(callSuper = false)
    @Builder
    public static class Statement {
        private String Effect;
        private String Principal;
        private String Action;
        private String Resource;
    }
}
