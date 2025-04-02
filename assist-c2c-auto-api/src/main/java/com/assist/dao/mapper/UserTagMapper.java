package com.assist.dao.mapper;

import com.assist.dao.model.UserTag;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface UserTagMapper extends Mapper<UserTag> {

    List<UserTag> selectUserTagByUserId(@Param("userId") Integer userId);

    /**
     * 查找符合标签的陪诊师
     * @param tagName
     * @return
     */
    List<UserTag> selectUserTagByName(@Param("tagName") String tagName);

    /**
     * 查找不符合标签的陪诊师
     * @param tagName
     * @return
     */
    List<UserTag> selectUserWithoutTagByName(@Param("tagName") String tagName);
}