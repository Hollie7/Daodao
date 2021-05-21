// index.js
// 获取应用实例
const app = getApp()
var plugin = requirePlugin('WechatSI')
let manager = plugin.getRecordRecognitionManager()

manager.onStart = function(res) {
  console.log("成功开始录音识别", res)
}

manager.onRecognize = res=>{
  let text = res.result 
  //app.globalData.voice_text = res.result  
  console.log(text)
}
manager.onStop = res=>{
  let text = res.result
  app.globalData.voice_text += res.result  
  console.log(app.globalData.voice_text)
  if(text==''){
    console.log('用户没有说话')
    wx.showToast({
      icon:'none',
      title: '未识别',
    })
  }
  else{
    console.log(text)
  }
}
manager.onError = function (res) {
  wx.showToast({
    icon:'none',
    title: '报错了',
  })
}

Page({
  data: {
    motto: 'Hello World',
    userInfo: {},
    hasUserInfo: false,
    canIUse: wx.canIUse('button.open-type.getUserInfo'),
    text_now:"",
    tempData:null,
    btn_voice_judge:app.globalData.index_btn_voice_judge,
    backImg:'',
    characterImg:'',
    disableStart:false,
    disablePause:true,
    disableStop:true,
    SusOrCon:"../../icon/start.png",
    SusOrConArray:["../../icon/start.png","../../icon/pause.png"],
    SusOrConIndex:1,
  },
  bindViewTap: function() {
    wx.navigateTo({
      url: '../logs/logs'
    })
  },
  onLoad: function () {
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
    
    if (app.globalData.userInfo) {
      this.setData({
        userInfo: app.globalData.userInfo,
        hasUserInfo: true
      })
    } else if (this.data.canIUse){
      // 由于 getUserInfo 是网络请求，可能会在 Page.onLoad 之后才返回
      // 所以此处加入 callback 以防止这种情况
      app.userInfoReadyCallback = res => {
        this.setData({
          userInfo: res.userInfo,
          hasUserInfo: true
        })
      }
    } else {
      // 在没有 open-type=getUserInfo 版本的兼容处理
      wx.getUserInfo({
        success: res => {
          app.globalData.userInfo = res.userInfo
          this.setData({
            userInfo: res.userInfo,
            hasUserInfo: true
          })
        }
      })
    }
  },
  getUserInfo: function(e) {
    console.log(e)
    app.globalData.userInfo = e.detail.userInfo
    this.setData({
      userInfo: e.detail.userInfo,
      hasUserInfo: true
    })
  },
  startFunc:function(e){
    this.setData({
      disableStart:true,
      disablePause:false,
      disableStop:false
    }),
    // 控制
    manager.start({
      lang: 'zh_CN',
      duration:60000
    })
    //console.log("stop",disableStop)
  },
  pauseFunc:function(e){
    this.setData({
      SusOrConIndex:(this.data.SusOrConIndex+1)%2,
      SusOrCon:this.data.SusOrConArray[this.data.SusOrConIndex],
      disableStop:false
    })
    console.log("stop")
    console.log("SusOrConIndex",this.data.SusOrConIndex)
    // 控制
    if(this.data.SusOrConIndex == 0){
      // 继续
      manager.start({
        lang: 'zh_CN',
        duration:60000
      })
    }
    else{  
      // 暂停
      app.globalData.index_btn_voice_judge = false
      this.setData({
        btn_voice_judge:app.globalData.index_btn_voice_judge
      })
      manager.stop() 
      app.globalData.voice_text 
    }
  },
  stopFunc:function(e){
    this.setData({
      SusOrConIndex:0,
      SusOrCon:"../../icon/start.png",
    }),
    // 控制
    app.globalData.index_btn_voice_judge = false
    this.setData({
      btn_voice_judge:app.globalData.index_btn_voice_judge
    })
    manager.stop() 
    if(app.globalData.voice_text == ''){
      wx.showToast({
        title: '请再次点击！',
        icon: 'loading',
        duration: 2000
      })
    }
    else {
      wx.navigateTo({
        url: '/pages/voiceText/voiceText'
      })
    }
    
  },  
  

  ToWordcloud:function(){
    wx.navigateTo({
      url: '/pages/wordcloud/wordcloud'
  })
  },

  ToRemark:function(){
    wx.navigateTo({
      url: '/pages/remark/remark'
  })
  },

  ToVoice:function(){
    wx.navigateTo({
      url: '/pages/voiceText/voiceText'
  })
  },

  Reset:function(){
    app.globalData.voice_text = ""
    app.globalData.index_btn3_judge = true
    app.globalData.judge = false
    app.globalData.backEndData = {"wordCloud":[{"name":"初始","textSize":30},{"name":"初始","textSize":25},{"name":"初始","textSize":20},{"name":"初始","textSize":20},{"name":"初始","textSize":20},{"name":"初始","textSize":15}],"RadarPlot":{"categories":["初始(8)","初始(8)","初始(5)","初始(5)","初始(3)","初始(3)","初始(2)","初始(2)"],"series":{"name":"情绪特征雷达图","data":["8","8","5","5","3","3","2","2"]}},"EmotionScore":"0"}
    this.setData({
      text_now:"",
      btn_voice_judge:app.globalData.index_btn_voice_judge,
      disableStop:true,
      SusOrCon:"../../icon/start.png",
      SusOrConIndex:1
    }
    )
  }

})
