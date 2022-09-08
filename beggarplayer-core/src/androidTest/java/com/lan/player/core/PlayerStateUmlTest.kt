package com.lan.player.core

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.beggar.beggarplayer.core.BeggarPlayerController
import com.beggar.beggarplayer.core.config.BeggarPlayerConfig
import com.beggar.statemachine.uml.toUml
import org.junit.Test
import org.junit.runner.RunWith

/**
 * author: BeggarLan
 * created on: 2022/9/8 9:14 下午
 * description:
 */
@RunWith(AndroidJUnit4::class)
class PlayerStateUmlTest {
  @Test
  fun testUml() {
    val playerController = BeggarPlayerController(BeggarPlayerConfig(null))
    println("PlayerStateUmlTest" + playerController.coreManager.stateMachine.toUml())
  }
}