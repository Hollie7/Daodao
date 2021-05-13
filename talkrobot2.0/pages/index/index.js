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
    backImg:null,
    characterImg:null,
    disableStart:false,
    disablePause:true,
    disableStop:true,
    SusOrCon:"暂停",
    SusOrConArray:["暂停","继续"],
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
    if(this.data.SusOrConIndex == 1){
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
      SusOrConIndex:1,
      SusOrCon:'暂停',
      disableStart:false,
      disablePause:true,
      disableStop:true
    }),
    // 控制
    app.globalData.index_btn_voice_judge = false
    this.setData({
      btn_voice_judge:app.globalData.index_btn_voice_judge
    })
    manager.stop() 
  },  
  change: function(){
    if(app.globalData.voice_text){
      this.setData({
        text_now:app.globalData.voice_text
      })
    }
    else{
      this.setData({
        text_now:"app.globalData.voice_text"
      })
    }
    var that = this;
    /*if(app.globalData.voice_text){*/
      wx.request({
        url: 'https://www.talkrobot.top/Test',
        /*url: 'http://localhost:9000/Test',*/
        method:'POST',
        header: {
          'content-type':'application/x-www-form-urlencoded',
          'Accept': 'application/json'
        },
        data:{
          text:"最近一段时间，大概从 12 月开始的，情绪特别低落，12 月是觉得不开心和沮丧，1 月份以来的最近几天，每天都会哭，" +
        "不知道为什么，莫名就觉得孤单，被抛弃，自己一无是处。以前情绪不好的时候，哭过一次大概就好了，三天前，因为觉得特别孤独，又没人可以说，" +
        "特别难过，哭了一个晚上，但是第二天，电话里，男票的一句很普通的话戳中了我，他说——下学期准备考驾照，反正你也不想考。我瞬间就有被抛弃的感觉了，" +
        "觉得我不想长大，只想躲起来，看着人群来来往往，却没有人可以陪我，和我同路，听我说话，我只能是一个人。可能是大三上学期就要结束了，" +
        "不是考研就是要找工作了，回首了下目前为止的大学，想的好多要做的事，都没有做，大部分精力都用在和惰性斗争了，心里有点恐惧和自责。" +
        "这几天几乎每天都要哭，还是觉得情绪里有无穷无尽的悲伤，低沉地我不知道怎么办了，我性格本来就有点内向，不能尽情开心，压抑着自己的那种，" +
        "平常大多是不高兴也不悲伤的情绪，看过一些心理学的书，怀疑自己可能有神经症，大一暑假去号脉的时候，医生说我有神经官能紊乱，那时候就有点不开心，" +
        "现在情绪的情况这样，要不要去看医生吃药？真的好难受啊。"
          /*text:app.globalData.voice_text*/
      },
  
        success: function (res) {
          console.log(res.data.EmotionScore)
          app.globalData.backEndData = res.data
          app.globalData.index_btn_voice_judge = false
          that.setData({
            btn_voice_judge:app.globalData.index_btn_voice_judge,
            text_now:app.globalData.voice_text
          }
          )
        }
  ,
        fail:function(err){
         console.log("error:"+err.data);
        }
      })
    
    /*}*/
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
    app.globalData.voice_text = null
    app.globalData.index_btn3_judge = true
    app.globalData.judge = false
    app.globalData.backEndData = {"wordCloud":[{"name":"初始","textSize":30},{"name":"初始","textSize":25},{"name":"初始","textSize":20},{"name":"初始","textSize":20},{"name":"初始","textSize":20},{"name":"初始","textSize":15}],"RadarPlot":{"categories":["初始(8)","初始(8)","初始(5)","初始(5)","初始(3)","初始(3)","初始(2)","初始(2)"],"series":{"name":"情绪特征雷达图","data":["8","8","5","5","3","3","2","2"]}},"EmotionScore":"0"}
    this.setData({
      text_now:"",
      btn_voice_judge:app.globalData.index_btn_voice_judge
    }
    )
  }

})
