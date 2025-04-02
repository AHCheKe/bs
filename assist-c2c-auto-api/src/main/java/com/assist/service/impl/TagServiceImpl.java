package com.assist.service.impl;

import com.assist.dao.mapper.TagMapper;
import com.assist.dao.mapper.UserTagMapper;
import com.assist.dao.model.Tag;
import com.assist.dao.model.User;
import com.assist.dao.model.UserTag;
import com.assist.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TagServiceImpl implements TagService {

    @Autowired
    private TagMapper tagMapper;
    @Autowired
    private UserTagMapper userTagMapper;

    @Override
    public void validateUserTag(User user, List<Tag> tags) {

    }

    @Override
    public void validateUserTag(User user) {
        List<UserTag> userTags = userTagMapper.selectUserTagByUserId(user.getUserId());
        for(UserTag userTag : userTags) {
            userTag.setValidate(1);
            userTagMapper.updateByPrimaryKeySelective(userTag);
        }
    }

    @Override
    public void rejectUserTag(User user) {
        List<UserTag> userTags = userTagMapper.selectUserTagByUserId(user.getUserId());
        for(UserTag userTag : userTags) {
            userTag.setValidate(-1);
            userTagMapper.updateByPrimaryKeySelective(userTag);
        }
    }

    @Override
    public List<UserTag> findUserTags(User user) {
        return userTagMapper.selectUserTagByUserId(user.getUserId());
    }

    @Override
    public void updateUserTags(User user, List<Integer> tagIds) {
        //1.删除当前的标签关联
        UserTag userTag = new UserTag();
        userTag.setUserId(user.getUserId());
        userTagMapper.delete(userTag);
        //2.创建新的标签关联并存入数据库
        for(Integer tagId : tagIds) {
            UserTag record = new UserTag();
            record.setUserId(user.getUserId());
            record.setTagId(tagId);
            record.setValidate(0);//待审核状态
            userTagMapper.insertSelective(record);
        }
    }
}
