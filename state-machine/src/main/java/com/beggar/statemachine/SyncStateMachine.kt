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
  protected val initialState: State
) {

  companion object {
    private const val TAG = "SyncStateMachine"
  }

  // 当前状态
  var currentState: State? = null

  // 状态机是否已经stop
  var isStopped = false

  // 创建时所在的线程
  private val thread: Thread = Thread.currentThread()

  // 操作队列
  private val actionQueue = ActionQueue()

  /**
   * 启动状态机
   */
  abstract fun start()

  /**
   * 停止状态机
   */
  fun stop() {
    checkThread()
    if (isStopped) {
      return
    }
    /**
     * 加入到队列中
     * 1. 保证在状态退出之前，把队列前面的内容先执行掉
     * 2. 保证在onExit中postEvent(event会被加入到队列)，event可以在状态迁移完成后执行
     */
    addAction {
      isStopped = true
      currentState?.onExit()
      currentState = null
    }
  }

  /**
   * 从根状态机发送事件
   * 1. 当前状态机是根状态机：那么直接处理事件
   * 2. 当前状态机是子状态机：那么parent.sendEventFromRoot，这样最终交给根状态机处理
   */
  internal abstract fun sendEventFromRoot(event: Event)

  /**
   * 当前有事件正在处理的话，那么新事件会加入到队列尾部
   * @return null表示事件入队列排队了(现在正在执行其他事件的逻辑: 如某执行体中又addAction这种递归)
   * 通常发生在，在状态的onEnter或者onExit中调用了sendEvent()
   *
   * state structure:
   * A -> (sub state) B -> (sub state) C
   * B是A的子状态，C是B的子状态。这样当进入A的时候会进入子状态的初始状态，
   * 因此会有这样的调用顺序 A::onEnter -> B::onEnter -> C::onEnter。
   * 假设他们在onEnter的时候又发送了事件，如下：
   * A {
   *   A::onEnter {
   *     post event-1
   *
   *     B {
   *       B::onEnter {
   *         post event-2
   *
   *         C {
   *           C::onEnter {
   *            post event-3
   *           }
   *         }
   *
   *       }
   *     }
   *
   *    }
   *
   *   上述的post-event会被队列起来，当上一个状态迁移完成之后才会执行。而每一层状态机都有自己的队列。
   *   执行结果：
   *   execute event-1
   *   execute event-2
   *   execute event-3
   * }
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
  // TODO: 因为当前状态已经确定了，所以这里最完美的应该是直接操作最底层的状态去处理事件，无法处理的话在往上抛
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

    // 处理事件
    return currentState.handleEvent(event)
  }

  /**
   *  添加操作
   */
  protected fun addAction(action: () -> Unit) {
    actionQueue.addAction(action)
  }

  /**
   * 保证操作状态机的线程 必须是 创建状态机时的线程
   * 直接抛异常
   */
  protected fun checkThread() {
    check(thread == Thread.currentThread()) {
      TAG.plus(" only the original thread that created a stateMachine can operate.")
        .plus("original thread: " + thread)
        .plus(", current Thread: " + Thread.currentThread())
    }
  }

}