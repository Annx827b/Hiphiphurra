package com.example.hiphiphurra.repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.hiphiphurra.models.Friend
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.text.SimpleDateFormat

class FriendsRepository {

    private val baseUrl = "https://birthdaysrest.azurewebsites.net/api/"
    // the specific (collection) part of the URL is on the individual methods in the interface FrindstoreService

    //"http://anbo-restserviceproviderPersonss.azurewebsites.net/Service1.svc/"
    private val friendsService : FriendsService
    val FriendsLiveData: MutableLiveData<List<Friend>> = MutableLiveData<List<Friend>>()
    val errorMessageLiveData: MutableLiveData<String> = MutableLiveData()
    val updateMessageLiveData: MutableLiveData<String> = MutableLiveData()

    init {
        //val moshi = Moshi.Builder().addLast(KotlinJsonAdapterFactory()).build()
        val build: Retrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(MoshiConverterFactory.create()) // GSON
            //.addConverterFactory(KotlinJsonAdapterFactory)
            //.addConverterFactory(MoshiConverterFactory.create(moshi)) // Moshi, added to Gradle dependencies
            .build()
        friendsService = build.create(FriendsService::class.java)
        getPersons3()
    }

    fun onFailureMessage(t: Throwable){
        errorMessageLiveData.postValue(t.message)
        Log.d("APPLE","my method. " + t.message!!)
    }

    fun listResponceFail(response: Response<List<Friend>>){
        val message = response.code().toString() + " " + response.message()
        errorMessageLiveData.postValue(message)
        Log.d("APPLE", message)
    }

    fun checkResponse(response: Response<Friend>, operation: String){
        if (response.isSuccessful) {
            Log.d("APPLE", "$operation: " + response.body())
            updateMessageLiveData.postValue("$operation: " + response.body())
        } else {
            val message = response.code().toString() + " " + response.message()
            errorMessageLiveData.postValue(message)
            Log.d("APPLE", message)
        }
    }
    fun getPersons(user_id: String?){
        friendsService.getUserFriends(user_id).enqueue(object : Callback<List<Friend>>{
            override fun onResponse(call: Call<List<Friend>>, response: Response<List<Friend>>) {
                if (response.isSuccessful) {
                    Log.d("APPLE", friendsService.getUserFriends(user_id).toString() )
                    Log.d("APPLE",user_id +"rdszxdfgho")
                    val b: List<Friend>? = response.body()
                    FriendsLiveData.postValue(b!!)
                    errorMessageLiveData.postValue("")
                } else {
                    listResponceFail(response)
                }
            }

            override fun onFailure(call: Call<List<Friend>>, t: Throwable) {
                onFailureMessage(t)
            }
        })
    }

    fun getPersons3() {
        friendsService.getAllFriends().enqueue(object : Callback<List<Friend>> {
            override fun onResponse(call: Call<List<Friend>>, response: Response<List<Friend>>) {
                if (response.isSuccessful) {
                    //Log.d("APPLE", response.body().toString())
                    val b: List<Friend>? = response.body()
                    FriendsLiveData.postValue(b!!)
                    errorMessageLiveData.postValue("")
                } else {
                    val message = response.code().toString() + " " + response.message()
                    errorMessageLiveData.postValue(message)
                    Log.d("APPLE", message)
                }
            }

            override fun onFailure(call: Call<List<Friend>>, t: Throwable) {
                //booksLiveData.postValue(null)
                errorMessageLiveData.postValue(t.message)
                Log.d("APPLE", t.message!!)
            }
        })
    }
    fun getFriends2(userId: String?) {
        friendsService.getAllFriends().enqueue(object : Callback<List<Friend>> {
            override fun onResponse(call: Call<List<Friend>>, response: Response<List<Friend>>) {
                if (response.isSuccessful) {
                    Log.d("APPLE", response.body().toString() + "repo")
                    val f: List<Friend>? = response.body()
                    val userFriends: MutableList<Friend> = mutableListOf()
                    if (f != null) {
                        for(Friend in f) {
                            if(Friend.userId == userId) {
                                Log.d("APPLE", userId.toString())
                                userFriends.add(Friend)

                            }
                        }
                    }
                    FriendsLiveData.postValue(userFriends)
                    errorMessageLiveData.postValue("")
                } else {
                    listResponceFail(response)
                }
            }

            override fun onFailure(call: Call<List<Friend>>, t: Throwable) {
                onFailureMessage(t)
            }
        })
    }

    fun add(Friends: Friend) {
        friendsService.saveFriend(Friends).enqueue(object : Callback<Friend> {
            override fun onResponse(call: Call<Friend>, response: Response<Friend>) {
                checkResponse(response, "Added")

            }

            override fun onFailure(call: Call<Friend>, t: Throwable) {
                onFailureMessage(t)
            }
        })
    }
    fun delete(id: Int) {
        friendsService.deleteFriend(id).enqueue(object : Callback<Friend> {
            override fun onResponse(call: Call<Friend>, response: Response<Friend>) {
                checkResponse(response, "Deleted")
            }

            override fun onFailure(call: Call<Friend>, t: Throwable) {
                onFailureMessage(t)
            }
        })
    }


    fun update(person: Friend) {
        friendsService.updateFriend(person.id , person).enqueue(object : Callback<Friend> {
            override fun onResponse(call: Call<Friend>, response: Response<Friend>) {
                checkResponse(response, "Updated")
            }

            override fun onFailure(call: Call<Friend>, t: Throwable) {
                onFailureMessage(t)
            }
        })
    }

    fun sortByName() {
        FriendsLiveData.value = FriendsLiveData.value?.sortedBy { it.name.uppercase() }
    }

    fun sortByNameDescending() {
        FriendsLiveData.value = FriendsLiveData.value?.sortedByDescending { it.name.uppercase() }
    }

    fun sortByAge() {
        FriendsLiveData.value = FriendsLiveData.value?.sortedBy { it.age }
    }

    fun sortByAgeDescending() {
        FriendsLiveData.value = FriendsLiveData.value?.sortedByDescending { it.age }
    }

    fun sortByBirth() {
        val sdf = SimpleDateFormat("dd-MM")
        FriendsLiveData.value = FriendsLiveData.value?.sortedBy {
            sdf.parse("${it.birthDayOfMonth.toString()}-${it.birthMonth.toString()}")
        }
    }

    fun sortByBirthDescending() {
        val sdf = SimpleDateFormat("dd-MM")
        FriendsLiveData.value = FriendsLiveData.value?.sortedByDescending {
            sdf.parse("${it.birthDayOfMonth.toString()}-${it.birthMonth.toString()}")
        }

    }

    fun filter(condition: String, PersonsId: String?){
        friendsService.getUserFriends(PersonsId).enqueue(object : Callback<List<Friend>> {
            override fun onResponse(call: Call<List<Friend>>, response: Response<List<Friend>>) {
                if (response.isSuccessful) {
                    val b: List<Friend>? = response.body()
                    val tryNum: String = condition

                    if(tryNum.toIntOrNull() != null) {
                        FriendsLiveData.value = b?.filter { friend -> friend.age == tryNum.toInt()}
                    }
                    else {
                        FriendsLiveData.value = b?.filter { friend -> friend.name.uppercase().contains(condition.uppercase()) }
                    }
                } else {
                    listResponceFail(response)
                }
            }

            override fun onFailure(call: Call<List<Friend>>, t: Throwable) {
                onFailureMessage(t)
            }
        })
    }
}


