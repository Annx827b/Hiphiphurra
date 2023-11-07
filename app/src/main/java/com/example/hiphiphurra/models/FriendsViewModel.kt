package com.example.hiphiphurra.models

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.hiphiphurra.repository.FriendsRepository


class FriendsViewModel : ViewModel() {
    private val repo = FriendsRepository()
    val FriendsLiveData: LiveData<List<Friend>> = repo.FriendsLiveData
    val errorMessageLiveData: LiveData<String> = repo.errorMessageLiveData
    val updateMessageLiveData: LiveData<String> = repo.updateMessageLiveData

    fun reload(userId: String?) {
        Log.d("reloadData", "viewmodel userId: " + userId)
        repo.getPersons(userId)
    }

    operator fun get(index: Int): Friend? {
        return FriendsLiveData.value?.get(index)
    }

    fun add(person: Friend) {
        repo.add(person)
    }

    fun delete(id: Int) {
        repo.delete(id)
    }

    fun update(person: Friend) {
        repo.update(person)
    }

    fun sortByName() {
        repo.sortByName()
    }

    fun sortByNameDescending() {
        repo.sortByNameDescending()
    }

    fun sortByAge() {
        repo.sortByAge()
    }

    fun sortByAgeDescending() {
        repo.sortByAgeDescending()
    }
    fun sortByBirth() {
        repo.sortByBirth()
    }
    fun sortByBirthDescending() {
        repo.sortByBirthDescending()
    }

    fun filter(condition: String, userId: String?) {
        repo.filter(condition, userId)
    }

}