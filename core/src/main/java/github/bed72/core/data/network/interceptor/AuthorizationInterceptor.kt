package github.bed72.core.data.network.interceptor

import okhttp3.HttpUrl
import okhttp3.Interceptor
import okhttp3.Response
import java.math.BigInteger
import java.security.MessageDigest
import java.util.*

class AuthorizationInterceptor(
    private val publicKey: String,
    private val privateKey: String,
    private val calendar: Calendar
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val requestUrl = request.url
        val newUrl = handleUrlHash(requestUrl)

        return chain.proceed(
            request.newBuilder()
                .url(newUrl)
                .build()
        )
    }

    private fun handleUrlHash(requestUrl: HttpUrl): HttpUrl {
        val ts = (calendar.timeInMillis / BASE_TO_CONVERT_TIME_IN_SECONDS).toString()
        val hash = "$ts$privateKey$publicKey".md5()

        return requestUrl.newBuilder()
            .addQueryParameter(QUERY_PARAMETER_TS, ts)
            .addQueryParameter(QUERY_PARAMETER_API_KEY, publicKey)
            .addQueryParameter(QUERY_PARAMETER_HASH, hash)
            .build()
    }

    @Suppress("MagicNumber")
    private fun String.md5(): String {
        val md = MessageDigest.getInstance("MD5")

        return BigInteger(1, md.digest(toByteArray())).toString(16).padStart(32, '0')
    }

    companion object {
        private const val QUERY_PARAMETER_TS = "ts"
        private const val QUERY_PARAMETER_HASH = "hash"
        private const val QUERY_PARAMETER_API_KEY = "apiHey"
        private const val BASE_TO_CONVERT_TIME_IN_SECONDS = 1000L
    }
}