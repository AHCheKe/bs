// pages/service/page.js
var app = getApp()
import util from '../../utils/util.js'
Page({

  /**
   * 页面的初始数据
   */
  data: {
    historyList: null, 
    noMore: false,
    userInfo: {}
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad(options) {
    this.initUserInfo()
  },

  /**
   * 生命周期函数--监听页面显示
   */
  onShow() {
    this.loadHistory()
    this.checkCount()
  },

  //加载当前登录用户的信息
  initUserInfo: function(){
    let isLogin = util.checkLogin()
    console.log('isLogin:', isLogin)
    if(!isLogin){
      //进入授权登录页面
      wx.redirectTo({
        url: '/pages/user/login/login',
      })
    }else{
      let userInfo = wx.getStorageSync('LOGIN_USER')
      console.log('userInfo:', userInfo)
      this.setData({userInfo: userInfo})
    }
  },

  /**
   * 页面相关事件处理函数--监听用户下拉动作
   */
  onPullDownRefresh() {
    this.setData({historyList: null})
    this.loadHistory()
  },

  /**
   * 页面上拉触底事件的处理函数
   */
  onReachBottom() {
    if(this.data.historyList==null || this.data.historyList.currentpage==this.data.historyList.totalpage){
      this.setData({noMore:true})
      return
    }
    let tmp = this.data.historyList
    tmp.currentpage=tmp.currentpage+1
    this.setData({historyList: tmp})
    this.loadHistory()
  },

  //查询预约历史信息
  loadHistory: function(){
    let that = this
    util.request(
      app.globalData.apiBaseUrl+'/order/list',
      {}
    ).then(res=>{
      wx.stopPullDownRefresh()
      let isEmpty = false
      if(res.data.totalrecord==0){
        isEmpty = true
      }
      that.setData({
        historyList: res.data,
        noMore: isEmpty
      })
    })
  },

  checkCount: function(e){
    util.request(
      app.globalData.apiBaseUrl+'/order/assistant/neworders',
      {}
    ).then(res=>{
      if(res.data>0){
        wx.setTabBarBadge({index: 2, text: res.data+''});
      }
    })
  },

  toDetail: function(e){
    let sid = e.currentTarget.dataset.oid
    wx.navigateTo({
      url: '/pages/book/order-detail?orderid='+sid,
    })
  },
})