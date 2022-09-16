package com.beggar.beggarplayer.core

import android.content.Context
import com.beggar.beggarplayer.core.config.BeggarPlayerConfig
import com.beggar.beggarplayer.core.datasource.BeggarPlayerDataSource
import com.beggar.beggarplayer.core.observer.IBeggarPlayerStateObserver
import com.beggar.beggarplayer.core.player.IBeggarPlayerLogic
import com.beggar.beggarplayer.core.player.systemplayer.SystemMediaPlayerLogic
import com.beggar.beggarplayer.core.statemachine.BeggarPlayerCoreManager
import com.beggar.beggarplayer.core.statemachine.BeggarPlayerCoreManager.PlayerEvent
import com.beggar.beggarplayer.core.view.BeggarPlayerTextureView

/**
 * author: BeggarLan
 * created on: 2022/8/30 8:51 下午
 * description: 播放器controller
 *
 * 该类主要负责组装功能(状态机、播放器具体逻辑、textureView)
 *
 * @param config 配置
 */
class BeggarPlayerController(
  private val context: Context,
  private val config: BeggarPlayerConfig
) : IBeggarPlayerController {

  companion object {
    private const val TAG = "BeggarBasePlayer"
  }

  // 核心逻辑处理
  internal val coreManager: BeggarPlayerCoreManager

  init {
    coreManager = BeggarPlayerCoreManager(buildPlayerLogic())
  }

  /**
   * 使用方要设置
   */
  override fun setTextureView(view: BeggarPlayerTextureView) {
    coreManager.setTextureView(view)
  }

  override fun registerObserver(observer: IBeggarPlayerStateObserver) {
    coreManager.registerObserver(observer)
  }

  override fun unregisterObserver(observer: IBeggarPlayerStateObserver) {
    coreManager.unregisterObserver(observer)
  }

  // ********************* 生命周期相关 *********************
  override fun reset() {
    coreManager.sendEvent(PlayerEvent.Reset())
  }

  override fun setDataSource(dataSource: BeggarPlayerDataSource) {
    coreManager.sendEvent(PlayerEvent.SetDataSource(dataSource))
  }

  override fun prepareSync() {
    coreManager.sendEvent(PlayerEvent.Prepare(true))
  }

  override fun prepareAsync() {
    coreManager.sendEvent(PlayerEvent.Prepare(false))
  }

  override fun start() {
    coreManager.sendEvent(PlayerEvent.Start())
  }

  override fun pause() {
    coreManager.sendEvent(PlayerEvent.Pause())
  }

  override fun stop() {
    coreManager.sendEvent(PlayerEvent.Stop())
  }

  override fun release() {
    coreManager.sendEvent(PlayerEvent.Release())
  }

  // ********************* 生命周期相关 *********************

  // ********************* 其他操作方法 *********************
  override fun seekTo(timeMs: Long) {
    coreManager.sendEvent(PlayerEvent.SeekTo(timeMs))
  }

  override fun setVolume(leftVolume: Float, rightVolume: Float) {
    coreManager.setVolume(leftVolume, rightVolume)
  }

  override fun setLoop(loop: Boolean) {
    coreManager.setLoop(loop)
  }

  override fun setMuteStatus(isMute: Boolean) {
    coreManager.setMuteStatus(isMute)
  }
  // ********************* 其他操作方法 *********************

  // ********************* 获得一些信息 *********************
  override fun getVideoWidth(): Int {
    return coreManager.getVideoWidth()
  }

  override fun getVideoHeight(): Int {
    return coreManager.getVideoHeight()
  }
  // ********************* 获得一些信息 *********************

  /**
   * 构造logic，允许外部替换logic
   * 默认采用系统播放器实现
   */
  private fun buildPlayerLogic(): IBeggarPlayerLogic {
    return SystemMediaPlayerLogic(context)
  }

}