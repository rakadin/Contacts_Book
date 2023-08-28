package com.example.contactsbook.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import com.example.contactsbook.R
import java.text.SimpleDateFormat
import java.util.*

class ContactInformationActivity : AppCompatActivity() {
    private lateinit var detailsBut : TextView
    private lateinit var historyBut : TextView
    private lateinit var frameLayout: FrameLayout
    private lateinit var name_in_txt : TextView
    private lateinit var phone_in_txt : TextView
    private lateinit var email_in_txt : TextView
    private lateinit var address_in_txt : TextView
    private lateinit var birthday_in_txt : TextView
    private lateinit var contactContentProvider : ContactContentProvider
    private var  longId : Long = 0


    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        intent?.let {
            longId = it.getLongExtra("contactID", 0)
            setTextFromChooser(longId)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contact_information)
        supportActionBar?.hide()
        longId = intent.getLongExtra("contactID",0)
        contactContentProvider = ContactContentProvider(this)
        getIDs()
        setTextFromChooser(longId)
    }
    fun getIDs(){
        detailsBut = findViewById(R.id.detail_button)
        historyBut = findViewById(R.id.history_button)
        frameLayout = findViewById(R.id.layout_insert)
        name_in_txt = findViewById(R.id.name_in)
        phone_in_txt = findViewById(R.id.txt_phonenum)
        email_in_txt = findViewById(R.id.email_in)
        address_in_txt = findViewById(R.id.address_in)
        birthday_in_txt = findViewById(R.id.birthday_in)
    }
    fun BackToMainFunc(view: View) {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }
    fun setTextFromChooser(contactID : Long){
        /*
        after get the ID clickedfrom previous, get another data by its ID
         */
        val contactdetails : ContactContentProvider.ContactDetails = contactContentProvider.getContactDetails(contactID!!)
        name_in_txt.text = contactdetails.displayName
        phone_in_txt.text = formatPhoneNumber(contactdetails.phoneNumbers)
        /*
       if(contact doesn't have some data (null) -> set null text
   */
        if (contactdetails.emailAddresses == "null" ){
            email_in_txt.text = "No data"
        } else {
            email_in_txt.text = contactdetails.emailAddresses
        }

        if (contactdetails.address == "null" ) {
            address_in_txt.text = "No data"
        } else {
            address_in_txt.text = contactdetails.address
        }

        if (contactdetails.birthday == "null") {
            birthday_in_txt.text = "No data"
        } else {
            birthday_in_txt.text = formatDate(contactdetails.birthday)
        }
        longId = contactID
//        Toast.makeText(this, "$longId", Toast.LENGTH_SHORT).show()
        Log.v("check_value","$contactdetails.emailAddresses")

    }
    fun UpdateContactInfo(view: View) {
        // Create an intent to open the ContactDetailsActivity
        val intent2 = Intent(this, UpdateContactInformation::class.java).apply {
            putExtra("contactID", longId)
////            putExtra("contactName", name_in_txt.text.toString().trim())
////            putExtra("contactNumber", phone_in_txt.text.toString().trim())
////            putExtra("contactEmail", email_in_txt.text.toString().trim())
////            putExtra("contactAddress", address_in_txt.text.toString().trim())
        }
        startActivity(intent2)
        Log.v("check","update but called")
    }
    fun ShareContactInfo(view: View) {
        val contactdetails : ContactContentProvider.ContactDetails = contactContentProvider.getContactDetails(longId!!)

        val contactInformation = "Name: ${contactdetails.displayName}\nPhone: ${contactdetails.phoneNumbers}"

        // Create an intent to share text
        val sharingIntent = Intent(Intent.ACTION_SEND)
        sharingIntent.type = "text/plain"
        sharingIntent.putExtra(Intent.EXTRA_SUBJECT, "Contact Information")
        sharingIntent.putExtra(Intent.EXTRA_TEXT, contactInformation)

        // Start the sharing activity
        startActivity(Intent.createChooser(sharingIntent, "Share Contact Information"))
    }
    fun DeleteContactInfo(view: View) {
        // Create an AlertDialog.Builder
        val alertDialogBuilder = AlertDialog.Builder(this)

        // Set the title and message for the dialog
        alertDialogBuilder.setTitle("Delete Contact")
        alertDialogBuilder.setMessage("Are you sure you want to delete this contact?")

        // Set positive button
        alertDialogBuilder.setPositiveButton("Yes") { dialog, which ->
            // Code to handle positive button click (delete the contact)
            val deleteResult = contactContentProvider.deleteContactByID(longId)

            if (deleteResult) {
                // Deletion successful
                Toast.makeText(this, "Contact deleted", Toast.LENGTH_SHORT).show()
                val intent = Intent(this,MainActivity::class.java)
                startActivity(intent)
            } else {
                // Deletion failed
                Toast.makeText(this, "Failed to delete contact", Toast.LENGTH_SHORT).show()
            }
        }

        // Set negative button
        alertDialogBuilder.setNegativeButton("No") { dialog, which ->
            // Code to handle negative button click (dismiss the dialog)
            dialog.dismiss()
        }

        // Create and show the AlertDialog
        val alertDialog: AlertDialog = alertDialogBuilder.create()
        alertDialog.show()

    }

    fun ShowDetailsFunc(view: View) {
        // update buttonUI
        detailsBut.setBackgroundResource(R.drawable.rounded_blue_button)
        historyBut.setBackgroundResource(R.color.white)
        detailsBut.setTextColor(ContextCompat.getColor(this, android.R.color.white))
        historyBut.setTextColor(ContextCompat.getColor(this, android.R.color.black)) // Just an example color

        // Inflate the contact_details.xml layout
        val inflater = LayoutInflater.from(this)
        val detailsView = inflater.inflate(R.layout.contact_detail, null)

        // Access the views in the new layout and set the text
        val address_in_txt_new = detailsView.findViewById<TextView>(R.id.address_in)
        val phone_in_txt_new = detailsView.findViewById<TextView>(R.id.txt_phonenum)
        val email_in_txt_new = detailsView.findViewById<TextView>(R.id.email_in)
        val birthday_in_txt_new = detailsView.findViewById<TextView>(R.id.birthday_in)

        val contactdetails: ContactContentProvider.ContactDetails = contactContentProvider.getContactDetails(longId)
        Log.v("check","contact details: $contactdetails")
        phone_in_txt_new.text = formatPhoneNumber(contactdetails.phoneNumbers)
        Log.v("check","contact details: ${contactdetails.displayName}")
        name_in_txt.text = contactdetails.displayName // because name_in_txt doesnt include in new inflate
        if(contactdetails.address == "null"){
            address_in_txt_new.text = "No data"
        }
        else{
            address_in_txt_new.text = contactdetails.address
        }
        if(contactdetails.emailAddresses == "null"){
            email_in_txt_new.text = "No data"
        }
        else{
            email_in_txt_new.text = contactdetails.emailAddresses
        }
        if(contactdetails.birthday == "null"){
            birthday_in_txt_new.text = "No data"
        }
        else{
            birthday_in_txt_new.text = formatDate(contactdetails.birthday)
        }
        // Clear previous content and add the inflated view to the FrameLayout
        frameLayout.removeAllViews()
        frameLayout.addView(detailsView)
    }

    fun ShowHistoryFunc(view: View) {
        // update buttonUI
        historyBut.setBackgroundResource(R.drawable.rounded_blue_button)
        detailsBut.setBackgroundResource(R.color.white)
        historyBut.setTextColor(ContextCompat.getColor(this, android.R.color.white))
        detailsBut.setTextColor(ContextCompat.getColor(this, android.R.color.black)) // Just an example color
        // Inflate the contact_details.xml layout
        val inflater = LayoutInflater.from(this)
        val detailsView = inflater.inflate(R.layout.history_infomation, null)

        // Clear previous content and add the inflated view to the FrameLayout
        frameLayout.removeAllViews()
        frameLayout.addView(detailsView)
    }
    fun formatDate(inputDate: String): String {

        val inputFormat = SimpleDateFormat("yyyyMMdd", Locale.getDefault())
        val outputFormat = SimpleDateFormat("yyyy/MM/dd", Locale.getDefault())

        val date = inputFormat.parse(inputDate)
        return outputFormat.format(date!!)
    }
    fun formatPhoneNumber(phoneNumber: String): String {
        if (phoneNumber.length == 10) {
            return "${phoneNumber.substring(0, 4)} ${phoneNumber.substring(4, 7)} ${phoneNumber.substring(7)}"
        } else {
            return phoneNumber
        }
    }
}

