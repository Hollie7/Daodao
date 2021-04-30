// pages/remark.js
const app = getApp()
Page({
  data: {
    nickName:null,
    currentWordNumber:0,
    wordMax:300,
    wordMin:0,
    remark_text:"",
    evaluate_contant: ['总体满意度', '情感分析准确度', '情感反馈即时度','产品使用流畅度'],
    stars: [0, 1, 2, 3, 4],
    normalSrc: '../../icon/star_normal.png',
    selectedSrc: '../../icon/star.png',
    halfSrc: '../../icon/star_half.png',
    score: 5,
    scores: [5, 5, 5, 5],
  },

  onLoad: function () {
    this.setData({
      nickName:app.globalData.userInfo.nickName
    }
    )
  },

  //点击左边,半颗星
  selectLeft: function (e) {
    var score = e.currentTarget.dataset.score
    if (this.data.score == 0.5 && e.currentTarget.dataset.score == 0.5) {
      score = 0;
    }

    this.data.scores[e.currentTarget.dataset.idx] = score,
      this.setData({
        scores: this.data.scores,
        score: score
      })

  },

  //点击右边,整颗星
  selectRight: function (e) {
    var score = e.currentTarget.dataset.score

    this.data.scores[e.currentTarget.dataset.idx] = score,
      this.setData({
        scores: this.data.scores,
        score: score
      })
  },

  inputs: function (e) {
    // 获取输入框的内容
    var value = e.detail.value;
    // 获取输入框内容的长度
    var len = parseInt(value.length);
    //最多字数限制
    if (len > this.data.wordMax) 
    return;
    // 当输入框内容的长度大于最大长度限制（max)时，终止setData()的执行
    this.setData({
      remark_text : value,
      currentWordNumber: len //当前字数  
    });
  },

  handleBtn:function(){
    var that = this;
    wx.request({
      url: 'https://www.talkrobot.top/Remark',
      /*url: 'http://localhost:9000/Remark',*/
      method:'POST',
      header: {
        'content-type':'application/x-www-form-urlencoded',
        'Accept': 'application/json'
      },
      data:{
        user:this.data.nickName,
        scores:this.data.scores,
        text:this.data.remark_text
      },

      success: function (res) {
        if(res.data == "评价成功"){
          wx.showToast({
            title: '评价成功！',
            icon: 'success',
            duration: 2000
          })
        }
        else if(res.data == "更新评价成功"){
          wx.showToast({
            title: '更新评价成功！',
            icon: 'success',
            duration: 2000
          })
        }
        else if(res.data == "评价失败"){
          wx.showToast({
            title: '评价失败！',
            icon: 'fail',
            duration: 2000
          })
        }
      },
      fail:function(err){
        console.log("error:"+err.data);
      }
    })
  }
})