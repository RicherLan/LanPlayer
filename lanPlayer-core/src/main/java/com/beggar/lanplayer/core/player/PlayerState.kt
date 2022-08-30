package com.beggar.lanplayer.core.player

/**
 * author: lanweihua
 * created on: 2022/8/30 10:20 上午
 * description: 播放器状态，值越大表示状态越往后
 * <a href="https://developer.android.google.cn/reference/android/media/MediaPlayer#state-diagram">android播放器状态流转图</a>
 */
enum class PlayerState(val value: Int) {

  IDLE(1 shl 1),
  INITIALIZED(1 shl 2), // 设置完数据源后
  PREPARING(1 shl 3), // 准备中
  PREPARED(1 shl 4), // 准备完成
  STARTED(1 shl 5), // 开始播放
  PAUSED(1 shl 6), // 暂停
  STOPPED(1 shl 7), // 停止
  COMPLETED(1 shl 8), // 完成
  ERROR(1 shl 9), // 出错
  END(1 shl 10), // 结束(release后)

}