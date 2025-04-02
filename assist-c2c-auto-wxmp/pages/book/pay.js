// pages/publish/publish.js
var app = getApp()
import util from '../../utils/util.js'
import Dialog from '@vant/weapp/dialog/dialog'
Page({

  /**
   * 页面的初始数据
   */
  data: {
    orderInfo: {},
    orderId: '',
    amount: 0,
    orderId: '',
    showAuth: true,
    hasWaitPay: true
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    let amt = parseFloat(options.price)
    let oid = options.oid
    this.setData({
      amount: amt,
      showAuth: true,
      orderId: oid
    },()=>{
      
    })
  },

  /**
   * 生命周期函数--监听页面显示
   */
  onShow: function () {
    
  },

  // loadOrder: function(){
  //   let that = this
  //   util.request(
  //     app.globalData.apiBaseUrl+'/order/detail?orderId='+that.data.orderId,
  //     {}
  //   ).then(res=>{
  //     that.setData({
  //       orderInfo: res.data
  //     })
  //   })
  // },

  onCloseAuth: function(){
    this.setData({showAuth: false})
  },

  //确认支付
  doPay: function(e){
    this.finishOrder()
  },

  // 结束订单
  finishOrder: function(){
    let orderId = this.data.orderId
    util.request(
      app.globalData.apiBaseUrl+'/order/finish?orderId='+orderId,
      {}
    ).then(res=>{
      Dialog.alert({
        title: '提示',
        message: '订单已完成',
      }).then(() => {
        wx.switchTab({
          url: '/pages/book/myorders',
        })
      })
    })
  },
})