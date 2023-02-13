package com.macro.xander.mapper;

import com.macro.xander.model.UmsMemberReceiveAddress;
import com.macro.xander.model.UmsMemberReceiveAddressExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface UmsMemberReceiveAddressMapper {
    long countByExample(UmsMemberReceiveAddressExample example);

    int deleteByExample(UmsMemberReceiveAddressExample example);

    int deleteByPrimaryKey(Long id);

    int insert(UmsMemberReceiveAddress row);

    int insertSelective(UmsMemberReceiveAddress row);

    List<UmsMemberReceiveAddress> selectByExample(UmsMemberReceiveAddressExample example);

    UmsMemberReceiveAddress selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("row") UmsMemberReceiveAddress row, @Param("example") UmsMemberReceiveAddressExample example);

    int updateByExample(@Param("row") UmsMemberReceiveAddress row, @Param("example") UmsMemberReceiveAddressExample example);

    int updateByPrimaryKeySelective(UmsMemberReceiveAddress row);

    int updateByPrimaryKey(UmsMemberReceiveAddress row);
}