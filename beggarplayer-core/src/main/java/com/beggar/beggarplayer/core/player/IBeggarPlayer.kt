package com.beggar.beggarplayer.core.player

import com.beggar.beggarplayer.core.player.data.BeggarPlayerDataSource
import com.beggar.beggarplayer.core.player.listener.IBeggarPlayerStateChangeListener

/**
 * author: BeggarLan
 * created on: 2022/8/30 10:33 上午
 * description: 播放器接口
 */
interface IBeggarPlayer {

  /**
   * 状态更改监听
   */
  fun setStateListener(listener: IBeggarPlayerStateChangeListener?)

  // ********************* 生命周期相关 *********************
  /**
   * 设置数据源
   */
  fun setDataSource(dataSource: BeggarPlayerDataSource)

  /**
   * 准备
   */
  fun prepare()

  /**
   * 异步准备
   */
  fun prepareAsync()

  /**
   * 开始
   */
  fun start()

  /**
   * 暂停
   */
  fun pause()

  /**
   * 停止
   */
  fun stop()

  /**
   * 重置
   */
  fun reset()

  /**
   * 释放
   */
  fun release()
  // ********************* 生命周期相关 *********************

  // ********************* 其他操作方法 *********************
  /**
   * 跳转到指定时间
   */
  fun seekTo(timeMs: Long)

  /**
   * 设置音量
   */
  fun setVolume(volume: Float)

  /**
   * 设置是否循环播放
   */
  fun setLoop(loop: Boolean)
  // ********************* 其他操作方法 *********************


  // ********************* 获得一些信息 *********************
  /**
   * 获得视频的宽度
   */
  fun getVideoWidth(): Int

  /**
   * 获得视频的高度
   */
  fun getVideoHeight(): Int
  // ********************* 获得一些信息 *********************

}