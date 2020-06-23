package mazouri.kvcache.service

import android.content.Context
import android.text.TextUtils
import mazouri.kvcache.KVCache
import java.lang.Exception

/**
 * @Description: 默认的存储方式，即使用SharedPreferences
 * @Author: wangdongdong
 * @Email: wangdd_bj@163.com
 * @Github: https://github.com/mazouri
 * @CSDN: https://blog.csdn.net/dongdong230
 * @CreateDate: 2020/6/23 12:45 PM
 * @Version: 1.0
 */
internal class KVPrefs: IKVConfig {

    companion object {
        var SP_DEFAULT_FILE_NAME = "kv_cache_shared_prefs"
            set(value) {
                if (!TextUtils.isEmpty(value)) field = value
            }
    }

    override fun hasKey(key: String?): Boolean {
        return KVCache.context!!.getSharedPreferences(SP_DEFAULT_FILE_NAME, Context.MODE_PRIVATE).contains(key)
    }

    override fun getLongValue(key: String?, defValue: Long): Long {
        return getPrefsValue(key, defValue) as Long
    }

    override fun getBooleanValue(key: String?, defValue: Boolean): Boolean {
        return getPrefsValue(key, defValue) as Boolean
    }

    override fun getIntValue(key: String?, defValue: Int): Int {
        return getPrefsValue(key, defValue) as Int
    }

    override fun getFloatValue(key: String?, defValue: Float): Float {
        return getPrefsValue(key, defValue) as Float
    }

    override fun getStringValue(key: String?, defValue: String?): String {
        return getPrefsValue(key, defValue) as String
    }

    override fun setBooleanValue(key: String?, value: Boolean, isSync: Boolean) {
        setPrefsValue(key, value, isSync)
    }

    override fun setLongValue(key: String?, value: Long, isSync: Boolean) {
        setPrefsValue(key, value, isSync)
    }

    override fun setIntValue(key: String?, value: Int, isSync: Boolean) {
        setPrefsValue(key, value, isSync)
    }

    override fun setFloatValue(key: String?, value: Float, isSync: Boolean) {
        setPrefsValue(key, value, isSync)
    }

    override fun setStringValue(key: String?, value: String?, isSync: Boolean) {
        setPrefsValue(key, value, isSync)
    }

    private fun setPrefsValue(
        key: String?,
        value: Any?,
        isSync: Boolean
    ) {
        if (value == null) {
            return
        }

        val type = value::class.java
        val editor = KVCache.context!!.getSharedPreferences(SP_DEFAULT_FILE_NAME, Context.MODE_PRIVATE).edit()
        when {
            java.lang.String::class.java == type -> {
                editor.putString(key, value as String?)
            }
            java.lang.Integer::class.java == type -> {
                editor.putInt(key, (value as Int?)!!)
            }
            java.lang.Boolean::class.java == type -> {
                editor.putBoolean(key, (value as Boolean?)!!)
            }
            java.lang.Float::class.java == type -> {
                editor.putFloat(key, (value as Float?)!!)
            }
            java.lang.Long::class.java == type -> {
                editor.putLong(key, (value as Long?)!!)
            }
        }
        if (isSync) {
            editor.commit()
        } else {
            editor.apply()
        }
    }

    private fun getPrefsValue(
        key: String?,
        defaultValue: Any?
    ): Any? {
        if (defaultValue == null) {
            return null
        }
        val type = defaultValue::class.java
        val prefs = KVCache.context!!.getSharedPreferences(SP_DEFAULT_FILE_NAME, Context.MODE_PRIVATE)

        when {
            java.lang.String::class.java == type -> {
                return prefs.getString(key, defaultValue as String?)
            }
            java.lang.Integer::class.java == type -> {
                return prefs.getInt(key, defaultValue as Int)
            }
            java.lang.Boolean::class.java == type -> {
                return prefs.getBoolean(key, defaultValue as Boolean)
            }
            java.lang.Float::class.java == type -> {
                return prefs.getFloat(key, defaultValue as Float)
            }
            java.lang.Long::class.java == type -> {
                return prefs.getLong(key, defaultValue as Long)
            }
            else -> throw Exception("get type is wrong!!!")
        }
    }

}