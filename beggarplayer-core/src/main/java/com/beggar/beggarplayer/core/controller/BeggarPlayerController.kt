package com.beggar.beggarplayer.core.controller

import com.beggar.beggarplayer.core.controller.config.BeggarPlayerControllerConfig
import com.beggar.beggarplayer.core.player.IBeggarPlayer
import com.beggar.beggarplayer.core.player.observer.IBeggarPlayerStateObsever
import com.beggar.beggarplayer.core.player.systemplayer.SystemMediaPlayerLogic
import com.beggar.beggarplayer.core.view.BeggarPlayerTextureView

/**
 * author: BeggarLan
 * created on: 2022/8/30 7:35 下午
 * description: 播放器控制类
 * 1. 管理播放器和textureView
 * 2. 允许使用方替换播放器实例，默认内部采用系统播放器
 * 3. 提供常用的播放器方法
 * 4. 提供监听播放器状态的能力
 * @param config 配置
 */
class BeggarPlayerController(
  private val config: BeggarPlayerControllerConfig
) : IBeggarPlayerController {

  // 播放器实例
  private var player: IBeggarPlayer
  private lateinit var textureView: BeggarPlayerTextureView

  // 用于分发播放器状态更改事件的
  private val playerStateChangeEventHub = BeggarPlayerStateChangeEventHub()

  init {
    // 允许使用发那个替换播放器实现
    if (config.player != null) {
      player = config.player
    } else {
      // 默认采用系统播放器
      player = createSystemMediaPlayer()
    }
  }

  override fun registerStateChangeListener(listener: IBeggarPlayerStateObsever) {
    playerStateChangeEventHub.registerListener(listener)
  }

  override fun unregisterStateChangeListener(listener: IBeggarPlayerStateObsever) {
    playerStateChangeEventHub.unregisterListener(listener)
  }

  override fun setTextureView(view: BeggarPlayerTextureView) {
    if (textureView == view) {
      return
    }
    textureView = view
    // TODO: view更换时其他逻辑处理
  }


  // ********************* 生命周期相关 *********************
  override fun setDataSource() {
    TODO("Not yet implemented")
  }

  override fun startPlay() {
    TODO("Not yet implemented")
  }

  override fun pausePlay() {
    TODO("Not yet implemented")
  }

  override fun release() {
    TODO("Not yet implemented")
  }
  // ********************* 生命周期相关 *********************


  override fun seekTo(timeMs: Long) {
    TODO("Not yet implemented")
  }

  override fun setVolume(volume: Float) {
    TODO("Not yet implemented")
  }

  override fun setMuteStatus(isMute: Boolean) {
    TODO("Not yet implemented")
  }

  /**
   * 创建系统播放器
   */
  private fun createSystemMediaPlayer(): IBeggarPlayer {
    return SystemMediaPlayerLogic()
  }

}