package com.beggar.beggarplayer.core.player

import android.os.Message
import androidx.annotation.IntDef
import com.beggar.beggarplayer.core.player.data.BeggarPlayerDataSource
import com.beggar.beggarplayer.core.player.listener.IBeggarPlayerStateChangeListener
import com.beggar.beggarplayer.core.player.statemachine.BeggarPlayerState
import com.beggar.beggarplayer.core.player.statemachine.PlayerState
import com.beggar.statemachine.Event
import com.beggar.statemachine.State
import com.beggar.statemachine.SyncStateMachine

/**
 * author: BeggarLan
 * created on: 2022/8/30 8:51 下午
 * description: 播放器基类
 * 1. 状态流转封装
 */
abstract class BeggarBasePlayer : IBeggarPlayer {

  companion object {
    private const val TAG = "BeggarBasePlayer"
  }

  // 进入Prepared状态的方式
  @IntDef(
    PreparedStateWay.preparedStateWaySync,
    PreparedStateWay.preparedStateWayAsync
  )
  @Retention(AnnotationRetention.SOURCE)
  annotation class PreparedStateWay {
    companion object {
      const val preparedStateWaySync = 1 // 同步
      const val preparedStateWayAsync = 2 // 异步
    }
  }

  // 状态机
  private lateinit var stateMachine

  // 状态更改监听
  private var stateChangeListener: IBeggarPlayerStateChangeListener? = null

  init {
    buildStateMachine()
  }

  // ********************* 状态机事件 *********************
  class SetDataSource(dataSource: BeggarPlayerDataSource) : Event
  class PrepareSync : Event
  class PrepareAsync : Event
  class Start : Event
  class Pause : Event
  class Stop : Event
  class Complete : Event
  class Error : Event
  class End : Event
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
  protected val initializedState = object : State<Any>("IdleState") {
    override fun onEnter(param: Any) {
      super.onEnter(param)
    }

    override fun onExit() {
      super.onExit()
    }
  }

  // 准备中
  protected val preparingState = object : State("IdleState") {
    override fun onEnter() {
      super.onEnter()
    }

    override fun onExit() {
      super.onExit()
    }
  }

  // 准备完成
  protected val preparedState = object : State("IdleState") {
    override fun onEnter() {
      super.onEnter()
    }

    override fun onExit() {
      super.onExit()
    }
  }

  // 开始播放
  protected val startedState = object : State("IdleState") {
    override fun onEnter() {
      super.onEnter()
    }

    override fun onExit() {
      super.onExit()
    }
  }

  // 暂停
  protected val pausedState = object : State("IdleState") {
    override fun onEnter() {
      super.onEnter()
    }

    override fun onExit() {
      super.onExit()
    }
  }

  // 停止
  protected val stoppedState = object : State("IdleState") {
    override fun onEnter() {
      super.onEnter()
    }

    override fun onExit() {
      super.onExit()
    }
  }

  // 完成
  protected val completedState = object : State("IdleState") {
    override fun onEnter() {
      super.onEnter()
    }

    override fun onExit() {
      super.onExit()
    }
  }

  // 出错
  protected val errorState = object : State("IdleState") {
    override fun onEnter() {
      super.onEnter()
    }

    override fun onExit() {
      super.onExit()
    }
  }

  // 结束(release后)
  protected val endState = object : State("IdleState") {
    override fun onEnter() {
      super.onEnter()
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

  // ********************* 生命周期相关 *********************
  override fun setDataSource(dataSource: BeggarPlayerDataSource) {
    val message = Message.obtain()
    message.obj = dataSource
    stateMachine.transitionTo(BeggarPlayerState.InitializedState, message)
  }

  override fun prepare() {
    val message = Message.obtain()
    message.what = PreparedStateWay.preparedStateWaySync
    stateMachine.transitionTo(BeggarPlayerState.PreparedState, message)
  }

  override fun prepareAsync() {
    stateMachine.transitionTo(BeggarPlayerState.PreparingState)
  }

  override fun start() {
    stateMachine.transitionTo(BeggarPlayerState.StartedState)
  }

  override fun pause() {
    stateMachine.transitionTo(BeggarPlayerState.PausedState)
  }

  override fun stop() {
    stateMachine.transitionTo(BeggarPlayerState.StoppedState)
  }

  override fun reset() {
    stateMachine.transitionTo(BeggarPlayerState.IdleState)
  }

  override fun release() {
    stateMachine.transitionTo(BeggarPlayerState.EndState)
  }

  // 子类在异步prepare完成时调用
  protected fun onPreparedByAsync() {
    val message = Message.obtain()
    message.what = PreparedStateWay.preparedStateWayAsync
    stateMachine.transitionTo(BeggarPlayerState.PreparedState)
  }

  // 子类在完成时调用
  protected fun ooCompleted() {
    stateMachine.transitionTo(BeggarPlayerState.CompletedState)
  }

  // 子类在出错时调用
  protected fun ooError() {
    stateMachine.transitionTo(BeggarPlayerState.ErrorState)
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