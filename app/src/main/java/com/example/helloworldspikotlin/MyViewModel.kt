package com.example.helloworldspikotlin

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class MyViewModel : ViewModel() {

    private val _users = MutableLiveData<List<User>>()
    val users: LiveData<List<User>> = _users

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

    fun fetchUsers() {
        viewModelScope.launch {
            try {
                val response = RetrofitInstance.api.getUsers()
                _users.value = response.users
            } catch (e: Exception) {
                _errorMessage.value = "Failed to load users: ${e.message}"
                e.printStackTrace()
            }
        }
    }
}