package com.assist.dao.mapper;

import com.assist.dao.model.Tag;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface TagMapper extends Mapper<Tag> {

    List<Tag> selectTagByUserId(@Param("userId") Integer userId);

    Tag selectTagById(@Param("tagId") Integer tagId);
}