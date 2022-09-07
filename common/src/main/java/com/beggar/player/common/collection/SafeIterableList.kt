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

  companion object {
    private const val TAG = "SafeIterableList"
  }

  // 原数据
  // arrayList
  private var list = mutableListOf<T>()

  // copy
  private val modifiableList: MutableList<T>
    get() {
      if (isNewIterator) {
        list = list.toMutableList()
        version++
        isNewIterator = false
      }

      return list
    }

  /**
   * list每copy一次版本就 +1
   */
  private var version = 0

  /***
   * 使用了迭代器但是还没有进行list的copy 为true
   * 创建迭代器的时候 设置为true
   * 在list进行copy后会置false
   */
  private var isNewIterator = false

  override fun iterator(): MutableIterator<T> {
    return object : MutableIterator<T> {
      private val iterator = list.iterator()

      init {
        // 使用迭代器的时候更新值
        isNewIterator = true
      }

      override fun hasNext(): Boolean {
        val hasNext = iterator.hasNext()
        if (!hasNext) {

        }
        return hasNext
      }

      override fun next(): T {
        return iterator.next()
      }

      override fun remove() {
        throw IllegalAccessError(TAG + "[iterator not support remove]")
      }
    }
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