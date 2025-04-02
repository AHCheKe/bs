// pages/user/fav/fav-list.js
const app = getApp()
import utils from '../../../utils/util.js'
import Dialog from '@vant/weapp/dialog/dialog'
Page({

  /**
   * 页面的初始数据
   */
  data: {
    dataList: null,
    noMore: false,
    serviceId: ''
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad(options) {
    let that = this
    this.setData({
      serviceId: options.sid
    },()=>{
      that.loadDataList()
    })
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
    this.loadDataList()
  },
  
  loadDataList: function(){
    let that = this
    let req = {
      serviceId: this.data.serviceId
    }
    // utils.request(
    //   app.globalData.apiBaseUrl+'/order/campaign/service/list',
    //   req
    // ).then(res=>{
    //   wx.stopPullDownRefresh()
    //   that.setData({
    //     dataList: res.data
    //   })
    // })
  },

  /**
   * 页面上拉触底事件的处理函数
   */
  onReachBottom() {
    
  },

  //下拉刷新
  onPullDownRefresh(){
    this.setData({dataList: null})
    this.loadDataList()
  },

  selectAssistant: function(e){
    let cid = e.currentTarget.dataset.cid
    console.log('选定陪诊师',cid)
    let req = {cId: cid}
    let price = this.data.dataList[0].service.price
    Dialog.confirm({
      title: '提示',
      message: '确定选择该陪诊师？确认后将生成陪诊订单，并支付陪诊费用。陪诊费将由平台暂时担保，在陪诊结束后转给陪诊师。',
    }).then(() => {
      utils.request(
        app.globalData.apiBaseUrl+'/order/user/confirm',
        req
      ).then(res=>{
        wx.redirectTo({
          url: '/pages/book/pay?price='+price,
        })
      })
    });
  }
})