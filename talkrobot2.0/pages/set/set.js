// pages/set/set.js
const app = getApp()
Page({

  data: {
    gendersRadio: [
      {value: '男'},{value: '女'}
    ],
    scenesRadio: [
      {value: '场景1'},{value: '场景2'},{value: '场景3'},{value: '场景4'}
    ],
    partGender:null,
    partScene:null
  },

  onLoad: function (options) {
  },

  sceneChange: function(e) {
    this.setData({
      partScene:e.detail.value
    })
    console.log(this.data.partScene)
  },

  genderChange: function(e) {
    this.setData({
      partGender:e.detail.value
    })
    console.log(this.data.partGender)
  },

  btnSure: function () {
    if (this.data.partGender == null || this.data.partScene == null){
      wx.showToast({
        title: '请设置信息',
        icon: 'loading',
        duration: 2000
      })
    } else {
      app.globalData.gender = this.data.partGender
      app.globalData.scene = this.data.partScene
      if (app.globalData.gender == null || app.globalData.scene == null){
        wx.showToast({
          title: '请重新点击确认',
          icon: 'loading',
          duration: 2000
        })
      } else {
        console.log(app.globalData.gender)
        console.log(app.globalData.scene)
        wx.request({
          url: 'https://www.talkrobot.top/SetScene',
          /*url: 'http://localhost:9000/SetScene',*/
          method:'POST',
          header: {
            'content-type':'application/x-www-form-urlencoded',
            'Accept': 'application/json'
          },
          data:{
            name:app.globalData.name,
            gender:app.globalData.gender,
            scene:app.globalData.scene
          },
      
          success: function (res) {
            if(res.data == "设置成功"){
              wx.showToast({
                title: '设置成功！',
                icon: 'success',
                duration: 2000
              })
              wx.navigateTo({
                url: '/pages/index/index'
              })
            }
            else if(res.data == "设置失败，请重试"){
              wx.showToast({
                title: '设置失败，请重试！',
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
  }

})