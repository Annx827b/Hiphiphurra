package com.example.hiphiphurra.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.hiphiphurra.models.Friend
import com.example.hiphurra.repository.FriendsRepository

class FriendsViewModel : ViewModel() {
    private val repository = FriendsRepository()
    val friendLiveData: LiveData<List<Friend>> = repository.friendLiveData

    fun reload(userId: String?) {
        Log.d("Reload_log", "userId: " + userId)
        repository.getFriends(userId)
    }

    operator fun get(index: Int): Friend? {
        return friendLiveData.value?.get(index)
    }

    fun add(friend: Friend) {
        repository.add(friend)
    }

    fun delete(id: Int) {
        repository.delete(id)
    }

    fun update(friend: Friend) {
        repository.update(friend)
    }

    fun sortByName() {
        repository.sortByName()
    }

    fun sortByNameDescending() {
        repository.sortByNameDescending()
    }

    fun sortByBirth() {
        repository.sortByBirth()
    }

    fun sortByBirthDescending() {
        repository.sortByBirthDescending()
    }

    fun sortByAge() {
        repository.sortByAge()
    }

    fun sortByAgeDescending() {
        repository.sortByAgeDescending()
    }

    fun filter(condition: String, idUser: String?) {
        repository.filter(condition, idUser)
    }
}