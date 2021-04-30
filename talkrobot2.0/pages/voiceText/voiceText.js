// pages/voiceText/voiceText.js
const app = getApp()
Page({

  data: {
    btn_judge:app.globalData.index_btn_judge,
    /*text:"最近一段时间，大概从 12 月开始的，情绪特别低落，12 月是觉得不开心和沮丧，1 月份以来的最近几天，每天都会哭，" +
        "不知道为什么，莫名就觉得孤单，被抛弃，自己一无是处。以前情绪不好的时候，哭过一次大概就好了，三天前，因为觉得特别孤独，又没人可以说，" +
        "特别难过，哭了一个晚上，但是第二天，电话里，男票的一句很普通的话戳中了我，他说——下学期准备考驾照，反正你也不想考。我瞬间就有被抛弃的感觉了，" +
        "觉得我不想长大，只想躲起来，看着人群来来往往，却没有人可以陪我，和我同路，听我说话，我只能是一个人。可能是大三上学期就要结束了，" +
        "不是考研就是要找工作了，回首了下目前为止的大学，想的好多要做的事，都没有做，大部分精力都用在和惰性斗争了，心里有点恐惧和自责。" +
        "这几天几乎每天都要哭，还是觉得情绪里有无穷无尽的悲伤，低沉地我不知道怎么办了，我性格本来就有点内向，不能尽情开心，压抑着自己的那种，" +
        "平常大多是不高兴也不悲伤的情绪，看过一些心理学的书，怀疑自己可能有神经症，大一暑假去号脉的时候，医生说我有神经官能紊乱，那时候就有点不开心，" +
        "现在情绪的情况这样，要不要去看医生吃药？真的好难受啊。"*/
    text:null,
    backImg:null,
    characterImg:"null"
  },

  onLoad: function(){
    this.setData({
      text:app.globalData.voice_text
    })

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

  ToIndex:function(){
    wx.navigateBack({
      dalta: 3     // 默认值是 1
  })
  },
  ToWordcloud:function(){
    wx.navigateTo({
      url: '/pages/wordcloud/wordcloud'
  })
  },

  change: function(){
    var that = this;
    wx.request({
      url: 'https://www.talkrobot.top/Test',
      /*url: 'http://localhost:9000/Test',*/
      method:'POST',
      header: {
        'content-type':'application/x-www-form-urlencoded',
        'Accept': 'application/json'
      },
      data:{
        text:this.data.text
        /*text:"最近一段时间，大概从 12 月开始的，情绪特别低落，12 月是觉得不开心和沮丧，1 月份以来的最近几天，每天都会哭，" +
        "不知道为什么，莫名就觉得孤单，被抛弃，自己一无是处。以前情绪不好的时候，哭过一次大概就好了，三天前，因为觉得特别孤独，又没人可以说，" +
        "特别难过，哭了一个晚上，但是第二天，电话里，男票的一句很普通的话戳中了我，他说——下学期准备考驾照，反正你也不想考。我瞬间就有被抛弃的感觉了，" +
        "觉得我不想长大，只想躲起来，看着人群来来往往，却没有人可以陪我，和我同路，听我说话，我只能是一个人。可能是大三上学期就要结束了，" +
        "不是考研就是要找工作了，回首了下目前为止的大学，想的好多要做的事，都没有做，大部分精力都用在和惰性斗争了，心里有点恐惧和自责。" +
        "这几天几乎每天都要哭，还是觉得情绪里有无穷无尽的悲伤，低沉地我不知道怎么办了，我性格本来就有点内向，不能尽情开心，压抑着自己的那种，" +
        "平常大多是不高兴也不悲伤的情绪，看过一些心理学的书，怀疑自己可能有神经症，大一暑假去号脉的时候，医生说我有神经官能紊乱，那时候就有点不开心，" +
        "现在情绪的情况这样，要不要去看医生吃药？真的好难受啊。"*/

        /*text:"今天很开心，我的朋友们来看我了，而且今天天气很棒，我们出去吃了顿大餐，还一起到迪士尼玩了一天，真的非常开心。快乐的日子总是短暂的，希望每天都能这么开心"*/
      },
  
      success: function (res) {
        console.log(res.data.EmotionScore)
        app.globalData.backEndData = res.data
        app.globalData.index_btn_judge = false
        that.setData({
          btn_judge:app.globalData.index_btn_judge,
        }
        )
      }
,
      fail:function(err){
        console.log("error:"+err.data);
      }
    })
  },
})