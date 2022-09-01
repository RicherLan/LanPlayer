package com.beggar.beggarplayer.core.player

import android.os.Message
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
          BeggarPlayerState.PreparedState -> prepareInner()
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
    stateMachine.transitionTo(BeggarPlayerState.PreparedState)
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

  // 子类在完成时调用
  protected fun transitionToCompleted() {
    stateMachine.transitionTo(BeggarPlayerState.CompletedState)
  }

  // 子类在出错时调用
  protected fun transitionToError() {
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