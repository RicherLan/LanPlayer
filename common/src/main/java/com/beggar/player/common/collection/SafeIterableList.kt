package com.beggar.player.common.collection

/**
 * author: BeggarLan
 * created on: 2022/9/7 11:37 上午
 * description: 允许在迭代的过程中add、remove元素
 * 思想是CopyOnWrite，但是仅仅在迭代的时候【写操作】才copy
 *
 * 非线程安全的list
 */
class SafeIterableList<T> : MutableCollection<T> {

  // 原数据
  // arrayList
  private val list = mutableListOf<T>()

  // copy
  private val modifiableList: MutableList<T>
    get() {

    }

  /**
   * list每copy一次版本就 +1
   */
  private var verison = 0

  /***
   * 当前版本用了多少迭代器
   * 使用迭代器的时候 +1
   *
   */
  private var iteratorCount = 0

  override fun iterator(): MutableIterator<T> {
    TODO("Not yet implemented")
  }

  override val size: Int
    get() = list.size

  override fun contains(element: T): Boolean {
    return list.contains(element)
  }

  override fun containsAll(elements: Collection<T>): Boolean {
    return list.containsAll(elements)
  }

  override fun isEmpty(): Boolean {
    return list.isEmpty()
  }

  // ***************************** 结构性操作用copy的list *********************
  override fun add(element: T): Boolean {
    return modifiableList.add(element)
  }

  override fun addAll(elements: Collection<T>): Boolean {
    return modifiableList.addAll(elements)
  }

  override fun clear() {
    modifiableList.clear()
  }

  override fun remove(element: T): Boolean {
    return modifiableList.remove(element)
  }

  override fun removeAll(elements: Collection<T>): Boolean {
    return modifiableList.removeAll(elements)
  }

  override fun retainAll(elements: Collection<T>): Boolean {
    return modifiableList.retainAll(elements)
  }
  // ***************************** 结构性操作用copy的list *********************

}