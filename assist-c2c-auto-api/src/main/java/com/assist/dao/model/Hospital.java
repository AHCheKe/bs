package com.assist.dao.model;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Transient;
import java.util.List;

public class Hospital {
    /**
     * 主键
     */
    @Id
    @Column(name = "hospital_id")
    private Integer hospitalId;

    /**
     * 医院名称
     */
    @Column(name = "hospital_name")
    private String hospitalName;

    /**
     * 标签
     */
    @Column(name = "hospital_tags")
    private String hospitalTags;

    @Transient
    private List<String> tagsList;

    /**
     * 所在城市
     */
    private String city;

    private String area;

    private String address;

    @Column(name = "main_pic")
    private String mainPic;

    /**
     * 联系电话
     */
    private String tel;

    /**
     * 详情
     */
    private String detail;

    /**
     * 服务须知
     */
    private String notice1;

    /**
     * 注意事项
     */
    private String notice2;

    /**
     * 获取主键
     *
     * @return hospital_id - 主键
     */
    public Integer getHospitalId() {
        return hospitalId;
    }

    /**
     * 设置主键
     *
     * @param hospitalId 主键
     */
    public void setHospitalId(Integer hospitalId) {
        this.hospitalId = hospitalId;
    }

    /**
     * 获取医院名称
     *
     * @return hospital_name - 医院名称
     */
    public String getHospitalName() {
        return hospitalName;
    }

    /**
     * 设置医院名称
     *
     * @param hospitalName 医院名称
     */
    public void setHospitalName(String hospitalName) {
        this.hospitalName = hospitalName == null ? null : hospitalName.trim();
    }

    /**
     * 获取标签
     *
     * @return hospital_tags - 标签
     */
    public String getHospitalTags() {
        return hospitalTags;
    }

    /**
     * 设置标签
     *
     * @param hospitalTags 标签
     */
    public void setHospitalTags(String hospitalTags) {
        this.hospitalTags = hospitalTags == null ? null : hospitalTags.trim();
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
     * 获取联系电话
     *
     * @return tel - 联系电话
     */
    public String getTel() {
        return tel;
    }

    /**
     * 设置联系电话
     *
     * @param tel 联系电话
     */
    public void setTel(String tel) {
        this.tel = tel == null ? null : tel.trim();
    }

    /**
     * 获取详情
     *
     * @return detail - 详情
     */
    public String getDetail() {
        return detail;
    }

    /**
     * 设置详情
     *
     * @param detail 详情
     */
    public void setDetail(String detail) {
        this.detail = detail == null ? null : detail.trim();
    }

    /**
     * 获取服务须知
     *
     * @return notice1 - 服务须知
     */
    public String getNotice1() {
        return notice1;
    }

    /**
     * 设置服务须知
     *
     * @param notice1 服务须知
     */
    public void setNotice1(String notice1) {
        this.notice1 = notice1 == null ? null : notice1.trim();
    }

    /**
     * 获取注意事项
     *
     * @return notice2 - 注意事项
     */
    public String getNotice2() {
        return notice2;
    }

    /**
     * 设置注意事项
     *
     * @param notice2 注意事项
     */
    public void setNotice2(String notice2) {
        this.notice2 = notice2 == null ? null : notice2.trim();
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getMainPic() {
        return mainPic;
    }

    public void setMainPic(String mainPic) {
        this.mainPic = mainPic;
    }


    public List<String> getTagsList() {
        return tagsList;
    }

    public void setTagsList(List<String> tagsList) {
        this.tagsList = tagsList;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }
}