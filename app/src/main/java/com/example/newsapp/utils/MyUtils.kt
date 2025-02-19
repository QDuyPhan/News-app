//package com.example.newsapp.utils
//
//import android.content.Context
//import com.example.newsapp.R
//import okhttp3.OkHttpClient
//import java.io.InputStream
//import java.security.KeyStore
//import java.security.cert.CertificateFactory
//import java.security.cert.X509Certificate
//import javax.net.ssl.SSLContext
//import javax.net.ssl.TrustManagerFactory
//import javax.net.ssl.X509TrustManager
//
//object MyUtils {
//    fun getUnsafeOkHttpClient(context: Context): OkHttpClient {
//        try {
//            // 1. Tải chứng chỉ từ res/raw
//            val certInputStream: InputStream =
//                context.resources.openRawResource(R.raw.mykeystore)
//            val cf = CertificateFactory.getInstance("X.509")
//            val cert = cf.generateCertificate(certInputStream) as X509Certificate
//
//            // 2. Tạo KeyStore chứa chứng chỉ tin cậy của bạn
//            val keyStore = KeyStore.getInstance(KeyStore.getDefaultType())
//            keyStore.load(null, null)
//            keyStore.setCertificateEntry("mydomain", cert)
//
//            // 3. Tạo TrustManagerFactory bằng KeyStore của bạn
//            val tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm())
//            tmf.init(keyStore)
//
//            // 4. Tạo SSLContext bằng TrustManagerFactory của bạn
//            val sslContext = SSLContext.getInstance("TLS")
//            sslContext.init(null, tmf.getTrustManagers(), null)
//
//            // 5. Tạo OkHttpClient tin tưởng chứng chỉ tự ký
//            val builder = OkHttpClient.Builder()
//            builder.sslSocketFactory(
//                sslContext.socketFactory,
//                tmf.trustManagers[0] as X509TrustManager
//            )
//            builder.hostnameVerifier { hostname, session -> true } // Bỏ qua xác minh hostname (KHÔNG AN TOÀN)
//
//            return builder.build()
//        } catch (e: Exception) {
//            throw RuntimeException(e)
//        }
//    }
//}