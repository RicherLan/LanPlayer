package com.beggar.beggarplayer.core.player

/**
 * author: BeggarLan
 * created on: 2022/9/7 8:30 下午
 * description: 播放器状态枚举
 */
@Retention(AnnotationRetention.SOURCE)
annotation class BeggarPlayerState {
  companion object {
    const val idleState = 1
    const val initializedState = 2
    const val preparingState = 3
    const val preparedState = 4
    const val startedState = 5
    const val pausedState = 6
    const val stoppedState = 7
    const val completedState = 8
    const val errorState = 9
    const val endState = 10
  }
}
