// pages/user/info.js
var app = getApp()
import utils from '../../utils/util.js'
import Dialog from '@vant/weapp/dialog/dialog';
// 引入SDK核心类，js文件根据自己业务，位置可自行放置
var QQMapWX = require('../../utils/qqmap-wx-jssdk.min');
var qqmapsdk;
Page({

  /**
   * 页面的初始数据
   */
  data: {
    profileImg: null,
    userInfo: {},
    user: {
      mobile: "",
      pwd: "",
      userId: "",
      pic: "",
      city: "",
      realName: '',
      age: '',
      gender: '',
      lat: '',
      lng: ''
    },
    genderTypes: ['男', '女', '保密'],
    gindex: 0,
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    this.initUserInfo()
    // 实例化API核心类  (腾讯位置服务的)配置
    qqmapsdk = new QQMapWX({
      key: 'NXDBZ-PWQRD-3RK4F-HHB34-C2DPH-KBFBM'
    });
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

  //选择图片
  chooseImg: function(){
    let _this = this
    wx.chooseMedia({
      count: 1,
      sizeType: ['compressed'],
      sourceType: ['album', 'camera'],
      mediaType: ['image'],
      success (res) {
        // tempFilePath可以作为img标签的src属性显示图片
        const picUrl = res.tempFiles[0].tempFilePath
        wx.navigateTo({
          url: '/pages/copper/cropper?picurl='+picUrl,
        })
      }
    })
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
      console.log(appInfo)
      let gindex = 0
      if(appInfo.gender=='男'){
        gindex = 0
      }else if(appInfo.gender=='女'){
        gindex = 1
      }else{
        gindex = 2
      }
      this.setData({
        userInfo : appInfo,
        profileImg: appInfo.pic,
        gindex: gindex,
        user: appInfo
      })
    }
  },

  bindRealNameChange: function(e){
    this.setData({ 'user.realName': e.detail.value})
  },

  bindMobileChange: function (e) {
    this.setData({ 'user.mobile': e.detail.value })
  },

  bindShortDescChange: function (e) {
    this.setData({ 'user.shortDesc': e.detail.value })
  },

  bindAgeChange: function (e) {
    this.setData({ 'user.age': e.detail.value })
  },

  bindGenderChange: function(e) {
    let gindex = e.detail.value
    let gtype = this.data.genderTypes[gindex]
    this.setData({'user.gender': gtype, gindex: gindex})
  },

  //提交表单
  publishUpdate: function(e){
    wx.showLoading({
      title: '正在更新',
    })
    let that = this
    let req = this.data.user
    utils.request(
      app.globalData.apiBaseUrl+'/user/updateUser',
      req
    ).then(res=>{
      wx.hideLoading()
      Dialog.alert({
        title: '提示',
        message: '个人信息已更新',
      }).then(() => {
        console.log(res)
        console.log('更新用户成功：设置全局变量', app.globalData.userInfo)
        app.globalData.userInfo = res.data
        that.setData({
          userInfo: res.data
        })
        wx.setStorageSync('LOGIN_USER', res.data)
        console.log('更新用户成功：获取全局变量', app.globalData.userInfo)
        wx.switchTab({
          url: '/pages/user/profile',
        })
      });
    })
    app.globalData.imgSrc=''
  },

  //刷新用户信息
  reloadUser: function(){
    var that = this
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

  // 获取当前定位
  getCurrentPositioning: function() {
    const that = this
    wx.showLoading({
      title: '正在定位',
    })
    wx.getLocation({
      type: 'wgs84',
      altitude: true,
      isHighAccuracy: true,
      highAccuracyExpireTime: 2000,
      success: function(res) {
        console.log(res)
        that.setData({
          latitude: res.latitude,
          longitude: res.longitude,
          'photoService.posLat': res.latitude,
          'photoService.posLng': res.longitude
        })
        // 构建请求地址
        // 逆解析接口 /ws/geocoder/v1
        var qqMapApi = 'http://apis.map.qq.com/ws/geocoder/v1/' + "?location=" + that.data.latitude + ',' +
          that.data.longitude + "&key=" + 'NXDBZ-PWQRD-3RK4F-HHB34-C2DPH-KBFBM' + "&get_poi=1";
        that.sendRequest(qqMapApi);
      },
      fail: function(res) {
        console.log(res)
        wx.showToast({
          title: '获取位置信息失败',
          icon: 'none'
        })
      }
    })
  },
  sendRequest:function(qqMapApi) {
    const that = this
    wx.request({
      url: qqMapApi,
      header: {
        'Content-Type': 'application/json'
      },
      data: {},
      method:'GET',
      success: (res) => {
        wx.hideLoading()
        console.log('定位成功：',res)
        if (res.statusCode == 200 && res.data.status == 0) {
          // 从返回值中提取需要的业务地理信息数据 国家、省、市、县区、街道
          // that.setData({ 'address.province': res.data.resul
          let cityStr = res.data.result.address
          that.setData({ 
            'userInfo.lat': res.data.result.location.lat,
            'userInfo.lng': res.data.result.location.lng,
            'userInfo.location': cityStr
          });
        }
      }
    })
  },
})