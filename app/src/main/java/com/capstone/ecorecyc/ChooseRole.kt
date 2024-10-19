package com.capstone.ecorecyc

import android.content.Intent
import android.os.Bundle
import android.util.TypedValue
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class ChooseRole : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_choose_role)

        // Reference to the buttons
        val userBtn: Button = findViewById(R.id.radio_user)
        val junkOwnerBtn: Button = findViewById(R.id.radio_junkshop_owner)

        // Apply the ripple effect programmatically to both buttons
        applyRippleEffect(userBtn)
        applyRippleEffect(junkOwnerBtn)

        // Set onClickListeners
        userBtn.setOnClickListener {
            val intent = Intent(this, Login::class.java)
            intent.putExtra("USER_TYPE", "USER")
            startActivity(intent)
        }

        junkOwnerBtn.setOnClickListener {
            val intent = Intent(this, Login::class.java)
            intent.putExtra("USER_TYPE", "JUNKSHOP_OWNER")
            startActivity(intent)
        }
    }

    // Function to apply ripple effect to any button
    private fun applyRippleEffect(button: Button) {
        val outValue = TypedValue()
        theme.resolveAttribute(android.R.attr.selectableItemBackground, outValue, true)
        button.foreground = getDrawable(outValue.resourceId)
    }
}
