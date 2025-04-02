package com.assist.dao.model;

import java.util.Date;
import javax.persistence.*;

@Table(name = "service_order")
public class ServiceOrder {
    /**
     * 物理主键
     */
    @Id
    @Column(name = "order_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer orderId;

    /**
     * 服务需求ID
     */
    @Column(name = "service_id")
    private Integer serviceId;

    @Transient
    private ServiceNeed serviceNeed;

    /**
     * 客户ID
     */
    @Column(name = "user_id")
    private Integer userId;

    @Transient
    private User user;

    /**
     * 订单类型：1抢单；2主动预约
     */
    @Column(name = "order_type")
    private Integer orderType;

    /**
     * 陪诊师ID
     */
    @Column(name = "assistant_id")
    private Integer assistantId;

    @Transient
    private User assistant;

    /**
     * 接单时间
     */
    @Column(name = "accept_time")
    private Date acceptTime;

    @Column(name = "create_time")
    private Date createTime;

    @Column(name = "service_time")
    private Date serviceTime;

    private String remark;

    @Column(name = "order_code")
    private String orderCode;

    /**
     * 完成时间
     */
    @Column(name = "finish_time")
    private Date finishTime;

    /**
     * 订单价格
     */
    private Double price;

    /**
     * 订单状态
     */
    @Column(name = "order_status")
    private Integer orderStatus;

    @Transient
    private Feedback feedback;

    /**
     * 获取物理主键
     *
     * @return order_id - 物理主键
     */
    public Integer getOrderId() {
        return orderId;
    }

    /**
     * 设置物理主键
     *
     * @param orderId 物理主键
     */
    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    /**
     * 获取服务需求ID
     *
     * @return service_id - 服务需求ID
     */
    public Integer getServiceId() {
        return serviceId;
    }

    /**
     * 设置服务需求ID
     *
     * @param serviceId 服务需求ID
     */
    public void setServiceId(Integer serviceId) {
        this.serviceId = serviceId;
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
     * 获取订单类型：1抢单；2主动预约
     *
     * @return order_type - 订单类型：1抢单；2主动预约
     */
    public Integer getOrderType() {
        return orderType;
    }

    /**
     * 设置订单类型：1抢单；2主动预约
     *
     * @param orderType 订单类型：1抢单；2主动预约
     */
    public void setOrderType(Integer orderType) {
        this.orderType = orderType;
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
     * 获取接单时间
     *
     * @return accept_time - 接单时间
     */
    public Date getAcceptTime() {
        return acceptTime;
    }

    /**
     * 设置接单时间
     *
     * @param acceptTime 接单时间
     */
    public void setAcceptTime(Date acceptTime) {
        this.acceptTime = acceptTime;
    }

    /**
     * 获取完成时间
     *
     * @return finish_time - 完成时间
     */
    public Date getFinishTime() {
        return finishTime;
    }

    /**
     * 设置完成时间
     *
     * @param finishTime 完成时间
     */
    public void setFinishTime(Date finishTime) {
        this.finishTime = finishTime;
    }

    /**
     * 获取订单价格
     *
     * @return price - 订单价格
     */
    public Double getPrice() {
        return price;
    }

    /**
     * 设置订单价格
     *
     * @param price 订单价格
     */
    public void setPrice(Double price) {
        this.price = price;
    }

    /**
     * 获取订单状态
     *
     * @return order_status - 订单状态
     */
    public Integer getOrderStatus() {
        return orderStatus;
    }

    /**
     * 设置订单状态
     *
     * @param orderStatus 订单状态
     */
    public void setOrderStatus(Integer orderStatus) {
        this.orderStatus = orderStatus;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public User getAssistant() {
        return assistant;
    }

    public void setAssistant(User assistant) {
        this.assistant = assistant;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Feedback getFeedback() {
        return feedback;
    }

    public void setFeedback(Feedback feedback) {
        this.feedback = feedback;
    }

    public Date getServiceTime() {
        return serviceTime;
    }

    public void setServiceTime(Date serviceTime) {
        this.serviceTime = serviceTime;
    }

    public ServiceNeed getServiceNeed() {
        return serviceNeed;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public void setServiceNeed(ServiceNeed serviceNeed) {
        this.serviceNeed = serviceNeed;
    }

    public String getOrderCode() {
        return orderCode;
    }

    public void setOrderCode(String orderCode) {
        this.orderCode = orderCode;
    }
}