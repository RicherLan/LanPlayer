package com.beggar.beggarplayer.core

/**
 * author: BeggarLan
 * created on: 2022/9/7 8:30 下午
 * description: 播放器状态枚举
 */
@Retention(AnnotationRetention.SOURCE)
annotation class BeggarPlayerState {
  companion object {
    const val IdleState = 1
    const val InitializedState = 2
    const val PreparingState = 3
    const val PreparedState = 4
    const val StartedState = 5
    const val PausedState = 6
    const val StoppedState = 7
    const val CompletedState = 8
    const val ErrorState = 9
    const val EndState = 10
  }
}
