package com.assist.dao.model;

import java.util.Date;
import javax.persistence.*;

public class Feedback {
    /**
     * 主键
     */
    @Id
    @Column(name = "feedback_id")
    private Integer feedbackId;

    /**
     * 订单ID
     */
    @Column(name = "order_id")
    private Integer orderId;

    /**
     * 服务ID
     */
    @Column(name = "service_id")
    private Integer serviceId;

    /**
     * 评价时间
     */
    @Column(name = "feedback_time")
    private Date feedbackTime;

    /**
     * 客户ID
     */
    @Column(name = "user_id")
    private Integer userId;

    @Transient
    private User user;

    /**
     * 陪诊师ID
     */
    @Column(name = "assistant_id")
    private Integer assistantId;

    @Transient
    private User assistant;

    /**
     * 评分
     */
    private Integer rate;

    /**
     * 评价内容
     */
    private String content;

    /**
     * 获取主键
     *
     * @return feedback_id - 主键
     */
    public Integer getFeedbackId() {
        return feedbackId;
    }

    /**
     * 设置主键
     *
     * @param feedbackId 主键
     */
    public void setFeedbackId(Integer feedbackId) {
        this.feedbackId = feedbackId;
    }

    /**
     * 获取订单ID
     *
     * @return order_id - 订单ID
     */
    public Integer getOrderId() {
        return orderId;
    }

    /**
     * 设置订单ID
     *
     * @param orderId 订单ID
     */
    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    /**
     * 获取服务ID
     *
     * @return service_id - 服务ID
     */
    public Integer getServiceId() {
        return serviceId;
    }

    /**
     * 设置服务ID
     *
     * @param serviceId 服务ID
     */
    public void setServiceId(Integer serviceId) {
        this.serviceId = serviceId;
    }

    /**
     * 获取评价时间
     *
     * @return feedback_time - 评价时间
     */
    public Date getFeedbackTime() {
        return feedbackTime;
    }

    /**
     * 设置评价时间
     *
     * @param feedbackTime 评价时间
     */
    public void setFeedbackTime(Date feedbackTime) {
        this.feedbackTime = feedbackTime;
    }

    /**
     * 获取客户ID
     *
     * @return user_id - 客户ID
     */
    public Integer getUserId() {
        return userId;
    }

    /**
     * 设置客户ID
     *
     * @param userId 客户ID
     */
    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    /**
     * 获取陪诊师ID
     *
     * @return assistant_id - 陪诊师ID
     */
    public Integer getAssistantId() {
        return assistantId;
    }

    /**
     * 设置陪诊师ID
     *
     * @param assistantId 陪诊师ID
     */
    public void setAssistantId(Integer assistantId) {
        this.assistantId = assistantId;
    }

    /**
     * 获取评分
     *
     * @return rate - 评分
     */
    public Integer getRate() {
        return rate;
    }

    /**
     * 设置评分
     *
     * @param rate 评分
     */
    public void setRate(Integer rate) {
        this.rate = rate;
    }

    /**
     * 获取评价内容
     *
     * @return content - 评价内容
     */
    public String getContent() {
        return content;
    }

    /**
     * 设置评价内容
     *
     * @param content 评价内容
     */
    public void setContent(String content) {
        this.content = content == null ? null : content.trim();
    }

    public User getAssistant() {
        return assistant;
    }

    public void setAssistant(User assistant) {
        this.assistant = assistant;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}