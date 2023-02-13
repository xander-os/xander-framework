package com.macro.xander.mapper;

import com.macro.xander.model.PmsComment;
import com.macro.xander.model.PmsCommentExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface PmsCommentMapper {
    long countByExample(PmsCommentExample example);

    int deleteByExample(PmsCommentExample example);

    int deleteByPrimaryKey(Long id);

    int insert(PmsComment row);

    int insertSelective(PmsComment row);

    List<PmsComment> selectByExampleWithBLOBs(PmsCommentExample example);

    List<PmsComment> selectByExample(PmsCommentExample example);

    PmsComment selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("row") PmsComment row, @Param("example") PmsCommentExample example);

    int updateByExampleWithBLOBs(@Param("row") PmsComment row, @Param("example") PmsCommentExample example);

    int updateByExample(@Param("row") PmsComment row, @Param("example") PmsCommentExample example);

    int updateByPrimaryKeySelective(PmsComment row);

    int updateByPrimaryKeyWithBLOBs(PmsComment row);

    int updateByPrimaryKey(PmsComment row);
}