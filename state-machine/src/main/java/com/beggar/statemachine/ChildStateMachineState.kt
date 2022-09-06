package com.beggar.statemachine

/**
 * author: lanweihua
 * created on: 2022/9/5 8:57 下午
 * description: 含有子状态机的状态
 *
 * @param stateMachine      当前节点所在的状态机(当前层的状态机)
 * @param childStateMachine 当前节点的子状态机
 */
class ChildStateMachineState(
  name: String,
  private val stateMachine: SyncStateMachine,
  val childStateMachine: ChildSyncStateMachine
) : State(name, stateMachine) {

}