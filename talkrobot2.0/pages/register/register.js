// pages/login/login.js
const app = getApp()
Page({
  data: {
    name:null,
    code:null,
    codeConfirm:null
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

  // 获取输入密码
  codeConfirmInput: function (e) {
    this.setData({
      codeConfirm: e.detail.value
    })
  },

  // 登录
  register: function () {
    var that = this;
    if (this.data.name.length == 0 || this.data.code.length == 0 || this.data.codeConfirm.length == 0) {
      wx.showToast({
        title: '请输入信息',
        icon: 'loading',
        duration: 2000
      })
    } else if(this.data.code == this.data.codeConfirm){
      wx.request({
        url: 'https://www.talkrobot.top/Register',
        /*url: 'http://localhost:9000/Register',*/
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
          if(res.data == "注册成功"){
            wx.showToast({
              title: '注册成功！',
              icon: 'success',
              duration: 2000
            })
            app.globalData.name = that.data.name
            wx.navigateTo({
              url: '/pages/set/set'
            })
          }
          else if(res.data == "该昵称已被注册，请更换后重试"){
            wx.showToast({
              title: '该昵称已被注册，请更换后重试！',
              icon: 'loading',
              duration: 2000
            })
          }
        },
        fail:function(err){
          console.log("error:"+err.data);
        }
      })
    } else if(this.data.code != this.data.codeConfirm){
      wx.showToast({
        title: '请确认密码输入是否正确',
        icon: 'loading',
        duration: 2000
      })
    }
  }

})