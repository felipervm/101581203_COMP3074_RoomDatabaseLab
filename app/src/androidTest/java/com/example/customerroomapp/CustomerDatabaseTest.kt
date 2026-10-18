package com.example.customerroomapp

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.customerroomapp.data.Customer
import com.example.customerroomapp.data.CustomerDatabase
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class CustomerDatabaseTest {
    @Test
    fun customersCanBeInsertedOrderedByIdAndDeleted() = runBlocking {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val database = Room.inMemoryDatabaseBuilder(context, CustomerDatabase::class.java).build()
        try {
            val dao = database.customerDao()
            dao.insert(Customer(name = "Zoe", age = 25, isActive = true))
            dao.insert(Customer(name = "adam", age = 30, isActive = false))
            val saved = dao.getAllCustomers().first()
            assertEquals(listOf("Zoe", "adam"), saved.map { it.name })
            assertTrue(saved.all { it.id > 0 })
            assertEquals(listOf(25, 30), saved.map { it.age })
            assertEquals(listOf(true, false), saved.map { it.isActive })
            dao.delete(saved.first())
            assertEquals(listOf("adam"), dao.getAllCustomers().first().map { it.name })
        } finally {
            database.close()
        }
    }
}
