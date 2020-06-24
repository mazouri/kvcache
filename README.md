# kvcache
[![](https://jitpack.io/v/mazouri/kvcache.svg)](https://jitpack.io/#mazouri/kvcache)

## About kvcache
This library is a tool to help you set and get key-vlue data with better way in Andrtoid development. From now, change your sharedpreference code and other key-value code to `kvcache`, and coding the beautiful code.

## How to use
> Step 1/2/3 is to init kvcache
> you only need to modidy interface KV when you need add a key-value data.(Step 4)
> and 

Step 1. Add it in your root build.gradle at the end of repositories:

	allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
Step 2. Add the dependency
	
	dependencies {
	        implementation 'com.github.mazouri:kvcache:1.1'
	}

Step 3. Init the KVCache in your Application class

	private fun initKV() {
        val kvCache = KVCache.Builder(this)
            // 使用addServiceConfig（）设置您自己的存储方法
            // 如果未设置，则默认使用SharedPreference
            // Use addServiceConfig() to set your own storage method
            // If you not set, shared preference will be used by default
            .addServiceConfig(SimpleIKVConfig())
            // 如果使用默认的SharedPreference方式来存储，则文件名默认为kv_cache_shared_prefs.xml
            // 可以使用addDefaultPrefsFileName()来修改保存的文件名
            // If the default shared preference storage is used, the file name defaults to kv_cache_shared_prefs.xml
            // You can use addDefaultPrefsFileName() to modify the saved file name
            .addDefaultPrefsFileName("kv_cache_main_sp")
            .build()

        kv = kvCache.create(KV::class.java)
    }

Step 4. Create a interface KV to config your key-value infomation

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

Step 5.now you can use it everywhere in your app easyly
	
	// save you key value to cache, default is using shared preference
	KVApp.kv.testKVCacheString().put("hello KVCache")
        KVApp.kv.testKVCacheInt().put(2020)
        KVApp.kv.testKVCacheFloat().put(3.14f)
        KVApp.kv.testKVCacheBoolean().put(true)
        KVApp.kv.testKVCacheLong().put(111100001111L)
	
	// get value by key
	val resultString = KVApp.kv.testKVCacheString().get()
        val resultInt = KVApp.kv.testKVCacheInt().get()
        val resultFloat = KVApp.kv.testKVCacheFloat().get()
        val resultBoolean = KVApp.kv.testKVCacheBoolean().get()
        val resultLong = KVApp.kv.testKVCacheLong().get()

## How does `kvcache` works
> 如果你使用过Retrofit，并且理解Retrofit的实现原理，那么你看下面的实现原理将非常顺滑

### KVCache
> KVCache使用设计模式中的门面模式，客户端仅需使用该类

KVCache的创建方式如下：

```
private fun initKV() {
        val kvCache = KVCache.Builder(this)
            .addServiceConfig(SimpleIKVConfig())
            .addDefaultPrefsFileName("kv_cache_main_sp")
            .build()

        kv = kvCache.create(KV::class.java)
    }

```
> 使用了设计模式中的Builder模式

Builder代码如下：

```
class KVCache private constructor(private val builder: Builder){
	
	...
	
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
```
通过该类，可以配置自定义的ServiceConfig和默认缓存方式shared_preference使用的文件名。

下面看KVCache的create函数实现：

```
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
```
> 使用了设计模式中的动态代理模式

通过动态代理，将客户端写的KV接口的类，转换成KV对象调用请求

这里看下loadServiceMethod(method)的实现：

```
    private fun loadServiceMethod(method: Method): KVMethod<*> {
        var result = serviceMethodCache[method]
        if (result != null) return result

        // 2.解析method
        synchronized(serviceMethodCache) {
            result = serviceMethodCache[method]

            if (result == null) {
                // 3.将method传到KVMethod中解析
                result = KVMethod.Builder<Any?>(method).build()
                serviceMethodCache[method] = result as KVMethod<*>
            }
        }

        return result!!
    }
```

这里加了一个缓存，如果之前已经解析过的方法，就不用再解析了，提高效率。

然后将method传到KVMethod中去解析

### KVMethod
该类用于解析客户端定义的注解类

从上面的代码可以知道KVMethod也是通过Builder构建的

> 这里使用了设计模式的Builder模式

重点我们看下，method传进来后，是怎么解析的：

```
class Builder<T> constructor(private val method: Method) {

        private val methodAnnotations = method.annotations
        var typeClass: Class<T>
        lateinit var key: String
        lateinit var default: String
        var isSync = false

        init {
            val returnType = method.genericReturnType as ParameterizedType
            typeClass = returnType.actualTypeArguments[0] as Class<T>
        }

        fun build(): KVMethod<T> {
            for (annotation in methodAnnotations) {
                parseMethodAnnotation(annotation)
            }

            return KVMethod<T>(this)
        }

        private fun parseMethodAnnotation(annotation: Annotation) {
            when(annotation) {
                is KEY -> key = annotation.value
                is DEFAULT -> default = annotation.value
                is SYNC -> isSync = true
            }
        }
    }
```
拿到method的注解信息和返回值类型。

比如下面的代码：

```
		@KEY("save_String")
	    @DEFAULT("")
	    fun testKVCacheString(): Call<String>
```
解析注解时，
- 如果发现注解是KEY这个类型，就把该注解的value值给到key
- 如果发现注解是DEFAULT这个类型，就把该注解的value值给到default

### KVCall

在存取数据的时候会这么使用：

```
        // save you key value to cache, default is using shared preference
		KVApp.kv.testKVCacheString().put("hello KVCache")
       
		// get value by key
		val resultString = KVApp.kv.testKVCacheString().get()
```

那么put()和get()是怎么实现的呢？

看下KVCall这个类中的put方法：

```
internal class KVCall<T> constructor(private val serviceMethod: KVMethod<*>): Call<T> {

	...
	
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
	    
	    ...
	    
        private fun setStringValue(key: String, value: String, isSync: Boolean) {
            KVConfigManager.instance.setStringValue(key, value, isSync)
   	   }
}
```
在这里，根据KVMethod的返回值类型，调用缓存管理类KVConfigManager设置对应的数据

### IKVConfig
> 缓存key-value数据的接口类，客户端通过实现该接口实现自己的缓存方式

```
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
```

### KVConfigManager
> 缓存方式管理类，默认使用KVPrefs
```
internal class KVConfigManager private constructor(): IKVConfig {

    private var serviceConfig: IKVConfig = KVPrefs()

    companion object {
        val instance: KVConfigManager by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            KVConfigManager()
        }
    }

    internal fun setServiceConfig(serviceConfig: IKVConfig?) {
        Log.d("", "setServiceConfig called")
        if (serviceConfig == null) return
        this.serviceConfig = serviceConfig
    }
    
    override fun setStringValue(key: String?, value: String?, isSync: Boolean) = serviceConfig.setStringValue(key, value, isSync)
    ...
} 
```
> 这里使用了设计模式中的单例模式

如果客户端没有使用自定义的缓存方式，则默认使用KVPrefs缓存

### KVPrefs
> 默认的存储方式，即使用SharedPreferences

比较简单，这里就不赘述了，可以直接看源码[KVPrefs](https://github.com/mazouri/kvcache/blob/master/kvcache-android/src/main/kotlin/mazouri/kvcache/service/KVPrefs.kt)


欢迎Star🌟🌟🌟Github：[kvcache:在Android开发中优雅的存取key/value数据，从此不用再写SharedPreference代码](https://github.com/mazouri/kvcache)

