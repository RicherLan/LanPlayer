package com.beggar.statemachine

/**
 * author: BeggarLan
 * created on: 2022/9/5 1:03 下午
 * description: 状态机事件
 */
interface Event

// 状态机启动的事件
class InitialEvent : Event

// 状态机stop的事件
class StopEvent : Event