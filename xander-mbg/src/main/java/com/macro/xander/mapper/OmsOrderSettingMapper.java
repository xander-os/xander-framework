package com.macro.xander.mapper;

import com.macro.xander.model.OmsOrderSetting;
import com.macro.xander.model.OmsOrderSettingExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface OmsOrderSettingMapper {
    long countByExample(OmsOrderSettingExample example);

    int deleteByExample(OmsOrderSettingExample example);

    int deleteByPrimaryKey(Long id);

    int insert(OmsOrderSetting row);

    int insertSelective(OmsOrderSetting row);

    List<OmsOrderSetting> selectByExample(OmsOrderSettingExample example);

    OmsOrderSetting selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("row") OmsOrderSetting row, @Param("example") OmsOrderSettingExample example);

    int updateByExample(@Param("row") OmsOrderSetting row, @Param("example") OmsOrderSettingExample example);

    int updateByPrimaryKeySelective(OmsOrderSetting row);

    int updateByPrimaryKey(OmsOrderSetting row);
}