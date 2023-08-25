package com.example.contactsbook.activity

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.DatePicker
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import com.example.contactsbook.R

class AddContactActivity : AppCompatActivity() {
    private lateinit var nameEdtIn : EditText
    private lateinit var phoneNumEdt : EditText
    private lateinit var emailEdt : EditText
    private lateinit var addressEdt : EditText
    private lateinit var birthdayPicker: DatePicker
    private lateinit var tick_but: ImageView
    private var isInsertable = false
    private var isFormatting = false // To prevent recursive calls

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_contact)
        supportActionBar?.hide()
        getIDs()
    }

    override fun onResume() {
        super.onResume()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            birthdayPicker.setOnDateChangedListener { view, year, monthOfYear, dayOfMonth ->
                // The user selected a new date, do something with it
                // You can use year, monthOfYear, and dayOfMonth here
                Toast.makeText(this, "$year/$monthOfYear/$dayOfMonth", Toast.LENGTH_SHORT).show()
            }
        }
        getTextChange()
        formatPhoneNumberOnTextChange()
    }
    fun getTextChange(){
        nameEdtIn.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
                // This method is called before the text is changed.
            }

            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
                // This method is called when the text is being changed.
                val newText = charSequence.toString().trim()
                // Do something with the new text, like validation or updates.
                if(newText!=""){
                    tick_but.setImageResource(R.drawable.green_tick)
                    isInsertable = true
                }
                else{
                    tick_but.setImageResource(R.drawable.gray_tick)
                    isInsertable = false

                }
            }

            override fun afterTextChanged(editable: Editable) {
                // This method is called after the text has been changed.
            }
        })
    }
    fun formatPhoneNumberOnTextChange(){
        phoneNumEdt.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
                // This method is called before the text is changed.
            }

            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
                // This method is called when the text is being changed.
                // Do something with the new text, like validation or updates.
                if (!isFormatting) {
                    isFormatting = true

                    // Remove any existing non-numeric characters
                    val digitsOnly = charSequence.toString().replace("\\D+".toRegex(), "")

                    // Apply the desired formatting (0357 623 945)
                    val formattedText = StringBuilder()
                    for (i in digitsOnly.indices) {
                        if (i == 4 || i == 7) {
                            formattedText.append(" ")
                        }
                        formattedText.append(digitsOnly[i])
                    }

                    // Set the formatted text
                    phoneNumEdt.setText(formattedText)
                    phoneNumEdt.setSelection(formattedText.length) // Move cursor to the end

                    isFormatting = false
                }
            }
            override fun afterTextChanged(editable: Editable) {
                // This method is called after the text has been changed.
            }
        })
    }
    fun getIDs(){
        nameEdtIn = findViewById(R.id.name_edt)
        phoneNumEdt = findViewById(R.id.phone_number_edt)
        emailEdt = findViewById(R.id.email_edt)
        addressEdt = findViewById(R.id.address_edt)
        birthdayPicker = findViewById(R.id.birthday_picker)
        tick_but = findViewById(R.id.tick_button)
    }
    fun cancelAddFunc(view: View) {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }
    fun acceptAddFunc(view: View) {
        if(isInsertable == true){
            Toast.makeText(this, "inserted", Toast.LENGTH_SHORT).show()
        }
        else{
            Toast.makeText(this, "You have to insert contact name!", Toast.LENGTH_SHORT).show()

        }
    }
}