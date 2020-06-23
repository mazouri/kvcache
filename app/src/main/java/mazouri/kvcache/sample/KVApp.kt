package mazouri.kvcache.sample

import android.app.Application
import mazouri.kvcache.KVCache
import mazouri.kvcache.sample.example.KV
import mazouri.kvcache.sample.example.SimpleIKVConfig

/**
 * @Description: Application
 * @Author: wangdongdong
 * @Email: wangdd_bj@163.com
 * @Github: https://github.com/mazouri
 * @CSDN: https://blog.csdn.net/dongdong230
 * @CreateDate: 2020/6/23 6:20 PM
 * @Version: 1.0
 */
class KVApp : Application() {

    companion object {
        lateinit var kv: KV
    }

    override fun onCreate() {
        super.onCreate()

        // 可以在Application里初始化，也可以放到单独类中，在此调用
        // 比如，把初始化的代码放到名为 KVClient 的类中，使用时就可以使用 KVClient.kv.xxx.get()
        // Can be initialized in Application, or can be placed in a separate class, and called here
        // For example, put the initialization code in a class called KVClient, and you can use KVClient.kv.xxx.get()
        initKV()
    }

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

}