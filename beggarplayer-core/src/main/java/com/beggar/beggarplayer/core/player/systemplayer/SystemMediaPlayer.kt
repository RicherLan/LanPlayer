package com.beggar.beggarplayer.core.player.systemplayer

import com.beggar.beggarplayer.core.player.BeggarBasePlayer
import com.beggar.beggarplayer.core.player.data.BeggarPlayerDataSource

/**
 * author: BeggarLan
 * created on: 2022/8/30 8:33 下午
 * description: 采用系统播放器实现
 */
class SystemMediaPlayer : BeggarBasePlayer() {

  companion object {
    private const val TAG = "SystemMediaPlayer"
  }

  // ********************* 驱动生命周期的方法 *********************
  override fun setDataSourceInner(dataSource: BeggarPlayerDataSource) {

  }

  override fun prepareInner() {

  }

  override fun prepareAsyncInner() {

  }

  override fun startInner() {

  }

  override fun pauseInner() {

  }

  override fun stopInner() {

  }

  override fun resetInner() {

  }

  override fun releaseInner() {

  }
  // ********************* 驱动生命周期的方法 *********************


  // ********************* 其他方法 *********************
  override fun seekTo(timeMs: Long) {

  }

  override fun setVolume(volume: Float) {

  }

  override fun setLoop(loop: Boolean) {

  }
  // ********************* 其他方法 *********************

}