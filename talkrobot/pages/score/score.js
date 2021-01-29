const app = getApp()
import uCharts from '../uchart/u-charts.js';
var _self;

Page({
  data: {
    cWidth:'',
    cHeight:'',
    pixelRatio:1,
    score1:0,
  },

  onLoad: function () {
    _self=this;
    this.cWidth = wx.getSystemInfoSync().windowWidth;
    this.cHeight = 750 / 750 * wx.getSystemInfoSync().windowWidth;
    this.getServerData();
  },
  
 
  getServerData:function(){
    this.setData({
      score1:app.globalData.backEndData.EmotionScore
    })
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
  }
})