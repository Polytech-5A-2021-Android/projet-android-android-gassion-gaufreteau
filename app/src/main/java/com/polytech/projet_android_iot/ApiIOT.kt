package com.polytech.projet_android_iot

import com.google.gson.annotations.SerializedName
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.polytech.projet_android_iot.model.BoardIOT
import com.polytech.projet_android_iot.model.PresetsIOT
import com.polytech.projet_android_iot.model.User
import com.polytech.projet_android_iot.model.UserIOT
import com.squareup.moshi.Json
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.Deferred
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import okhttp3.OkHttpClient

import okhttp3.logging.HttpLoggingInterceptor




private const val BASE_URL = "http://78.198.193.139:8080/"

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private var retrofit = Retrofit.Builder()
    .addConverterFactory(GsonConverterFactory.create())
    .addCallAdapterFactory(CoroutineCallAdapterFactory())
    .baseUrl(BASE_URL)
    .build()

interface ApiIOT {

    @GET("presets/{bid}")
    fun getPresets(@Path("bid") bid: Long): Deferred<List<PresetsIOT>>

    @GET("boards")
    fun getBoards(): Deferred<List<BoardIOT>>

    @POST("login")
    fun login(@Body logInfo: LoginInfo) : Deferred<loginRet>

    @POST("register")
    fun register(@Body user: UserIOT) : Deferred<UserIOT>

    @POST("changePassword")
    fun changePwd(@Body changepwd: ChangePwd) : Deferred<boolRet>

    @POST("createPreset")
    fun createPreset(@Body createPreset: PresetsIOT) : Deferred<boolRet>

    @POST("usePreset")
    fun usePreset(@Body presetID: Long?) : Deferred<boolRet>

    @POST("useColors")
    fun useColors(@Body preset: PresetsIOT) : Deferred<boolRet>

    @POST("displayMessage")
    fun displayMessage(@Body message: DispayMess): Deferred<boolRet>

    @POST("soundDetector")
    fun switchSoundDetector(@Body switch: switchSD): Deferred<boolRet>

}

object MyApiIOT {
    val retrofitService : ApiIOT by lazy {

        val logging = HttpLoggingInterceptor()

        logging.level = HttpLoggingInterceptor.Level.BODY
        val httpClient = OkHttpClient.Builder()

        httpClient.addInterceptor(logging) // <-- this is the important line!

        retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .client(httpClient.build())
            .build()
        retrofit.create(ApiIOT::class.java) }
}

data class boolRet(
    @Json(name="confirm") val confirm: Boolean
)

data class loginRet(
    @Json(name="id") val id: Long,
    @Json(name="response") val response: Boolean
)

data class LoginInfo(
    @SerializedName("login") val login: String?,
    @SerializedName("password") val password: String?
)


data class ChangePwd(
    @SerializedName("userID") val userID: Long?,
    @SerializedName("new_password") val new_password: String?,
    @SerializedName("old_password") val old_password: String?
)

data class DispayMess(
    @SerializedName("boardID") val boardID: Long?,
    @SerializedName("message") val message: String?,
)


data class createPreset(
    @SerializedName("name") val name: String,
    @SerializedName("led1") val led1: String,
    @SerializedName("led2") val led2: String,
    @SerializedName("led3") val led3: String

)

data class switchSD(
    @SerializedName("boardID") val boardID: Long?,
    @SerializedName("_switch") val switch: Boolean
)