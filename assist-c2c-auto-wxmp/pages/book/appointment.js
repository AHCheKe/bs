// pages/publish/publish.js
var app = getApp()
import util from '../../utils/util.js'
Page({

  /**
   * 页面的初始数据
   */
  data: {
    serviceTypeList: ['全程陪诊', '出入院办理', '取送报告', '寻医服务'],
    sindex: 0,
    uploadImgs: [],
    service: {
      serviceType: '全程陪诊',
      reserveType: 1,
      title: '',
      needPickup: '',
      city: '',
      serviceDate: '',
      hospital: '',
      dept: '',
      price: '',
      mobile: '',
      status: 0,
      content: ''
    },
    showPopup: false,
    userInfo: {},
    pickupItems: [
      {value: '0', name: '不需要', checked: 'true'},
      {value: '1', name: '需要'},
    ],
    pindex: 0,
    defaultDate: '',
    assistant: '',
    assistantId: ''
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    let isLogin = util.checkLogin()
    if(!isLogin){
      //进入授权登录页面
      wx.redirectTo({
        url: '/pages/user/login/login',
      })
    }else{
      this.initDate(options)
    }
  },

  initDate:function(options){
    // let cdate = new Date()
    let ddate = new Date()
    ddate.setDate(ddate.getDate()+1)
    ddate.setHours(9)
    ddate.setMinutes(0)
    this.setData({defaultDate: ddate.getTime(), assistant: options.aname, assistantId: options.aid})
  },

  /**
   * 生命周期函数--监听页面初次渲染完成
   */
  onReady: function () {
    
  },

  /**
   * 生命周期函数--监听页面显示
   */
  onShow: function () {
    this.setData({'service.city': app.globalData.currCity})
    // console.log(app.globalData.currCity, this.data.service)
  },
  bindFieldRemarkInput: function(e){
    this.setData({ 'service.content': e.detail.value })
  },
  bindMobileInput: function(e){
    this.setData({ 'service.mobile': e.detail.value })
  },
  bindPriceInput: function(e){
    this.setData({ 'service.price': e.detail.value })
  },
  bindDeptInput: function(e){
    this.setData({ 'service.dept': e.detail.value })
  },
  bindHospitalInput: function(e){
    this.setData({ 'service.hospital': e.detail.value })
  },

  /**
   * 提交表单
   */
  publishForm: function(){
    if(this.data.service.city==''||this.data.service.hospital==''||this.data.service.mobile==''||this.data.service.price==''){
      wx.showToast({title:'请输入必填项', icon: 'error'})
      return
    }
    wx.showLoading({
      title: '加载中',
    })
    let req = {
      service: this.data.service,
      aid: this.data.assistantId
    }
    util.request(
      app.globalData.apiBaseUrl+'/service/app/save',
      req,
      'POST'
    ).then(res=>{
      console.log(res)
      if(res.success){
        wx.showModal({
          title: '提示',
          content: '已向陪诊师提交预约订单，请留意订单状态。',
          showCancel: false,
          complete: (res) => {
            wx.navigateBack()
          }
        })
      }else{
        wx.showToast({
          icon: 'error',
          title: '预约失败',
        })
      }
    })
  },

  bindPickupChange: function(e) {
    let p = parseInt(e.detail.value)
    this.setData({ pindex: p, 'service.needPickup': p})
  },

  bindServiceTypeChange: function(e) {
    let p = parseInt(e.detail.value)
    this.setData({ pindex: p, 'service.serviceType': this.data.serviceTypeList[p]})
  },

  onDateSelect(event) {
    this.setData({
      'service.serviceDate': event.detail,
    });
  },
  
  onShowPopup: function(){
    this.setData({ showPopup: true });
  },

  onClose: function(){
    this.setData({ showPopup: false });
  },

  onChangeCity: function(){
    wx.navigateTo({
      url: '/pages/assist-info/city?servicetype=4',
    })
  },
})