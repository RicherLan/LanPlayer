package com.beggar.statemachine

/**
 * author: BeggarLan
 * created on: 2022/9/6 3:20 下午
 * description: 状态节点
 * 把一些操作(比如和状态机对接)从State中拿出来，让state只关注状态本身
 *
 * @param state        关联的状态
 */
open class StateNode(
  val state: State,
) {

  // 当前节点所在的状态机(当前层的状态机)
  private lateinit var stateMachine: SyncStateMachine

  // 初始化，必须调用
  open fun init(machine: SyncStateMachine) {
    stateMachine = machine
  }

  // 进入状态
  open fun enter() {
    state.onEnter()
  }

  // 退出状态
  open fun exit() {
    state.onExit()
  }

  /**
   * 向状态机发送事件
   * 事件交给根层状态机分发
   */
  fun sendEventFromRoot(event: Event) {
    stateMachine.sendEventFromRoot(event)
  }

  /**
   * 处理事件
   * @return 表示是否处理了事件
   */
  open fun handleEvent(event: Event): Boolean {
    return state.handleEvent(event)
  }

}