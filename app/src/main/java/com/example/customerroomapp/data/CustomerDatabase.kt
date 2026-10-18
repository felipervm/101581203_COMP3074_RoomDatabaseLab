package com.example.customerroomapp.data

import android.content.Context
import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Entity(tableName = "customers")
data class Customer(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val age: Int,
    val isActive: Boolean
)

@Dao
interface CustomerDao {
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(customer: Customer)

    @Delete
    suspend fun delete(customer: Customer)

    @Query("SELECT * FROM customers ORDER BY id")
    fun getAllCustomers(): Flow<List<Customer>>
}

@Database(entities = [Customer::class], version = 2, exportSchema = true)
abstract class CustomerDatabase : RoomDatabase() {
    abstract fun customerDao(): CustomerDao

    companion object {
        @Volatile private var instance: CustomerDatabase? = null

        fun getInstance(context: Context): CustomerDatabase = instance ?: synchronized(this) {
            instance ?: Room.databaseBuilder(
                context.applicationContext,
                CustomerDatabase::class.java,
                "customers.db"
            ).fallbackToDestructiveMigration(true).build().also { instance = it }
        }
    }
}

class CustomerRepository(private val dao: CustomerDao) {
    val customers = dao.getAllCustomers()
    suspend fun insert(customer: Customer) = dao.insert(customer)
    suspend fun delete(customer: Customer) = dao.delete(customer)
}
