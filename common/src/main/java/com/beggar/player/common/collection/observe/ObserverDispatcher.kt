package com.beggar.player.common.collection.observe

import com.beggar.player.common.collection.SafeIterableList

/**
 * author: BeggarLan
 * created on: 2022/9/7 5:11 下午
 * 1. 观察者管理(注册、解注册)
 * 2. 分发回调
 */
open class ObserverDispatcher<Observer> {

  private val observers = SafeIterableList<Observer>()

  fun registerObserver(observer: Observer) {
    observers.add(observer)
  }

  fun unregisterObserver(observer: Observer) {
    observers.remove(observer)
  }

  protected fun dispatch(callback: Observer.() -> Unit) {
    observers.forEach {
      it.callback()
    }
  }

}