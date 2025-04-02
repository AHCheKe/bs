// pages/current/processing.js
const app = getApp()
import utils from '../../../utils/util.js'
Page({

  /**
   * 页面的初始数据
   */
  data: {
    activityId: '',
    activity: {},
    userInfo: {},
    nodata: true,
    singInCount: 0,
    msgList: null,
    msgContent: '',
    lastLoadTime: -1,
    msgInterval: {},
    msgLoading: true,
    bodyText:'', // 展示的文本内容
    scrollTop:0  // 竖向滚动条的位置
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad(options) {
		this.initUserInfo()
    this.loadMsgInterval()
  },

  /**
   * 生命周期函数--监听页面初次渲染完成
   */
  onReady() {

  },

  /**
   * 生命周期函数--监听页面显示
   */
  onShow() {
    if(wx.getStorageSync('LOGIN_USER')!=''){
    }
  },

  onUnload(){
    console.log('清除读取消息定时器')
    clearInterval(this.data.msgInterval)
  },

  onHide(){
    console.log('清除读取消息定时器')
    clearInterval(this.data.msgInterval)
  },

  loadMsgInterval: function(){
    let that = this
    //定时器，用于轮询新消息
    let timer = setInterval(() => {
      // let timestamp = (new Date()).getTime();
      // that.setData({lastLoadTime: timestamp})
      that.loadMessage()
    }, 3000)  
    this.setData({msgInterval: timer})
  },

  /**
   * 页面相关事件处理函数--监听用户下拉动作
   */
  onPullDownRefresh() {
    
  },

  /**
   * 用户点击右上角分享
   */
  onShareAppMessage() {

	},
	
	//加载当前登录用户的信息
  initUserInfo: function(){
    let userInfo = wx.getStorageSync('LOGIN_USER')
    this.setData({userInfo: userInfo})
  },

  //查询消息列表
  loadMessage: function(){
    let that = this
    let req = {lastLoadTime: this.data.lastLoadTime, activityId: this.data.activity.activityId}
    // if(this.data.msgList!=null){
    //   req.total=this.data.msgList.totalrecord,
    //   req.page=this.data.msgList.currentpage
    // }
    utils.request(
      app.globalData.apiBaseUrl+'/online/message/list',
      req,
      "POST","加载中", "application/x-www-form-urlencoded", false
    ).then(res=>{
      wx.stopPullDownRefresh()
      that.setData({lastLoadTime: (new Date()).getTime()})
			that.setData({
				msgList: res.data,
				msgLoading: false
			})
			that.autoScroll() //重定位到底部
    })
	},
	
  sendTxt: function(){
    let text = this.data.msgContent
    this.sendMessgae(text)
  },

  //发送消息
  sendMessgae: function(msgContent){
    let that = this
    let req = {
      content: msgContent,
    }
    utils.request(
      app.globalData.apiBaseUrl+'/online/message/send',
      req,
      'POST'
    ).then(res=>{
      console.log(res)
      that.setData({msgContent: '', msgType: 1})
    })
  },

  bindMsgContentInput: function(e){
    this.setData({ msgContent: e.detail.value })
  },

  autoScroll() {
    let that = this
    let query = wx.createSelectorQuery()
    // 通过class选择器定位到scorll-view
    query.select('.scroll-text').boundingClientRect(res => {
        that.setData({
            // 由于res.height效果不明显，所以乘以100系数，这个系数可以根据实际效果调整
            scrollTop: res.height * 100
        })
    })
    query.exec(res => {})
  },

})