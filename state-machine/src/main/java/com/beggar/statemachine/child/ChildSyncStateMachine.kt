package com.beggar.statemachine.child

import com.beggar.statemachine.Event
import com.beggar.statemachine.State
import com.beggar.statemachine.StateNode
import com.beggar.statemachine.SyncStateMachine
import com.beggar.statemachine.Transition
import com.beggar.statemachine.error.StateMachineException

/**
 * author: BeggarLan
 * created on: 2022/9/5 7:43 下午
 * description: 子状态机(非第一层)
 */
class ChildSyncStateMachine(
  states: List<StateNode>,
  transitions: Map<State, List<Transition>>,
  initialState: StateNode
) : SyncStateMachine(states, transitions, initialState) {

  lateinit var parent: SyncStateMachine

  override fun start() {
    throw StateMachineException("ChildStateMachine can't call start()!")
  }

  override fun sendEventFromRoot(event: Event) {
    parent.sendEventFromRoot(event)
  }

}