package com.example.hiphiphurra.repository

import com.example.hiphiphurra.models.Friend
import retrofit2.Call
import retrofit2.http.*


interface FriendsService {
    @GET("persons")
    fun getAllFriends(): Call<List<Friend>>
    @GET("persons")
    fun getUserFriends(@Query("user_id") userId : String?): Call<List<Friend>>

    @GET("persons/{personId}")
    fun getFriendById(@Path("personId") personId: Int): Call<Friend>

    @POST("persons")
    fun saveFriend(@Body friend: Friend): Call<Friend>

    @DELETE("persons/{id}")
    fun deleteFriend(@Path("id") id: Int): Call<Friend>

    @PUT("persons/{id}")
    fun updateFriend(@Path("id") id: Int, @Body person: Friend): Call<Friend>

}