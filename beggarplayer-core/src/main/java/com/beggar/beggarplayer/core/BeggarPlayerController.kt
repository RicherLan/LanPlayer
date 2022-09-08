package com.beggar.beggarplayer.core

import com.beggar.beggarplayer.core.config.BeggarPlayerConfig
import com.beggar.beggarplayer.core.datasource.BeggarPlayerDataSource
import com.beggar.beggarplayer.core.observer.IBeggarPlayerStateObserver
import com.beggar.beggarplayer.core.player.IBeggarPlayerLogic
import com.beggar.beggarplayer.core.player.systemplayer.SystemMediaPlayerLogic
import com.beggar.beggarplayer.core.statemachine.BeggarPlayerCoreManager
import com.beggar.beggarplayer.core.statemachine.BeggarPlayerCoreManager.PlayerEvent
import com.beggar.beggarplayer.core.view.BeggarPlayerTextureView
import com.beggar.statemachine.Event

/**
 * author: BeggarLan
 * created on: 2022/8/30 8:51 下午
 * description: 播放器controller
 *
 * 该类主要负责组装功能(状态机、播放器具体逻辑、UI)
 *
 * @param config 配置
 */
class BeggarPlayerController(private val config: BeggarPlayerConfig) : IBeggarPlayerController {

  companion object {
    private const val TAG = "BeggarBasePlayer"
  }

  // 播放器具体逻辑
  private val playerLogic: IBeggarPlayerLogic

  // 核心逻辑处理
  private val coreManager: BeggarPlayerCoreManager

  // UI
  private lateinit var textureView: BeggarPlayerTextureView

  init {
    playerLogic = buildPlayerLogic()
    coreManager = buildStateMachineManager()
  }

  /**
   * 使用方要设置
   */
  override fun setTextureView(view: BeggarPlayerTextureView) {
    if (textureView == view) {
      return
    }
    textureView = view
    // TODO: view更换时其他逻辑处理
  }

  override fun registerObserver(observer: IBeggarPlayerStateObserver) {
    coreManager.registerObserver(observer)
  }

  override fun unregisterObserver(observer: IBeggarPlayerStateObserver) {
    coreManager.unregisterObserver(observer)
  }

  /**
   * 向状态机发送事件
   */
  private fun sendEvent(event: Event) {
    coreManager.sendEvent(event)
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
    coreManager.sendEvent(PlayerEvent.End())
  }

  // ********************* 生命周期相关 *********************

  // ********************* 其他操作方法 *********************
  override fun seekTo(timeMs: Long) {
    playerLogic.seekTo(timeMs)
  }

  override fun setVolume(volume: Float) {
    playerLogic.setVolume(volume)
  }

  override fun setLoop(loop: Boolean) {
    playerLogic.setLoop(loop)
  }

  override fun setMuteStatus(isMute: Boolean) {
    TODO("Not yet implemented")
  }
  // ********************* 其他操作方法 *********************

  // ********************* 获得一些信息 *********************
  override fun getVideoWidth(): Int {
    return playerLogic.getVideoWidth()
  }

  override fun getVideoHeight(): Int {
    return playerLogic.getVideoHeight()
  }
  // ********************* 获得一些信息 *********************

  /**
   * 构造logic，允许外部替换logic
   * 默认采用系统播放器实现
   */
  private fun buildPlayerLogic(): IBeggarPlayerLogic {
    // 监听播放器的事件
    val callback = object : IBeggarPlayerLogic.IPlayerCallback {
      override fun onPrepared() {
        sendEvent(PlayerEvent.Prepared())
      }

      override fun onCompletion() {
        sendEvent(PlayerEvent.Complete())
      }

      override fun onError() {
        sendEvent(PlayerEvent.Error())
      }
    }

    // 替换为外面的实现
    if (config.playerLogic != null) {
      config.playerLogic.setPlayerCallback(callback)
      return config.playerLogic
    }

    // 默认实现
    val systemMediaPlayerLogic = SystemMediaPlayerLogic()
    systemMediaPlayerLogic.setPlayerCallback(callback)
    return systemMediaPlayerLogic
  }

  /**
   * 构造状态机管理器
   * 监听状态变化 --> 逻辑处理
   */
  private fun buildStateMachineManager(): BeggarPlayerCoreManager {
    val stateMachineManager = BeggarPlayerCoreManager(playerLogic)
    return stateMachineManager
  }

}