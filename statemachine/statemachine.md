# 组件设计

## 思想
普通状态机(一层)，复杂度为O(N2），当状态多的时候，很容易形成复杂的网状。</br>
HFSM是分层有限状态机。引入不同的层来降低复杂度，子状态机在上层看来只是一个节点而已，这样大大降级了复杂度</br>
状态只关心进入、退出，处理自己关心的事件，不关心从哪里来、到哪里去。transition判断并处理状态转移</br>


## 方案设计
![](https://github.com/BeggarLan/PicDb/blob/main/beggarStateMachine/StateMachineTest.png)
以上图为例，整个状态机包含abcd这四个状态，其中b状态内部又包含b1、b2、b3这3个状态。</br>
从最上层看，只能看到这4个状态，每个状态更复杂的逻辑(b内还有3个状态)做在状态机内部</br>
如果所有状态平铺开来，那么复杂度大大增加</br>
设计状态机的时候，应该把比较内聚的一些业务逻辑拆成单独的一层，其中每层都是一个普通的FSM(单层状态机)</br>

#### 状态迁移定义在同一层级

#### 状态同步处理

#### 递归事件

#### 声明式状态机

#### 可视化


## 概念
#### 状态
```kotlin
open class State<EnterParam>(
  val name: String,
) {

  /**
   * 状态进入时调用(该函数结束后currentState才会指向当前状态)
   * 做一些初始化的操作
   */
  open fun onEnter(param: EnterParam) {}

  /**
   * 状态退出时调用
   *
   * 该函数结束后currentState才会指向其他值：
   * 1.另一个状态(另一个状态onEnter())
   * 2.null(状态机stop)
   */
  open fun onExit() {}

  /**
   * 向状态机发送事件
   * 事件交给根层状态机分发
   */
  open fun sendEvent(event: Event) 

  /**
   * 处理事件
   * @return 表示是否处理了事件
   */
  open fun handleEvent(event: Event): Boolean = false

}

// 含有子状态的状态
open class ChildStateMachineState<EnterParam>(name: String) : State<EnterParam>(name) { }
```
状态只关心进入、退出，处理自己关心的事件，不关心从哪里来、到哪里去。transition判断并处理状态转移</br>
ChildStateMachineState是含有子状态的状态

#### 事件
```kotlin
interface Event
```
非枚举，不同的事件需要定义不同的类去描述，这样是为了更方便的传递数据</br>

#### 状态迁移
```kotlin
class Transition(
  val name: String,
  val from: State<*>,
  val to: State<*>,
  val eventType: Class<*>,
) 
```
描述状态之间的转换: fromState -> toState : Event </br>

## 类图


## 其他
