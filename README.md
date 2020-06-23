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
... I will write a blog later
