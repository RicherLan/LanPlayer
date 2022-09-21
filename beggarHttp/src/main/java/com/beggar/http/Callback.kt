package com.beggar.http

import com.beggar.http.call.Call
import com.beggar.http.response.Response
import java.io.IOException

/**
 * author: lanweihua
 * created on: 2022/9/21 12:50 下午
 * description: 请求结果
 */
interface Callback {

  /**
   * 远端server成功返回http请求的回执时
   */
  fun onResponse(call: Call, response: Response)

  /**
   * 失败
   */
  fun onFile(call: Call, error: IOException)

}