package com.beggar.beggarplayer.core.controller

import com.beggar.beggarplayer.core.base.EventHub
import com.beggar.beggarplayer.core.player.observer.IBeggarPlayerObserver
import com.beggar.beggarplayer.core.player.statemachine.PlayerState

/**
 * author: BeggarLan
 * created on: 2022/8/30 7:31 下午
 * description: 播放器状态更改事件分发
 */
class BeggarPlayerStateChangeEventHub
  : EventHub<IBeggarPlayerObserver>(), IBeggarPlayerObserver {

  override fun onStateChange(newState: PlayerState) {
    for (listener in listeners) {
      listener.onStateChange(newState)
    }
  }

}