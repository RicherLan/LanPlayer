package com.beggar.beggarplayer.core.controller

import com.beggar.beggarplayer.core.base.EventHub
import com.beggar.beggarplayer.core.player.BeggarPlayerState
import com.beggar.beggarplayer.core.player.IBeggarPlayerStateChangeListener

/**
 * author: BeggarLan
 * created on: 2022/8/30 7:31 下午
 * description: 播放器状态更改事件分发
 */
class BeggarPlayerStateChangeEventHub
  : EventHub<IBeggarPlayerStateChangeListener>(), IBeggarPlayerStateChangeListener {

  override fun onChange(oldState: BeggarPlayerState, newState: BeggarPlayerState) {
    for (listener in listeners) {
      listener.onChange(oldState, newState)
    }
    listeners.forEach { listener ->
      run {
        listener.onChange(oldState, newState)
      }
    }
  }

}