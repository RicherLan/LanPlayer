package com.beggar.http.interceptor

import com.beggar.http.response.Response

/**
 * author: BeggarLan
 * created on: 2022/9/20 8:08 下午
 * description: 拦截器
 * 1. 请求发起时拦截
 * 2. 回执到达后拦截
 */
interface Interceptor {

  fun intercept(chain: InterceptorChain): Response

}