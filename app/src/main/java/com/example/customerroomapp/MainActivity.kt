package com.example.customerroomapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.customerroomapp.ui.theme.CustomerRoomAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent { CustomerRoomAppTheme { CustomerApp() } }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomerApp(model: CustomerViewModel = viewModel()) {
    val customers by model.customers.collectAsStateWithLifecycle()
    var name by rememberSaveable { mutableStateOf("") }
    var age by rememberSaveable { mutableStateOf("") }
    var isActive by rememberSaveable { mutableStateOf(true) }
    var submitted by rememberSaveable { mutableStateOf(false) }
    val ageValue = age.toIntOrNull()

    Scaffold(
        topBar = { TopAppBar(title = { Text("Room Customer Demo") }) }
    ) { padding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(padding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = name,
                        onValueChange = { name = it },
                        label = { Text("Customer name") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        isError = submitted && name.isBlank(),
                        supportingText = { if (submitted && name.isBlank()) Text("Name cannot be blank") }
                    )
                    OutlinedTextField(
                        value = age,
                        onValueChange = { age = it },
                        label = { Text("Age") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        isError = submitted && ageValue == null,
                        supportingText = { if (submitted && ageValue == null) Text("Age must be a valid integer") }
                    )
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Switch(checked = isActive, onCheckedChange = { isActive = it })
                        Spacer(Modifier.width(8.dp))
                        Text("Active customer")
                    }
                    Button(onClick = {
                        submitted = true
                        if (name.isNotBlank() && ageValue != null) {
                            model.addCustomer(name, ageValue, isActive)
                            name = ""
                            age = ""
                            isActive = true
                            submitted = false
                        }
                    }) { Text("Add Customer") }
                }
            }
            if (customers.isEmpty()) {
                item { Text("No customers in the database.") }
            }
            items(customers, key = { it.id }) { customer ->
                Card(Modifier.fillMaxWidth()) {
                    Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text(customer.name, style = MaterialTheme.typography.titleLarge)
                        Text("ID: ${customer.id}")
                        Text("Age: ${customer.age}")
                        Text(if (customer.isActive) "Active customer" else "Inactive customer")
                        Button(onClick = { model.deleteCustomer(customer) }) { Text("Delete") }
                    }
                }
            }
        }
    }
}
