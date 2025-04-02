// app.js
App({
  onLaunch() {
    let apt = this
    //全局定时器，用于轮询新消息
    // let timer = setInterval(() => {
    //   let timestamp = Date.parse(new Date());
    //   // wx.setTabBarBadge({index: 1, text: '3'});
    //   console.log('当前用户：', apt.globalData.userInfo)
    // }, 3000)  
  },
  
  globalData: {
    apiBaseUrl: 'http://localhost:2510/assist-server/api',
    // apiBaseUrl: 'http://192.168.1.11:8028/assist-server/api',
    userInfo: null,
    imgSrc: '',
    timerLock:false,//是否关闭合成定时器,
    uuid: 'f15bab49-cb22-6491-f18c-60dae76eb775'
  },
})
