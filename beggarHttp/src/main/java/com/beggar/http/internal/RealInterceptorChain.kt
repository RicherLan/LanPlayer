package com.beggar.http.internal

import com.beggar.http.interceptor.Interceptor
import com.beggar.http.interceptor.InterceptorChain
import com.beggar.http.request.Request
import com.beggar.http.response.Response

/**
 * author: BeggarLan
 * created on: 2022/9/22 3:31 下午
 * description:
 *
 * @param interceptors 所有的拦截器
 * @param index        表示当前interceptor的索引
 * @param request      请求
 */
class RealInterceptorChain(
  private val interceptors: List<Interceptor>,
  private val index: Int,
  private val request: Request
) : InterceptorChain {

  // 调用proceed的次数
  private var callProceedNum = 0

  override fun proceed(request: Request): Response {
    // 索引超过列表大小了，直接抛异常
    check(index < interceptors.size) {
      "index exceed interceptors.size"
    }
    callProceedNum++

    val nextIndex = index + 1
    val nextChain = copy(index = nextIndex, request = request)
    val interceptor = interceptors[index]
    val response = interceptor.intercept(nextChain)

    // 每个拦截器只能调用一次proceed
    // nextIndex是下一个interceptor的索引，如果正好是最后一个拦截器，那么就不会调用proceed，因此不判断自后一个拦截器
    check(nextIndex >= interceptors.size || nextChain.callProceedNum == 1) {
      "interceptor $interceptor must call proceed() exactly once"
    }

    return response
  }

  private fun copy(index: Int, request: Request): RealInterceptorChain {
    return RealInterceptorChain(interceptors, index, request)
  }

}