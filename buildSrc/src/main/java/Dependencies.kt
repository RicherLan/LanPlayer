/**
 * author: BeggarLan
 * created on: 2022/9/7 10:31 上午
 * description: 依赖管理
 */
object Versions {
  val retrofit = "2.3.0"
  val rxjava = "2.1.9"
}

object Libs {
  val retrofit = "com.squareup.retrofit2:retrofit:${Versions.retrofit}"
  val retrofit_rxjava_adapter = "com.squareup.retrofit2:adapter-rxjava2:${Versions.retrofit}"
  val rxjava = "io.reactivex.rxjava2:rxjava:${Versions.rxjava}"
}