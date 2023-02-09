package com.macro.xander.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.lang.annotation.Annotation;

/**
 * @author ：ZhRunXin
 * @date ：Created in 2023/2/9 21:02
 * @email ：zhrunxin33@gmail.com
 * @description：
 */
public class FlagValidatorClass implements ConstraintValidator<FlagValidator, Object> {

    private String[] values;

    @Override
    public void initialize(FlagValidator flagValidator) {
        this.values = flagValidator.value();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        boolean isValid = false;
        // value为空使用默认值
        if (value == null){
            return true;
        }
        for (int i = 0; i < values.length; i++) {
            if(contain(value,values[i])){
                isValid = true;
                break;
            }
        }
        return isValid;
    }

    private static boolean contain(Object valueObj, String value){
        // String
        if(valueObj instanceof String){
            return value.equals((String)valueObj);
        // Integer
        }else if (valueObj instanceof Integer){
            return value.equals(String.valueOf(valueObj));
        }
        return false;
    }
}
