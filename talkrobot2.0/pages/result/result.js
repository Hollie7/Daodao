// pages/result/result.js
const app = getApp()

Page({
  data: {
    backImg:'',
    characterImg:'',
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
  },

  ToWordCloud:function(){
    wx.navigateTo({
      url: '/pages/wordcloud/wordcloud'
    })
  },

  ToRadar:function(){
    wx.navigateTo({
      url: '/pages/radar/radar'
    })
  },

  ToScore:function(){
    wx.navigateTo({
      url: '/pages/score/score'
    })
  },

  ToRemarkWjx:function(){
    wx.navigateToMiniProgram({
      appId: 'wxd947200f82267e58',
      path: 'pages/wjxqList/wjxqList?activityId=117795391',
      extraData: {
        foo: 'bar'
      },
      envVersion: 'release',
      success(res) {
        // 打开成功
      }
    })
  },
})