package com.example.contactsbook.activity

import android.content.ContentResolver
import android.content.Context
import android.content.pm.PackageManager
import android.database.Cursor
import android.Manifest
import android.content.ContentUris
import android.provider.ContactsContract
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.appcompat.app.AppCompatActivity
import android.net.Uri
import android.util.Log

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

    // Fetch contacts data
    fun fetchContacts(): List<Pair<String, Long>> {
        val contactsList = mutableListOf<Pair<String, Long>>()
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
                val contactID = it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.CONTACT_ID)
                while (it.moveToNext()) {
                    // Check if the columns exist before extracting data
                    val name = if (displayNameIndex != -1 && !it.isNull(displayNameIndex)) {
                        it.getString(displayNameIndex)
                    } else {
                        ""
                    }
                    val contactNumID = if (contactID != -1 && !it.isNull(contactID)) {
                        it.getLong(contactID)
                    } else {
                        0L
                    }
                    if (name.isNotEmpty() && contactNumID != 0L) {
                        contactsList.add(Pair(name, contactNumID))
                    }
                }
            }
        }
        return contactsList
    }

    // Fetches detailed contact information based on the provided contact ID
    fun getContactDetails(contactId: Long): ContactDetails {
        val contentResolver = context.contentResolver

        // Log that the function to get contact details has been called
        Log.v("check_contact", "get contact details called")

        // Construct the URI for the contact using the provided contact ID
        val contactUri = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, contactId)
        // Log the constructed URI for debugging purposes
        Log.d("check_contact", "Constructed URI: $contactUri")

        // Query the content resolver to fetch contact details
        val cursor = contentResolver.query(contactUri, null, null, null, null)
        // Log the result cursor for debugging purposes
        Log.d("check_contact", "Constructed URI: $cursor")

        var displayName = ""

        cursor?.use {
            Log.v("check_contact", "cursor execute")
            // Move to the first row of the cursor
            if (it.moveToFirst()) {
                Log.v("check_contact", "move to first called")
                // Retrieve the index of the display name column
                val displayNameIndex = it.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)
                if (displayNameIndex != -1) {
                    Log.v("check_contact", "get name")
                    // Get the display name value
                    displayName = it.getString(displayNameIndex)
                } else {
                    // Handle the case when DISPLAY_NAME column doesn't exist
                    Log.v("check_contact", "doesnt exist column")
                }
            }
        }

        var phoneNumber: String? = null
        val phoneNumberCursor = contentResolver.query(
            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
            null,
            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
            arrayOf(contactId.toString()),
            null
        )

        phoneNumberCursor?.use { phoneCursor ->
            if (phoneCursor.moveToFirst()) {
                val phoneNumberIndex = phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)
                if (phoneNumberIndex != -1) {
                    phoneNumber = phoneCursor.getString(phoneNumberIndex)
                } else {
                    // Handle the case when NUMBER column doesn't exist
                    Log.v("check_contact","doesnt exist column")

                }
            }
        }

        var emailAddress: String? = null
        val emailCursor = contentResolver.query(
            ContactsContract.CommonDataKinds.Email.CONTENT_URI,
            null,
            ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = ?",
            arrayOf(contactId.toString()),
            null
        )

        emailCursor?.use { emailCursor ->
            if (emailCursor.moveToFirst()) {
                val emailIndex = emailCursor.getColumnIndex(ContactsContract.CommonDataKinds.Email.ADDRESS)
                if (emailIndex != -1) {
                    emailAddress = emailCursor.getString(emailIndex)
                } else {
                    // Handle the case when ADDRESS column doesn't exist
                    Log.v("check_contact","doesnt exist column")

                }
            }
        }

        var birthday: String? = null
        val birthdayCursor = contentResolver.query(
            ContactsContract.Data.CONTENT_URI,
            null,
            ContactsContract.Data.CONTACT_ID + " = ? AND " + ContactsContract.Data.MIMETYPE + " = ?",
            arrayOf(contactId.toString(), ContactsContract.CommonDataKinds.Event.CONTENT_ITEM_TYPE),
            null
        )

        birthdayCursor?.use { cursor ->
            while (cursor.moveToNext()) {
                val eventTypeIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Event.TYPE)
                val birthdayIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Event.START_DATE)
                if (eventTypeIndex != -1 && birthdayIndex != -1) {
                    val eventType = cursor.getInt(eventTypeIndex)
                    if (eventType == ContactsContract.CommonDataKinds.Event.TYPE_BIRTHDAY) {
                        birthday = cursor.getString(birthdayIndex)
                        break // Assume we only need one birthday
                    }
                } else {
                    // Handle the case when TYPE or START_DATE columns don't exist
                    Log.v("check_contact","doesnt exist column")

                }
            }
        }

        // ... (address retrieval code)

        var address: String? = null
        val addressCursor = contentResolver.query(
            ContactsContract.CommonDataKinds.StructuredPostal.CONTENT_URI,
            null,
            ContactsContract.CommonDataKinds.StructuredPostal.CONTACT_ID + " = ?",
            arrayOf(contactId.toString()),
            null
        )

        addressCursor?.use { cursor ->
            if (cursor.moveToFirst()) {
                val addressIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.FORMATTED_ADDRESS)
                if (addressIndex != -1) {
                    address = cursor.getString(addressIndex)
                } else {
                    // Handle the case when FORMATTED_ADDRESS column doesn't exist
                    Log.v("check_contact","doesnt exist column")

                }
            }
        }

        return ContactDetails(displayName, phoneNumber.toString(), emailAddress.toString(), address.toString(), birthday.toString())
    }

    data class ContactDetails(
            val displayName: String,
            val phoneNumbers: String,
            val emailAddresses: String,
            val address : String,
            val birthday :String
        )
    }
