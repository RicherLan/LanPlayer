package com.beggar.beggarplayer.core.controller.config

import com.beggar.beggarplayer.core.player.IBeggarPlayer

/**
 * author: lanweihua
 * created on: 2022/9/1 11:46 上午
 * description: 播放器控制类配置
 * @param player 允许使用方传入播放器实例，如果null那么内部采用默认播放器
 */
data class BeggarPlayerControllerConfig(
  val player: IBeggarPlayer?
)
