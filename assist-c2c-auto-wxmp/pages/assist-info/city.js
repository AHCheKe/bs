// 获取到app实例
const app = getApp()
// 引入SDK核心类，js文件根据自己业务，位置可自行放置
var QQMapWX = require('../../utils/qqmap-wx-jssdk.min');
var qqmapsdk;
import cityJson from '../../utils/city'
Page({
  /**
   * 页面的初始数据
   */
  data: {
    cityJson, // 城市数据源
    hotCity: ['北京市', '上海市', '广州市', '深圳市', '厦门市', '长沙市', '武汉市', '杭州市', '西安市', '昆明市', '成都市', '重庆市'], // 热门城市
    searchResult: [], // 搜索城市的结果
    inputShowed: false, // 输入框是否显示
    inputVal: '', // 搜索框输入的内容
    // 当前城市
    // CurrentLocation: {
    //   province: '定位',   // 省份
    //   city: '', // 城市
    //   district: '',// 地区
    // },
    CurrentLocation: '正在定位',
    // 显示的下标
    showIndex: 10,
    // 获取全局共享数据 状态栏高度
    statsBarHeight: app.globalData.statusBarHeight, // 44px
    //  获取全局共享数据 导航栏高度
    naviBarHeight: app.globalData.naviBarHeight, // 44px
    currService: 1
  },
  // 后退上一页 back
  tapBack() {
    wx.navigateBack({
      delta: 1,
      fail() {
        wx.switchTab({
          url: '/pages/home/index',
        })
      },
    })
  },
  // 折叠面板 展开收起的方法
  changOpen(e) {
    this.data.questList[e.currentTarget.dataset.index].t = !this.data.questList[e.currentTarget.dataset.index].t
    this.setData({
      questList: this.data.questList
    })
    if (e.currentTarget.dataset.index != this.data.showIndex) {
      // console.log('true的时候');
      this.setData({
        showIndex: e.currentTarget.dataset.index,
      })
    }
    else {
      // console.log('false的时候');
      this.setData({
        showIndex: 10
      })
    }
 
  },
  
  // 定位
  getLocation() {
    console.log('打开城市选择');
    wx.navigateTo({
      // url: '/pages/subPackages/other/selectCity/selectCity',
    })
  },
  // 获取当前定位
  getCurrentPositioning: function() {
    const that = this
    wx.getLocation({
      type: 'wgs84',
      altitude: true,
      isHighAccuracy: true,
      highAccuracyExpireTime: 2000,
      success: function(res) {
        wx.hideLoading()
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
        console.log('定位成功：',res)
        if (res.statusCode == 200 && res.data.status == 0) {
          // 从返回值中提取需要的业务地理信息数据 国家、省、市、县区、街道
          // that.setData({ 'address.province': res.data.resul
          let cityStr = res.data.result.address_component.city
          // that.setData({ 'photoService.posLng': res.data.result.location.lat });
          // that.setData({ 'photoService.posLat': res.data.result.location.lng });
          that.setData({
            CurrentLocation: cityStr
          })
        }
      }
    })
  },


  getCurrentPositioning0() {
    // 微信小程序自带获取当前的地理位置
    wx.getLocation({
      type: 'wgs84'
    }).then((res) => {
      // 腾讯api周边
      qqmapsdk.search({
        keyword: '学校',
        success: (res) => {
          // console.log('腾讯api周边', res.data[0]);
          const CurrentLocation = res.data[0].ad_info
          console.log('CurrentLocation', CurrentLocation);
          this.setData({
            CurrentLocation: CurrentLocation
          })
        },
      })
    }).catch((error) => {
      // console.log('授权失败', error);
      if (error.errMsg == 'getLocation:fail auth deny') {
        this.setData({
          "CurrentLocation.city": '点击选择当前城市'
        })
      }
    })
  },
  // 显示输入框
  showInput() {
    this.setData({
      inputShowed: true
    })
  },
  // 清除输入框
  clearInput() {
    console.log('111');
    this.setData({
      inputVal: "",
      inputShowed: false,
      searchResult: []
    })
    wx.hideKeyboard() //强行隐藏键盘
  },
  // 输入框 输入触发事件
  inputTyping(e) {
    // console.log('输入法输入触发的事件', e);
    this.setData({
      inputVal: e.detail.value
    }, () => {
      // 调用搜索城市
      this.searchCity()
    })
  },
  // 搜索城市
  searchCity() {
    let result = []
    // 双遍历
    this.data.cityJson.forEach((item1, index1) => {
      item1.data.forEach((item2, index2) => {
        if (item2.keyword.indexOf(this.data.inputVal.toLocaleUpperCase()) !== -1) {
          result.push(item2.cityName)
        }
      })
    })
    this.setData({
      searchResult: result,
    })
  },
  // 搜索出来的关键词的点击事件
  selectCity(e) {
    let cityName = e.currentTarget.dataset.name;
    console.log('搜索出来的关键词的点击事件', cityName);
    this.setData({
      'CurrentLocation': cityName
    })
    app.globalData.currCity=cityName
    //返回上一页
    let lastPage = '/pages/home/index'
    console.log('返回上一页', this.data.currService)
    if(this.data.currService==1){
      // lastPage = '/pages/assist-info/publish'
      wx.navigateBack()
    }else{
      if(this.data.currService==3){
        lastPage = '/pages/assistant/assistant-list'
      }else if(this.data.currService==4){
        wx.navigateBack()
      }
      wx.switchTab({
        url: lastPage,
      })
    }
  },
  selectCity2(e) {
    let { cityName } = e.currentTarget.dataset.name;
    console.log('点击列表中的城市事件', cityName);
    this.setData({
      'CurrentLocation': cityName
    })
    app.globalData.currCity=cityName
    //返回上一页
    let lastPage = '/pages/assist-info/service-list'
    if(this.data.currService==1){
      lastPage = '/pages/assist-info/publish'
    }
    wx.navigateTo({
      url: lastPage,
    })
  },

  confirmSelect(){
    app.globalData.currCity=this.data.CurrentLocation
    //返回上一页
    let lastPage = '/pages/home/index'
    console.log('返回上一页', this.data.currService)
    if(this.data.currService==1){
      // lastPage = '/pages/assist-info/publish'
      wx.navigateBack()
    }else{
      if(this.data.currService==3){
        lastPage = '/pages/assistant/assistant-list'
      }else if(this.data.currService==4){
        wx.navigateBack()
      }
      wx.switchTab({
        url: lastPage,
      })
    }
  },
 
  /**
   * 生命周期函数--监听页面加载
   */
  onLoad(options) {
    let st = options.servicetype
    this.setData({currService: st})
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
    // // 实例化API核心类  (腾讯位置服务的)配置
    qqmapsdk = new QQMapWX({
      key: 'NXDBZ-PWQRD-3RK4F-HHB34-C2DPH-KBFBM'
    });
    // 用户获取当前地理位置信息
    this.getCurrentPositioning()
  },
 
  /**
   * 生命周期函数--监听页面隐藏
   */
  onHide() {
 
  },
 
  /**
   * 生命周期函数--监听页面卸载
   */
  onUnload() {
 
  },
 
})