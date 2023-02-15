package com.beggar.player.common

import android.app.Activity
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.graphics.Paint
import android.view.View

/**
 * author: lanweihua
 * created on: 2023/2/15 4:30 PM
 * description: 页面置灰
 */
object GaryUtil {

//  描述一个颜色的时候，会有如下几个数据：色相、饱和度、亮度/明度。
//
//  色相：色彩的基本属性，就是平常所说的颜色名称，如红色、黄色等。
//  饱和度：是指色彩的纯度，越高色彩越纯，低则逐渐变灰，取值0-100%。
//  亮度/明度：色彩的明亮程度，一般情况下颜色加白色亮就越来越高，加黑色则越来越暗，取值0-100%。

  private val garyPaint: Paint by lazy {
    Paint().apply {
      val garyColorMatrix = ColorMatrix()
      // 设置饱和度为0
      garyColorMatrix.setSaturation(0f)
      colorFilter = ColorMatrixColorFilter(garyColorMatrix)
    }
  }

  private val unGaryPaint: Paint by lazy {
    Paint().apply {
      val garyColorMatrix = ColorMatrix()
      garyColorMatrix.setSaturation(1f)
      colorFilter = ColorMatrixColorFilter(garyColorMatrix)
    }
  }

  fun garyActivity(activity: Activity) {
    garyView(activity.window.decorView)
  }

  fun garyView(view: View) {
    view.setLayerType(View.LAYER_TYPE_HARDWARE, garyPaint)
  }

  fun unGaryView(view: View) {
    view.setLayerType(View.LAYER_TYPE_HARDWARE, unGaryPaint)
  }

}