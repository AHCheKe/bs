// pages/user/fav/fav-list.js
const app = getApp()
import utils from '../../../utils/util.js'
Page({

  /**
   * 页面的初始数据
   */
  data: {
    dataList: null,
    noMore: false
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad(options) {
    
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
    let req = {}
    if(this.data.dataList!=null){
      req.total=this.data.dataList.totalrecord,
      req.page=this.data.dataList.currentpage
    }
    utils.request(
      app.globalData.apiBaseUrl+'/order/campaign/my/list',
      req
    ).then(res=>{
      wx.stopPullDownRefresh()
      that.setData({
        dataList: res.data
      })
    })
  },

  /**
   * 页面上拉触底事件的处理函数
   */
  onReachBottom() {
    if(this.data.dataList==null || this.data.dataList.currentpage==this.data.dataList.totalpage){
      this.setData({noMore:true})
      return
    }
    let tmp = this.data.dataList
    tmp.currentpage=tmp.currentpage+1
    this.setData({dataList: tmp})
    this.loadDataList()
  },

  //下拉刷新
  onPullDownRefresh(){
    this.setData({dataList: null})
    this.loadDataList()
  },

  toBlogDetail: function(e){
    let bid = e.currentTarget.dataset.blogid
    console.log('跳转到详情页',bid)
    wx.navigateTo({
      url: '/pages/discovery/blog-detail?bid='+bid,
    })
  },
})