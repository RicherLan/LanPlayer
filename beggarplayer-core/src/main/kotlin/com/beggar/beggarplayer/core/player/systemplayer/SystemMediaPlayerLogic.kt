package com.beggar.beggarplayer.core.player.systemplayer

import android.content.Context
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.os.Build
import android.view.Surface
import com.beggar.beggarplayer.core.datasource.BeggarPlayerDataSource
import com.beggar.beggarplayer.core.log.BeggarPlayerLogger
import com.beggar.beggarplayer.core.player.IBeggarPlayerLogic

/**
 * author: BeggarLan
 * created on: 2022/8/30 8:33 下午
 * description: 采用系统播放器实现
 */
class SystemMediaPlayerLogic(private val context: Context) : IBeggarPlayerLogic {

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

  override fun setPlayerCallback(callback: IBeggarPlayerLogic.IPlayerCallback) {
    playerCallback = callback
  }

  override fun setSurface(surface: Surface?) {
    mediaPlayer.setSurface(surface)
  }

  override fun setDataSource(dataSource: BeggarPlayerDataSource) {
    mediaPlayer.setDataSource(context, dataSource.uri)
  }

  override fun prepareSync() {
    mediaPlayer.prepare()
  }

  override fun prepareAsync() {
    mediaPlayer.prepareAsync()
  }

  override fun start() {
    mediaPlayer.start()
  }

  override fun pause() {
    mediaPlayer.pause()
  }

  override fun stop() {
    mediaPlayer.stop()
  }

  override fun reset() {
    mediaPlayer.reset()
  }

  override fun release() {
    mediaPlayer.release()
  }

  override fun seekTo(timeMs: Long) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
      // 注意SEEK_CLOSEST有性能开销
      mediaPlayer.seekTo(timeMs, MediaPlayer.SEEK_CLOSEST)
    } else {
      mediaPlayer.seekTo(timeMs.toInt())
    }
  }

  override fun setVolume(leftVolume: Float, rightVolume: Float) {
    mediaPlayer.setVolume(leftVolume, rightVolume)
  }

  override fun setLoop(loop: Boolean) {
    mediaPlayer.isLooping = loop
  }

  override fun getVideoWidth(): Int {
    return mediaPlayer.videoWidth
  }

  override fun getVideoHeight(): Int {
    return mediaPlayer.videoHeight
  }

}