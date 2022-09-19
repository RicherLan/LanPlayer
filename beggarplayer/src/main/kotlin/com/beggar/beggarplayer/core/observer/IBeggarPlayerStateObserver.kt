package com.beggar.beggarplayer.core.observer

/**
 * author: BeggarLan
 * created on: 2022/9/8 1:01 下午
 * description: 播放器状态机监听
 */
interface IBeggarPlayerStateObserver {
  fun onEnterIdle() {}
  fun onEnterInitialized() {}
  fun onEnterPreparing() {}
  fun onEnterPrepared() {}
  fun onEnterStarted() {}
  fun onEnterPaused() {}
  fun onEnterStopped() {}
  fun onEnterCompleted() {}
  fun onEnterError() {}
  fun onEnterEnd() {}
}