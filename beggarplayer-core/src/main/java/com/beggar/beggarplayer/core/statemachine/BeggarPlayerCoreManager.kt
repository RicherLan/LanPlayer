package com.beggar.beggarplayer.core.statemachine

import com.beggar.beggarplayer.core.datasource.BeggarPlayerDataSource
import com.beggar.beggarplayer.core.observer.BeggarPlayerObserverDispatcher
import com.beggar.beggarplayer.core.observer.IBeggarPlayerStateObserver
import com.beggar.beggarplayer.core.player.IBeggarPlayerLogic
import com.beggar.beggarplayer.core.statemachine.BeggarPlayerCoreManager.PlayerEvent.*
import com.beggar.statemachine.Event
import com.beggar.statemachine.State
import com.beggar.statemachine.SyncStateMachine

/**
 * author: BeggarLan
 * created on: 2022/9/8 12:49 下午
 * description: 播放器核心逻辑处理
 *
 * 1. 状态机构建和维护
 * 2. 播放器核心逻辑处理，如开始、暂停等
 * 3. 提供播放器状态监听
 *
 *
 * 状态流转图：
 * @see <a href=>https://developer.android.google.cn/reference/android/media/MediaPlayer#state-diagram</a>
 *
 * @param playerLogic   播放器具体逻辑
 */
class BeggarPlayerCoreManager(
  val playerLogic: IBeggarPlayerLogic
) {

  // 状态机
  private var stateMachine: SyncStateMachine

  // 播放器事件分发
  private val observerDispatcher = BeggarPlayerObserverDispatcher()

  init {
    stateMachine = buildStateMachine()
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
      .transition()
    // TODO: transition

    return builder.build()
  }

  fun registerObserver(observer: IBeggarPlayerStateObserver) {
    observerDispatcher.registerObserver(observer)
  }

  fun unregisterObserver(observer: IBeggarPlayerStateObserver) {
    observerDispatcher.unregisterObserver(observer)
  }

  /**
   * 向状态机发送事件
   */
  internal fun sendEvent(event: Event) {
    stateMachine.sendEvent(event)
  }

  // ********************* 状态机事件 *********************
  class PlayerEvent {
    class Reset : Event // 进idle
    class SetDataSource(val dataSource: BeggarPlayerDataSource) : Event // 设置数据源
    class Prepare(val isSync: Boolean) : Event // 区分同步和异步
    class Prepared : Event // prepare完成(异步prepare在完成的时候发送该事件)
    class Start : Event // 开始播放
    class Pause : Event // 暂停播放
    class Stop : Event // 停止播放
    class SeekTo(timeMs: Long) : Event // 跳转
    class Complete : Event // 播放完毕
    class Error : Event // 出错
    class Release : Event // 释放，这里就结束播放器的生命了
  }
  // ********************* 状态机事件 *********************

  // ********************* 状态 *********************
  protected val idleState = object : State<Any>("IdleState") {
    override fun onEnter(param: Any) {
      super.onEnter(param)
      observerDispatcher.onEnterIdle()
    }

    override fun onExit() {
      super.onExit()
    }
  }

  // 设置完数据源后
  protected val initializedState = object : State<SetDataSource>("initializedState") {
    override fun onEnter(param: SetDataSource) {
      super.onEnter(param)
      observerDispatcher.onEnterInitialized()
    }

    override fun onExit() {
      super.onExit()
    }
  }

  // 准备中
  protected val preparingState = object : State<Prepare>("preparingState") {
    override fun onEnter(param: Prepare) {
      super.onEnter(param)
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
      observerDispatcher.onEnterPreparing()
    }

    override fun onExit() {
      super.onExit()
    }
  }

  // 准备完成
  protected val preparedState = object : State<Prepared>("preparedState") {
    override fun onEnter(param: Prepared) {
      super.onEnter(param)
      observerDispatcher.onEnterPrepared()
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
      observerDispatcher.onEnterStarted()
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
      observerDispatcher.onEnterPaused()
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
      observerDispatcher.onEnterStopped()
    }

    override fun onExit() {
      super.onExit()
    }
  }

  // 完成
  protected val completedState = object : State<Complete>("completedState") {
    override fun onEnter(param: Complete) {
      super.onEnter(param)
      observerDispatcher.onEnterCompleted()
    }

    override fun onExit() {
      super.onExit()
    }
  }

  // 出错
  protected val errorState = object : State<Error>("errorState") {
    override fun onEnter(param: Error) {
      super.onEnter(param)
      observerDispatcher.onEnterError()
    }

    override fun onExit() {
      super.onExit()
    }
  }

  // 结束(release后)
  protected val endState = object : State<Release>("endState") {
    override fun onEnter(param: Release) {
      super.onEnter(param)
      playerLogic.release()
      observerDispatcher.onEnterEnd()
    }

    override fun onExit() {
      super.onExit()
    }
  }
  // ********************* 状态 *********************

}