package com.capstone.ecorecyc

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.auth.FirebaseAuth
import java.util.UUID

class SellAnItem : AppCompatActivity() {

    private lateinit var uploadBtn: Button
    private lateinit var confirmBtn: Button
    private lateinit var prodName: EditText
    private lateinit var price: EditText
    private lateinit var description: EditText
    private lateinit var condition: EditText
    private lateinit var location: EditText
    private lateinit var username: EditText  // Added username field

    private var imageUri: Uri? = null
    private val storage = FirebaseStorage.getInstance().reference
    private val firestore = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sell_an_item)

        // Initialize views
        uploadBtn = findViewById(R.id.upload_btn)
        confirmBtn = findViewById(R.id.confirm_sell_item_btn)
        prodName = findViewById(R.id.prod_Name)
        price = findViewById(R.id.price)
        description = findViewById(R.id.description)
        condition = findViewById(R.id.condition)
        location = findViewById(R.id.market_Location)
        username = findViewById(R.id.usernameList)  // Initialize username field

        // Set upload button listener to pick image
        uploadBtn.setOnClickListener {
            selectImage()
        }

        // Set confirm button listener to upload image and save item
        confirmBtn.setOnClickListener {
            uploadItem()
        }
    }

    // Launch image picker
    private fun selectImage() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, 100)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 100 && resultCode == Activity.RESULT_OK) {
            imageUri = data?.data // Get image URI from intent result
            Toast.makeText(this, "Image selected!", Toast.LENGTH_SHORT).show()
        }
    }

    // Function to upload item details to Firebase
    private fun uploadItem() {
        val productName = prodName.text.toString()
        val productPrice = price.text.toString()
        val productDescription = description.text.toString()
        val productCondition = condition.text.toString()
        val productLocation = location.text.toString()
        val userName = username.text.toString()  // Get username
        val userId = FirebaseAuth.getInstance().currentUser?.uid // Get current user ID

        if (productName.isEmpty() || productPrice.isEmpty() || productDescription.isEmpty() || productCondition.isEmpty() || productLocation.isEmpty() || imageUri == null || userName.isEmpty()) {
            Toast.makeText(this, "Please fill all fields and select an image", Toast.LENGTH_SHORT).show()
            return
        }

        val fileName = UUID.randomUUID().toString()
        val imageRef = storage.child("items/$fileName.jpg")

        imageUri?.let { uri ->
            imageRef.putFile(uri).addOnSuccessListener {
                imageRef.downloadUrl.addOnSuccessListener { downloadUrl ->
                    val itemData = hashMapOf(
                        "name" to productName,
                        "price" to productPrice,
                        "description" to productDescription,
                        "condition" to productCondition,
                        "location" to productLocation,
                        "imageUrl" to downloadUrl.toString(),
                        "userId" to userId, // Store user ID
                        "username" to userName // Store username
                    )

                    firestore.collection("items").add(itemData).addOnSuccessListener {
                        Toast.makeText(this, "Item uploaded successfully!", Toast.LENGTH_SHORT).show()
                        finish()
                    }.addOnFailureListener {
                        Toast.makeText(this, "Failed to upload item", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }
}
