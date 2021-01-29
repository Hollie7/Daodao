// pages/mine/mine.js
const app = getApp()
import uCharts from '../uchart/u-charts.js';
var _self;
var canvasRadar=null;

Page({
  data: {
    cWidth:'',
    cHeight:'',
    pixelRatio:1,
    score1:0,
    describe1:''
  },

  onLoad: function () {
    _self=this;
    this.cWidth = wx.getSystemInfoSync().windowWidth;
    this.cHeight = 750 / 750 * wx.getSystemInfoSync().windowWidth;
    this.getServerData();
  },
  
  getServerData:function(){
    let Radar={categories:[],series:[]};       
    Radar.categories = app.globalData.backEndData.RadarPlot.categories;
    Radar.series = [app.globalData.backEndData.RadarPlot.series];
    this.setData({
      describe1:app.globalData.backEndData.EmotionDescribe
    })

    _self.showRadar("canvasRadar",Radar);
  },

  showRadar(canvasId,chartData){
    canvasRadar=new uCharts({
      $this:_self,
      canvasId: canvasId,
      type: 'radar',
      fontSize:15,
      legend:{show:true},
      background:'#FFFFFF',
      pixelRatio:_self.pixelRatio,
      animation: true,
      dataLabel: true,
      categories: chartData.categories,
      series: chartData.series,
      width: this.cWidth,
      height: this.cHeight,
      extra: {
        radar: {
          max: 10//雷达数值的最大值
        }
      }
    });
  },
  ToWordCloud:function(){
    wx.redirectTo({
      url: '/pages/wordcloud/wordcloud'
  })
  },
  ToIndex:function(){
    wx.navigateBack({
      dalta: 2     // 默认值是 1
  })
  },
  ToScore:function(){
    wx.redirectTo({
      url: '/pages/score/score'
  })
  }
})