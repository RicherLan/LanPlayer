package com.beggar.beggarplayer.core.controller

import android.view.TextureView
import com.beggar.beggarplayer.core.player.listener.IBeggarPlayerStateChangeListener

/**
 * author: BeggarLan
 * created on: 2022/8/30 7:35 下午
 * description: 采用系统播放器实现
 */
class BeggarSystemPlayerController : IBeggarPlayerController {

  // 用于分发播放器状态更改事件的
  private val playerStateChangeEventHub = BeggarPlayerStateChangeEventHub()

  override fun registerStateChangeListener(listener: IBeggarPlayerStateChangeListener) {
    playerStateChangeEventHub.registerListener(listener)
  }

  override fun unregisterStateChangeListener(listener: IBeggarPlayerStateChangeListener) {
    playerStateChangeEventHub.unregisterListener(listener)
  }

  override fun setDataSource() {
    TODO("Not yet implemented")
  }

  override fun setTextureView(view: TextureView) {
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

  override fun seekTo(timeMs: Long) {
    TODO("Not yet implemented")
  }

  override fun setVolume(volume: Float) {
    TODO("Not yet implemented")
  }

  override fun setMuteStatus(isMute: Boolean) {
    TODO("Not yet implemented")
  }
}