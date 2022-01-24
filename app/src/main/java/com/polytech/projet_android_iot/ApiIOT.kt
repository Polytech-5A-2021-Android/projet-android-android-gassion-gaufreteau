package com.polytech.projet_android_iot

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.polytech.projet_android_iot.model.BoardIOT
import com.polytech.projet_android_iot.model.PresetsIOT
import com.polytech.projet_android_iot.model.UserIOT
import com.squareup.moshi.Json
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.Deferred
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

private const val BASE_URL = "https://google.com"

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .addCallAdapterFactory(CoroutineCallAdapterFactory())
    .baseUrl(BASE_URL)
    .build()

interface ApiIOT {
    //TODO : GET OU POST puis déclaration de la fonction qui fait office d'API
/*    @GET("realestate")
    fun getProperties(): Deferred<List<MarsProperty>>*/

    @GET("presets/{bid}")
    fun getPresets(@Path("bid") bid: Long): Deferred<List<PresetsIOT>>

    @GET("boards")
    fun getBoards(): Deferred<List<BoardIOT>>

    @POST("login")
    fun login(@Body logInfo: LoginInfo) : Deferred<UserIOT>

    @POST("changePassword")
    fun changePwd(@Body changepwd: ChangePwd) : Deferred<UserIOT>

    @POST("createPreset")
    fun createPreset(@Body preset: PresetsIOT) : Deferred<PresetsIOT>

    @POST("usePreset")
    fun usePreset(@Body presetId: Long) : Deferred<Boolean>

    @POST("displayMessage")
    fun displayMessage(@Body message: String): Deferred<Boolean>

    @POST("soundDetector")
    fun switchSoundDetector(@Body switch: Boolean): Deferred<Boolean>

}

object MyApiIOT {
    val retrofitService : ApiIOT by lazy { retrofit.create(ApiIOT::class.java) }
}

data class LoginInfo(
    val login: String?,
    val password: String?
)


data class ChangePwd(
    val userID: Long?,
    val new_password: String?,
    val old_password: String?
)
