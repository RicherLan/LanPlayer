package com.beggar.beggarplayer.core.player.observer

import com.beggar.beggarplayer.core.player.BeggarPlayerState

/**
 * author: BeggarLan
 * created on: 2022/8/30 5:42 下午
 * description: 播放器监听
 */
interface IBeggarPlayerObserver {

  /**
   * 播放器状态更改
   * @param newState 新状态
   * @see BeggarPlayerState 所有状态枚举
   */
  fun onStateChange(@BeggarPlayerState newState: Int)

}