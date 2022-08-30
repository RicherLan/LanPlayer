package com.beggar.beggarplayer.core.player.listener

import com.beggar.beggarplayer.core.player.BeggarPlayerState

/**
 * author: BeggarLan
 * created on: 2022/8/30 5:42 下午
 * description: 播放器状态更改监听
 */
interface IBeggarPlayerStateChangeListener {

  /**
   * 播放器状态更改
   * @param oldState 原状态
   * @param newState 新状态
   */
  fun onChange(oldState: BeggarPlayerState, newState: BeggarPlayerState)

}