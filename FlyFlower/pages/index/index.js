// index.js
// 获取应用实例
const app = getApp()

Page({
  data: {
    motto: 'Hello World',
    userInfo: {},
    hasUserInfo: false,
    shi3: "诗三百全文",
    keystr: "花",
    res: "四月南风大麦黄",
    canIUse: wx.canIUse('button.open-type.getUserInfo'),
    canIUseGetUserProfile: false,
    canIUseOpenData: wx.canIUse('open-data.type.userAvatarUrl') && wx.canIUse('open-data.type.userNickName') // 如需尝试获取用户信息可改为false
  },

  go2s: function(e){
    this.data.keystr=e.detail.value.keystr
    this.data.res=""
    if(this.data.shi3.length<20){
      var that=this
      wx.request({
        url: 'https://7a78-zxx-cloud-4golphdi6140f16e-1311941279.tcb.qcloud.la/tangshi300.txt?sign=8851fff455bdf512eda91e32a058b5d3&t=1652625632',
        success(res){
          that.data.shi3=res.data
        }
      })
    }
    var reg = new RegExp(".*"+this.data.keystr+".*")
    var lines=this.data.shi3.split("\n")
    var cti=0
    for(var i1 in lines){
      if(reg.test(lines[i1])){
        this.data.res += lines[i1]+"\n"
        console.log(lines[i1])
        if(++cti > 5){
          this.data.res += "……\n"
          break;
        }
      }
    }
    this.setData({
      res: this.data.res
    })
  },



  // 事件处理函数
  bindViewTap() {
    wx.navigateTo({
      url: '../logs/logs'
    })
  },
  onLoad() {
    if (wx.getUserProfile) {
      this.setData({
        canIUseGetUserProfile: true
      })
    }
  },
  getUserProfile(e) {
    // 推荐使用wx.getUserProfile获取用户信息，开发者每次通过该接口获取用户个人信息均需用户确认，开发者妥善保管用户快速填写的头像昵称，避免重复弹窗
    wx.getUserProfile({
      desc: '展示用户信息', // 声明获取用户个人信息后的用途，后续会展示在弹窗中，请谨慎填写
      success: (res) => {
        console.log(res)
        this.setData({
          userInfo: res.userInfo,
          hasUserInfo: true
        })
      }
    })
  },
  getUserInfo(e) {
    // 不推荐使用getUserInfo获取用户信息，预计自2021年4月13日起，getUserInfo将不再弹出弹窗，并直接返回匿名的用户个人信息
    console.log(e)
    this.setData({
      userInfo: e.detail.userInfo,
      hasUserInfo: true
    })
  }
})
