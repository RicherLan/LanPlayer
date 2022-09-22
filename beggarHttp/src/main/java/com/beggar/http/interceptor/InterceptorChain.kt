package com.beggar.http.interceptor

import com.beggar.http.request.Request
import com.beggar.http.response.Response

/**
 * author: BeggarLan
 * created on: 2022/9/20 9:10 下午
 * description: 拦截器责任链
 */
interface InterceptorChain {

  fun proceed(request: Request): Response

}