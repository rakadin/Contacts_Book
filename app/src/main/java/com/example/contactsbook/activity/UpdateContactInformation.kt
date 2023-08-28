package com.example.contactsbook.activity

import android.Manifest.permission.WRITE_CONTACTS
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.core.app.ActivityCompat
import android.Manifest
import com.example.contactsbook.R

class UpdateContactInformation : AppCompatActivity() {
    private var longId = 0L
    private var tempName =""
    private var tempPhone =""
    private var tempEmail =""
    private var tempAddress =""
    private lateinit var edtName : EditText
    private lateinit var edtPhone : EditText
    private lateinit var edtEmail : EditText
    private lateinit var edtAddress : EditText
    private lateinit var contactContentProvider : ContactContentProvider
    private val WRITE_CONTACTS_PERMISSION_REQUEST = 2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_contact_information)
        supportActionBar?.hide()
        contactContentProvider = ContactContentProvider(this)
        getIDs()
        longId = intent.getLongExtra("contactID", 0)
        updateUI()
    }
    fun getIDs(){
        edtName = findViewById(R.id.edt_name)
        edtPhone = findViewById(R.id.edt_phonenum)
        edtEmail = findViewById(R.id.edt_email)
        edtAddress = findViewById(R.id.edt_address)
    }
    fun updateUI(){
        val contactdetails : ContactContentProvider.ContactDetails = contactContentProvider.getContactDetails(longId)
        edtName.setText(contactdetails.displayName)
        edtPhone.setText(contactdetails.phoneNumbers)
        edtEmail.setText(contactdetails.emailAddresses)
        edtAddress.setText(contactdetails.address)

    }

    fun updateConfirmFunc(view: View) {
        val updatedDetails = ContactContentProvider.ContactDetails(
            edtName.text.toString().trim(),
            edtPhone.text.toString().trim(),
            edtEmail.text.toString().trim(),
            edtAddress.text.toString().trim(),
            ""
        )
        val updateResult = contactContentProvider.updateContactByID(longId, updatedDetails)

        if (updateResult) {
            // Update successful
            Toast.makeText(this, "Update successful", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        } else {
            // Update failed
            Toast.makeText(this, "Update failed", Toast.LENGTH_SHORT).show()
        }

    }

    fun updateCancelFunc(view: View) {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }
}