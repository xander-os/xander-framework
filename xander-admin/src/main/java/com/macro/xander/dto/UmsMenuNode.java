package com.macro.xander.dto;


import com.macro.xander.model.UmsMenu;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author ：ZhRunXin
 * @date ：Created in 2023/2/17 17:12
 * @email ：zhrunxin33@gmail.com
 * @description：后台菜单节点封装
 */
@Getter
@Setter
public class UmsMenuNode extends UmsMenu {
    @ApiModelProperty(value = "子级菜单")
    private List<UmsMenuNode> children;
}
