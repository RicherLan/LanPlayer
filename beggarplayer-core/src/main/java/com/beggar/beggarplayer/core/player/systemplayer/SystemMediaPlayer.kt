package com.beggar.beggarplayer.core.player.systemplayer

import com.beggar.beggarplayer.core.base.BeggarPlayerLogger
import com.beggar.beggarplayer.core.player.BeggarPlayerState
import com.beggar.beggarplayer.core.player.IBeggarPlayer
import com.beggar.beggarplayer.core.player.listener.IBeggarPlayerStateChangeListener

/**
 * author: BeggarLan
 * created on: 2022/8/30 8:33 下午
 * description: 采用系统播放器实现
 */
class SystemMediaPlayer : IBeggarPlayer {

  companion object {
    private const val TAG = "SystemMediaPlayer"
  }

  // 播放器当前的状态，默认idle
  private var mCurrentPlayerState: BeggarPlayerState = BeggarPlayerState.IDLE

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

  private fun moveToState(newState: BeggarPlayerState) {
    BeggarPlayerLogger.log(
      TAG, "moveToState: ".plus(mCurrentPlayerState.name).plus(" -> ").plus(newState.name)
    )
    mCurrentPlayerState = newState
  }

}