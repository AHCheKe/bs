package com.assist.dao.model;

import java.util.Date;
import java.util.List;
import javax.persistence.*;

public class User {
    /**
     * 物理ID
     */
    @Id
    @Column(name = "user_id")
    private Integer userId;

    /**
     * 手机号
     */
    private String mobile;

    /**
     * 微信的openid
     */
    @Column(name = "open_id")
    private String openId;

    /**
     * 角色：1-病人；2-陪诊师
     */
    private Integer role;

    /**
     * 状态：1-正常；2-已实名；-1已注销
     */
    private Integer status;

    /**
     * 所在地区
     */
    private String location;

    /**
     * 头像图片
     */
    private String pic;

    private String tags;

    @Transient
    private List<String> tagList;

    @Transient
    private List<UserTag> userTagList;

    @Transient
    private List<String> picList;
    /**
     * 昵称
     */
    @Column(name = "nick_name")
    private String nickName;

    /**
     * 性别
     */
    private String gender;

    /**
     * 出生日期
     */
    private Integer age;

    /**
     * 身份证号
     */
    @Column(name = "id_no")
    private String idNo;

    /**
     * 真实姓名
     */
    @Column(name = "real_name")
    private String realName;

    /**
     * 陪诊师简介
     */
    @Column(name = "assist_desc")
    private String assistDesc;

    /**
     * 总评分
     */
    private Double rate;

    /**
     * 总服务次数
     */
    @Column(name = "total_service")
    private Integer totalService;

    private String lat;
    private String lng;

    /**
     * 距离，在匹配陪诊师时使用，用于排序
     */
    @Transient
    private double distance;

    @Transient
    private Certify certify;

    /**
     * 获取物理ID
     *
     * @return user_id - 物理ID
     */
    public Integer getUserId() {
        return userId;
    }

    /**
     * 设置物理ID
     *
     * @param userId 物理ID
     */
    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    /**
     * 获取手机号
     *
     * @return mobile - 手机号
     */
    public String getMobile() {
        return mobile;
    }

    /**
     * 设置手机号
     *
     * @param mobile 手机号
     */
    public void setMobile(String mobile) {
        this.mobile = mobile == null ? null : mobile.trim();
    }

    /**
     * 获取微信的openid
     *
     * @return open_id - 微信的openid
     */
    public String getOpenId() {
        return openId;
    }

    /**
     * 设置微信的openid
     *
     * @param openId 微信的openid
     */
    public void setOpenId(String openId) {
        this.openId = openId == null ? null : openId.trim();
    }

    /**
     * 获取角色：1-病人；2-陪诊师
     *
     * @return role - 角色：1-病人；2-陪诊师
     */
    public Integer getRole() {
        return role;
    }

    /**
     * 设置角色：1-病人；2-陪诊师
     *
     * @param role 角色：1-病人；2-陪诊师
     */
    public void setRole(Integer role) {
        this.role = role;
    }

    /**
     * 获取状态：1-正常；2-已实名；-1已注销
     *
     * @return status - 状态：1-正常；2-已实名；-1已注销
     */
    public Integer getStatus() {
        return status;
    }

    /**
     * 设置状态：1-正常；2-已实名；-1已注销
     *
     * @param status 状态：1-正常；2-已实名；-1已注销
     */
    public void setStatus(Integer status) {
        this.status = status;
    }

    /**
     * 获取所在地区
     *
     * @return location - 所在地区
     */
    public String getLocation() {
        return location;
    }

    /**
     * 设置所在地区
     *
     * @param location 所在地区
     */
    public void setLocation(String location) {
        this.location = location == null ? null : location.trim();
    }

    /**
     * 获取头像图片
     *
     * @return pic - 头像图片
     */
    public String getPic() {
        return pic;
    }

    /**
     * 设置头像图片
     *
     * @param pic 头像图片
     */
    public void setPic(String pic) {
        this.pic = pic == null ? null : pic.trim();
    }

    /**
     * 获取昵称
     *
     * @return nick_name - 昵称
     */
    public String getNickName() {
        return nickName;
    }

    /**
     * 设置昵称
     *
     * @param nickName 昵称
     */
    public void setNickName(String nickName) {
        this.nickName = nickName == null ? null : nickName.trim();
    }

    /**
     * 获取性别
     *
     * @return gender - 性别
     */
    public String getGender() {
        return gender;
    }

    /**
     * 设置性别
     *
     * @param gender 性别
     */
    public void setGender(String gender) {
        this.gender = gender == null ? null : gender.trim();
    }

    /**
     * 获取出生日期
     *
     * @return age - 出生日期
     */
    public Integer getAge() {
        return age;
    }

    /**
     * 设置出生日期
     *
     * @param age 出生日期
     */
    public void setAge(Integer age) {
        this.age = age;
    }

    /**
     * 获取身份证号
     *
     * @return id_no - 身份证号
     */
    public String getIdNo() {
        return idNo;
    }

    /**
     * 设置身份证号
     *
     * @param idNo 身份证号
     */
    public void setIdNo(String idNo) {
        this.idNo = idNo == null ? null : idNo.trim();
    }

    /**
     * 获取真实姓名
     *
     * @return real_name - 真实姓名
     */
    public String getRealName() {
        return realName;
    }

    /**
     * 设置真实姓名
     *
     * @param realName 真实姓名
     */
    public void setRealName(String realName) {
        this.realName = realName == null ? null : realName.trim();
    }

    /**
     * 获取陪诊师简介
     *
     * @return assist_desc - 陪诊师简介
     */
    public String getAssistDesc() {
        return assistDesc;
    }

    /**
     * 设置陪诊师简介
     *
     * @param assistDesc 陪诊师简介
     */
    public void setAssistDesc(String assistDesc) {
        this.assistDesc = assistDesc == null ? null : assistDesc.trim();
    }

    /**
     * 获取总评分
     *
     * @return rate - 总评分
     */
    public Double getRate() {
        return rate;
    }

    /**
     * 设置总评分
     *
     * @param rate 总评分
     */
    public void setRate(Double rate) {
        this.rate = rate;
    }

    /**
     * 获取总服务次数
     *
     * @return total_service - 总服务次数
     */
    public Integer getTotalService() {
        return totalService;
    }

    /**
     * 设置总服务次数
     *
     * @param totalService 总服务次数
     */
    public void setTotalService(Integer totalService) {
        this.totalService = totalService;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public List<String> getTagList() {
        return tagList;
    }

    public void setTagList(List<String> tagList) {
        this.tagList = tagList;
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

    public Certify getCertify() {
        return certify;
    }

    public void setCertify(Certify certify) {
        this.certify = certify;
    }

    public List<UserTag> getUserTagList() {
        return userTagList;
    }

    public void setUserTagList(List<UserTag> userTagList) {
        this.userTagList = userTagList;
    }
    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public List<String> getPicList() {
        return picList;
    }

    public void setPicList(List<String> picList) {
        this.picList = picList;
    }
}