package com.beggar.beggarplayer.core.statemachine

/**
 * author: BeggarLan
 * created on: 2022/9/8 1:01 下午
 * description: 播放器状态机监听
 */
interface IBeggarPlayerStateMachineObserver {
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