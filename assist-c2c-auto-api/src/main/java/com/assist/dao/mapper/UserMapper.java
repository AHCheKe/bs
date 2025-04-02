package com.assist.dao.mapper;

import com.assist.dao.model.User;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface UserMapper extends Mapper<User> {

    User selectUserById(@Param("userId") Integer userId);

    List<User> selectAllValidateAssistant();

    List<User> selectTagModifyUsers(@Param("start") Integer start, @Param("end") Integer end);

    Integer selectTagModifyUsersCount();
}