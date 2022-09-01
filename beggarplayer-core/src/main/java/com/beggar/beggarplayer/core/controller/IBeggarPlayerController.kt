package com.beggar.beggarplayer.core.controller

import com.beggar.beggarplayer.core.player.listener.IBeggarPlayerStateChangeListener
import com.beggar.beggarplayer.core.view.BeggarPlayerTextureView

/**
 * author: BeggarLan
 * created on: 2022/8/30 6:12 下午
 * description: 播放器控制类
 * 封装了播放器，主要提供以下功能：
 * 1. 驱动播放器声明周期
 * 2. 对播放器的一些操作：音量控制等
 * 3. 提供一些监听：监听播放器状态更改等
 */
interface IBeggarPlayerController {

  /**
   * 注册状态更改监听
   */
  fun registerStateChangeListener(listener: IBeggarPlayerStateChangeListener)

  fun unregisterStateChangeListener(listener: IBeggarPlayerStateChangeListener)

  /**
   * 给播放器设置TextureView，由该TextureView产生的surface进行播放
   */
  fun setTextureView(view: BeggarPlayerTextureView)

  // ********************* 生命周期相关 *********************
  /**
   * 设置数据源
   */
  fun setDataSource()

  /**
   * 开始播放
   */
  fun startPlay()

  /**
   * 暂停播放
   */
  fun pausePlay()

  /**
   * 释放播放器，调用后这个播放器将不可用
   */
  fun release()
  // ********************* 生命周期相关 *********************

  /**
   * 跳转到指定时间
   */
  fun seekTo(timeMs: Long)

  /**
   * 设置音量
   */
  fun setVolume(volume: Float)

  /**
   * 设置静音状态
   * @param isMute {@code true} 静音
   */
  fun setMuteStatus(isMute: Boolean)


}