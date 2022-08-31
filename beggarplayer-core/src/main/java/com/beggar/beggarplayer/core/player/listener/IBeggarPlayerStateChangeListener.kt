package com.beggar.beggarplayer.core.player.listener

import com.beggar.beggarplayer.core.player.statemachine.BeggarPlayerState
import com.beggar.beggarplayer.core.player.statemachine.PlayerState

/**
 * author: BeggarLan
 * created on: 2022/8/30 5:42 下午
 * description: 播放器状态更改监听
 */
interface IBeggarPlayerStateChangeListener {

  /**
   * 播放器状态更改
   * @param newState 新状态
   * @see BeggarPlayerState 所有状态枚举
   */
  fun onChange(newState: PlayerState)

}