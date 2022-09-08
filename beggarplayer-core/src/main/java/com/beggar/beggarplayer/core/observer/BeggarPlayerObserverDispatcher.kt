package com.beggar.beggarplayer.core.observer

import com.beggar.beggarplayer.core.BeggarPlayerState
import com.beggar.player.common.collection.observe.ObserverDispatcher

/**
 * author: BeggarLan
 * created on: 2022/9/7 8:35 下午
 * description: 播放器事件分发
 */
class BeggarPlayerObserverDispatcher : ObserverDispatcher<IBeggarPlayerObserver>(),
  IBeggarPlayerObserver {

  // 当前状态
  var currentState = BeggarPlayerState.IdleState

  override fun onStateChange(@BeggarPlayerState newState: Int) {
    currentState = newState
    dispatch { onStateChange(newState) }
  }
}