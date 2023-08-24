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
        phone_in_txt.text = contactdetails.phoneNumbers
        email_in_txt.text = contactdetails.emailAddresses
        address_in_txt.text = contactdetails.address
        birthday_in_txt.text = formatDate(contactdetails.birthday)
        longId = contactID
        Toast.makeText(this, "$longId", Toast.LENGTH_SHORT).show()
        Log.v("check_value","$contactdetails.emailAddresses")

    }
    fun UpdateContactInfo(view: View) {}
    fun ShareContactInfo(view: View) {}
    fun DeleteContactInfo(view: View) {}

    fun ShowDetailsFunc(view: View) {
        // update buttonUI
        detailsBut.setBackgroundResource(R.drawable.rounded_blue_button)
        historyBut.setBackgroundResource(R.color.white)
        detailsBut.setTextColor(ContextCompat.getColor(this, android.R.color.white))
        historyBut.setTextColor(ContextCompat.getColor(this, android.R.color.black)) // Just an example color
        // Inflate the contact_details.xml layout
        val inflater = LayoutInflater.from(this)
        val detailsView = inflater.inflate(R.layout.contact_detail, null)

        // Clear previous content and add the inflated view to the FrameLayout
        frameLayout.removeAllViews()
        frameLayout.addView(detailsView)
        Log.v("check_value","$longId")
        setTextFromChooser(longId)
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
}

