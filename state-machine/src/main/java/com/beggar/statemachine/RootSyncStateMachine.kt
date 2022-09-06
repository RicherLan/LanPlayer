package com.beggar.statemachine

/**
 * author: lanweihua
 * created on: 2022/9/5 7:41 下午
 * description: 第一层的状态机
 */
class RootSyncStateMachine(
  name: String,
  states: List<State>,
  transitions: Map<State, List<Transition>>,
  initialState: State
) : SyncStateMachine(name, states, transitions, initialState) {

  override fun start() {
    checkThread()
    // 已经stop了
    check(isStopped) {
      "stateMachine has stopped!"
    }

    // 加入到队列中: 保证在onEnter中postEvent(event会被加入到队列)，event可以在状态迁移完成后执行
    addAction {
      val state = initialState
      // 先回调onEnter，在更新当前状态
      state.onEnter()
      currentState = state
    }
  }

  override fun sendEventFromRoot(event: Event) {
    sendEvent(event)
  }

}