package com.beggar.beggarplayer.core

import com.beggar.beggarplayer.core.config.BeggarPlayerConfig
import com.beggar.beggarplayer.core.datasource.BeggarPlayerDataSource
import com.beggar.beggarplayer.core.observer.BeggarPlayerObserverDispatcher
import com.beggar.beggarplayer.core.observer.IBeggarPlayerObserver
import com.beggar.beggarplayer.core.player.IBeggarPlayerLogic
import com.beggar.beggarplayer.core.player.systemplayer.SystemMediaPlayerLogic
import com.beggar.beggarplayer.core.statemachine.BeggarPlayerStateMachineManager
import com.beggar.beggarplayer.core.statemachine.BeggarPlayerStateMachineManager.PlayerEvent
import com.beggar.beggarplayer.core.view.BeggarPlayerTextureView
import com.beggar.statemachine.Event

/**
 * author: BeggarLan
 * created on: 2022/8/30 8:51 下午
 * description: 播放器controller
 * 1. 状态机构建和维护
 * 2. 播放器具体逻辑允许外部替换，否则使用默认logic:
 * @see IBeggarPlayerLogic
 *
 * 3. 提供播放器状态监听
 *
 * @param config 配置
 */
class BeggarPlayerController(private val config: BeggarPlayerConfig) : IBeggarPlayerController {

  companion object {
    private const val TAG = "BeggarBasePlayer"
  }

  // 播放器具体逻辑
  private val playerLogic: IBeggarPlayerLogic

  // UI
  private lateinit var textureView: BeggarPlayerTextureView

  // 状态机
  private val stateMachineManager: BeggarPlayerStateMachineManager

  // 播放器事件分发
  private val observerDispatcher = BeggarPlayerObserverDispatcher()

  init {
    playerLogic = buildPlayerLogic()
    stateMachineManager = BeggarPlayerStateMachineManager()
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

  override fun registerObserver(observer: IBeggarPlayerObserver) {
    observerDispatcher.registerObserver(observer)
  }

  override fun unregisterObserver(observer: IBeggarPlayerObserver) {
    observerDispatcher.unregisterObserver(observer)
  }

  /**
   * 向状态机发送事件
   */
  private fun sendEvent(event: Event) {
    stateMachineManager.sendEvent(event)
  }

  // ********************* 生命周期相关 *********************
  override fun reset() {
    stateMachineManager.sendEvent(PlayerEvent.Reset())
  }

  override fun setDataSource(dataSource: BeggarPlayerDataSource) {
    stateMachineManager.sendEvent(PlayerEvent.SetDataSource(dataSource))
  }

  override fun prepareSync() {
    stateMachineManager.sendEvent(PlayerEvent.Prepare(true))
  }

  override fun prepareAsync() {
    stateMachineManager.sendEvent(PlayerEvent.Prepare(false))
  }

  override fun start() {
    stateMachineManager.sendEvent(PlayerEvent.Start())
  }

  override fun pause() {
    stateMachineManager.sendEvent(PlayerEvent.Pause())
  }

  override fun stop() {
    stateMachineManager.sendEvent(PlayerEvent.Stop())
  }

  override fun release() {
    stateMachineManager.sendEvent(PlayerEvent.End())
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

}