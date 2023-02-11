package com.macro.xander.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author ：ZhRunXin
 * @date ：Created in 2023/2/12 1:36
 * @email ：zhrunxin33@gmail.com
 * @description：文件上传返回对象
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class MinIOUploadDto {
    @ApiModelProperty(value = "文件访问url")
    private String url;
    @ApiModelProperty(value="文件名称")
    private String name;
}
