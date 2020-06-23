package mazouri.kvcache.sample

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import mazouri.kvcache.KVCache

/**
 * @Description: Application
 * @Author: wangdongdong
 * @Email: wangdd_bj@163.com
 * @Github: https://github.com/mazouri
 * @CSDN: https://blog.csdn.net/dongdong230
 * @CreateDate: 2020/6/23 6:20 PM
 * @Version: 1.0
 */
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun onclick(view: View) {
        // save you key value to cache, default is using shared preference
        KVApp.kv.testKVCacheString().put("hello KVCache")
        KVApp.kv.testKVCacheInt().put(2020)
        KVApp.kv.testKVCacheFloat().put(3.14f)
        KVApp.kv.testKVCacheBoolean().put(true)
        KVApp.kv.testKVCacheLong().put(111100001111L)

        val resultString = KVApp.kv.testKVCacheString().get()
        val resultInt = KVApp.kv.testKVCacheInt().get()
        val resultFloat = KVApp.kv.testKVCacheFloat().get()
        val resultBoolean = KVApp.kv.testKVCacheBoolean().get()
        val resultLong = KVApp.kv.testKVCacheLong().get()

        Log.d("MainActivity",
            "\nresultString: $resultString" +
                    "\nresultInt: $resultInt" +
                    "\nresultFloat: $resultFloat" +
                    "\nresultBoolean: $resultBoolean" +
                    "\nresultLong: $resultLong")
    }
}