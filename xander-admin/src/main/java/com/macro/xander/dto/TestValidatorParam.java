package com.macro.xander.dto;

import com.macro.xander.validator.FlagValidator;
import lombok.Data;

/**
 * @author ：ZhRunXin
 * @date ：Created in 2023/2/12 12:25
 * @email ：zhrunxin33@gmail.com
 * @description：测试校验
 */
@Data
public class TestValidatorParam {

    private String name;
    @FlagValidator(value = {"1","2"},message = "性别只能是1和2")
    private String sex;
}
