package com.lan.player

import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.beggar.beggarplayer.core.BeggarPlayerController
import com.beggar.beggarplayer.core.config.BeggarPlayerConfig
import com.beggar.beggarplayer.core.datasource.BeggarPlayerDataSource
import com.beggar.beggarplayer.core.observer.IBeggarPlayerStateObserver
import com.beggar.beggarplayer.core.view.BeggarPlayerTextureView
import com.beggar.player.common.GaryUtil

/**
 * author: BeggarLan
 * created on: 2022/9/9 10:25 上午
 * description:
 */
class MainActivity : AppCompatActivity() {

  private lateinit var playerController: BeggarPlayerController

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    // 页面置灰
    GaryUtil.garyActivity(this)

    setContentView(R.layout.activity_main)

    val textureView = findViewById<BeggarPlayerTextureView>(R.id.main_player_texture_view)
    initPlayer(textureView)
  }

  override fun onResume() {
    super.onResume()
    // 这个网络链接的编码不支持
//    val uri = Uri.parse("http://vfx.mtime.cn/Video/2018/07/06/mp4/180706094003288023.mp4")
    val uri = Uri.parse("android.resource://" + getPackageName() + "/"+ R.raw.raw_video)
    val dataSource = BeggarPlayerDataSource(uri)
    playerController.setDataSource(dataSource)
//    playerController.prepareSync()
    playerController.prepareAsync()
  }

  override fun onDestroy() {
    super.onDestroy()
    playerController.release()
  }

  private fun initPlayer(textureView: BeggarPlayerTextureView) {
    playerController = BeggarPlayerController(this, BeggarPlayerConfig(null))
    playerController.setTextureView(textureView)
    playerController.registerObserver(object : IBeggarPlayerStateObserver {
      override fun onEnterPrepared() {
        super.onEnterPrepared()
        playerController.start()
      }
    })
  }

}