// pages/rent/rent-detail.js
var app = getApp()
import util from '../../utils/util.js'
import QRCode from '../../utils/qrcode.js'
import Dialog from '@vant/weapp/dialog/dialog'
Page({

  /**
   * 页面的初始数据
   */
  data: {
    orderId: '',
    order: {},
    isSuccessPage: false,
    qrCodeUrl: '',
    qrCodeHtml: '',
    userInfo: {},
    dataList: null,
    rateNum: 0,
    feedback: {
      orderId: '',
      rate: 0,
      content: ''
    },
    showFee: false,
    showCodeValidate: false,
    orderCode: '',
    amount: '',
    remark: ''
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad(options) {
    let that = this
    let ri = options.orderid
    if(options.complete){
      this.setData({isSuccessPage: true})
    }
    this.setData({orderId: ri, 'feedback.orderId': ri}, res=>{
      that.loadOrder()
    })
    this.initUserInfo()
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

  loadOrder: function(){
    let req = this.data.orderId
    let that = this
    util.request(
      app.globalData.apiBaseUrl+'/order/detail?orderId='+req,
      {}
    ).then(res=>{
      wx.stopPullDownRefresh()
      // console.log('读取订单信息：', res.data)
      //二维码里包含的文本：服务码-订单编号-手机号
      let qrContent = res.data.orderId+'-'+res.data.serviceId+'-'+res.data.assistantId
      const qrcode = new QRCode(qrContent) //将以上文本编码成二维码
      //得到二维码数据数组
      let modules = qrcode.make()
      //将二维码输出成html，显示在小程序页面
      let qrhtml = util.printQRCode(modules, "#52ae89")
      that.setData({
        order: res.data,
        //qrCodeUrl: 'https://api.qrserver.com/v1/create-qr-code?data='+res.data.serviceCode+'-'+res.data.orderNo+'-'+res.data.mobile+'&color=5d7db3'
        qrCodeHtml: qrhtml
      })
    })
  },

  /**
   * 接受预约订单
   */
  confirmlOrder: function(){
    let orderId = this.data.orderId
    Dialog.confirm({
      title: '提示',
      message: '确认接受该陪诊预约订单？',
    }).then(() => {
      util.request(
        app.globalData.apiBaseUrl+'/order/assistant/confirm?orderId='+orderId,
        {}
      ).then(res=>{
        Dialog.alert({
          title: '提示',
          message: '已接受陪诊订单，请及时与客人联系并准时提供陪诊服务。',
        }).then(() => {
          wx.navigateBack()
        })
      })
    });
  },

  //拒绝订单
  rejectOrder: function(){
    let orderId = this.data.orderId
    Dialog.confirm({
      title: '提示',
      message: '确认拒绝该陪诊预约订单？',
    }).then(() => {
      util.request(
        app.globalData.apiBaseUrl+'/order/assistant/reject?orderId='+orderId,
        {}
      ).then(res=>{
        Dialog.alert({
          title: '提示',
          message: '已拒绝该邀约陪诊订单',
        }).then(() => {
          wx.navigateBack()
        })
      })
    });
  },

  /**
   * 取消预约
   */
  cancelOrder: function(){
    let orderId = this.data.orderId
    Dialog.confirm({
      title: '提示',
      message: '是否确认取消该订单？',
    }).then(() => {
      util.request(
        app.globalData.apiBaseUrl+'/order/cancel?orderId='+orderId,
        {}
      ).then(res=>{
        Dialog.alert({
          title: '提示',
          message: '已取消',
        }).then(() => {
          wx.navigateBack()
        })
      })
    });
  },

  //扫描二维码，完成订单！
  scanQr: function(){
    let that = this
    //调用微信小程序的scanCode方法，扫码二维码，识别到二维码里的文本后，可以提取到订单号
    wx.scanCode({
      scanType: ['barCode', 'qrCode'],
      success (res) {
        console.log('扫描结果：',res)
        let oid = res.result.split('-')[0]
        console.log(oid, that.data.order.orderId)
        if(res.errMsg=='scanCode:ok'){
          // that.setData({ orderId: oid })
          // that.finishOrder()//解码得到订单号，然后调用完成订单接口
          if(oid == that.data.order.orderId){
            that.onShowFee()
            wx.showToast({
              title: '请填写费用',
              icon: 'error'
            })
          }else{
            wx.showModal({
              title: '提示',
              content: '订单不匹配，请扫描正确的二维码！',
              showCancel: false,
              complete: (res) => {}
            })
          }
        }
      },
      fail(res){
        console.log(res)
        wx.showToast({
          title: '未识别二维码',
          icon: 'error'
        })
      }
    })  
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
        wx.navigateBack()
      })
    })
  },

  //提交反馈
  onFeedback: function(){
    let req = this.data.feedback
    let that = this
    util.request(
      app.globalData.apiBaseUrl+'/order/feedback',
      req
    ).then(res=>{
      Dialog.alert({
        title: '提示',
        message: '已完成评价',
      }).then(() => {
        that.loadOrder()
      })
    })
  },

  onCall: function(e){
    let mobile = e.currentTarget.dataset.mobile
    wx.makePhoneCall({
      phoneNumber: mobile
    })
  },

  loadDataList: function(){
    // let that = this
    // let req = {serviceId: this.data.serviceId}
    // util.request(
    //   app.globalData.apiBaseUrl+'/order/campaign/service/list',
    //   req
    // ).then(res=>{
    //   wx.stopPullDownRefresh()
    //   that.setData({
    //     dataList: res.data
    //   })
    // })
  },

  //打分
  onChangeRate(event){
    this.setData({
      rateNum: event.detail,
      'feedback.rate': event.detail
    });
  },

  bindFeedbackContent: function(e){
    this.setData({ 'feedback.content': e.detail.value })
  },

  bindCodeInput: function(e){
    this.setData({ orderCode: e.detail.value })
  },

  toAssistantDetail: function(e){
    let bid = e.currentTarget.dataset.aid
    console.log('跳转到详情页',bid)
    wx.navigateTo({
      url: '/pages/assistant/assistant-detail?bid='+bid,
    })
  },

  onShowFee: function(e){
    this.setData({showFee: true})
  },

  onShowCode: function(e){
    this.setData({showCodeValidate: true})
  },

  bindRemarkInput: function(e){
    this.setData({ remark: e.detail.value })
  },

  bindFeeInput: function(e){
    this.setData({ amount: e.detail.value })
  },

  onFeeUpload: function(){
    let that = this
    let req = {orderId: this.data.order.orderId, amount: this.data.amount, remark: this.data.remark}
    util.request(
      app.globalData.apiBaseUrl+'/order/update/fee',
      req
    ).then(res=>{
      that.setData({
        showFee: false
      })
      that.loadOrder()
    })
  },

  onCheckCode: function(){
    let that = this
    let req = {orderId: this.data.order.orderId, orderCode: this.data.orderCode}
    util.request(
      app.globalData.apiBaseUrl+'/order/validate/code',
      req
    ).then(res=>{
      if(res.success){
        wx.showToast({
          title: '验证成功',
          icon: 'success'
        })
        that.setData({
          showFee: true,
          showCodeValidate: false
        })
      }else{
        wx.showToast({
          title: '服务码错误',
          icon: 'error'
        })
      }
    })
  },

  onPay: function(){
    let amount = this.data.order.price
    let oid = this.data.orderId
    wx.redirectTo({
      url: '/pages/book/pay?price='+amount+'&oid='+oid,
    })
  }
})