package com.square.repos.data.remote

import com.facebook.stetho.okhttp3.StethoInterceptor
import com.square.repos.model.Repo
import com.square.repos.model.User
import io.reactivex.Flowable
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import java.security.KeyStore
import java.util.*
import javax.net.ssl.TrustManagerFactory
import javax.net.ssl.X509TrustManager

/**
 * Class responsible for the communication with server
 * All remote data access comes from here.
 *
 */

class NetworkApi {

    companion object {
        private const val BASE_URL = "https://api.github.com/"
        private const val LIST_URL = "orgs/square/repos"
        private const val STARGAZERS_URL = "repos/square/{repo_name}/stargazers"
    }

    private val retrofit: Retrofit
    private val reviewsApi: ReviewsApi

    // API

    interface ReviewsApi {
        @GET(LIST_URL)
        fun fetchList(): Flowable<List<Repo>>

        @GET(STARGAZERS_URL)
        fun fetchStargazers(@Path("repo_name") repoName:String): Flowable<List<User>>
    }

    // PUBLIC

    fun fetchList(): Flowable<List<Repo>> = reviewsApi.fetchList()
    fun fetchStargazers(repoName:String): Flowable<List<User>> = reviewsApi.fetchStargazers(repoName)

    // INIT

    init {
        val clientBuilder = OkHttpClient.Builder()
                .addNetworkInterceptor(StethoInterceptor())

        val trustManagerFactory = TrustManagerFactory.getInstance(
                TrustManagerFactory.getDefaultAlgorithm())
        trustManagerFactory.init(null as KeyStore?)
        val trustManagers = trustManagerFactory.trustManagers
        if (trustManagers.size != 1 || trustManagers[0] !is X509TrustManager) {
            throw IllegalStateException("Unexpected default trust managers:" + Arrays.toString(trustManagers))
        }
        val trustManager = trustManagers[0] as X509TrustManager
        clientBuilder.sslSocketFactory(TLSSocketFactory(), trustManager)

        retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(clientBuilder.build())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(MoshiConverterFactory.create())
                .build()

        reviewsApi = retrofit.create(ReviewsApi::class.java)
    }
}
