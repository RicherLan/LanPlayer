package com.beggar.player.common.collection

/**
 * author: BeggarLan
 * created on: 2022/9/7 11:37 上午
 * description: 允许在迭代的过程中add、remove元素
 * 思想是CopyOnWrite，但是仅仅在迭代的时候【写操作】才copy
 * 核心实现：调用方获取迭代器的时候，返回的是当前list的iterator, 当进行增删操作时，list = list.copy，这样iterator是旧list的
 *
 * 非线程安全的list
 */
class SafeIterableList<T> : MutableCollection<T> {

  // 只要copy过一次，该list会同步更新为新的
  private var list = mutableListOf<T>()

  // copy-list
  private val modifiableList: MutableList<T>
    get() {
      // 不为0表示：有正在使用迭代器的 && list没有copy过
      if (iteratorCount != 0) {
        list = list.toMutableList()
        version++
        // 做个flag
        iteratorCount = 0
      }

      return list
    }

  /**
   * list每copy一次版本就 +1
   */
  private var version = 0

  /***
   * 使用了迭代器但是还没有进行list的copy 为true
   * 创建迭代器的时候 +1
   * 在list进行copy后 置为0
   */
  private var iteratorCount = 0

  override fun iterator(): MutableIterator<T> {
    return object : MutableIterator<T> {
      /**
       * 这里是核心，如果有增删的话，这里的iterator指向的还是之前的那个, 集合的快失败异常会在迭代器的next()方法中触发(不仅仅next())
       */
      private val iterator = list.iterator()
      private var myVersion = version

      init {
        // 使用迭代器的时候更新值
        iteratorCount++
      }

      override fun hasNext(): Boolean {
        val hasNext = iterator.hasNext()
        if (!hasNext) {
          // 如果版本不一致了，说明在该iterator创建后，list被copy了(copy的时候会把iteratorCount reset)
          if (version == myVersion && myVersion != -1) {
            iteratorCount--
            // 标记已经处理过
            myVersion = -1
          }
        }
        return hasNext
      }

      override fun next(): T {
        return iterator.next()
      }

      override fun remove() {
        throw UnsupportedOperationException("iterator not support remove")
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