package mazouri.kvcache.sample.example

import mazouri.kvcache.Call
import mazouri.kvcache.annotation.DEFAULT
import mazouri.kvcache.annotation.KEY

/**
 * @Description:
 * @Author: wangdongdong
 * @Email: wangdd_bj@163.com
 * @Github: https://github.com/mazouri
 * @CSDN: https://blog.csdn.net/dongdong230
 * @CreateDate: 2020/6/23 2:12 PM
 * @Version: 1.0
 */
interface KV {

    @KEY("save_String")
    @DEFAULT("")
    fun testKVCacheString(): Call<String>

    @KEY("save_int")
    @DEFAULT("0")
    fun testKVCacheInt(): Call<Int>

    @KEY("save_boolean")
    @DEFAULT("false")
    fun testKVCacheBoolean(): Call<Boolean>

    @KEY("save_float")
    @DEFAULT("0")
    fun testKVCacheFloat(): Call<Float>

    @KEY("save_long")
    @DEFAULT("0")
    fun testKVCacheLong(): Call<Long>
}