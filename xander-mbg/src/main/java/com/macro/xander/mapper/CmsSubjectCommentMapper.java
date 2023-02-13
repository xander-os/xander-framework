package com.macro.xander.mapper;

import com.macro.xander.model.CmsSubjectComment;
import com.macro.xander.model.CmsSubjectCommentExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface CmsSubjectCommentMapper {
    long countByExample(CmsSubjectCommentExample example);

    int deleteByExample(CmsSubjectCommentExample example);

    int deleteByPrimaryKey(Long id);

    int insert(CmsSubjectComment row);

    int insertSelective(CmsSubjectComment row);

    List<CmsSubjectComment> selectByExample(CmsSubjectCommentExample example);

    CmsSubjectComment selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("row") CmsSubjectComment row, @Param("example") CmsSubjectCommentExample example);

    int updateByExample(@Param("row") CmsSubjectComment row, @Param("example") CmsSubjectCommentExample example);

    int updateByPrimaryKeySelective(CmsSubjectComment row);

    int updateByPrimaryKey(CmsSubjectComment row);
}