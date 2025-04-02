package com.assist.dao.model;

import java.util.Date;
import javax.persistence.*;

public class Admin {
    /**
     * 物理ID
     */
    @Id
    @Column(name = "admin_id")
    private Integer adminId;

    /**
     * 手机号
     */
    private String mobile;

    /**
     * 密码
     */
    private String pwd;

    /**
     * 姓名
     */
    @Column(name = "real_name")
    private String realName;

    /**
     * 上次登录时间
     */
    @Column(name = "last_login")
    private Date lastLogin;

    /**
     * 性别
     */
    private String gender;

    /**
     * 状态：0-待审核；1-审核通过；2：冻结；-1：拒绝申请
     */
    private Integer status;

    /**
     * 获取物理ID
     *
     * @return admin_id - 物理ID
     */
    public Integer getAdminId() {
        return adminId;
    }

    /**
     * 设置物理ID
     *
     * @param adminId 物理ID
     */
    public void setAdminId(Integer adminId) {
        this.adminId = adminId;
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
     * 获取密码
     *
     * @return pwd - 密码
     */
    public String getPwd() {
        return pwd;
    }

    /**
     * 设置密码
     *
     * @param pwd 密码
     */
    public void setPwd(String pwd) {
        this.pwd = pwd == null ? null : pwd.trim();
    }

    /**
     * 获取姓名
     *
     * @return real_name - 姓名
     */
    public String getRealName() {
        return realName;
    }

    /**
     * 设置姓名
     *
     * @param realName 姓名
     */
    public void setRealName(String realName) {
        this.realName = realName == null ? null : realName.trim();
    }

    /**
     * 获取上次登录时间
     *
     * @return last_login - 上次登录时间
     */
    public Date getLastLogin() {
        return lastLogin;
    }

    /**
     * 设置上次登录时间
     *
     * @param lastLogin 上次登录时间
     */
    public void setLastLogin(Date lastLogin) {
        this.lastLogin = lastLogin;
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
     * 获取状态：0-待审核；1-审核通过；2：冻结；-1：拒绝申请
     *
     * @return status - 状态：0-待审核；1-审核通过；2：冻结；-1：拒绝申请
     */
    public Integer getStatus() {
        return status;
    }

    /**
     * 设置状态：0-待审核；1-审核通过；2：冻结；-1：拒绝申请
     *
     * @param status 状态：0-待审核；1-审核通过；2：冻结；-1：拒绝申请
     */
    public void setStatus(Integer status) {
        this.status = status;
    }
}