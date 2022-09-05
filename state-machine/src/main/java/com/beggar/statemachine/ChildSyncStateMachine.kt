package com.beggar.statemachine

/**
 * author: lanweihua
 * created on: 2022/9/5 7:43 下午
 * description: 子状态机(非第一层)
 */
class ChildSyncStateMachine(
  name: String,
  states: List<State>,
  transitions: Map<State, List<Transition>>,
  initialState: State
) : SyncStateMachine(name, states, transitions, initialState) {

}