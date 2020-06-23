package mazouri.kvcache.service

import android.util.Log

/**
 * @Description: 存储方式管理类，默认使用KVPrefs
 * @Author: wangdongdong
 * @Email: wangdd_bj@163.com
 * @Github: https://github.com/mazouri
 * @CSDN: https://blog.csdn.net/dongdong230
 * @CreateDate: 2020/6/23 12:42 PM
 * @Version: 1.0
 */
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

    override fun hasKey(key: String?) = serviceConfig.hasKey(key)

    override fun getLongValue(key: String?, defValue: Long) = serviceConfig.getLongValue(key, defValue)

    override fun getBooleanValue(key: String?, defValue: Boolean) = serviceConfig.getBooleanValue(key, defValue)

    override fun getIntValue(key: String?, defValue: Int) = serviceConfig.getIntValue(key, defValue)

    override fun getFloatValue(key: String?, defValue: Float) = serviceConfig.getFloatValue(key, defValue)

    override fun getStringValue(key: String?, defValue: String?) = serviceConfig.getStringValue(key, defValue)

    override fun setBooleanValue(key: String?, value: Boolean, isSync: Boolean) = serviceConfig.setBooleanValue(key, value, isSync)

    override fun setLongValue(key: String?, value: Long, isSync: Boolean) = serviceConfig.setLongValue(key, value, isSync)

    override fun setIntValue(key: String?, value: Int, isSync: Boolean) = serviceConfig.setIntValue(key, value, isSync)

    override fun setFloatValue(key: String?, value: Float, isSync: Boolean) = serviceConfig.setFloatValue(key, value, isSync)

    override fun setStringValue(key: String?, value: String?, isSync: Boolean) = serviceConfig.setStringValue(key, value, isSync)
}