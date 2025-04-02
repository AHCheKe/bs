function checkLogin(){
  let isLogin = true
  if(wx.getStorageSync('TOKEN')==''){
    isLogin = false
  }else{
    let t0 = parseFloat(wx.getStorageSync('LAST_LOGIN_TIME'))
    let t1 = (new Date()).getTime()
    if(t1-t0>604800000){
      //登录超过7天，重新登录
      isLogin = false
    }
  }
  return isLogin
}

/**
 * 封装微信的的request
 */
function request(
    url, 
    data = {}, 
    method = "POST", 
    tip = "加载中...", 
    header = "application/x-www-form-urlencoded", 
    showLoading = true
  ) {
  if (showLoading) {
    wx.showLoading({
      title: tip,
    });
  }
  console.log(url)
  //超时判断
  if (wx.getStorageSync('token') != '') {
    let now = parseFloat((new Date()).getTime())
    let tokenTime = parseFloat(wx.getStorageSync('tokenTime'))
    if (now - tokenTime > 259200000) {
      //登录超时，三天
      console.log('登录超时，三天！')
      wx.removeStorageSync('loginUser')
      wx.removeStorageSync('token')
      wx.redirectTo({
        url: '/pages/profile/login/mobile-auth',
      })
      return
    }
  }
  let token = wx.getStorageSync('TOKEN')
  return new Promise(function (resolve, reject) {
    wx.request({
      url: url,
      data: data,
      method: method,
      header: {
        //cookie: wx.getStorageSync('JSESSIONID'),
        'Authorization': token,
        'content-type': 'application/json;charset=utf-8' //'application/x-www-form-urlencoded'
      },
      success: function (res) {
        wx.hideLoading();
        console.log('封装的request, res:', res)
        if (res.statusCode == 200) {
          if (res.data.code == 403||res.data.msg.indexOf('Authorization失效')>-1) {
            //未登录拦截
            console.log('token失效了，需要重新登录')
            wx.navigateTo({
              url: '/pages/user/login/login',
            })
          } else {
            //正常情况，以promise返回给调用的地方处理
            resolve(res.data);
          }
        } else {
          console.log('进入封装的request的错误分支', res)
          if(res.data.indexOf('Authorization失效')>-1){
            wx.redirectTo({
              url: '/pages/user/login/login',
            })
          }else{
            reject(res.errMsg);
          }
        }
      },
      fail: function (err) {
        reject(err)
      },
      timeout: 60000
    })
  });
}

function getCurrDate() {
  var today = new Date();  
  var year = today.getFullYear();
  var month = today.getMonth() + 1;
  if(month < 10){
    month = "0"+month;
  }
  var date = today.getDate();
  if(date<10){
    date = "0"+date
  }
  var curDate = year+"-"+month+"-"+date;
  return curDate;
}

function getCurrTimme() {
  var myDate = new Date();  
  var H = myDate.getHours();  //获取当前小时
  var i = myDate.getMinutes();    //获取当前分钟
  // 小时不足10补0
  if(H < 10){
    H = '0' + H;
  }
  // 分钟不足10补0
  if(i < 10){
    i = '0' + i;
  }
  var currTime = H+":"+i
  return currTime;
}

function printQRCode(data, color) {
  if(color==null||color==undefined||color==''){
    color="black"
  }
  //传入的data是一个二维数组，每一项是一个数组，代表一行，每一项是0或1，代表黑白，0是白色div，1是黑色div，输出一段HTML字符串
  let width = '5px' // 每个div的宽度
  let height = '5px' // 每个div的高度
  let html = '<div style="text-align:center;width:130px;margin:10px auto;">'
  for (let i = 0; i < data.length; i++) {
      let row = data[i]
      for (let j = 0; j < row.length; j++) {
          let currColor = row[j] === 1 ? color : 'white'
          html += `<div style="width:${width};height:${height};background-color:${currColor};float:left;"></div>`
      }
      html += '<div style="clear:both;"></div>'
  }
  html+='</div>'
  return html
}

export default {
  request,
  checkLogin,  
  getCurrDate,
  getCurrTimme,
  printQRCode
}