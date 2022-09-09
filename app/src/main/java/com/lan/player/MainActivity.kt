package com.lan.player

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.beggar.beggarplayer.core.BeggarPlayerController
import com.beggar.beggarplayer.core.config.BeggarPlayerConfig
import com.beggar.beggarplayer.core.view.BeggarPlayerTextureView

/**
 * author: lanweihua
 * created on: 2022/9/9 10:25 上午
 * description:
 */
class MainActivity : AppCompatActivity() {

  private lateinit var playerController: BeggarPlayerController

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)

    val textureView = findViewById<BeggarPlayerTextureView>(R.id.main_player_texture_view)
    initPlayer(textureView)
  }

  override fun onResume() {
    super.onResume()
  }

  private fun initPlayer(textureView: BeggarPlayerTextureView) {
    playerController = BeggarPlayerController(this, BeggarPlayerConfig(null))
    playerController.setTextureView(textureView)
  }

}