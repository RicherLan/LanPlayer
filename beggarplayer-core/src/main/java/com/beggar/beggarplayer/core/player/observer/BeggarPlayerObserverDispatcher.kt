package com.beggar.beggarplayer.core.player.observer

import com.beggar.beggarplayer.core.player.BeggarPlayerState
import com.beggar.player.common.collection.observe.ObserverDispatcher

/**
 * author: BeggarLan
 * created on: 2022/9/7 8:35 下午
 * description:
 */
class BeggarPlayerObserverDispatcher : ObserverDispatcher<IBeggarPlayerStateObsever>(),
  IBeggarPlayerStateObsever {

  override fun onChange(newState: BeggarPlayerState) {
    dispatch { onChange(newState) }
  }
}