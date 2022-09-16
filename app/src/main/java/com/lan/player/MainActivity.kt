package com.lan.player

import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.beggar.beggarplayer.core.BeggarPlayerController
import com.beggar.beggarplayer.core.config.BeggarPlayerConfig
import com.beggar.beggarplayer.core.datasource.BeggarPlayerDataSource
import com.beggar.beggarplayer.core.view.BeggarPlayerTextureView

/**
 * author: BeggarLan
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
    val dataSource = BeggarPlayerDataSource(
      Uri.parse("http://vfx.mtime.cn/Video/2018/07/06/mp4/180706094003288023.mp4")
    )
    playerController.setDataSource(dataSource)
    playerController.prepareSync()
//    playerController.prepareAsync()
    playerController.start()
  }

  override fun onDestroy() {
    super.onDestroy()
    playerController.release()
  }

  private fun initPlayer(textureView: BeggarPlayerTextureView) {
    playerController = BeggarPlayerController(this, BeggarPlayerConfig(null))
    playerController.setTextureView(textureView)
  }

}