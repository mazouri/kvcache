package mazouri.kvcache

import android.content.Context
import mazouri.kvcache.service.IKVConfig
import mazouri.kvcache.service.KVConfigManager
import mazouri.kvcache.service.KVPrefs
import java.lang.reflect.InvocationHandler
import java.lang.reflect.Method
import java.lang.reflect.Proxy

/**
 * @Description: KVCache外观类，客户端仅需使用该类
 * @Author: wangdongdong
 * @Email: wangdd_bj@163.com
 * @Github: https://github.com/mazouri
 * @CSDN: https://blog.csdn.net/dongdong230
 * @CreateDate: 2020/6/23 11:18 AM
 * @Version: 1.0
 */
class KVCache private constructor(private val builder: Builder){

    init {
        KVConfigManager.instance.setServiceConfig(builder.serviceConfig)
        KVPrefs.SP_DEFAULT_FILE_NAME = builder.filename
    }

    companion object {
        internal var context: Context? = null
    }

    private val serviceMethodCache = LinkedHashMap<Method, KVMethod<*>>()

    @Suppress("UNCHECKED_CAST")
    fun <T> create(service: Class<T>): T {
        context = builder.context.applicationContext
        val proxy = Proxy.newProxyInstance(
            service.classLoader,
            arrayOf<Class<*>>(service),
            object : InvocationHandler {
                override fun invoke(proxy: Any?, method: Method?, args: Array<out Any>?): Any? {
                    if (method!!.declaringClass == Any::class.java) {
                        return method.invoke(this, args)
                    }
                    return KVCall<T>(loadServiceMethod(method))
                }

            }
        )

        return proxy as T
    }

    private fun loadServiceMethod(method: Method): KVMethod<*> {
        var result = serviceMethodCache[method]
        if (result != null) return result

        // 2.解析method
        synchronized(serviceMethodCache) {
            result = serviceMethodCache[method]

            if (result == null) {
                // 3.将method传到ServiceMethod中解析
                result = KVMethod.Builder<Any?>(method).build()
                serviceMethodCache[method] = result as KVMethod<*>
            }
        }

        return result!!
    }

    class Builder constructor(val context: Context) {
        internal var serviceConfig: IKVConfig? = null
        internal var filename: String = ""

        fun addServiceConfig(serviceConfig: IKVConfig): Builder {
            this.serviceConfig = serviceConfig
            return this
        }

        fun addDefaultPrefsFileName(filename: String): Builder {
            this.filename = filename
            return this
        }

        fun build(): KVCache {
            return KVCache(this)
        }
    }
}