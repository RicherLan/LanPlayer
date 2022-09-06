package com.beggar.statemachine

/**
 * author: BeggarLan
 * created on: 2022/9/5 12:48 下午
 * description: 描述状态
 *
 * @param stateMachine 当前节点所在的状态机(当前层的状态机)
 */
open class State(
  val name: String,
  private val stateMachine: SyncStateMachine
) {

  /**
   * 状态进入时调用
   */
  open fun onEnter() {}

  /**
   * 状态退出时调用
   */
  open fun onExit() {}

  /**
   * 向状态机发送事件
   * 事件交给根层状态机分发
   */
  open fun sendEvent(event: Event) {
    stateMachine.sendEventFromRoot(event)
  }

  /**
   * 处理事件
   * @return 表示是否处理了事件
   */
  open fun handleEvent(event: Event): Boolean = false

}