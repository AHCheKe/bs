package com.assist.controller.weixin;

import com.alibaba.fastjson.JSONObject;
import com.assist.author.AuthRequired;
import com.assist.controller.vo.JsonResult;
import com.assist.dao.mapper.MessageMapper;
import com.assist.dao.model.Message;
import com.assist.dao.model.User;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("api/online/message")
public class ServiceMessageApi extends ApiBaseController{

    @Autowired
    private MessageMapper messageMapper;

    /**
     * 发送消息
     * @param jsonObject
     * @return
     */
    @RequestMapping("send")
    @AuthRequired
    public JsonResult sendMessage(@RequestBody JSONObject jsonObject, HttpServletRequest request) {
        JsonResult result = new JsonResult(true, 200, "发送成功");
        User user = getCurrUser(request);
        String content = jsonObject.getString("content");
        Message message = new Message();
        message.setContent(content);
        message.setSendTime(new Date());
        message.setSenderId(user.getUserId());
        message.setAdminId("user");
        messageMapper.insertSelective(message);
        return result;
    }

    /**
     * 获取对话列表
     * @param jsonObject
     * @return
     */
    @RequestMapping("list")
    public JsonResult getMessageList(@RequestBody JSONObject jsonObject, HttpServletRequest request) {
        JsonResult result = new JsonResult(true, 200, "查询成功");
        User user = getCurrUser(request);
        Integer senderId = user.getUserId();
        List<Message> messageList = messageMapper.selectUserDialog(senderId);
        result.setData(messageList);
        return result;
    }

}
