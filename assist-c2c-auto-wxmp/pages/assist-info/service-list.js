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
    currCity: '',
    totalrecords: null,
    page: null
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad(options) {
    let city = app.globalData.currCity
    console.log('当前城市：',city)
    if(city==undefined||city==null||city==''){
      app.globalData.currCity = '广州市'
      // this.getCurrentPositioning()
    }else{
      // this.setData({listData: null, noMore: false, currCity: city})
      // this.loadList()
    }
    // // 实例化API核心类  (腾讯位置服务的)配置
    qqmapsdk = new QQMapWX({
      key: 'NXDBZ-PWQRD-3RK4F-HHB34-C2DPH-KBFBM'
    });    
  },

  onShow(){
    let city = app.globalData.currCity
    console.log('当前城市：',city)
    if(city==undefined||city==null||city==''){
      
    }else{
      this.setData({listData: null, noMore: false, currCity: city})
      this.loadList()
    }
    if(wx.getStorageSync('LOGIN_USER')!=''){
      this.checkCount()
    }
  },

  /**
   * 页面相关事件处理函数--监听用户下拉动作
   */
  onPullDownRefresh() {
    this.setData({listData: null})
    this.loadList()
  },

  /**
   * 页面上拉触底事件的处理函数
   */
  onReachBottom() {
    if(this.data.listData==null || this.data.listData.currentpage==this.data.listData.totalpage){
      this.setData({noMore:true})
      return
    }
    let tmp = this.data.listData
    tmp.currentpage=tmp.currentpage+1
    this.setData({listData: tmp})
    this.loadNews()
  },

  //查询列表信息
  loadList: function(){
    let that = this
    let req = {currCity: this.data.currCity}
    if(this.data.listData!=null){
      req.total=this.data.listData.totalrecord,
      req.page=this.data.listData.currentpage
    }
    utils.request(
      app.globalData.apiBaseUrl+'/service/list',
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

  //搜索
  onSearch: function(){
    let that = this
    let req = {kw: this.data.kw, currCity: this.data.currCity}
    utils.request(
      app.globalData.apiBaseUrl+'/service/list',
      req
    ).then(res=>{
      let noResult = false
      if(res.data.records.length==0){
        noResult = true
      }
      wx.stopPullDownRefresh()
        that.setData({
          listData: res.data,
          noSearch: noResult
        })
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

  bindKwInput: function(e){
    this.setData({ kw: e.detail.value })
  },

  toDetailPage: function(e){
    let aid = e.currentTarget.dataset.aid
    console.log('跳转到详情页',aid)
    wx.navigateTo({
      url: '/pages/assist-info/detail?pid='+aid,
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
      url: '/pages/assist-info/city?servicetype=2',
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
    wx.navigateTo({
      url: '/pages/assist-info/publish',
    })
  }
})