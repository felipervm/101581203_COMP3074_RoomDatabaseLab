# Room Customer Demo - COMP3074

Kotlin app using Room, Compose, Flow, ViewModel, LazyColumn and KSP.

The single application table, `customers`, contains exactly `id: Int`
(auto-generated primary key), `name: String`, `age: Int` and `isActive: Boolean`.

The form contains Customer name, Age, an Active customer switch and an Add Customer
button. A name cannot be blank; age must parse as an Int. Customers appear in ID
order with name, ID, age, active status and a Delete button. Deletion removes the
Room entity. An empty list displays "No customers in the database."

Database version 2 uses destructive migration for this development lab: upgrading
from the old version 1 database discards its records and creates the new schema.

Open the project in Android Studio, sync and run the app on API 24 or newer.
Build with `.\gradlew.bat clean assembleDebug` using the existing JDK setup.

Manual verification:
1. Submit a blank name or non-integer age and check the validation messages.
2. Add active and inactive customers and verify all row fields and ID ordering.
3. Restart the app and confirm records persist.
4. Delete records and confirm the empty message after deleting the last one.
