package com.beggar.beggarplayer.core.player.statemachine

import com.beggar.beggarplayer.core.base.StateMachine
import com.beggar.beggarplayer.core.player.statemachine.BeggarPlayerState.IdleState
import com.beggar.beggarplayer.core.player.statemachine.BeggarPlayerState.InitializedState
import com.google.common.collect.ImmutableList
import com.google.common.collect.ImmutableTable


/**
 * author: BeggarLan
 * created on: 2022/8/31 9:53 上午
 * description: 播放器状态机
 * <a href="https://developer.android.google.cn/reference/android/media/MediaPlayer#state-diagram">android播放器状态流转图</a>
 */
class BeggarPlayerStateMachine : StateMachine<PlayerState>() {

  companion object {
    private const val TAG = "BeggarPlayerStateMachine"
  }

  init {
    setInitialState(BeggarPlayerState.IdleState)
  }

  override fun getStateGraph(): ImmutableTable<PlayerState, PlayerState, List<PlayerState>> {
    // TODO: 补充完整
    return ImmutableTable.builder<PlayerState, PlayerState, List<PlayerState>>()
      .put(IdleState, InitializedState, ImmutableList.of(InitializedState))
      .build()
  }

}