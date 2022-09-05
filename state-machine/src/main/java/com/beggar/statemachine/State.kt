package com.beggar.statemachine

/**
 * author: BeggarLan
 * created on: 2022/9/5 12:48 下午
 * description: 描述状态
 *
 * @param stateMachine 状态机
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
   */
  open fun sendEvent(event: Event) {

  }

  /**
   * 处理事件
   */
  open fun handleEvent(event: Event) {

  }

}