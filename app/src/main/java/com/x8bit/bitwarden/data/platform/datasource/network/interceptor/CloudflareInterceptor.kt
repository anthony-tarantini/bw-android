package com.x8bit.bitwarden.data.platform.datasource.network.interceptor

import com.x8bit.bitwarden.data.platform.datasource.network.util.CF_ACCESS_CLIENT_ID
import com.x8bit.bitwarden.data.platform.datasource.network.util.CF_ACCESS_CLIENT_SECRET
import okhttp3.Interceptor
import okhttp3.Response

/**
 * Interceptor responsible for adding various headers to all API requests.
 */
class CloudflareInterceptor : Interceptor {
    var cloudflareClientId: String = ""
        set(value) {
            field = value
            println("CloudflareClientId = $value")
        }
    var cloudflareClientSecret: String = ""
        set(value) {
            field = value
            println("CloudflareClientSecret = $value")
        }

    override fun intercept(chain: Interceptor.Chain): Response {
        return if (cloudflareClientSecret.isNotEmpty() && cloudflareClientSecret.isNotEmpty()) {
            chain.proceed(
                chain.request()
                    .newBuilder()
                    .header(CF_ACCESS_CLIENT_ID, cloudflareClientId)
                    .header(CF_ACCESS_CLIENT_SECRET, cloudflareClientSecret)
                    .build()
            )
        } else {
            chain.proceed(chain.request())
        }
    }
}
