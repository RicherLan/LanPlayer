package com.beggar.statemachine.root

import com.beggar.statemachine.Event
import com.beggar.statemachine.InitialEvent
import com.beggar.statemachine.State
import com.beggar.statemachine.StateNode
import com.beggar.statemachine.SyncStateMachine
import com.beggar.statemachine.Transition

/**
 * author: BeggarLan
 * created on: 2022/9/5 7:41 下午
 * description: 第一层的状态机
 */
class RootSyncStateMachine(
  states: List<StateNode>,
  transitions: Map<State<*>, List<Transition>>,
  initialState: StateNode
) : SyncStateMachine(states, transitions, initialState) {

  override fun start() {
    checkThread()
    // 已经stop了
    check(!isStopped) {
      "stateMachine has stopped!"
    }

    // 加入到队列中: 保证在onEnter中postEvent(event会被加入到队列)，event可以在状态迁移完成后执行
    addAction {
      enter(InitialEvent())
    }
  }

  override fun sendEventFromRoot(event: Event) {
    sendEvent(event)
  }

}