package com.beggar.statemachine

/**
 * author: BeggarLan
 * created on: 2022/9/5 12:49 下午
 * description: 同步状态机
 * 这里的同步: 操作状态机时检查当前线程 是否是 创建状态机所在的线程，并不是通过postMsg的方式去放在同一线程
 * @param states        所有的状态
 * @param transitions   描述状态图。key: 当前节点, value: 从当前节点出发的所有路径
 * @param initialState  初始状态
 */
class SyncStateMachine private constructor(
  val name: String,
  private val states: List<State>,
  private val transitions: Map<State, List<Transition>>,
  private val initialState: State
) {

  companion object {
    private const val TAG = "SyncStateMachine"
  }

  // 创建时所在的线程
  private val thread: Thread = Thread.currentThread()


  /**
   * 保证操作状态机的线程 必须是 创建状态机时的线程
   * 直接抛异常
   */
  private fun checkThread() {
    check(thread == Thread.currentThread()) {
      TAG.plus(" only the original thread that created a stateMachine can operate.")
        .plus("original thread: " + thread)
        .plus(", current Thread: " + Thread.currentThread())
    }
  }

}