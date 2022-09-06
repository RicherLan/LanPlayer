package com.beggar.statemachine.child

import com.beggar.statemachine.Event
import com.beggar.statemachine.State
import com.beggar.statemachine.SyncStateMachine
import com.beggar.statemachine.Transition
import com.beggar.statemachine.error.StateMachineException

/**
 * author: lanweihua
 * created on: 2022/9/5 7:43 下午
 * description: 子状态机(非第一层)
 */
class ChildSyncStateMachine(
  private val parent: SyncStateMachine,
  states: List<State>,
  transitions: Map<State, List<Transition>>,
  initialState: State
) : SyncStateMachine(states, transitions, initialState) {

  override fun start() {
    throw StateMachineException("ChildStateMachine can't call start()!")
  }

  override fun sendEventFromRoot(event: Event) {
    parent.sendEventFromRoot(event)
  }

}