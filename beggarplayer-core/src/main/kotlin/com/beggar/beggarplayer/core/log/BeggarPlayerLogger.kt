package com.beggar.beggarplayer.core.log

import android.util.Log

/**
 * author: BeggarLan
 * created on: 2022/8/30 8:45 下午
 * description: log
 */
class BeggarPlayerLogger {

  companion object {
    private const val TAG = "BeggarPlayer"

    fun log(desc: String) {
      Log.i(TAG, desc)
    }

    fun log(tag: String, desc: String) {
      Log.i(TAG.plus("-").plus(tag), desc)
    }
  }

}