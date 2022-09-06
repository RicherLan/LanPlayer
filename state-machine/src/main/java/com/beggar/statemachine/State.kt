package com.beggar.statemachine

/**
 * author: BeggarLan
 * created on: 2022/9/5 12:48 下午
 * description: 描述状态
 */
open class State<EnterParam>(
  val name: String,
) {

  // 关联的状态节点，内部封装了一些操作(主要是和状态机对接)
  internal lateinit var stateNode: StateNode

  /**
   * 状态进入时调用
   */
  open fun onEnter(param: EnterParam) {}

  /**
   * 状态退出时调用
   */
  open fun onExit() {}

  /**
   * 向状态机发送事件
   * 事件交给根层状态机分发
   */
  open fun sendEvent(event: Event) {
    stateNode.sendEventFromRoot(event)
  }

  /**
   * 处理事件
   * @return 表示是否处理了事件
   */
  open fun handleEvent(event: Event): Boolean = false

}