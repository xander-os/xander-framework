package com.macro.xander.mapper;

import com.macro.xander.model.CmsHelpCategory;
import com.macro.xander.model.CmsHelpCategoryExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface CmsHelpCategoryMapper {
    long countByExample(CmsHelpCategoryExample example);

    int deleteByExample(CmsHelpCategoryExample example);

    int deleteByPrimaryKey(Long id);

    int insert(CmsHelpCategory row);

    int insertSelective(CmsHelpCategory row);

    List<CmsHelpCategory> selectByExample(CmsHelpCategoryExample example);

    CmsHelpCategory selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("row") CmsHelpCategory row, @Param("example") CmsHelpCategoryExample example);

    int updateByExample(@Param("row") CmsHelpCategory row, @Param("example") CmsHelpCategoryExample example);

    int updateByPrimaryKeySelective(CmsHelpCategory row);

    int updateByPrimaryKey(CmsHelpCategory row);
}