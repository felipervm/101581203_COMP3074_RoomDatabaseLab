package com.example.customerroomapp

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.customerroomapp.data.*
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class CustomerViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = CustomerRepository(CustomerDatabase.getInstance(application).customerDao())
    val customers = repository.customers
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    fun addCustomer(name: String, age: Int, isActive: Boolean) {
        if (name.isBlank()) return
        viewModelScope.launch {
            repository.insert(Customer(name = name, age = age, isActive = isActive))
        }
    }

    fun deleteCustomer(customer: Customer) {
        viewModelScope.launch {
            repository.delete(customer)
        }
    }
}
