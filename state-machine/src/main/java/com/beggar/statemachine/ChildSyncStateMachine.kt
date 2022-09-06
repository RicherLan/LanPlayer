package com.beggar.statemachine

import com.beggar.statemachine.error.StateMachineException

/**
 * author: lanweihua
 * created on: 2022/9/5 7:43 下午
 * description: 子状态机(非第一层)
 */
class ChildSyncStateMachine(
  name: String,
  private val parent: SyncStateMachine,
  states: List<State>,
  transitions: Map<State, List<Transition>>,
  initialState: State
) : SyncStateMachine(name, states, transitions, initialState) {

  override fun start() {
    throw StateMachineException("ChildStateMachine can't call start()!")
  }

}