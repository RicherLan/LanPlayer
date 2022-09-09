package com.beggar.statemachine

import java.util.*

/**
 * author: BeggarLan
 * created on: 2022/9/5 5:52 下午
 * description: 操作队列
 * 当一个action中，执行了addAction时(递归)，后面的action先不会执行(入队列)
 */
class ActionQueue {

  // 队列
  private val actionQueue = LinkedList<() -> Unit>()

  // 是否正在执行action
  private var isActionExecuting = false

  /**
   * 操作入队
   * isActionExecuting: 当一个action中，执行了addAction时(递归)，后面的action先不会执行(入队列)
   */
  fun addAction(action: () -> Unit) {
    if (!isActionExecuting) {
      execute(action)
      drainActionQueue()
    } else {
      actionQueue.add(action)
    }
  }

  /**
   * 执行
   */
  private fun execute(action: () -> Unit) {
    try {
      isActionExecuting = true
      action()
    } finally {
      isActionExecuting = false
    }
  }

  /**
   * 顺序执行队列中的所有任务
   */
  private fun drainActionQueue() {
    while (!actionQueue.isEmpty()) {
      execute(actionQueue.removeFirst())
    }
  }

}