// pages/login/login.js
const app = getApp()
Page({
  data: {
    name:null,
    code:null
  },
  onLoad: function (options) {

  },
  // 获取输入账号
  nameInput: function (e) {
    this.setData({
      name: e.detail.value
    })
  },

  // 获取输入密码
  codeInput: function (e) {
    this.setData({
      code: e.detail.value
    })
  },
  anonymity: function (){
    app.globalData.name = "000"
    wx.navigateTo({
      url: '/pages/set/set'
    })
  },

  // 登录
  login: function () {
    var that = this;
    if (this.data.name.length == 0 || this.data.code.length == 0) {
      wx.showToast({
        title: '请输入信息',
        icon: 'loading',
        duration: 2000
      })
    } else {
      wx.request({
        url: 'https://www.talkrobot.top/Login',
        /*url: 'http://localhost:9000/Login',*/
        method:'POST',
        header: {
          'content-type':'application/x-www-form-urlencoded',
          'Accept': 'application/json'
        },
        data:{
          name:this.data.name,
          code:this.data.code
        },
    
        success: function (res) {
          if(res.data == "登录成功"){
            wx.showToast({
              title: '登录成功！',
              icon: 'success',
              duration: 2000
            })
            app.globalData.name = that.data.name
            console.log(app.globalData.name)
            wx.request({
              url: 'https://www.talkrobot.top/GetScene',
              /*url: 'http://localhost:9000/GetScene',*/
              method:'POST',
              header: {
                'content-type':'application/x-www-form-urlencoded',
                'Accept': 'application/json'
              },
              data:{
                name:that.data.name
              },
          
              success: function (re) {
                app.globalData.gender = re.data.gender
                app.globalData.scene = re.data.scene
                if(app.globalData.gender == null || app.globalData.scene == null){
                  wx.navigateTo({
                    url: '/pages/set/set'
                  })
                } else{
                  wx.navigateTo({
                    url: '/pages/index/index'
                  })
                }

              },
              fail:function(er){
                console.log("error:"+er.data);
              }
            })
          }
          else if(res.data == "登录失败"){
            wx.showToast({
              title: '登录失败！',
              icon: 'loading',
              duration: 2000
            })
          }
          else if(res.data == "该用户不存在"){
            wx.showToast({
              title: '该用户不存在！',
              icon: 'loading',
              duration: 2000
            })
          }
        },
        fail:function(err){
          console.log("error:"+err.data);
        }
      })
    }
  }

})