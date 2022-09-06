package com.beggar.beggarplayer.core.player

import android.os.Message
import androidx.annotation.IntDef
import com.beggar.beggarplayer.core.base.StateMachine
import com.beggar.beggarplayer.core.player.data.BeggarPlayerDataSource
import com.beggar.beggarplayer.core.player.listener.IBeggarPlayerStateChangeListener
import com.beggar.beggarplayer.core.player.statemachine.BeggarPlayerState
import com.beggar.beggarplayer.core.player.statemachine.BeggarPlayerStateMachine
import com.beggar.beggarplayer.core.player.statemachine.PlayerState

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
  private var stateMachine = BeggarPlayerStateMachine()

  // 状态更改监听
  private var stateChangeListener: IBeggarPlayerStateChangeListener? = null

  init {
    stateMachine.setStateCallback(object : StateMachine.StateCallback<PlayerState> {
      override fun onStatePreEnter(state: PlayerState, msg: Message?) {
        when (state) {
          BeggarPlayerState.IdleState -> resetInner()
          BeggarPlayerState.InitializedState -> {
            setDataSourceInner(msg!!.obj as BeggarPlayerDataSource)
          }
          BeggarPlayerState.PreparingState -> prepareAsyncInner()
          BeggarPlayerState.PreparedState -> {
            when (msg!!.what) {
              // 同步的方式
              PreparedStateWay.preparedStateWaySync -> prepareInner()
              // 异步的方式去prepare，触发prepared是回调的形式，此时已经完成了逻辑，所以不需要额外处理
              PreparedStateWay.preparedStateWayAsync -> {}
            }
          }
          BeggarPlayerState.StartedState -> startInner()
          BeggarPlayerState.PausedState -> pauseInner()
          BeggarPlayerState.StoppedState -> stopInner()
          BeggarPlayerState.EndState -> releaseInner()
        }
      }

      override fun onStateEntered(state: PlayerState) {
        stateChangeListener?.onChange(state)
      }
    })
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