package com.beggar.beggarplayer.core.player

import com.beggar.beggarplayer.core.player.data.BeggarPlayerDataSource
import com.beggar.beggarplayer.core.player.listener.IBeggarPlayerStateChangeListener
import com.beggar.statemachine.Event
import com.beggar.statemachine.State
import com.beggar.statemachine.SyncStateMachine

/**
 * author: BeggarLan
 * created on: 2022/8/30 8:51 下午
 * description: 播放器基类
 * 1. 状态机构建和维护
 * 2.
 */
abstract class BeggarBasePlayer : IBeggarPlayer {

  companion object {
    private const val TAG = "BeggarBasePlayer"
  }

  // 状态机
  private lateinit var stateMachine: SyncStateMachine

  // 状态更改监听
  private var stateChangeListener: IBeggarPlayerStateChangeListener? = null

  init {
    buildStateMachine()
  }

  // ********************* 状态机事件 *********************
  class Reset : Event // 进idle
  class SetDataSource(dataSource: BeggarPlayerDataSource) : Event // 设置数据源
  class PrepareSync : Event // 同步步prepare
  class PrepareAsync : Event // 异步prepare
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
    }

    override fun onExit() {
      super.onExit()
    }
  }

  // 设置完数据源后
  protected val initializedState = object : State<SetDataSource>("IdleState") {
    override fun onEnter(param: SetDataSource) {
      super.onEnter(param)
    }

    override fun onExit() {
      super.onExit()
    }
  }

  // 准备中
  protected val preparingState = object : State<PrepareAsync>("IdleState") {
    override fun onEnter(param: PrepareAsync) {
      super.onEnter(param)
    }

    override fun onExit() {
      super.onExit()
    }
  }

  // 准备完成
  protected val preparedState = object : State<Prepared>("IdleState") {
    override fun onEnter(param: Prepared) {
      super.onEnter(param)
    }

    override fun onExit() {
      super.onExit()
    }
  }

  // 开始播放
  protected val startedState = object : State<Start>("IdleState") {
    override fun onEnter(param: Start) {
      super.onEnter(param)
    }

    override fun onExit() {
      super.onExit()
    }
  }

  // 暂停
  protected val pausedState = object : State<Pause>("IdleState") {
    override fun onEnter(param: Pause) {
      super.onEnter(param)
    }

    override fun onExit() {
      super.onExit()
    }
  }

  // 停止
  protected val stoppedState = object : State<Stop>("IdleState") {
    override fun onEnter(param: Stop) {
      super.onEnter(param)
    }

    override fun onExit() {
      super.onExit()
    }
  }

  // 完成
  protected val completedState = object : State<Complete>("IdleState") {
    override fun onEnter(param: Complete) {
      super.onEnter(param)
    }

    override fun onExit() {
      super.onExit()
    }
  }

  // 出错
  protected val errorState = object : State<Error>("IdleState") {
    override fun onEnter(param: Error) {
      super.onEnter(param)
    }

    override fun onExit() {
      super.onExit()
    }
  }

  // 结束(release后)
  protected val endState = object : State<End>("IdleState") {
    override fun onEnter(param: End) {
      super.onEnter(param)
    }

    override fun onExit() {
      super.onExit()
    }
  }

  // ********************* 状态 *********************


  /**
   * 构造状态机
   */
  private fun buildStateMachine() {
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

    stateMachine = builder.build()
  }

  override fun setStateListener(listener: IBeggarPlayerStateChangeListener?) {
    stateChangeListener = listener
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
    sendEvent(PrepareSync())
  }

  override fun prepareAsync() {
    sendEvent(PrepareAsync())
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

  // 子类在完成时调用
  protected fun ooCompleted() {
    sendEvent(Complete())
  }

  // 子类在出错时调用
  protected fun onError() {
    sendEvent(Error())
  }

  override fun release() {
    sendEvent(End())
  }

  // ********************* 生命周期相关 *********************


  // ********************* 子类实现，驱动生命周期的相关方法 *********************
  protected abstract fun setDataSourceInner(dataSource: BeggarPlayerDataSource)

  protected abstract fun prepareInner()

  protected abstract fun prepareAsyncInner()

  protected abstract fun startInner()

  protected abstract fun pauseInner()

  protected abstract fun stopInner()

  protected abstract fun resetInner()

  protected abstract fun releaseInner()
  // ********************* 子类实现，生命驱动生命周期的相关方法 *********************
}