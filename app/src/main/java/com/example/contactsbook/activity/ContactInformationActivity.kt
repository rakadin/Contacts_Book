package com.example.contactsbook.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.example.contactsbook.R

class ContactInformationActivity : AppCompatActivity() {
    private lateinit var detailsBut : TextView
    private lateinit var historyBut : TextView
    private lateinit var frameLayout: FrameLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contact_information)
        supportActionBar?.hide()
        getIDs()
    }
    fun getIDs(){
        detailsBut = findViewById(R.id.detail_button)
        historyBut = findViewById(R.id.history_button)
        frameLayout = findViewById(R.id.layout_insert)
    }
    fun BackToMainFunc(view: View) {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
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
}