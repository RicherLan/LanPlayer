package com.beggar.beggarplayer.core

import com.beggar.beggarplayer.core.observer.IBeggarPlayerStateObserver
import com.beggar.beggarplayer.core.player.IBeggarPlayer
import com.beggar.beggarplayer.core.view.BeggarPlayerTextureView

/**
 * author: BeggarLan
 * created on: 2022/8/30 6:12 下午
 * description: 播放器控制类
 * 封装了播放器，主要提供以下功能：
 * 1. 播放器生命周期管理
 * 2. 对播放器的一些操作：音量控制等
 * 3. 提供一些监听：监听播放器状态更改等
 */
interface IBeggarPlayerController : IBeggarPlayer {

  /**
   * 监听播放器
   */
  fun registerObserver(observer: IBeggarPlayerStateObserver)

  fun unregisterObserver(observer: IBeggarPlayerStateObserver)

  /**
   * 给播放器设置TextureView，由该TextureView产生的surface进行播放
   */
  fun setTextureView(view: BeggarPlayerTextureView)

  /**
   * 设置静音状态
   * @param isMute {@code true} 静音
   */
  fun setMuteStatus(isMute: Boolean)

}