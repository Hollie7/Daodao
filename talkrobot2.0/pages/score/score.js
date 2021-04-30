const app = getApp()
import uCharts from '../uchart/u-charts.js';
var _self;

Page({
  data: {
    cWidth:'',
    cHeight:'',
    pixelRatio:1,
    score1:0,
    describe1:'',
    backImg:null,
    characterImg:null
  },

  onLoad: function () {
    _self=this;
    this.cWidth = wx.getSystemInfoSync().windowWidth;
    this.cHeight = 750 / 750 * wx.getSystemInfoSync().windowWidth;
    this.getServerData();
    this.judgeDescribe();

    if(app.globalData.gender == "男"){
      this.setData({
        characterImg:"../../icon/boy.png"
      })
    }else{
      this.setData({
        characterImg:"../../icon/girl.png"
      })
    }

    if(app.globalData.scene == "场景1"){
      this.setData({
        backImg:"../../icon/scene1.png"
      })
    }else if(app.globalData.scene == "场景2"){
      this.setData({
        backImg:"../../icon/scene2.png"
      })
    }else if(app.globalData.scene == "场景3"){
      this.setData({
        backImg:"../../icon/scene3.png"
      })
    }else if(app.globalData.scene == "场景4"){
      this.setData({
        backImg:"../../icon/scene4.png"
      })
    }
  },
  
 
  getServerData:function(){
    this.setData({
      score1:(app.globalData.backEndData.EmotionScore/app.globalData.backEndData.SentenceLength*100).toFixed(2),
      //describe1:app.globalData.backEndData.EmotionDescribe
    })
  },

  judgeDescribe:function(){
    if(this.data.score1>=0){
      this.setData({
        describe1:"您的情绪相对稳定且趋于乐观正面，请继续保持良好的情绪与心态"
      })
    }
    else if(this.data.score1>=-1){
      this.setData({
        describe1:"您的情绪相对稳定但是较为消极，请及时调整情绪与心情"
      })
    }
    else if(this.data.score1>=-2){
      this.setData({
        describe1:"您的情绪比较消极且可能存在恶化的情况，请及时调整情绪与心情，必要时请及时进行心理咨询与疏导"
      })
    }
    else{
      this.setData({
        describe1:"您的情绪很消极，请及时进行心理咨询与疏导"
      })
    }
  },

  ToRadar:function(){
    wx.redirectTo({
      url: '/pages/radar/radar'
  })
  },
  ToIndex:function(){
    wx.navigateBack({
      dalta: 2     // 默认值是 1
  })
  },
  ToWordCloud:function(){
    wx.redirectTo({
      url: '/pages/wordcloud/wordcloud'
  })
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