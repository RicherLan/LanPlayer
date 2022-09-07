package com.beggar.beggarplayer.core.player.systemplayer

import android.media.AudioAttributes
import android.media.MediaPlayer
import com.beggar.beggarplayer.core.base.BeggarPlayerLogger
import com.beggar.beggarplayer.core.player.IBeggarPlayerLogic
import com.beggar.beggarplayer.core.player.datasource.BeggarPlayerDataSource
import com.beggar.beggarplayer.core.player.observer.IBeggarPlayerObserver

/**
 * author: BeggarLan
 * created on: 2022/8/30 8:33 下午
 * description: 采用系统播放器实现
 */
class SystemMediaPlayerLogic : IBeggarPlayerLogic {

  companion object {
    private const val TAG = "SystemMediaPlayer"
  }

  // 系统播放器实例
  private val mediaPlayer = MediaPlayer()

  // 播放器的一些回调
  private var playerCallback: IBeggarPlayerLogic.IPlayerCallback? = null

  init {
    // 初始化视频播放器
    initMediaPlayer()
  }

  /**
   * 初始化视频播放器
   */
  private fun initMediaPlayer() {
    mediaPlayer.setAudioAttributes(
      AudioAttributes.Builder()
        .setUsage(AudioAttributes.USAGE_MEDIA)
        .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
        .build()
    )
    // 设置播放时屏幕打开
    mediaPlayer.setScreenOnWhilePlaying(true)

    // 数据源准备好时回调
    mediaPlayer.setOnPreparedListener {
      BeggarPlayerLogger.log(TAG, "onPrepared")
      playerCallback?.onPrepared()
    }
    // 在网络流缓冲区的状态发生变化时回调
    mediaPlayer.setOnBufferingUpdateListener(object : MediaPlayer.OnBufferingUpdateListener {
      override fun onBufferingUpdate(mp: MediaPlayer?, percent: Int) {
        BeggarPlayerLogger.log(TAG, "onBufferingUpdate")
      }
    })
    // seek完成时回调
    mediaPlayer.setOnSeekCompleteListener(object : MediaPlayer.OnSeekCompleteListener {
      override fun onSeekComplete(mp: MediaPlayer?) {
        BeggarPlayerLogger.log(TAG, "onSeekComplete")
      }
    })
    // 当某info/warning出现时调用
    mediaPlayer.setOnInfoListener(object : MediaPlayer.OnInfoListener {
      override fun onInfo(mp: MediaPlayer?, what: Int, extra: Int): Boolean {
        BeggarPlayerLogger.log(TAG, "[onInfo][what=".plus(what).plus("]"))
        return false
      }
    })
    // 当视频的宽高[确认]或者[改变]时回调
    mediaPlayer.setOnVideoSizeChangedListener(object : MediaPlayer.OnVideoSizeChangedListener {
      override fun onVideoSizeChanged(mp: MediaPlayer?, width: Int, height: Int) {
        BeggarPlayerLogger.log(
          TAG,
          "[onVideoSizeChanged][width=".plus(width).plus(", height=").plus(height).plus("]")
        )
      }
    })
    // 播放完成时回调
    mediaPlayer.setOnCompletionListener(object : MediaPlayer.OnCompletionListener {
      override fun onCompletion(mp: MediaPlayer?) {
        BeggarPlayerLogger.log(TAG, "onCompletion")
        playerCallback?.onCompletion()
      }
    })
    // error时回调
    mediaPlayer.setOnErrorListener(object : MediaPlayer.OnErrorListener {
      override fun onError(mp: MediaPlayer?, what: Int, extra: Int): Boolean {
        BeggarPlayerLogger.log(TAG, "[onError][what=".plus(what).plus("]"))
        playerCallback?.onError()
        return false
      }
    })
  }

  override fun setStateObserver(listener: IBeggarPlayerObserver?) {
    TODO("Not yet implemented")
  }

  override fun setPlayerCallback(callback: IBeggarPlayerLogic.IPlayerCallback) {
    playerCallback = callback
  }

  override fun setDataSource(dataSource: BeggarPlayerDataSource) {
    TODO("Not yet implemented")
  }

  override fun prepareSync() {
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

  override fun getVideoWidth(): Int {
    TODO("Not yet implemented")
  }

  override fun getVideoHeight(): Int {
    TODO("Not yet implemented")
  }

}