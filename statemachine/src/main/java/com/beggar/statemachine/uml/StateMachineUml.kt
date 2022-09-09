package com.beggar.statemachine.uml

import com.beggar.statemachine.SyncStateMachine
import com.beggar.statemachine.child.ChildStateMachineState
import com.beggar.statemachine.child.ChildStateMachineStateNode

/**
 * author: BeggarLan
 * created on: 2022/9/8 8:40 下午
 * description: 将状态机输出为 plant uml
 * [plant uml 语法](https://plantuml.com/zh/state-diagram)
 */
fun SyncStateMachine.toUml(): String {
  val body = StringBuilder()
  // start
  body.append("@startuml").appendLine()

  // skin
  body.append("skinparam backgroundColor LightYellow\n" +
      "skinparam state {\n" +
      "  StartColor MediumBlue\n" +
      "  EndColor Red\n" +
      "  BackgroundColor Peru\n" +
      "  BackgroundColor<<Warning>> Olive\n" +
      "  BorderColor Gray\n" +
      "  FontName Impact\n" +
      "}")
    .appendLine()

  // 状态机关系图
  body.append(getStateUml(this))
  // end
  body.append("@enduml").appendLine()
  return body.toString()
}

/**
 * 获取状态机关系图
 */
private fun SyncStateMachine.getStateUml(stateMachine: SyncStateMachine): String {
  val body = StringBuilder()
  // initState
  body.append("[*]").append("-->").append(stateMachine.initialState.state.name).appendLine()
  // 描述所有节点
  stateMachine.states.forEach {
    val state = it.state
    // 描述一个状态节点
    body.append("state ").append(state.name).append("{").appendLine()
    // 递归拼接子状态机
    if (state is ChildStateMachineState) {
      val childStateMachine = (state.stateNode as ChildStateMachineStateNode).childStateMachine
      body.append(getStateUml(childStateMachine))
    }
    body.append("}").appendLine()
  }

  // 描述节点之间的转化关系
  stateMachine.transitions.entries.forEach {
    val fromState = it.key
    // 从该节点出发所有的转化关系
    it.value.forEach {
      val toState = it.to
      // fromState-->toState : (event)
      body.append(fromState.name).append(" --> ").append(toState.name) // fromState-->toState
        .append(" : ").append("(" + it.eventType.simpleName + ")") // event
        .appendLine()
    }
  }

  return body.toString()
}
