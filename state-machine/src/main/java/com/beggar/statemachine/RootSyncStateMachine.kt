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


}