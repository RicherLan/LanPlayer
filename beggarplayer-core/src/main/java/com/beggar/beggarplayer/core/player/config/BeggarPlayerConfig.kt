package com.beggar.beggarplayer.core.player.config

import com.beggar.beggarplayer.core.player.IBeggarPlayerLogic

/**
 * author: BeggarLan
 * created on: 2022/9/7 6:00 下午
 * description: 播放器配置
 */
data class BeggarPlayerConfig(
  /**
   * 允许替换logic, null的话内部会用默认的logic实现
   */
  val playerLogic: IBeggarPlayerLogic?
)