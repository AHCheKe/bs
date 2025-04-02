package com.assist.controller.admin;

import com.alibaba.fastjson.JSONObject;
import com.assist.controller.vo.JsonResult;
import com.assist.dao.mapper.MessageMapper;
import com.assist.dao.mapper.UserMapper;
import com.assist.dao.model.Message;
import com.assist.dao.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/admin/message")
public class MessageApi {

    @Autowired
    private MessageMapper messageMapper;
    @Autowired
    private UserMapper userMapper;

    /**
     * 获取对话列表
     * @param jsonObject
     * @return
     */
    @RequestMapping("list")
    public JsonResult getMessageList(@RequestBody JSONObject jsonObject) {
        JsonResult result = new JsonResult(true, 200, "查询成功");
        Integer senderId = jsonObject.getInteger("senderId");
        List<Message> messageList = messageMapper.selectUserDialog(senderId);
        result.setData(messageList);
        return result;
    }

    /**
     * 获取所有对话用户
     * @param jsonObject
     * @return
     */
    @RequestMapping("user/list")
    public JsonResult getAllDialogUser(@RequestBody JSONObject jsonObject) {
        JsonResult result = new JsonResult(true, 200, "查询成功");
        List<User> allUser = messageMapper.getUserList();
//        for (User user : allUser) {
//            user.setDialogList(messageMapper.selectUserDialog(user.getUserId()));
//        }
        result.setData(allUser);
        return result;
    }

    /**
     * 发送消息
     * @param jsonObject
     * @return
     */
    @RequestMapping("send")
    public JsonResult sendMessage(@RequestBody JSONObject jsonObject) {
        JsonResult result = new JsonResult(true, 200, "发送成功");
        Integer senderId = jsonObject.getInteger("senderId");
        String content = jsonObject.getString("message");
        Message message = new Message();
        message.setSenderId(senderId);
        message.setContent(content);
        message.setSendTime(new Date());
        message.setAdminId("admin");
        messageMapper.insertSelective(message);
        return result;
    }
}
