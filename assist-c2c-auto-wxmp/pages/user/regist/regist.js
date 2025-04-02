// miniprogram/pages/login/login.js
var app = getApp()
import utils from '../../../utils/util.js'
var QQMapWX = require('../../../utils/qqmap-wx-jssdk.min');
var qqmapsdk;
Page({

  /**
   * 页面的初始数据
   */
  data: {
    mobile: '',
    pwd: '',
    realName: '',
    roleItems: [
      {value: '1', name: '病人/家属', checked: 'true'},
      {value: '2', name: '陪诊师'},
    ],
    role: 1,
    city: '',
    region: ['广东省', '广州市', '海珠区'],
    uploadImgs: [],
    attachment: '',
    appContent: '',
    userInfo: '',
    location: '请点击按钮获取定位',
    lat: '',
    lng: '',
    serviceTagList: [],
    genderTypes: ['男', '女', '保密'],
    gindex: 0,
    gender: '男',
    age: '',
    idNo: ''
  },

  onLoad: function(options){
   //弹出登录授权框
    this.setData({showAuth: true})
    this.initUserInfo()
    // 实例化API核心类  (腾讯位置服务的)配置
    qqmapsdk = new QQMapWX({
      key: 'NXDBZ-PWQRD-3RK4F-HHB34-C2DPH-KBFBM'
    });
    this.initDate()
  },

  initDate:function(){
    //加载标签列表
    let that = this
    utils.request(
      app.globalData.apiBaseUrl+'/index/tag/list',
      {}
    ).then(res=>{
      that.setData({
        serviceTagList: res.data,
      }, ()=>{
        console.log('ddd')
      })
    })
  },

  //向服务端发起激活更新请求
  onRegist(){
    let that = this
    wx.showLoading({
      title: '正在在加载',
    })
    //发起网络请求
    wx.request({
      url: app.globalData.apiBaseUrl+'/user/active',
      method: 'POST',
      data: {
        mobile: that.data.mobile,
        role: that.data.role,
        realName: that.data.realName,
        city: that.data.location,
        attachment: that.data.uploadImgs.join(','),
        appContent: that.data.appContent,
        userId: that.data.userInfo.userId,
        selectedTagIds: that.data.selectedTagIds.join(','),
        gender: that.data.gender,
        age: that.data.age,
        lat: that.data.lat,
        lng: that.data.lng,
        idNo: that.data.idNo
      },
      success(res){
        wx.hideLoading()
        if(res.data.code==200){
          if(that.data.role==2){
            wx.showModal({
              title: '提示',
              content: '陪诊师申请已提交，请耐心等候管理员审核。',
              complete: (res) => {
                wx.switchTab({
                  url: '/pages/home/index',
                })
              }
            })
          }else{
            wx.showToast({
              title: '注册成功',
            })
            wx.switchTab({
              url: '/pages/user/profile',
            })
          }
        }
      }
    })
  },

  toReg: function(){
    wx.navigateTo({
      url: '/pages/user/regist/regist'
    })
  }, 

  inputMobile: function(e){
    this.setData({ mobile: e.detail.value })
  },

  inputRealName: function (e) {
    this.setData({ realName: e.detail.value })
  },
  
  inputAppContent: function(e){
    this.setData({ appContent: e.detail.value })
  },

  bindRoleChange: function(e) {
    let p = parseInt(e.detail.value)
    this.setData({ role: p})
  },

  bindRegionChange: function (e) {
    console.log('picker发送选择改变，携带值为', e.detail.value)
    this.setData({
      region: e.detail.value,
      city: e.detail.value[1]
    })
  },

  //加载当前登录用户的信息
  initUserInfo: function(){
    let userInfo = wx.getStorageSync('LOGIN_USER')
    this.setData({userInfo: userInfo})
  },

  //选择图片
  chooseImg: function(){
    let _this = this
    wx.chooseMedia({
      count: 2,
      sizeType: ['compressed'],
      sourceType: ['album', 'camera'],
      mediaType: ['image'],
      success (res) {
        console.log(res)
        wx.showLoading({
          title: '上传图片中...',
        })
        // tempFilePath可以作为img标签的src属性显示图片
        const tempFilePaths = res.tempFiles
        for(var idx=0; idx<tempFilePaths.length; idx++){
          var tmpFileName = tempFilePaths[0].tempFilePath.substring(tempFilePaths[idx].tempFilePath.lastIndexOf('/')+1)
          wx.uploadFile({
            url: 'http://129.211.222.131:16602/upload-server/upload', //图片服务器
            filePath: tempFilePaths[idx].tempFilePath,
            name: 'myFile',
            formData: {
              'user': 'test',
              'fileName': tmpFileName,
              'u': app.globalData.uuid
            },
            success (res){
              //上传成功回调
              wx.hideLoading()
              let resjson = JSON.parse(res.data)
              console.log(resjson.url)
              let picArr = _this.data.uploadImgs
              picArr.push(resjson.url)
              _this.setData({
                uploadImgs: picArr
              })
            }
          })
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
            'lat': res.data.result.location.lat,
            'lng': res.data.result.location.lng,
            'location': cityStr
          });
        }
      }
    })
  },

  checkboxChange(e) {
    console.log('checkbox发生change事件，携带value值为：', e.detail.value)
    this.setData({selectedTagIds: e.detail.value})
  },

  bindAgeChange: function (e) {
    this.setData({ age: e.detail.value })
  },

  bindIdNoChange: function (e) {
    this.setData({ idNo: e.detail.value })
  },

  bindGenderChange: function(e) {
    let gindex = e.detail.value
    let gtype = this.data.genderTypes[gindex]
    this.setData({gender: gtype, gindex: gindex})
  },
})