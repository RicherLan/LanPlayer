package com.beggar.beggarplayer.core.base

import android.os.Message
import com.google.common.collect.ImmutableTable


/**
 * author: BeggarLan
 * created on: 2022/8/30 8:57 下午
 * description: 状态机
 */
abstract class StateMachine<S : StateMachine.State> {

  // 状态图: A状态-->B状态 : 经过一系列中间状态节点
  private val stateGraph: ImmutableTable<S, S, List<S>>

  // 当前状态，构造函数传入是指定初始状态
  lateinit var currentState: S

  private var stateCallback: StateCallback<S>? = null

  init {
    stateGraph = getStateGraph()
  }

  /**
   * 设置初始状态, 在构造的时候设置
   */
  protected fun setInitialState(state: S) {
    currentState = state
  }

  /**
   * 状态图
   */
  protected abstract fun getStateGraph(): ImmutableTable<S, S, List<S>>

  fun transitionTo(targetState: S): Boolean {
    return transitionTo(targetState, null)
  }

  /**
   * 状态转移
   *
   * @param targetState 目标状态
   * @return {@code true} 到达目标状态成功
   */
  fun transitionTo(targetState: S, msg: Message?): Boolean {
    val states: List<S>? = stateGraph.get(currentState, targetState)
    // 无法到达
    if (states == null || states.isEmpty()) {
      return false
    }
    for (state in states) {
      state.preEnter()
      stateCallback?.onStatePreEnter(state, msg)
      currentState = state
      stateCallback?.onStateEntered(state)
      state.entered()
    }
    return true
  }

  fun setStateCallback(callback: StateCallback<S>) {
    stateCallback = callback
  }

  // 状态
  interface State {
    // 即将进入状态
    fun preEnter()

    // 进入状态
    fun entered()
  }

  // 状态监听
  interface StateCallback<S : State> {
    // 即将进入状态
    fun onStatePreEnter(state: S, msg: Message?)

    // 进入状态
    fun onStateEntered(state: S)
  }

}