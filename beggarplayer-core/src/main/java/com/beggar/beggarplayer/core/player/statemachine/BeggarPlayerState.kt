package com.beggar.beggarplayer.core.player.statemachine

import android.os.Message
import androidx.annotation.CallSuper
import com.beggar.beggarplayer.core.base.BeggarPlayerLogger
import com.beggar.beggarplayer.core.base.StateMachine

/**
 * author: BeggarLan
 * created on: 2022/8/31 4:01 下午
 * description: 播放器状态
 */
sealed class BeggarPlayerState {
  object IdleState : PlayerState()
  object InitializedState : PlayerState() // 设置完数据源后
  object PreparingState : PlayerState()// 准备中
  object PreparedState : PlayerState()// 准备完成
  object StartedState : PlayerState()// 开始播放
  object PausedState : PlayerState() // 暂停
  object StoppedState : PlayerState()// 停止
  object CompletedState : PlayerState()// 完成
  object ErrorState : PlayerState()// 出错
  object EndState : PlayerState()// 结束(release后)
}

// 状态基类
open class PlayerState : StateMachine.State {
  companion object {
    private const val TAG = "PlayerState"
  }

  @CallSuper
  override fun preEnter() {
    BeggarPlayerLogger.log(TAG, this.javaClass.simpleName.plus(" preEnter"))
  }

  @CallSuper
  override fun entered() {
    BeggarPlayerLogger.log(TAG, this.javaClass.simpleName.plus(" entered"))
  }

  override fun processMsg(msg: Message) {
    trans
  }
}
