package mazouri.kvcache

import mazouri.kvcache.annotation.DEFAULT
import mazouri.kvcache.annotation.KEY
import mazouri.kvcache.annotation.SYNC
import java.lang.reflect.Method
import java.lang.reflect.ParameterizedType

/**
 * @Description: 用于解析客户端定义的注解类
 * @Author: wangdongdong
 * @Email: wangdd_bj@163.com
 * @Github: https://github.com/mazouri
 * @CSDN: https://blog.csdn.net/dongdong230
 * @CreateDate: 2020/6/23 11:29 AM
 * @Version: 1.0
 */
internal class KVMethod<T> private constructor(private val builder: Builder<T>){

    var key = builder.key
        private set
    var default = builder.default
        private set
    var isSync = builder.isSync
        private set
    var typeClass = builder.typeClass
        private set

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
}