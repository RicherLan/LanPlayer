package com.beggar.beggarplayer.core.statemachine

import android.graphics.SurfaceTexture
import android.view.Surface
import android.view.TextureView
import com.beggar.beggarplayer.core.base.BaseManager
import com.beggar.beggarplayer.core.datasource.BeggarPlayerDataSource
import com.beggar.beggarplayer.core.log.BeggarPlayerLogger
import com.beggar.beggarplayer.core.observer.BeggarPlayerObserverDispatcher
import com.beggar.beggarplayer.core.observer.IBeggarPlayerStateObserver
import com.beggar.beggarplayer.core.player.IBeggarPlayerLogic
import com.beggar.beggarplayer.core.statemachine.BeggarPlayerCoreManager.PlayerEvent.*
import com.beggar.beggarplayer.core.view.BeggarPlayerTextureView
import com.beggar.statemachine.Event
import com.beggar.statemachine.State
import com.beggar.statemachine.SyncStateMachine
import com.beggar.statemachine.uml.toUml

/**
 * author: BeggarLan
 * created on: 2022/9/8 12:49 下午
 * description: 播放器核心逻辑处理
 *
 * 1. 状态机构建和维护
 * 2. 播放器核心逻辑处理，如开始、暂停等
 * 3. 提供播放器状态监听
 *
 *
 * 状态流转图：
 * <a href=https://developer.android.google.cn/reference/android/media/MediaPlayer#state-diagram></a>
 *
 * @param playerLogic   播放器具体逻辑
 */
class BeggarPlayerCoreManager(
  val playerLogic: IBeggarPlayerLogic
) : BaseManager {

  companion object {
    private const val TAG = "BeggarPlayerCoreManager"
  }

  // 状态机
  internal var stateMachine: SyncStateMachine

  // 音量
  // TODO: 默认值
  private var leftVolume = 0f
  private var rightVolume = 0f

  // 播放器进行画面渲染的view
  // TODO: 把surfaceTexture封装到textureView中，对外值提供surface
  private var textureView: BeggarPlayerTextureView? = null
  private var surfaceTexture: SurfaceTexture? = null

  // 播放器进行画面渲染的surface
  private var surface: Surface? = null

  // 监听SurfaceTexture
  private val surfaceTextureListener = object : TextureView.SurfaceTextureListener {
    override fun onSurfaceTextureAvailable(
      st: SurfaceTexture,
      width: Int,
      height: Int
    ) {
      if (surfaceTexture != st) {
        surfaceTexture = st
        surface = Surface(surfaceTexture)
        playerLogic.setSurface(surface)
      }
    }

    override fun onSurfaceTextureSizeChanged(surface: SurfaceTexture, width: Int, height: Int) {}

    override fun onSurfaceTextureDestroyed(st: SurfaceTexture): Boolean {
      surface = null
      surfaceTexture = null
      playerLogic.setSurface(null)
      return true
    }

    override fun onSurfaceTextureUpdated(surface: SurfaceTexture) {}
  }

  // 播放器事件分发
  private val observerDispatcher = BeggarPlayerObserverDispatcher()

  // ********************* 状态机事件 *********************
  class PlayerEvent {
    class Reset : Event // 进idle
    class SetDataSource(val dataSource: BeggarPlayerDataSource) : Event // 设置数据源
    class Prepare(val isSync: Boolean) : Event // 区分同步和异步
    class Prepared(val isSync: Boolean) : Event // prepare完成(异步prepare在完成的时候发送该事件)
    class Start : Event // 开始播放
    class Pause : Event // 暂停播放
    class Stop : Event // 停止播放
    class SeekTo(val timeMs: Long) : Event // 跳转
    class Complete : Event // 播放完毕
    class Error : Event // 出错
    class Release : Event // 释放，这里就结束播放器的生命了
  }
  // ********************* 状态机事件 *********************

  // ********************* 状态 *********************
  private val idleState = object : State<Any>("IdleState") {
    override fun onEnter(param: Any) {
      super.onEnter(param)
      observerDispatcher.onEnterIdle()
    }

    override fun onExit() {
      super.onExit()
    }
  }

  // 设置完数据源后
  private val initializedState = object : State<SetDataSource>("initializedState") {
    override fun onEnter(param: SetDataSource) {
      super.onEnter(param)
      // 设置数据源
      playerLogic.setDataSource(param.dataSource)
      observerDispatcher.onEnterInitialized()
    }

    override fun onExit() {
      super.onExit()
    }
  }

  // 准备中
  private val preparingState = object : State<Prepare>("preparingState") {
    override fun onEnter(param: Prepare) {
      super.onEnter(param)
      // 同步
      if (param.isSync) {
        playerLogic.prepareSync()
        // 准备完成
        sendEvent(Prepared(true))

      } else {
        // 异步，准备完成通过回调
        playerLogic.prepareAsync()
      }
      observerDispatcher.onEnterPreparing()
    }

    override fun onExit() {
      super.onExit()
    }
  }

  // 准备完成
  private val preparedState = object : State<Prepared>("preparedState") {
    override fun onEnter(param: Prepared) {
      super.onEnter(param)
      observerDispatcher.onEnterPrepared()
    }

    override fun onExit() {
      super.onExit()
    }

    override fun handleEvent(event: Event): Boolean {
      when (event) {
        is SeekTo -> {
          handleSeekEvent(event)
          return true
        }
      }
      return false
    }
  }

  // 开始播放
  private val startedState = object : State<Start>("startedState") {
    override fun onEnter(param: Start) {
      super.onEnter(param)
      playerLogic.start()
      observerDispatcher.onEnterStarted()
    }

    override fun onExit() {
      super.onExit()
    }

    override fun handleEvent(event: Event): Boolean {
      when (event) {
        is SeekTo -> {
          handleSeekEvent(event)
          return true
        }
      }
      return false
    }
  }

  // 暂停
  private val pausedState = object : State<Pause>("pausedState") {
    override fun onEnter(param: Pause) {
      super.onEnter(param)
      playerLogic.pause()
      observerDispatcher.onEnterPaused()
    }

    override fun onExit() {
      super.onExit()
    }

    override fun handleEvent(event: Event): Boolean {
      when (event) {
        is SeekTo -> {
          handleSeekEvent(event)
          return true
        }
      }
      return false
    }
  }

  // 停止
  private val stoppedState = object : State<Stop>("stoppedState") {
    override fun onEnter(param: Stop) {
      super.onEnter(param)
      playerLogic.stop()
      observerDispatcher.onEnterStopped()
    }

    override fun onExit() {
      super.onExit()
    }
  }

  // 完成
  private val completedState = object : State<Complete>("completedState") {
    override fun onEnter(param: Complete) {
      super.onEnter(param)
      observerDispatcher.onEnterCompleted()
    }

    override fun onExit() {
      super.onExit()
    }
  }

  // 出错
  private val errorState = object : State<Error>("errorState") {
    override fun onEnter(param: Error) {
      super.onEnter(param)
      observerDispatcher.onEnterError()
    }

    override fun onExit() {
      super.onExit()
    }
  }

  // 结束(release后)
  private val endState = object : State<Release>("endState") {
    override fun onEnter(param: Release) {
      super.onEnter(param)
      playerLogic.release()
      observerDispatcher.onEnterEnd()
    }

    override fun onExit() {
      super.onExit()
    }
  }
  // ********************* 状态 *********************

  init {
    stateMachine = buildStateMachine()
    BeggarPlayerLogger.log(TAG, "\n\n" + stateMachine.toUml())
    stateMachine.start()
    initPlayerLogic()
  }

  /**
   * 构造状态机
   */
  private fun buildStateMachine(): SyncStateMachine {
    val builder = SyncStateMachine.Builder()
      .setInitialState(idleState)
      .state(idleState)
      .state(initializedState)
      .state(preparingState)
      .state(preparedState)
      .state(startedState)
      .state(pausedState)
      .state(stoppedState)
      .state(completedState)
      .state(errorState)
      .state(endState)
      .transition(  // 除End状态外，任何状态  --reset--> idleState
        "reset",
        setOf(
          initializedState, preparingState, preparedState, startedState,
          pausedState, stoppedState, completedState, errorState
        ),
        idleState,
        Reset::class.java
      )
      .transition(  // 任何状态  --Release--> endState
        "release",
        setOf(
          idleState, initializedState, preparingState, preparedState, startedState,
          pausedState, stoppedState, completedState, errorState
        ),
        endState,
        Release::class.java
      )
      .transition(  // 除End状态外, 任何状态  --error--> errorState
        "error",
        setOf(
          idleState, initializedState, preparingState, preparedState, startedState,
          pausedState, stoppedState, completedState
        ),
        errorState,
        Error::class.java
      )
      .transition(  // 设置数据源后 --> initializedState
        "setDataSource",
        idleState,
        initializedState,
        SetDataSource::class.java
      )
      .transition(  // 同步或者异步准备 --> preparingState
        "Prepare",
        setOf(initializedState, stoppedState),
        preparingState,
        Prepare::class.java
      )
      .transition(  // 准备完成 --> preparedState
        "Prepared",
        setOf(initializedState, preparingState, stoppedState),
        preparedState,
        Prepared::class.java
      )
      .transition(  // 开始播放 --> startedState
        "Start",
        setOf(preparedState, pausedState, completedState),
        startedState,
        Start::class.java
      )
      .transition(  // 暂停播放 --> pausedState
        "Pause",
        setOf(startedState),
        pausedState,
        Pause::class.java
      )
      .transition(  // 停止播放 --> stoppedState
        "Stop",
        setOf(preparedState, startedState, pausedState, completedState),
        stoppedState,
        Stop::class.java
      )
      .transition(  // 完成播放 --> completedState
        "Complete",
        setOf(startedState),
        completedState,
        Complete::class.java
      )

    return builder.build()
  }

  // 初始化PlayerLogic
  private fun initPlayerLogic() {
    // 监听播放器的事件
    val callback = object : IBeggarPlayerLogic.IPlayerCallback {
      // 异步加载完成
      override fun onPrepared() {
        sendEvent(Prepared(false))
      }

      // 播放完成
      override fun onCompletion() {
        sendEvent(Complete())
      }

      // error
      override fun onError() {
        sendEvent(Error())
      }
    }
    playerLogic.setPlayerCallback(callback)
  }

  /**
   * 使用方要设置
   */
  fun setTextureView(view: BeggarPlayerTextureView) {
    if (textureView == view) {
      return
    }
    // 释放之前的
    releaseSurface()
    textureView = view
    textureView?.surfaceTextureListener = surfaceTextureListener

    surfaceTexture = textureView?.surfaceTexture
    surfaceTexture?.let {
      surface = Surface(surfaceTexture)
      playerLogic.setSurface(surface)
    }
  }

  fun registerObserver(observer: IBeggarPlayerStateObserver) {
    observerDispatcher.registerObserver(observer)
  }

  fun unregisterObserver(observer: IBeggarPlayerStateObserver) {
    observerDispatcher.unregisterObserver(observer)
  }

  /**
   * 向状态机发送事件
   */
  fun sendEvent(event: Event) {
    stateMachine.sendEvent(event)
  }

  /**
   * 设置音量
   */
  fun setVolume(lV: Float, rV: Float) {
    leftVolume = lV
    rightVolume = rV
    playerLogic.setVolume(leftVolume, rightVolume)
  }

  /**
   * 设置静音状态
   * @param isMute {@code true} 静音
   */
  fun setMuteStatus(isMute: Boolean) {
    if (isMute) {
      playerLogic.setVolume(0f, 0f)
    } else {
      setVolume(leftVolume, rightVolume)
    }
  }

  /**
   * 设置是否循环播放
   */
  fun setLoop(loop: Boolean) {
    playerLogic.setLoop(loop)
  }

  // ********************* 获得一些信息 *********************
  /**
   * 获得视频的宽度
   */
  fun getVideoWidth(): Int {
    return playerLogic.getVideoWidth()
  }

  /**
   * 获得视频的高度
   */
  fun getVideoHeight(): Int {
    return playerLogic.getVideoHeight()
  }

  // ********************* 获得一些信息 *********************

  /**
   * 释放manager
   */
  override fun release() {
    observerDispatcher.clear()
    sendEvent(Release())
    stateMachine.stop()
    releaseSurface()
  }

  /**
   *  处理SeekTo事件
   *  目前可以seekTo的状态有：
   *  @see preparedState
   *  @see startedState
   *  @see pausedState
   */
  private fun handleSeekEvent(event: SeekTo) {
    playerLogic.seekTo(event.timeMs)
  }

  // 释放surface
  private fun releaseSurface() {
    playerLogic.setSurface(null)
    surface?.release()
    surface = null
    surfaceTexture?.release()
    surfaceTexture = null
    textureView?.surfaceTextureListener = null
    textureView = null
  }

}