package com.beggar.statemachine

/**
 * author: BeggarLan
 * created on: 2022/9/5 12:48 下午
 * description: 描述状态之间的转换
 * fromState -> toState : Event
 */
class Transition(
  val name: String,
  val from: State,
  val to: State,
  val eventType: Class<*>,
) {

}