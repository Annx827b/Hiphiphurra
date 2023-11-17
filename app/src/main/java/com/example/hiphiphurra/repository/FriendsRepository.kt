package com.example.hiphurra.repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.text.SimpleDateFormat
import com.example.hiphiphurra.models.Friend
import com.example.hiphiphurra.repository.FriendsService
import java.util.*


class FriendsRepository {
    private val baseUrl = "https://birthdaysrest.azurewebsites.net/api/"
    private val serviceFriend: FriendsService
    val friendLiveData: MutableLiveData<List<Friend>> = MutableLiveData<List<Friend>>()
    val errorLiveData: MutableLiveData<String> = MutableLiveData()
    val updateLiveData: MutableLiveData<String> = MutableLiveData()

    init {
        val build: Retrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
        serviceFriend = build.create(FriendsService::class.java)
    }

    fun onFailureMessage(t: Throwable){
        errorLiveData.postValue(t.message)
        Log.d("Repository_log","Method: " + t.message!!)
    }

    fun listResponseFail(response: Response<List<Friend>>){
        val message = response.code().toString() + " " + response.message()
        errorLiveData.postValue(message)
        Log.d("Repository_log", message)
    }

    fun checkResponse(response: Response<Friend>, operation: String){
        if (response.isSuccessful) {
            Log.d("Repository_log", "$operation: " + response.body())
            updateLiveData.postValue("$operation: " + response.body())
        } else {
            val message = response.code().toString() + " " + response.message()
            errorLiveData.postValue(message)
            Log.d("Repository_log", message)
        }
    }

    fun getFriends(user_id: String?){
        serviceFriend.getUserFriends(user_id).enqueue(object : Callback<List<Friend>> {
            override fun onResponse(call: Call<List<Friend>>, response: Response<List<Friend>>) {
                if (response.isSuccessful) {
                    Log.d("Repository_log", serviceFriend.getUserFriends(user_id).toString() )
                    Log.d("Repository_log",user_id +"hhhhh0000")
                    val b: List<Friend>? = response.body()
                    friendLiveData.postValue(b!!)
                    errorLiveData.postValue("")
                } else {
                    listResponseFail(response)
                }
            }
            override fun onFailure(call: Call<List<Friend>>, t: Throwable) {
                onFailureMessage(t)
            }
        })
    }

    fun add(friend: Friend) {
        serviceFriend.saveFriend(friend).enqueue(object : Callback<Friend> {
            override fun onResponse(call: Call<Friend>, response: Response<Friend>) {
                checkResponse(response, "Friend is added")
            }
            override fun onFailure(call: Call<Friend>, t: Throwable) {
                onFailureMessage(t)
            }
        })
    }

    fun update(friend: Friend) {
        serviceFriend.updateFriend(friend.id, friend).enqueue(object : Callback<Friend> {
            override fun onResponse(call: Call<Friend>, response: Response<Friend>) {
                checkResponse(response, "Updated")
            }
            override fun onFailure(call: Call<Friend>, t: Throwable) {
                onFailureMessage(t)
            }
        })
    }

    fun delete(id: Int) {
        serviceFriend.deleteFriend(id).enqueue(object : Callback<Friend> {
            override fun onResponse(call: Call<Friend>, response: Response<Friend>) {
                checkResponse(response, "Deleted")
            }
            override fun onFailure(call: Call<Friend>, t: Throwable) {
                onFailureMessage(t)
            }
        })
    }

    fun sortByName() {
        friendLiveData.value = friendLiveData.value?.sortedBy { it.name.uppercase() }
    }

    fun sortByNameDescending() {
        friendLiveData.value = friendLiveData.value?.sortedByDescending { it.name.uppercase() }
    }

    fun sortByBirth() {
        val simpleDateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
        friendLiveData.value = friendLiveData.value?.sortedBy { friend ->
            simpleDateFormat.parse("${friend.birthDayOfMonth}-${friend.birthMonth}-${friend.birthYear}")
        }
    }

    fun sortByBirthDescending() {
        val simpleDateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
        friendLiveData.value = friendLiveData.value?.sortedByDescending { friend ->
            simpleDateFormat.parse("${friend.birthDayOfMonth}-${friend.birthMonth}-${friend.birthYear}")
        }
    }

    fun sortByAge() {
        friendLiveData.value = friendLiveData.value?.sortedBy { it.age }
    }

    fun sortByAgeDescending() {
        friendLiveData.value = friendLiveData.value?.sortedByDescending { it.age }
    }

    fun filter(condition: String, friendId: String?){
        serviceFriend.getUserFriends(friendId).enqueue(object : Callback<List<Friend>> {
            override fun onResponse(call: Call<List<Friend>>, response: Response<List<Friend>>) {
                if (response.isSuccessful) {
                    val friends: List<Friend>? = response.body()

                    when {
                        condition.toIntOrNull() != null -> {
                            Log.d("Filter", "Filtering by age: $condition")
                            friendLiveData.value = friends?.filter { it.age == condition.toInt() }
                        }
                        else -> {
                            Log.d("Filter", "Filtering by name: $condition")
                            friendLiveData.value = friends?.filter { it.name.uppercase().contains(condition.uppercase()) }
                        }
                    }
                } else {
                    listResponseFail(response)
                }
            }
            override fun onFailure(call: Call<List<Friend>>, t: Throwable) {
                onFailureMessage(t)
            }
        })
    }
}