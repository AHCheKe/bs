package com.assist.dao.model;

import java.util.Date;
import java.util.List;
import javax.persistence.*;

@Table(name = "service_need")
public class ServiceNeed {
    /**
     * 物理主键
     */
    @Id
    @Column(name = "service_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer serviceId;

    /**
     * 服务需求类型
     */
    @Column(name = "service_type")
    private String serviceType;

    /**
     * 申请人
     */
    @Column(name = "user_id")
    private Integer userId;

    @Transient
    private User user;

    /**
     * 发布时间
     */
    @Column(name = "publish_time")
    private Date publishTime;

    /**
     * 服务标题
     */
    private String title;

    /**
     * 就诊时间
     */
    @Column(name = "service_date")
    private Date serviceDate;

    /**
     * 是否需要接送
     */
    @Column(name = "need_pickup")
    private Integer needPickup;

    @Column(name = "location")
    private String location;

    private String lat;
    private String lng;

    @Column(name = "select_model")
    private Integer selectModel;

    /**
     * 所在城市
     */
    private String city;

    /**
     * 医院名称
     */
    private String hospital;

    /**
     * 需求标签
     */
    @Column(name = "service_tags")
    private String serviceTags;

    /**
     * 服务价格
     */
    private Double price;

    /**
     * 附加图片
     */
    private String pics;

    @Transient
    private List<String> picList;

    /**
     * 联系电话
     */
    private String mobile;

    /**
     * 处理状态：0待接单；1已接单；2已完成；3已反馈
     */
    private Integer status;

    /**
     * 详情内容
     */
    private String content;

    private Integer genderNeed;

    private String ageRange;

    /**
     * 指定的陪诊师，提交服务需求时使用
     */
    @Transient
    private Integer appointAssist;

    /**
     * 获取物理主键
     *
     * @return service_id - 物理主键
     */
    public Integer getServiceId() {
        return serviceId;
    }

    /**
     * 设置物理主键
     *
     * @param serviceId 物理主键
     */
    public void setServiceId(Integer serviceId) {
        this.serviceId = serviceId;
    }

    /**
     * 获取服务需求类型
     *
     * @return service_type - 服务需求类型
     */
    public String getServiceType() {
        return serviceType;
    }

    /**
     * 设置服务需求类型
     *
     * @param serviceType 服务需求类型
     */
    public void setServiceType(String serviceType) {
        this.serviceType = serviceType == null ? null : serviceType.trim();
    }

    /**
     * 获取申请人
     *
     * @return user_id - 申请人
     */
    public Integer getUserId() {
        return userId;
    }

    /**
     * 设置申请人
     *
     * @param userId 申请人
     */
    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    /**
     * 获取发布时间
     *
     * @return publish_time - 发布时间
     */
    public Date getPublishTime() {
        return publishTime;
    }

    /**
     * 设置发布时间
     *
     * @param publishTime 发布时间
     */
    public void setPublishTime(Date publishTime) {
        this.publishTime = publishTime;
    }

    /**
     * 获取服务标题
     *
     * @return title - 服务标题
     */
    public String getTitle() {
        return title;
    }

    /**
     * 设置服务标题
     *
     * @param title 服务标题
     */
    public void setTitle(String title) {
        this.title = title == null ? null : title.trim();
    }

    /**
     * 获取就诊时间
     *
     * @return service_date - 就诊时间
     */
    public Date getServiceDate() {
        return serviceDate;
    }

    /**
     * 设置就诊时间
     *
     * @param serviceDate 就诊时间
     */
    public void setServiceDate(Date serviceDate) {
        this.serviceDate = serviceDate;
    }

    /**
     * 获取是否需要接送
     *
     * @return need_pickup - 是否需要接送
     */
    public Integer getNeedPickup() {
        return needPickup;
    }

    /**
     * 设置是否需要接送
     *
     * @param needPickup 是否需要接送
     */
    public void setNeedPickup(Integer needPickup) {
        this.needPickup = needPickup;
    }

    /**
     * 获取所在城市
     *
     * @return city - 所在城市
     */
    public String getCity() {
        return city;
    }

    /**
     * 设置所在城市
     *
     * @param city 所在城市
     */
    public void setCity(String city) {
        this.city = city == null ? null : city.trim();
    }

    /**
     * 获取医院名称
     *
     * @return hospital - 医院名称
     */
    public String getHospital() {
        return hospital;
    }

    /**
     * 设置医院名称
     *
     * @param hospital 医院名称
     */
    public void setHospital(String hospital) {
        this.hospital = hospital == null ? null : hospital.trim();
    }

    /**
     * 获取服务价格
     *
     * @return price - 服务价格
     */
    public Double getPrice() {
        return price;
    }

    /**
     * 设置服务价格
     *
     * @param price 服务价格
     */
    public void setPrice(Double price) {
        this.price = price;
    }

    /**
     * 获取附加图片
     *
     * @return pics - 附加图片
     */
    public String getPics() {
        return pics;
    }

    /**
     * 设置附加图片
     *
     * @param pics 附加图片
     */
    public void setPics(String pics) {
        this.pics = pics == null ? null : pics.trim();
    }

    /**
     * 获取联系电话
     *
     * @return mobile - 联系电话
     */
    public String getMobile() {
        return mobile;
    }

    /**
     * 设置联系电话
     *
     * @param mobile 联系电话
     */
    public void setMobile(String mobile) {
        this.mobile = mobile == null ? null : mobile.trim();
    }

    /**
     * 获取处理状态：0待接单；1已接单；2已完成；3已反馈
     *
     * @return status - 处理状态：0待接单；1已接单；2已完成；3已反馈
     */
    public Integer getStatus() {
        return status;
    }

    /**
     * 设置处理状态：0待接单；1已接单；2已完成；3已反馈
     *
     * @param status 处理状态：0待接单；1已接单；2已完成；3已反馈
     */
    public void setStatus(Integer status) {
        this.status = status;
    }

    /**
     * 获取详情内容
     *
     * @return content - 详情内容
     */
    public String getContent() {
        return content;
    }

    /**
     * 设置详情内容
     *
     * @param content 详情内容
     */
    public void setContent(String content) {
        this.content = content == null ? null : content.trim();
    }

    public List<String> getPicList() {
        return picList;
    }

    public void setPicList(List<String> picList) {
        this.picList = picList;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public String getServiceTags() {
        return serviceTags;
    }

    public Integer getSelectModel() {
        return selectModel;
    }

    public void setSelectModel(Integer selectModel) {
        this.selectModel = selectModel;
    }

    public void setServiceTags(String serviceTags) {
        this.serviceTags = serviceTags;
    }

    public Integer getGenderNeed() {
        return genderNeed;
    }

    public void setGenderNeed(Integer genderNeed) {
        this.genderNeed = genderNeed;
    }

    public String getAgeRange() {
        return ageRange;
    }

    public void setAgeRange(String ageRange) {
        this.ageRange = ageRange;
    }

    public Integer getAppointAssist() {
        return appointAssist;
    }

    public void setAppointAssist(Integer appointAssist) {
        this.appointAssist = appointAssist;
    }
}