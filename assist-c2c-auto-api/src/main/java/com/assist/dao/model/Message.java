package com.assist.dao.model;

import java.util.Date;
import javax.persistence.*;

public class Message {
    /**
     * 主键
     */
    @Id
    @Column(name = "msg_id")
    private Integer msgId;

    /**
     * 小程序用户ID
     */
    @Column(name = "sender_id")
    private Integer senderId;

    /**
     * 管理员标识
     */
    @Column(name = "admin_id")
    private String adminId;

    /**
     * 发送时间
     */
    @Column(name = "send_time")
    private Date sendTime;

    /**
     * 消息内容
     */
    private String content;

    /**
     * 获取主键
     *
     * @return msg_id - 主键
     */
    public Integer getMsgId() {
        return msgId;
    }

    /**
     * 设置主键
     *
     * @param msgId 主键
     */
    public void setMsgId(Integer msgId) {
        this.msgId = msgId;
    }

    /**
     * 获取小程序用户ID
     *
     * @return sender_id - 小程序用户ID
     */
    public Integer getSenderId() {
        return senderId;
    }

    /**
     * 设置小程序用户ID
     *
     * @param senderId 小程序用户ID
     */
    public void setSenderId(Integer senderId) {
        this.senderId = senderId;
    }

    /**
     * 获取管理员标识
     *
     * @return admin_id - 管理员标识
     */
    public String getAdminId() {
        return adminId;
    }

    /**
     * 设置管理员标识
     *
     * @param adminId 管理员标识
     */
    public void setAdminId(String adminId) {
        this.adminId = adminId == null ? null : adminId.trim();
    }

    /**
     * 获取发送时间
     *
     * @return send_time - 发送时间
     */
    public Date getSendTime() {
        return sendTime;
    }

    /**
     * 设置发送时间
     *
     * @param sendTime 发送时间
     */
    public void setSendTime(Date sendTime) {
        this.sendTime = sendTime;
    }

    /**
     * 获取消息内容
     *
     * @return content - 消息内容
     */
    public String getContent() {
        return content;
    }

    /**
     * 设置消息内容
     *
     * @param content 消息内容
     */
    public void setContent(String content) {
        this.content = content == null ? null : content.trim();
    }
}