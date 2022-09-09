package com.lan.player

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.beggar.beggarplayer.core.BeggarPlayerController
import com.beggar.beggarplayer.core.config.BeggarPlayerConfig

/**
 * author: lanweihua
 * created on: 2022/9/9 10:25 上午
 * description:
 */
class MainActivity : AppCompatActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)
    val playerController = BeggarPlayerController(BeggarPlayerConfig(null))
  }
}