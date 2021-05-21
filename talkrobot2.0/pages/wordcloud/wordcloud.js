const app = getApp()
import uCharts from '../uchart/u-charts.js';
var _self;
var canvasRadar=null;
var canvasWord=null;

Page({
  data: {
    cWidth:'',
    cHeight:'',
    pixelRatio:1,
  },

  onLoad: function () {
    _self=this;
    this.cWidth = wx.getSystemInfoSync().windowWidth;
    this.cHeight = 750 / 750 * wx.getSystemInfoSync().windowWidth;
    this.getServerData();
  },
  
 
  getServerData:function(){
    let Word={series:[]};
    Word.series = app.globalData.backEndData.wordCloud;
    _self.showWord("canvasWord",Word);
  },


  showWord(canvasId,chartData){
    canvasWord=new uCharts({
      $this:_self,
      canvasId: canvasId,
      type: 'word',
      background:'#FFFFFF',
      pixelRatio:_self.pixelRatio,
      series: chartData.series,
      width: this.cWidth,
      height: this.cHeight/1.5,
      extra: {
        word: {
          type: 'normal'
        }
      }
    });
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
  ToScore:function(){
    wx.redirectTo({
      url: '/pages/score/score'
  })
  }
})