// pages/user/info.js
var app = getApp()
import utils from '../../utils/util.js'
import Dialog from '@vant/weapp/dialog/dialog';
Page({

  /**
   * 页面的初始数据
   */
  data: {
    userInfo: {},
    user: {},
    myTagList: [],
    serviceTagList: [],
    selectedTagIds: []
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    this.initUserInfo()
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
    if(app.globalData.imgSrc!=null&&app.globalData.imgSrc!=''){
      //显示已裁剪的头像
      this.setData({profileImg: app.globalData.imgSrc, 'user.pic':app.globalData.imgSrc})
    }
  },

  initUserInfo(){
    if(app.globalData.userInfo == null){
      wx.showModal({
        title: '提示',
        content: '请先登录',
        success (res) {
          if (res.confirm) {
            wx.navigateTo({
              url: '/pages/user/login/login',
            })
          }
        }
      })
    }else{
      let appInfo = app.globalData.userInfo
      let that = this
      this.setData({
        userInfo : appInfo,
        user: appInfo
      })
      this.initDate()
    }
  },

  initDate:function(){
    //加载标签列表
    let that = this
    utils.request(
      app.globalData.apiBaseUrl+'/tag/data',
      {}
    ).then(res=>{
      that.setData({
        myTagList: res.data.userTags,
        serviceTagList: res.data.serviceTagList,
      }, ()=>{
      })
    })
  },

  bindMobileChange: function (e) {
    this.setData({ 'user.mobile': e.detail.value })
  },

  bindTagChange: function(e) {
    let p = e.detail.value
    this.setData({ pindex: p, 'service.serviceTags': p})
  },

  //提交表单
  publishUpdate: function(e){
    if(this.data.selectedTagIds.length==0){
      Dialog.alert({
        title: '提示',
        message: '请勾选新的标签',
      }).then()
      return
    }
    wx.showLoading({
      title: '正在提交',
    })
    let that = this
    let req = {newTagIds: this.data.selectedTagIds}
    utils.request(
      app.globalData.apiBaseUrl+'/tag/updateTags',
      req
    ).then(res=>{
      wx.hideLoading()
      Dialog.alert({
        title: '提示',
        message: '已提交新标签申请，请耐心等候管理员审核',
      }).then(() => {
        wx.switchTab({
          url: '/pages/user/profile',
        })
      });
    })
  },

  //刷新用户信息
  reloadUser: function(){
    wx.request({
      url: app.globalData.apiBaseUrl+'/user/updateUser',
      header: {
        'content-type': 'application/json' ,
        'Cookie': wx.getStorageSync('cookieKey')
      },
      method: 'POST',
      success(res){
        console.log(res)
        wx.hideLoading()
        if(res.data.code == "0"){
        }
      }
    })
  },

  checkboxChange(e) {
    console.log('checkbox发生change事件，携带value值为：', e.detail.value)
    this.setData({selectedTagIds: e.detail.value})
  }
})