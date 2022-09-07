package com.beggar.beggarplayer.core.player

/**
 * author: BeggarLan
 * created on: 2022/9/7 5:54 下午
 * description: 播放器具体逻辑
 * 不需要关心状态流转，只做具体的逻辑即可
 */
interface IBeggarPlayerLogic : IBeggarPlayer {

  fun setPlayerCallback(callback: IPlayerCallback)

  // 播放器的一些回调
  interface IPlayerCallback {
    // prepare完成
    fun onPrepared()

    // 播放完成
    fun onCompletion()

    // 出错时
    // TODO: 参数补充
    fun onError()
  }
}