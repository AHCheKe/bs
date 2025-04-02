package com.assist.dao.mapper;

import com.assist.dao.model.Message;
import com.assist.dao.model.User;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface MessageMapper extends Mapper<Message> {

    List<User> getUserList();

    List<Message> selectUserDialog(Integer senderId);
}