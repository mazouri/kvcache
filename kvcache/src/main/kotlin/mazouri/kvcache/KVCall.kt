package mazouri.kvcache

import android.text.TextUtils
import mazouri.kvcache.service.KVConfigManager

/**
 * @Description: 通过该类调用存储方法
 * @Author: wangdongdong
 * @Email: wangdd_bj@163.com
 * @Github: https://github.com/mazouri
 * @CSDN: https://blog.csdn.net/dongdong230
 * @CreateDate: 2020/6/23 11:30 AM
 * @Version: 1.0
 */
internal class KVCall<T> constructor(private val serviceMethod: KVMethod<*>): Call<T> {

    private var key = ""

    override fun get(): T? {
        val key = serviceMethod.key + key
        val defaultValue = serviceMethod.default
        val cls = serviceMethod.typeClass

        try {
            when (cls) {
                java.lang.Integer::class.java -> {
                    return getIntValue(key, if(TextUtils.isEmpty(defaultValue)) 0 else defaultValue.toInt()) as T
                }
                java.lang.Float::class.java -> {
                    return getFloatValue(key, if(TextUtils.isEmpty(defaultValue)) 0F else defaultValue.toFloat()) as T
                }
                java.lang.Boolean::class.java -> {
                    return getBooleanValue(key, if(TextUtils.isEmpty(defaultValue)) false else defaultValue.toBoolean()) as T
                }
                java.lang.Long::class.java -> {
                    return getLongValue(key, if(TextUtils.isEmpty(defaultValue)) 0L else defaultValue.toLong()) as T
                }
                java.lang.String::class.java -> {
                    return getStringValue(key, if(TextUtils.isEmpty(defaultValue)) "" else defaultValue) as T
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    override fun put(value: T) {
        val key = serviceMethod.key + key
        val cls = serviceMethod.typeClass
        val isSync = serviceMethod.isSync

        try {
            when (cls) {
                java.lang.Integer::class.java -> {
                    setIntValue(key, value as Int, isSync)
                }
                java.lang.Float::class.java -> {
                    setFloatValue(key, value as Float, isSync)
                }
                java.lang.Boolean::class.java -> {
                    setBooleanValue(key, value as Boolean, isSync)
                }
                java.lang.Long::class.java -> {
                    setLongValue(key, value as Long, isSync)
                }
                java.lang.String::class.java -> {
                    setStringValue(key, value as String, isSync)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun setKey(key: String): Call<T> {
        this.key = key
        return this
    }

    private fun getIntValue(key: String, defaultValue: Int): Int {
        return KVConfigManager.instance.getIntValue(key, defaultValue)
    }

    private fun getFloatValue(key: String, defaultValue: Float): Float {
        return KVConfigManager.instance.getFloatValue(key, defaultValue)
    }

    private fun getBooleanValue(key: String, defaultValue: Boolean): Boolean {
        return KVConfigManager.instance.getBooleanValue(key, defaultValue)
    }

    private fun getLongValue(key: String, defaultValue: Long): Long {
        return KVConfigManager.instance.getLongValue(key, defaultValue)
    }

    private fun getStringValue(key: String, defaultValue: String): String? {
        return KVConfigManager.instance.getStringValue(key, defaultValue)
    }

    private fun setIntValue(key: String, value: Int, isSync: Boolean) {
        KVConfigManager.instance.setIntValue(key, value, isSync)
    }

    private fun setFloatValue(key: String, value: Float, isSync: Boolean) {
        KVConfigManager.instance.setFloatValue(key, value, isSync)
    }

    private fun setBooleanValue(key: String, value: Boolean, isSync: Boolean) {
        KVConfigManager.instance.setBooleanValue(key, value, isSync)
    }

    private fun setLongValue(key: String, value: Long, isSync: Boolean) {
        KVConfigManager.instance.setLongValue(key, value, isSync)
    }

    private fun setStringValue(key: String, value: String, isSync: Boolean) {
        KVConfigManager.instance.setStringValue(key, value, isSync)
    }

}