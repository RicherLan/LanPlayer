package com.beggar.beggarplayer.core.player

import com.beggar.beggarplayer.core.player.config.BeggarPlayerConfig
import com.beggar.beggarplayer.core.player.datasource.BeggarPlayerDataSource
import com.beggar.beggarplayer.core.player.observer.BeggarPlayerObserverDispatcher
import com.beggar.beggarplayer.core.player.observer.IBeggarPlayerObserver
import com.beggar.beggarplayer.core.player.systemplayer.SystemMediaPlayerLogic
import com.beggar.statemachine.Event
import com.beggar.statemachine.State
import com.beggar.statemachine.SyncStateMachine

/**
 * author: BeggarLan
 * created on: 2022/8/30 8:51 下午
 * description: 播放器基类
 * 1. 状态机构建和维护
 * 2. 播放器具体逻辑允许外部替换，否则使用默认logic:
 * @see IBeggarPlayerLogic
 *
 * 2. 提供播放器状态监听
 *
 * @param config 配置
 */
class BeggarPlayer(private val config: BeggarPlayerConfig) : IBeggarPlayer {

  /**
   * 具体的播放器子类实现
   */
  protected interface IPlayerLogic : IBeggarPlayer

  companion object {
    private const val TAG = "BeggarBasePlayer"
  }

  // 播放器具体逻辑
  private val playerLogic: IBeggarPlayerLogic

  // 状态机
  private var stateMachine: SyncStateMachine

  // 播放器事件分发
  private val observerDispatcher = BeggarPlayerObserverDispatcher()

  init {
    playerLogic = buildPlayerLogic()
    stateMachine = buildStateMachine()
  }

  /**
   * 构造logic，允许外部替换logic
   * 默认采用系统播放器实现
   */
  private fun buildPlayerLogic(): IBeggarPlayerLogic {
    // 监听播放器的事件
    val callback = object : IBeggarPlayerLogic.IPlayerCallback {
      override fun onPrepared() {
        sendEvent(Prepared())
      }

      override fun onCompletion() {
        sendEvent(Complete())
      }

      override fun onError() {
        sendEvent(Error())
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
   * 构造状态机
   */
  private fun buildStateMachine(): SyncStateMachine {
    val builder = SyncStateMachine.Builder()
      .setInitialState(idleState)
      .state(idleState)
      .state(initializedState)
      .state(preparingState)
      .state(preparedState)
      .state(startedState)
      .state(pausedState)
      .state(stoppedState)
      .state(completedState)
      .state(errorState)
      .state(endState)
    // TODO: transition

    return builder.build()
  }

  // ********************* 状态机事件 *********************
  class Reset : Event // 进idle
  class SetDataSource(val dataSource: BeggarPlayerDataSource) : Event // 设置数据源
  class Prepare(val isSync: Boolean) : Event // 区分同步和异步
  class Prepared : Event // prepare完成
  class Start : Event // 开始播放
  class Pause : Event // 暂停播放
  class Stop : Event // 停止播放
  class Complete : Event // 播放完毕
  class Error : Event // 出错
  class End : Event // 这里就结束播放器的生命了
  // ********************* 状态机事件 *********************

  // ********************* 状态 *********************
  protected val idleState = object : State<Any>("IdleState") {
    override fun onEnter(param: Any) {
      super.onEnter(param)
      observerDispatcher.onStateChange(BeggarPlayerState.IdleState)
    }

    override fun onExit() {
      super.onExit()
    }
  }

  // 设置完数据源后
  protected val initializedState = object : State<SetDataSource>("initializedState") {
    override fun onEnter(param: SetDataSource) {
      super.onEnter(param)
      playerLogic.setDataSource(param.dataSource)
      observerDispatcher.onStateChange(BeggarPlayerState.InitializedState)
    }

    override fun onExit() {
      super.onExit()
    }
  }

  // 准备中
  protected val preparingState = object : State<Prepare>("preparingState") {
    override fun onEnter(param: Prepare) {
      super.onEnter(param)
      observerDispatcher.onStateChange(BeggarPlayerState.PreparingState)

      // 同步prepare完毕，直接发送prepare完成事件
      if (param.isSync) {
        playerLogic.prepareSync()
        sendEvent(Prepared())
      } else {
        /**
         * 异步prepare完成是在播放器的回调中
         * @see IBeggarPlayerLogic.IPlayerCallback.onPrepared
         */
        playerLogic.prepareAsync()
      }
    }

    override fun onExit() {
      super.onExit()
    }
  }

  // 准备完成
  protected val preparedState = object : State<Prepared>("preparedState") {
    override fun onEnter(param: Prepared) {
      super.onEnter(param)
      observerDispatcher.onStateChange(BeggarPlayerState.PreparedState)
    }

    override fun onExit() {
      super.onExit()
    }
  }

  // 开始播放
  protected val startedState = object : State<Start>("startedState") {
    override fun onEnter(param: Start) {
      super.onEnter(param)
      playerLogic.start()
      observerDispatcher.onStateChange(BeggarPlayerState.StartedState)
    }

    override fun onExit() {
      super.onExit()
    }
  }

  // 暂停
  protected val pausedState = object : State<Pause>("pausedState") {
    override fun onEnter(param: Pause) {
      super.onEnter(param)
      playerLogic.pause()
      observerDispatcher.onStateChange(BeggarPlayerState.PausedState)
    }

    override fun onExit() {
      super.onExit()
    }
  }

  // 停止
  protected val stoppedState = object : State<Stop>("stoppedState") {
    override fun onEnter(param: Stop) {
      super.onEnter(param)
      playerLogic.stop()
      observerDispatcher.onStateChange(BeggarPlayerState.StoppedState)
    }

    override fun onExit() {
      super.onExit()
    }
  }

  // 完成
  protected val completedState = object : State<Complete>("completedState") {
    override fun onEnter(param: Complete) {
      super.onEnter(param)
      observerDispatcher.onStateChange(BeggarPlayerState.CompletedState)
    }

    override fun onExit() {
      super.onExit()
    }
  }

  // 出错
  protected val errorState = object : State<Error>("errorState") {
    override fun onEnter(param: Error) {
      super.onEnter(param)
      observerDispatcher.onStateChange(BeggarPlayerState.ErrorState)
    }

    override fun onExit() {
      super.onExit()
    }
  }

  // 结束(release后)
  protected val endState = object : State<End>("endState") {
    override fun onEnter(param: End) {
      super.onEnter(param)
      playerLogic.release()
      observerDispatcher.onStateChange(BeggarPlayerState.EndState)
    }

    override fun onExit() {
      super.onExit()
    }
  }

  // ********************* 状态 *********************

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
    stateMachine.sendEvent(event)
  }

  // ********************* 生命周期相关 *********************
  override fun reset() {
    sendEvent(Reset())
  }

  override fun setDataSource(dataSource: BeggarPlayerDataSource) {
    sendEvent(SetDataSource(dataSource))
  }

  override fun prepareSync() {
    sendEvent(Prepare(true))
  }

  override fun prepareAsync() {
    sendEvent(Prepare(false))
  }

  // 子类在异步prepare完成时调用
  protected fun onPreparedByAsync() {
    sendEvent(Prepared())
  }

  override fun start() {
    sendEvent(Start())
  }

  override fun pause() {
    sendEvent(Pause())
  }

  override fun stop() {
    sendEvent(Stop())
  }

  override fun release() {
    sendEvent(End())
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