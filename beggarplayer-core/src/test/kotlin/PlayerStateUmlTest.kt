import com.beggar.beggarplayer.core.BeggarPlayerCoreManager
import com.beggar.statemachine.State
import com.beggar.statemachine.SyncStateMachine
import com.beggar.statemachine.child.ChildStateMachineState
import com.beggar.statemachine.uml.toUml
import org.junit.Test

/**
 * author: BeggarLan
 * created on: 2022/9/8 9:14 下午
 * description:
 */
class PlayerStateUmlTest {
  @Test
  fun testUml() {
    val stateMachine = buildStateMachine()
    println("PlayerStateUmlTest" + stateMachine.toUml())
    stateMachine.start()
  }

  /**
   * 构造状态机
   */

  private fun buildStateMachine(): SyncStateMachine {
    val aState = object : State<Any>("aState") {}

    val bState = object : ChildStateMachineState<Any>("bState") {}
    val bChildState1 = object : State<Any>("bChildState1") {}
    val bChildState2 = object : State<Any>("bChildState2") {}
    val bChildState3 = object : State<Any>("bChildState3") {}


    val cState = object : State<Any>("cState") {}
    val dState = object : State<Any>("dState") {}

    val builder = SyncStateMachine.Builder()
      .setInitialState(aState)
      .state(aState)
      .state(cState)
      .state(dState)
      .childStateMachine(
        bState,
        SyncStateMachine.ChildStateMachineBuilder()
          .setInitialState(bChildState1)
          .state(bChildState1)
          .state(bChildState2)
          .state(bChildState3)
          .transition(
            "eventb1",
            setOf(bChildState1),
            bChildState2,
            BeggarPlayerCoreManager.PlayerEvent.Reset::class.java
          )
          .transition(
            "eventb2",
            setOf(bChildState2),
            bChildState3,
            BeggarPlayerCoreManager.PlayerEvent.Reset::class.java
          )
          .transition(
            "eventb3",
            setOf(bChildState1),
            bChildState3,
            BeggarPlayerCoreManager.PlayerEvent.Reset::class.java
          )
      )
      .transition(
        "event1",
        setOf(aState),
        bState,
        BeggarPlayerCoreManager.PlayerEvent.Reset::class.java
      )
      .transition(
        "event2",
        setOf(bState),
        cState,
        BeggarPlayerCoreManager.PlayerEvent.Reset::class.java
      )
      .transition(
        "event3",
        setOf(bState, cState),
        dState,
        BeggarPlayerCoreManager.PlayerEvent.Reset::class.java
      )

    return builder.build()
  }

}