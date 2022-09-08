package com.beggar.beggarplayer.core.statemachine

import com.beggar.beggarplayer.core.datasource.BeggarPlayerDataSource
import com.beggar.beggarplayer.core.statemachine.BeggarPlayerStateMachineManager.PlayerEvent.*
import com.beggar.statemachine.Event
import com.beggar.statemachine.State
import com.beggar.statemachine.SyncStateMachine

/**
 * author: BeggarLan
 * created on: 2022/9/8 12:49 下午
 * description: 播放器状态机管理
 */
class BeggarPlayerStateMachineManager {

  // 状态机
  private var stateMachine: SyncStateMachine

  private var stateObserver: IBeggarPlayerStateMachineObserver? = null

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

  // 设置监听
  fun setStateObserver(observer: IBeggarPlayerStateMachineObserver?) {
    stateObserver = observer
  }

  /**
   * 向状态机发送事件
   */
  fun sendEvent(event: Event) {
    stateMachine.sendEvent(event)
  }

  // ********************* 状态机事件 *********************
  class PlayerEvent {
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
  }
  // ********************* 状态机事件 *********************

  // ********************* 状态 *********************
  protected val idleState = object : State<Any>("IdleState") {
    override fun onEnter(param: Any) {
      super.onEnter(param)
      stateObserver?.onEnterIdle()
    }

    override fun onExit() {
      super.onExit()
    }
  }

  // 设置完数据源后
  protected val initializedState = object : State<SetDataSource>("initializedState") {
    override fun onEnter(param: SetDataSource) {
      super.onEnter(param)
      stateObserver?.onEnterInitialized()
    }

    override fun onExit() {
      super.onExit()
    }
  }

  // 准备中
  protected val preparingState = object : State<Prepare>("preparingState") {
    override fun onEnter(param: Prepare) {
      super.onEnter(param)
      stateObserver?.onEnterPreparing()
    }

    override fun onExit() {
      super.onExit()
    }
  }

  // 准备完成
  protected val preparedState = object : State<Prepared>("preparedState") {
    override fun onEnter(param: Prepared) {
      super.onEnter(param)
      stateObserver?.onEnterPrepared()
    }

    override fun onExit() {
      super.onExit()
    }
  }

  // 开始播放
  protected val startedState = object : State<Start>("startedState") {
    override fun onEnter(param: Start) {
      super.onEnter(param)
      stateObserver?.onEnterStarted()
    }

    override fun onExit() {
      super.onExit()
    }
  }

  // 暂停
  protected val pausedState = object : State<Pause>("pausedState") {
    override fun onEnter(param: Pause) {
      super.onEnter(param)
      stateObserver?.onEnterPaused()
    }

    override fun onExit() {
      super.onExit()
    }
  }

  // 停止
  protected val stoppedState = object : State<Stop>("stoppedState") {
    override fun onEnter(param: Stop) {
      super.onEnter(param)
      stateObserver?.onEnterStopped()
    }

    override fun onExit() {
      super.onExit()
    }
  }

  // 完成
  protected val completedState = object : State<Complete>("completedState") {
    override fun onEnter(param: Complete) {
      super.onEnter(param)
      stateObserver?.onEnterCompleted()
    }

    override fun onExit() {
      super.onExit()
    }
  }

  // 出错
  protected val errorState = object : State<Error>("errorState") {
    override fun onEnter(param: Error) {
      super.onEnter(param)
      stateObserver?.onEnterError()
    }

    override fun onExit() {
      super.onExit()
    }
  }

  // 结束(release后)
  protected val endState = object : State<End>("endState") {
    override fun onEnter(param: End) {
      super.onEnter(param)
      stateObserver?.onEnterEnd()
    }

    override fun onExit() {
      super.onExit()
    }
  }
  // ********************* 状态 *********************

}