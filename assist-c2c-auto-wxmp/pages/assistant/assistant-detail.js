// pages/discover/news/news-detail.js
const app = getApp()
import utils from '../../utils/util.js'
import Dialog from '@vant/weapp/dialog/dialog'
Page({

  /**
   * 页面的初始数据
   */
  data: {
    uid: '',
    comments: [],
    commentContent: '',
    userInfo: {},
    assistant: {}
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad(options) {
    console.log(options)
    let bid = options.bid
    this.setData({uid: bid, userInfo: app.globalData.userInfo})
    this.loadBlogDetail()
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

  loadBlogDetail: function(){
    let that = this
    utils.request(
      app.globalData.apiBaseUrl+'/assistant/detail?uid='+that.data.uid,
      {}
    ).then(res=>{
      wx.stopPullDownRefresh()
      that.setData({
        assistant: res.data.assistant,
      })
    })
  },

  //浏览大图
  showLargeImage(e) {
    let currIdx = e.currentTarget.dataset.idx //被点击的图片在该笔记中的下标，比如有五张图片，被点的是第3张，currIdx则为2
    let curr = ''
    let imgList = []
    for(var idx=0; idx<this.data.assistant.picList.length; idx++){
      imgList.push(this.data.assistant.picList[idx])
      if(currIdx==idx){
          curr = this.data.assistant.picList[idx]
      }
    }
    wx.previewImage({
        urls: imgList, //需要预览的图片http链接列表，注意是数组
        current: curr, // 当前显示图片的http链接，默认是第一个
        success: function (res) { },
        fail: function (res) { },
        complete: function (res) { },
      })
  },

  //进入预约页面
  toApp: function(){
    let aid = this.data.assistant.userId
    let aname = this.data.assistant.realName
    wx.navigateTo({
      url: '/pages/assist-info/publish?aid='+aid+'&aname='+aname,
    })
  }
})