package com.beggar.beggarplayer.core.observer

import com.beggar.beggarplayer.core.BeggarPlayerState
import com.beggar.beggarplayer.core.statemachine.IBeggarPlayerStateMachineObserver
import com.beggar.player.common.collection.observe.ObserverDispatcher

/**
 * author: BeggarLan
 * created on: 2022/9/7 8:35 下午
 * description: 播放器事件分发
 */
class BeggarPlayerObserverDispatcher : ObserverDispatcher<IBeggarPlayerStateMachineObserver>(),
  IBeggarPlayerStateMachineObserver {

  // 当前状态
  @BeggarPlayerState
  var currentState = BeggarPlayerState.IdleState

  override fun onEnterIdle() {
    currentState = BeggarPlayerState.IdleState
    dispatch { onEnterIdle() }
  }

  override fun onEnterInitialized() {
    currentState = BeggarPlayerState.InitializedState
    dispatch { onEnterInitialized() }
  }

  override fun onEnterPreparing() {
    currentState = BeggarPlayerState.PreparingState
    dispatch { onEnterPreparing() }
  }

  override fun onEnterPrepared() {
    currentState = BeggarPlayerState.PreparedState
    dispatch { onEnterPrepared() }
  }

  override fun onEnterStarted() {
    currentState = BeggarPlayerState.StartedState
    dispatch { onEnterStarted() }
  }

  override fun onEnterPaused() {
    currentState = BeggarPlayerState.PausedState
    dispatch { onEnterPaused() }
  }

  override fun onEnterStopped() {
    currentState = BeggarPlayerState.StoppedState
    dispatch { onEnterStopped() }
  }

  override fun onEnterCompleted() {
    currentState = BeggarPlayerState.CompletedState
    dispatch { onEnterCompleted() }
  }

  override fun onEnterError() {
    currentState = BeggarPlayerState.ErrorState
    dispatch { onEnterError() }
  }

  override fun onEnterEnd() {
    currentState = BeggarPlayerState.EndState
    dispatch { onEnterEnd() }
  }

}