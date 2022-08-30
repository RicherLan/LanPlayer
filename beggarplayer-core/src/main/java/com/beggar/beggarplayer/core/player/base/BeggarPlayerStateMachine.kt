package com.beggar.beggarplayer.core.player.base

import com.google.common.collect.ImmutableTable


/**
 * author: BeggarLan
 * created on: 2022/8/30 8:57 下午
 * description: 播放器状态机
 * @param stateGraph   状态图: A状态-->B状态 : 经过一系列中间状态节点
 * @param currentState 当前状态，构造函数传入是指定初始状态
 */
class BeggarPlayerStateMachine(
  private val stateGraph: ImmutableTable<State, State, List<State>>,
  var currentState: State
) {

  /**
   * 状态转移
   *
   * @param targetState 目标状态
   * @return {@code true} 到达目标状态成功
   */
  fun transitionTo(targetState: State): Boolean {
    val states: List<State>? = stateGraph.get(currentState, targetState)
    // 无法到达
    if (states == null || states.isEmpty()) {
      return false
    }
    for (state in states) {
      state.preEnter()
      currentState = state
      state.entered()
    }
    return true
  }

  // 状态
  interface State {
    // 即将进入状态
    fun preEnter()

    // 进入状态
    fun entered()
  }

}