package com.example.contactsbook.activity

import android.content.ContentResolver
import android.content.Context
import android.content.pm.PackageManager
import android.database.Cursor
import android.Manifest
import android.provider.ContactsContract
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.appcompat.app.AppCompatActivity

class ContactContentProvider(private val context: Context) {

    // Permission request code
    private val READ_CONTACTS_PERMISSION_REQUEST = 1

    // Check if the app has contacts permission
    fun hasContactsPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.READ_CONTACTS
        ) == PackageManager.PERMISSION_GRANTED
    }

    // Request contacts permission
    fun requestContactsPermission() {
        if (!hasContactsPermission()) {
            ActivityCompat.requestPermissions(
                context as AppCompatActivity,
                arrayOf(Manifest.permission.READ_CONTACTS),
                READ_CONTACTS_PERMISSION_REQUEST
            )
        }
    }

    //fetch data
    fun fetchContacts(): List<Pair<String, String>> {
        val contactsList = mutableListOf<Pair<String, String>>()
        if (hasContactsPermission()) {
            val contentResolver: ContentResolver = context.contentResolver
            val cursor: Cursor? = contentResolver.query(
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                null,
                null,
                null,
                null
            )

            cursor?.use {
                val displayNameIndex = it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)
                val phoneNumberIndex = it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)

                while (it.moveToNext()) {
                    // Check if the columns exist before extracting data
                    val name = if (displayNameIndex != -1 && !it.isNull(displayNameIndex)) {
                        it.getString(displayNameIndex)
                    } else {
                        ""
                    }
                    val phoneNumber = if (phoneNumberIndex != -1 && !it.isNull(phoneNumberIndex)) {
                        it.getString(phoneNumberIndex)
                    } else {
                        ""
                    }
                    if (name.isNotEmpty() && phoneNumber.isNotEmpty()) {
                        contactsList.add(Pair(name, phoneNumber))
                    }
                }
            }
        }
        return contactsList
    }


}
