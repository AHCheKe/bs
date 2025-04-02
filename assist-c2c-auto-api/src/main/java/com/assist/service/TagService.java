package com.assist.service;

import com.assist.dao.model.Tag;
import com.assist.dao.model.User;
import com.assist.dao.model.UserTag;

import java.util.List;

public interface TagService {

    /**
     * 审核陪诊师的标签申请
     * @param user
     * @param tags
     */
    void validateUserTag(User user, List<Tag> tags);

    /**
     * 通过陪诊师的标签申请
     * @param user
     */
    void validateUserTag(User user);

    /**
     * 拒绝标签申请
     * @param user
     */
    void rejectUserTag(User user);

    /**
     * 查询用户的标签列表
     * @param user
     * @return
     */
    List<UserTag> findUserTags(User user);

    void updateUserTags(User user, List<Integer> tagIds);

}
