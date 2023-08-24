package com.example.contactsbook.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import com.example.contactsbook.R

class MainActivity : AppCompatActivity() {
    private lateinit var contactProvider: ContactContentProvider
    private lateinit var contactsListView: ListView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.hide()

        // Initialize the ContactContentProvider
        contactProvider = ContactContentProvider(this)

        // Get the reference to the ListView
        contactsListView = findViewById(R.id.contactsListView)

        // Request contacts permission if not granted
        contactProvider.requestContactsPermission()

        // Fetch contacts and display in ListView
        displayContacts()
    }
    private fun displayContacts() {
        // Fetch contacts using the ContactContentProvider
        val contacts = contactProvider.fetchContacts()

        // Create an array of contact strings for the ListView adapter
        val contactStrings = contacts.map { "${it.first}" }

        // Create an ArrayAdapter and set it to the ListView
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, contactStrings)
        contactsListView.adapter = adapter
        // Set the item click listener for the ListView
        contactsListView.setOnItemClickListener { _, _, position, _ ->
            val selectedContact = contactProvider.fetchContacts()[position]

            // Create an intent to open the ContactDetailsActivity
            val intent = Intent(this, ContactInformationActivity::class.java).apply {
                putExtra("contactID", selectedContact.second)
            }
            startActivity(intent)
        }
    }

    fun OpenMenuFunc(view: View) {
        val intent = Intent(this, ContactInformationActivity::class.java)
        startActivity(intent)
    }
    fun addContactFunc(view: View) {}
}