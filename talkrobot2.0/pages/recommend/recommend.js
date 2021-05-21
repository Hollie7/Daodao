// pages/recommend/recommend.js
const app = getApp()
Page({

  data: {
    backImg:'',
    characterImg:''
  },

  onLoad: function (options) {
    if(app.globalData.gender == "男"){
      this.setData({
        characterImg:"../../icon/boy.png"
      })
    }else if(app.globalData.gender == "女"){
      this.setData({
        characterImg:"../../icon/girl.png"
      })
    }else if(app.globalData.gender == "保密"){
      this.setData({
        characterImg:"../../icon/gendersecret.png"
      })
    }

    if(app.globalData.scene == "白天室外"){
      this.setData({
        backImg:"../../icon/dayout.jpg"
      })
    }else if(app.globalData.scene == "黑天室内"){
      this.setData({
        backImg:"../../icon/nightin.jpg"
      })
    }

    if(app.globalData.voice_text == ''){
      this.setData({
        btn_judge_done:true
      })
    } else {
      this.setData({
        btn_judge_done:false
      })
    }
  },

  Towjx1:function(){
    wx.navigateToMiniProgram({
      appId: 'wxd947200f82267e58',
      path: 'pages/wjxqList/wjxqList?activityId=110120943',
      extraData: {
        foo: 'bar'
      },
      envVersion: 'release',
      success(res) {
        // 打开成功
      }
    })
  },

  Towjx2:function(){
    wx.navigateToMiniProgram({
      appId: 'wxd947200f82267e58',
      path: 'pages/wjxqList/wjxqList?activityId=110121914',
      extraData: {
        foo: 'bar'
      },
      envVersion: 'release',
      success(res) {
        // 打开成功
      }
    })
  },

  Towjx3:function(){
    wx.navigateToMiniProgram({
      appId: 'wxd947200f82267e58',
      path: 'pages/wjxqList/wjxqList?activityId=110120126',
      extraData: {
        foo: 'bar'
      },
      envVersion: 'release',
      success(res) {
        // 打开成功
      }
    })
  },

  Towjx4:function(){
    wx.navigateToMiniProgram({
      appId: 'wxd947200f82267e58',
      path: 'pages/wjxqList/wjxqList?activityId=110120360',
      extraData: {
        foo: 'bar'
      },
      envVersion: 'release',
      success(res) {
        // 打开成功
      }
    })
  }
})