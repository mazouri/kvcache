package mazouri.kvcache.service

/**
 * @Description: 存储key-value数据的接口类，客户端通过实现该接口实现自己的存储方式
 * @Author: wangdongdong
 * @Email: wangdd_bj@163.com
 * @Github: https://github.com/mazouri
 * @CSDN: https://blog.csdn.net/dongdong230
 * @CreateDate: 2020/6/23 12:40 PM
 * @Version: 1.0
 */
interface IKVConfig {
    fun hasKey(key: String?): Boolean

    fun getLongValue(key: String?, defValue: Long): Long

    fun getBooleanValue(key: String?, defValue: Boolean): Boolean

    fun getIntValue(key: String?, defValue: Int): Int

    fun getFloatValue(key: String?, defValue: Float): Float

    fun getStringValue(key: String?, defValue: String?): String?

    fun setBooleanValue(
        key: String?,
        value: Boolean,
        isSync: Boolean
    )

    fun setLongValue(
        key: String?,
        value: Long,
        isSync: Boolean
    )

    fun setIntValue(
        key: String?,
        value: Int,
        isSync: Boolean)

    fun setFloatValue(
        key: String?,
        value: Float,
        isSync: Boolean
    )

    fun setStringValue(
        key: String?,
        value: String?,
        isSync: Boolean
    )
}