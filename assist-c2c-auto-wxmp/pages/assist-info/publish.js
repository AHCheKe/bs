// pages/publish/publish.js
var app = getApp()
import util from '../../utils/util.js'
Page({

  /**
   * 页面的初始数据
   */
  data: {
		serviceTypeList: [],
		hospitalList: [],
		sindex: 0,
		hindex: 0,
    uploadImgs: [],
    service: {
      serviceType: '',
      title: '',
      needPickup: 0,
      city: '',
      serviceDate: '',
      hospital: '',
      dept: '',
      price: '',
      mobile: '',
      status: 0,
      content: '',
      location: '请点击按钮获取定位',
      lat: '',
      lng: '',
      serviceTags: '无特殊要求',
      ageRange: '0',
      genderNeed: '0',
      selectModel: '0',
      appointAssist: ''
    },
    showPopup: false,
    userInfo: {},
    serviceTagList: [],
    pickupItems: [
      {value: '0', name: '不需要', checked: 'true'},
      {value: '1', name: '需要'},
    ],
    pindex: 0,
    allocationItems: [
      {value: '0', name: '系统分配', checked: true},
      {value: '1', name: '手动选择', checked: false},
    ],
    aindex: 0,
    ageRangeItems: [
      {value: '0', name: '无要求', checked: 'true'},
      {value: '1', name: '25岁以下'},
      {value: '2', name: '25~30岁'},
      {value: '3', name: '30~40岁'},
    ],
    aindex: 0,
    genderRangeItems: [
      {value: '0', name: '无要求', checked: 'true'},
      {value: '2', name: '女'},
      {value: '1', name: '男'},
    ],
    gindex: 0,
    defaultDate: '',
    defaultType: '',
    allocateName: ''
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    let isLogin = util.checkLogin()
    if(!isLogin){
      //进入授权登录页面
      wx.redirectTo({
        url: '/pages/user/login/login',
      })
    }else{
      this.initUserInfo()
      this.initDate()
      if(options.tp){
        let typeId = options.tp
        this.setData({defaultType: typeId})
      }
    }
    //如果是从陪诊师详情页面点击预约按钮进来的
    if(options.aid){
      let aitems = [
        {value: '0', name: '系统分配', checked: false},
        {value: '1', name: '手动选择', checked: true},
      ]
      this.setData({
        'service.selectModel': 1,
        'service.appointAssist': options.aid,
        pindex: 1,
        allocationItems: aitems,
        allocateName: options.aname
      })
    }
  },

  initDate:function(){
    // let cdate = new Date()
    let ddate = new Date()
    ddate.setDate(ddate.getDate()+1)
    ddate.setHours(9)
    ddate.setMinutes(0)
    this.setData({defaultDate: ddate.getTime()})
    //加载服务类别
    let that = this
    util.request(
      app.globalData.apiBaseUrl+'/service/type/list',
      {}
    ).then(res=>{
      that.setData({
				serviceTypeList: res.data.serviceList,
				hospitalList: res.data.hospitalList,
				'service.serviceType': res.data.serviceList[0].serviceName,
				'service.hospital': res.data.hospitalList[0].hospitalName,
        serviceTagList: res.data.tagList,
      }, ()=>{
        that.initTypeSel()
      })
    })
  },

  //加载当前登录用户的信息
  initUserInfo: function(){
    let that = this
    //到服务端实时load一下用户信息
    util.request(
      app.globalData.apiBaseUrl+'/order/load/user',
      {},
      'POST'
    ).then(res=>{
      wx.hideLoading()
      console.log(res)
      that.setData({userInfo: res.data, notLogin: false})
      wx.setStorageSync("LOGIN_USER", res.data)
      app.globalData.userInfo = res.data
      if(res.data.role!=1){
        wx.showModal({
          title: '提示',
          content: '当前登录用户角色为陪诊师，不可提交订单！',
          showCancel: false,
          complete: (res) => {
            wx.navigateBack()
          }
        })
      }
    })
  },

  /**
   * 生命周期函数--监听页面初次渲染完成
   */
  onReady: function () {
    
  },

  /**
   * 生命周期函数--监听页面显示
   */
  onShow: function () {
    this.setData({'service.city': app.globalData.currCity})
    // console.log(app.globalData.currCity, this.data.service)
  },

  //选择图片
  chooseImg: function(){
    let _this = this
    wx.chooseMedia({
      count: 9,
      sizeType: ['compressed'],
      sourceType: ['album', 'camera'],
      mediaType: ['image'],
      success (res) {
        console.log(res)
        wx.showLoading({
          title: '上传图片中...',
        })
        // tempFilePath可以作为img标签的src属性显示图片
        const tempFilePaths = res.tempFiles
        for(var idx=0; idx<tempFilePaths.length; idx++){
          var tmpFileName = tempFilePaths[0].tempFilePath.substring(tempFilePaths[idx].tempFilePath.lastIndexOf('/')+1)
          wx.uploadFile({
            url: 'http://129.211.222.131:16602/upload-server/upload', //图片服务器
            filePath: tempFilePaths[idx].tempFilePath,
            name: 'myFile',
            formData: {
              'user': 'test',
              'fileName': tmpFileName,
              'u': app.globalData.uuid
            },
            success (res){
              //上传成功回调
              wx.hideLoading()
              let resjson = JSON.parse(res.data)
              console.log(resjson.url)
              let picArr = _this.data.uploadImgs
              picArr.push(resjson.url)
              _this.setData({
                uploadImgs: picArr
              })
            }
          })
        }
      }
    })
  },

  initTypeSel: function(){
    for(let idx=0; idx<this.data.serviceTypeList.length; idx++){
      if(this.data.serviceTypeList[idx].serviceId==this.data.defaultType){
        this.setData({sindex: idx})
        break
      }
    }
  },

  bindFieldRemarkInput: function(e){
    this.setData({ 'service.content': e.detail.value })
  },
  bindMobileInput: function(e){
    this.setData({ 'service.mobile': e.detail.value })
  },
  bindPriceInput: function(e){
    this.setData({ 'service.price': e.detail.value })
  },
  bindDeptInput: function(e){
    this.setData({ 'service.dept': e.detail.value })
  },
  bindHospitalInput: function(e){
    this.setData({ 'service.hospital': e.detail.value })
  },
  bindServiceTypeChange: function(e) {
    let p = parseInt(e.detail.value)
    console.log('切换：', p)
    this.setData({ sindex: p, 'service.serviceType': this.data.serviceTypeList[p].serviceName})
  },
  bindTagChange: function(e) {
    let p = e.detail.value
    this.setData({ pindex: p, 'service.serviceTags': p})
  },

  /**
   * 提交表单
   */
  publishForm: function(){
    if(this.data.service.city==''||this.data.service.location==''||this.data.service.mobile==''||this.data.service.hospital==''){
      wx.showToast({title:'请输入必填项', icon: 'error'})
      return
    }
    wx.showLoading({
      title: '加载中',
    })
    let req = this.data.service
    let picStr = this.data.uploadImgs.join(",")
    req.pics=picStr
    util.request(
      app.globalData.apiBaseUrl+'/service/save',
      req,
      'POST'
    ).then(res=>{
      console.log(res)
      if(res.success){
        wx.showModal({
          title: '提示',
          content: res.msg,
          showCancel: false,
          complete: (res) => {
            wx.switchTab({
              url: '/pages/home/index',
            })
          }
        })
      }else{
        wx.showModal({
          title: '提示',
          content: '无合适人选，请重新选择',
          showCancel: false,
          complete: (res) => {
            if (res.confirm) {
              
            }
          }
        })
      }
    })
  },

  deletePic: function(e){
    let idx = e.currentTarget.dataset.idx
    let pics = this.data.uploadImgs
    pics.splice(idx, 1)
    this.setData({uploadImgs: pics})
  },

  bindPickupChange: function(e) {
    let p = parseInt(e.detail.value)
    this.setData({ pindex: p, 'service.needPickup': p})
  },

  bindAllocationChange: function(e) {
    let p = parseInt(e.detail.value)
    this.setData({ pindex: p, 'service.selectModel': p})
  },

  bindAgeRangeChange: function(e) {
    let p = parseInt(e.detail.value)
    console.log('ageRange:', p)
    this.setData({ pindex: p, 'service.ageRange': p})
  },

  bindGenderRangeChange: function(e) {
    let p = parseInt(e.detail.value)
    console.log('genderNeed:', p)
    this.setData({ pindex: p, 'service.genderNeed': p})
  },

  onDateSelect(event) {
    this.setData({
      'service.serviceDate': event.detail,
    });
  },
  
  onShowPopup: function(){
    this.setData({ showPopup: true });
  },

  onClose: function(){
    this.setData({ showPopup: false });
  },

  onChangeCity: function(){
    wx.navigateTo({
      url: '/pages/assist-info/city?servicetype=1',
    })
	},
	
	bindHospitalChange: function(e) {
    let p = parseInt(e.detail.value)
    this.setData({ hindex: p, 'service.hospital': this.data.hospitalList[p].hospitalName})
  },

  // 获取当前定位
  getCurrentPositioning: function() {
    const that = this
    wx.showLoading({
      title: '正在定位',
		})
		
		wx.chooseLocation({
			success: function(res) {
				wx.hideLoading()
				// 成功回调
				console.log('回调数据:', res);
				console.log('详细地址:', res.address);
				console.log('纬度:', res.latitude);
				console.log('经度:', res.longitude);
				
				// 这里可以将数据保存或发送到服务器
				that.setData({ 
					'service.lat': res.latitude,
					'service.lng': res.longitude,
					'service.location': res.name
				});
			},
			fail: function(err) {
				// 失败回调
				console.error('选择位置失败:', err);
				wx.hideLoading()
			}
		});


    // wx.getLocation({
    //   type: 'wgs84',
    //   altitude: true,
    //   isHighAccuracy: true,
    //   highAccuracyExpireTime: 2000,
    //   success: function(res) {
    //     console.log(res)
    //     that.setData({
    //       latitude: res.latitude,
    //       longitude: res.longitude,
    //       'photoService.posLat': res.latitude,
    //       'photoService.posLng': res.longitude
    //     })
    //     // 构建请求地址
    //     // 逆解析接口 /ws/geocoder/v1
    //     var qqMapApi = 'http://apis.map.qq.com/ws/geocoder/v1/' + "?location=" + that.data.latitude + ',' +
    //       that.data.longitude + "&key=" + 'NXDBZ-PWQRD-3RK4F-HHB34-C2DPH-KBFBM' + "&get_poi=1";
    //     that.sendRequest(qqMapApi);
    //   },
    //   fail: function(res) {
    //     console.log(res)
    //     wx.showToast({
    //       title: '获取位置信息失败',
    //       icon: 'none'
    //     })
    //   }
		// })
		

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
          let cityStr = res.data.result.address
          that.setData({ 
            'service.lat': res.data.result.location.lat,
            'service.lng': res.data.result.location.lng,
            'service.location': cityStr
          });
        }
      }
    })
  },

  showAssistants: function(){
    wx.switchTab({
      url: '/pages/assistant/assistant-list',
    })
  }
})