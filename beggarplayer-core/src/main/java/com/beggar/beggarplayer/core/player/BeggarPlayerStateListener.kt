package com.beggar.beggarplayer.core.player

/**
 * author: lanweihua
 * created on: 2022/8/30 5:42 下午
 * description: 播放器状态监听
 */
interface BeggarPlayerStateListener {

  /**
   * 播放器状态更改
   * @param oldState 原状态
   * @param newState 新状态
   */
  fun onChange(oldState: BeggarPlayerState, newState: BeggarPlayerState)

}