package mazouri.kvcache

/**
 * @Description:
 * @Author: wangdongdong
 * @Email: wangdd_bj@163.com
 * @Github: https://github.com/mazouri
 * @CSDN: https://blog.csdn.net/dongdong230
 * @CreateDate: 2020/6/23 11:30 AM
 * @Version: 1.0
 */
interface Call<T> {

    fun get(): T?

    fun put(value: T)

    fun setKey(key: String): Call<T>
}