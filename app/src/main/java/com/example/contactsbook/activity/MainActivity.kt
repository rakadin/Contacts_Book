package com.example.contactsbook.activity

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.contactsbook.R

class MainActivity : AppCompatActivity() {
    private lateinit var contactProvider: ContactContentProvider
    private lateinit var contactsListView: ListView
    private lateinit var imageWhenNoData : ImageView
    private lateinit var textWhenNoData :TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.hide()

        // Initialize the ContactContentProvider
        contactProvider = ContactContentProvider(this)

        // Get the reference to the ListView
        contactsListView = findViewById(R.id.contactsListView)
        imageWhenNoData = findViewById(R.id.tree_when_null)
        textWhenNoData = findViewById(R.id.text_when_null)

        // Request contacts permission if not granted
        contactProvider.requestContactsPermission()

        // Fetch contacts and display in ListView
        displayContacts()
        val searchEditText: EditText = findViewById(R.id.appCompatEditText)
        // Set up the TextWatcher for the searchEditText
        searchEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val query = s.toString()
                sortAndRefreshList(query)
            }

            override fun afterTextChanged(s: Editable?) {}
        })
    }
    private fun sortAndRefreshList(query: String) {
        val contacts = contactProvider.fetchContacts()

        // Filter and sort the contacts list based on the search query
        val filteredAndSortedContacts = contacts.filter { (name, _) ->
            name.contains(query, true)
        }.sortedBy { (name, _) -> name }

        if (filteredAndSortedContacts.isEmpty()) {
            imageWhenNoData.visibility = View.VISIBLE
            textWhenNoData.visibility = View.VISIBLE
            contactsListView.adapter = null // Clear the adapter to hide the list
        } else {
            imageWhenNoData.visibility = View.GONE
            textWhenNoData.visibility = View.GONE
            val contactStrings = filteredAndSortedContacts.map { "${it.first}" }
            val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, contactStrings)
            contactsListView.adapter = adapter

            contactsListView.setOnItemClickListener { _, _, position, _ ->
                val selectedContact = filteredAndSortedContacts[position]
                val intent = Intent(this, ContactInformationActivity::class.java).apply {
                    putExtra("contactID", selectedContact.second)
                }
                startActivity(intent)
            }
        }
    }

    private fun displayContacts() {
        // Fetch contacts using the ContactContentProvider
        val contacts = contactProvider.fetchContacts()

        // Create an array of contact strings for the ListView adapter
        val contactStrings = contacts.map { "${it.first}" }
        if(contactStrings.isEmpty()){ // if phone has no data to show, set image and text to notice
            Log.v("check","No data to showw")
            imageWhenNoData.visibility = View.VISIBLE
            textWhenNoData.visibility = View.VISIBLE
        }
        else{
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

    }

    fun OpenMenuFunc(view: View) {
    }
    fun addContactFunc(view: View) {
        val intent = Intent(this, AddContactActivity::class.java)
        startActivity(intent)
    }
}