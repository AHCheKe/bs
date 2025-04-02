// pages/discover/news/news-detail.js
const app = getApp()
import utils from '../../utils/util.js'
import Notify from '@vant/weapp/notify/notify'
import Dialog from '@vant/weapp/dialog/dialog'
Page({

  /**
   * 页面的初始数据
   */
  data: {
    serviceId: '',
    serviceInfo: {},
    userInfo: {},
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad(options) {
    let bid = options.pid
    this.setData({serviceId: bid})
    this.loadDetail()
    this.initUserInfo()
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

  },

  //加载当前登录用户的信息
  initUserInfo: function(){
    let userInfo = wx.getStorageSync('LOGIN_USER')
    this.setData({userInfo: userInfo})
  },

  loadDetail: function(){
    let that = this
    utils.request(
      app.globalData.apiBaseUrl+'/service/detail?pid='+this.data.serviceId,
      {}
    ).then(res=>{
      wx.stopPullDownRefresh()
      that.setData({
        serviceInfo: res.data.service,
      }, ()=>{

      })
    })
  },

  //抢单事件
  onSignUp: function(e){
    let that = this
    Dialog.confirm({
      title: '提示',
      message: '确认要提交陪诊抢单申请吗？',
    }).then(() => {
      that.submitSignUp()
    })
  },
  
  // 提交抢单请求
  submitSignUp: function(){
    let sid = this.data.serviceId
    utils.request(
      app.globalData.apiBaseUrl+'/order/campaign/save',
      {serviceId: sid}
    ).then(res=>{
      console.log('res', res)
      if(res.success){
        Dialog.alert({
          title: '提示',
          message: '已提交抢单申请，请关注个人中心-我的抢单中的状态变化，若抢单成功请及时与客人联系。',
        }).then(() => {
          wx.navigateBack()
        })
      }else{
        Dialog.alert({
          title: '提示',
          message: res.msg,
        }).then(() => {
        })
      }
    })
  }
})