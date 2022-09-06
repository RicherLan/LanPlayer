package com.beggar.statemachine

/**
 * author: BeggarLan
 * created on: 2022/9/5 12:49 下午
 * description: 同步状态机
 * 1. 这里的同步: 操作状态机时检查当前线程 是否是 创建状态机所在的线程，并不是通过postMsg的方式去放在同一线程
 * 2. 调用方发送事件的时候，事件会先向子状态机分发，如果子状态机无法处理，那么调用自己的currentState.handleEvent
 *
 * @param states        所有的状态
 * @param transitions   描述状态图。key: 当前节点, value: 从当前节点出发的所有路径
 * @param initialState  初始状态
 */
abstract class SyncStateMachine(
  val name: String,
  private val states: List<State>,
  private val transitions: Map<State, List<Transition>>,
  private val initialState: State
) {

  companion object {
    private const val TAG = "SyncStateMachine"
  }

  // 当前状态
  var currentState: State? = null

  // 状态机是否已经stop
  private var isStopped = false

  // 创建时所在的线程
  private val thread: Thread = Thread.currentThread()

  // 操作队列
  private val actionQueue = ActionQueue()

  /**
   * 启动状态机
   */
  fun start() {
    checkThread()
    if (isStopped) {
      throw IllegalStateException(TAG.plus("[stateMachine has stopped!]"))
    }
    val state = initialState
    // 先回调onEnter，在更新当前状态
    state.onEnter()
    currentState = state
  }

  /**
   * 停止状态机
   */
  fun stop() {
    checkThread()
    if (isStopped) {
      return
    }
    isStopped = true
    currentState?.onExit()
    currentState = null
  }

  /**
   * @return null表示事件入队列排队了(现在正在执行其他事件的逻辑)
   */
  fun sendEvent(event: Event): Boolean? {
    checkThread()
    var handled: Boolean? = null
    addAction {
      handled = sendEventDirect(event)
    }
    return handled
  }

  /*
   * 直接执行事件(不排队)
   * 事件会先向子状态机分发(因为"当前"状态肯定是最低层的叶子节点)，如果子状态机无法处理，那么调用自己的currentState.handleEvent
   */
  protected fun sendEventDirect(event: Event): Boolean {
    var currentState = currentState ?: throw IllegalStateException(
      TAG.plus("[stateMachine has already stopped")
    )
    // 如果当前状态含有子状态机，那么把事件向内部传递
    if (currentState is ChildStateMachineState) {
      var childHandled = currentState.childStateMachine.sendEventDirect(event)
      if (childHandled) {
        return true
      }
    }

    // 状态转换
    transitions[currentState]?.forEach { transition ->
      currentState.onExit()
      val toState = transition.to
      toState.onEnter()
      currentState = toState
      return true
    }

    return currentState.handleEvent(event)
  }

  /**
   *  添加操作
   */
  private fun addAction(action: () -> Unit) {
    actionQueue.addAction(action)
  }

  /**
   * 保证操作状态机的线程 必须是 创建状态机时的线程
   * 直接抛异常
   */
  private fun checkThread() {
    check(thread == Thread.currentThread()) {
      TAG.plus(" only the original thread that created a stateMachine can operate.")
        .plus("original thread: " + thread)
        .plus(", current Thread: " + Thread.currentThread())
    }
  }

}