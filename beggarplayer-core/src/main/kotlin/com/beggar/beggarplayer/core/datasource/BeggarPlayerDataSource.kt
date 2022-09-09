package com.beggar.beggarplayer.core.datasource

import android.content.Context
import android.net.Uri

/**
 * author: BeggarLan
 * created on: 2022/8/31 4:25 下午
 * description: 播放器数据源
 */
data class BeggarPlayerDataSource(
  val context: Context,
  val uri: Uri
)