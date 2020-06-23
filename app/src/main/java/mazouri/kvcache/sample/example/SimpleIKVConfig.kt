package mazouri.kvcache.sample.example

import mazouri.kvcache.service.IKVConfig

/**
 * @Description:
 * @Author: wangdongdong
 * @Email: wangdd_bj@163.com
 * @Github: https://github.com/mazouri
 * @CSDN: https://blog.csdn.net/dongdong230
 * @CreateDate: 2020/6/23 6:04 PM
 * @Version: 1.0
 */
class SimpleIKVConfig: IKVConfig {
    private val hash = LinkedHashMap<String, Any>()

    override fun hasKey(key: String?): Boolean {
        return hash.containsKey(key)
    }

    override fun getLongValue(key: String?, defValue: Long): Long {
        return hash[key] as Long
    }

    override fun getBooleanValue(key: String?, defValue: Boolean): Boolean {
        return hash[key] as Boolean
    }

    override fun getIntValue(key: String?, defValue: Int): Int {
        return hash[key] as Int
    }

    override fun getFloatValue(key: String?, defValue: Float): Float {
        return hash[key] as Float
    }

    override fun getStringValue(key: String?, defValue: String?): String? {
        return hash[key] as String
    }

    override fun setBooleanValue(key: String?, value: Boolean, isSync: Boolean) {
        hash[key!!] = value
    }

    override fun setLongValue(key: String?, value: Long, isSync: Boolean) {
        hash[key!!] = value
    }

    override fun setIntValue(key: String?, value: Int, isSync: Boolean) {
        hash[key!!] = value
    }

    override fun setFloatValue(key: String?, value: Float, isSync: Boolean) {
        hash[key!!] = value
    }

    override fun setStringValue(key: String?, value: String?, isSync: Boolean) {
        hash[key!!] = value!!
    }
}