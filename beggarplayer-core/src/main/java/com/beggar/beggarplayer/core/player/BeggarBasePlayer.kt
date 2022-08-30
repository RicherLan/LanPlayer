package com.beggar.beggarplayer.core.player

import com.beggar.beggarplayer.core.player.listener.IBeggarPlayerStateChangeListener

/**
 * author: lanweihua
 * created on: 2022/8/30 8:51 下午
 * description: 播放器基类
 * 1. 状态流转封装
 */
class BeggarBasePlayer : IBeggarPlayer {

  companion object {
    private const val TAG = "BeggarBasePlayer"
  }

  // 状态更改监听
  private var stateChangeListener: IBeggarPlayerStateChangeListener? = null

  override fun setStateListener(listener: IBeggarPlayerStateChangeListener?) {
    stateChangeListener = listener
  }

  override fun prepare() {
    TODO("Not yet implemented")
  }

  override fun prepareAsync() {
    TODO("Not yet implemented")
  }

  override fun start() {
    TODO("Not yet implemented")
  }

  override fun pause() {
    TODO("Not yet implemented")
  }

  override fun stop() {
    TODO("Not yet implemented")
  }

  override fun reset() {
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

  override fun setLoop(loop: Boolean) {
    TODO("Not yet implemented")
  }

}