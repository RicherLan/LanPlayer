package com.beggar.statemachine.child

import com.beggar.statemachine.State
import com.beggar.statemachine.StateNode
import com.beggar.statemachine.SyncStateMachine

/**
 * author: lanweihua
 * created on: 2022/9/6 4:32 下午
 * description: 子状态机状态节点
 *
 * @param stateMachine       当前节点所在的状态机(当前层的状态机)
 * @param childStateMachine  当前节点的子状态机
 */
class ChildStateMachineStateNode(
  state: State,
  stateMachine: SyncStateMachine,
  val childStateMachine: SyncStateMachine
) : StateNode(state, stateMachine) {

  private val childMachine = childStateMachine as ChildSyncStateMachine

  init {
    childMachine.parent = stateMachine
  }

  // 自己先enter，然后子状态机enter
  override fun enter() {
    super.enter()
    childMachine.enter()
  }

  // 子状态机先exit，然后自己在exit
  override fun exit() {
    childMachine.exit()
    super.exit()
  }

}