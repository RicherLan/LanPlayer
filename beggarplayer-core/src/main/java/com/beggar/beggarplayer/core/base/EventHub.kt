package com.beggar.beggarplayer.core.base
import kotlin.collections.ArrayList

/**
 * author: BeggarLan
 * created on: 2022/8/30 7:26 下午
 * description: 监听器管理
 */
open class EventHub<Listener> {

  protected val listeners: ArrayList<Listener> = ArrayList()

  fun registerListener(listener: Listener) {
    listeners.add(listener)
  }

  fun unregisterListener(listener: Listener) {
    listeners.remove(listener)
  }

}