// pages/discover/news/news-list.js
const app = getApp()
import utils from '../../utils/util.js'
// 引入SDK核心类，js文件根据自己业务，位置可自行放置
var QQMapWX = require('../../utils/qqmap-wx-jssdk.min');
var qqmapsdk;
Page({

  /**
   * 页面的初始数据
   */
  data: {
    listData: null,
    noMore: false,
    kw: '',
    noSearch: false,
    currCity: '杭州市',
    totalrecords: null,
    page: null,
    slidePics: [],
    serviceTypeList: [],
    noticeList: [],
    userInfo: {}
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad(options) {
    let city = app.globalData.currCity
    console.log('当前城市：',city)
    if(city==undefined||city==null||city==''){
      app.globalData.currCity = '杭州市'
      this.setData({currCity: '杭州市'})
      // this.getCurrentPositioning()
    }else{
      // this.setData({listData: null, noMore: false, currCity: city})
      // this.loadList()
    }
    // // 实例化API核心类  (腾讯位置服务的)配置
    qqmapsdk = new QQMapWX({
      key: 'NXDBZ-PWQRD-3RK4F-HHB34-C2DPH-KBFBM'
    });
    this.loadSlidePic()
    this.loadServiceType()
  },

  onShow(){
    let isLogin = utils.checkLogin()
    if(!isLogin){
      //进入授权登录页面
      wx.redirectTo({
        url: '/pages/user/login/login',
      })
    }
    if(wx.getStorageSync('LOGIN_USER')!=''){
      this.initUserInfo()
      this.checkCount()
    }
  },

  /**
   * 页面相关事件处理函数--监听用户下拉动作
   */
  onPullDownRefresh() {
    this.setData({listData: null})
    // this.loadRecList()
  },

  /**
   * 页面上拉触底事件的处理函数
   */
  onReachBottom() {
    console.log('触底了！', this.data.listData.currentpage, this.data.listData.totalpage)
    if(this.data.listData==null || this.data.listData.currentpage==this.data.listData.totalpage){
      this.setData({noMore:true})
      return
    }
    let tmp = this.data.listData
    tmp.currentpage=tmp.currentpage+1
    this.setData({listData: tmp})
    this.loadList()
  },

  loadSlidePic: function(){
    let that  = this
    utils.request(
      app.globalData.apiBaseUrl+'/index/slide/picture',
      {}
    ).then(res=>{
      that.setData({
        slidePics: res.data,
      })
    })
  },

  //查询公告列表信息
  loadList: function(){
    let that = this
    let req = {currCity: this.data.currCity}
    if(this.data.listData!=null){
      req.total=this.data.listData.totalrecord,
      req.page=this.data.listData.currentpage
    }
    utils.request(
      app.globalData.apiBaseUrl+'/index/notice/list',
      req
    ).then(res=>{
      wx.stopPullDownRefresh()
      let existList = that.data.listData
        if(existList!=null){
          existList.records=existList.records.concat(res.data.records)
          that.setData({
            listData: existList
          })
        }else{
          that.setData({
            listData: res.data,
            totalrecords: res.data.totalrecords,
            page: res.data.page
          })
        }
        let noMore = false
        if(res.data.page==res.data.totalpage||res.data.records.length==0){
          noMore=true 
        }
        that.setData({noMore: noMore})
    })
  },

  //加载当前登录用户的信息
  initUserInfo: function(){
    let that = this
    //到服务端实时load一下用户信息
    utils.request(
      app.globalData.apiBaseUrl+'/order/load/user',
      {},
      'POST'
    ).then(res=>{
      console.log(res)
      that.setData({userInfo: res.data, notLogin: false})
      wx.setStorageSync("LOGIN_USER", res.data)
      app.globalData.userInfo = res.data 
      wx.hideLoading()
    })
  },

  checkCount: function(e){
    utils.request(
      app.globalData.apiBaseUrl+'/order/assistant/neworders',
      {}
    ).then(res=>{
      if(res.data>0){
        wx.setTabBarBadge({index: 2, text: res.data+''});
      }
    })
  },

  toDetailPage: function(e){
    let aid = e.currentTarget.dataset.aid
    console.log('跳转到详情页',aid)
    wx.navigateTo({
      url: '/pages/hospital-info/detail?pid='+aid,
    })
  },

  toNoticeDetailPage: function(e){
    let aid = e.currentTarget.dataset.nid
    console.log('跳转到详情页',aid)
    wx.navigateTo({
      url: '/pages/home/notice-detail?nid='+aid,
    })
  },

  loadServiceType: function(){
    let that = this
    utils.request(
      app.globalData.apiBaseUrl+'/index/service/list',
      {}
    ).then(res=>{
      that.setData({
        serviceTypeList: res.data.serviceList,
        noticeList: res.data.noticeList
      })
    })
  },

  onClearSearch: function(e){
    console.log('EEEE')
    this.setData({kw: ''})
  },

  onHide(){
    this.setData({kw: ''})
  },

  changeCity: function(e){
    wx.navigateTo({
      url: '/pages/assist-info/city?servicetype=1',
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
          let cityStr = res.data.result.address_component.city
          // that.setData({ 'photoService.posLng': res.data.result.location.lat });
          // that.setData({ 'photoService.posLat': res.data.result.location.lng });
          that.setData({
            currCity: cityStr
          })
          //获得当前城市！
          app.globalData.currCity=cityStr
          that.loadList()
        }
      }
    })
  },

  toAddPage: function(e){
    let type = e.currentTarget.dataset.tid
    wx.navigateTo({
      url: '/pages/assist-info/publish?tp='+type,
    })
  }
})