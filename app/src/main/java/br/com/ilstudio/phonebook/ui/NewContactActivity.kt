package br.com.ilstudio.phonebook.ui

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import br.com.ilstudio.phonebook.R
import br.com.ilstudio.phonebook.database.DBHelper
import br.com.ilstudio.phonebook.databinding.ActivityNewContactBinding

class NewContactActivity : AppCompatActivity() {
    private lateinit var binding: ActivityNewContactBinding
    private lateinit var launcher: ActivityResultLauncher<Intent>
    private var id: Int? = -1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewContactBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val db = DBHelper(this)
        val i = intent

        binding.buttonAdd.setOnClickListener {
            val name = binding.editName.text.toString()
            val email = binding.editEmail.text.toString()
            val phone = binding.editPhone.text.toString()
            var imageId = -1
            if(id != null) imageId = id as Int

            val sharedPreferences = application.getSharedPreferences("login", Context.MODE_PRIVATE)
            val userId: Int = sharedPreferences.getInt("userId", 0)

            if(name.isNotEmpty()  && (email.isBlank().not() || phone.isBlank().not()) && userId > 0) {
                val response = db.insertContact(name = name, email = email, phone = phone, imageId = imageId, userId = userId)

                if(response > 0) {
                    Toast
                        .makeText(this, "Insert OK!", Toast.LENGTH_SHORT)
                        .show()

                    setResult(1, i)
                    finish()
                } else {
                    Toast
                        .makeText(this, "Insert Error!", Toast.LENGTH_SHORT)
                        .show()
                }
            } else {
                Toast
                    .makeText(this, "please insert email or phone", Toast.LENGTH_SHORT)
                    .show()
            }
        }

        binding.buttonClose.setOnClickListener {
            setResult(0, i)
            finish()
        }

        binding.imageContact.setOnClickListener {
            launcher.launch((Intent(applicationContext, ContactImageSelectionActivity::class.java)))
        }

        launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if(it.data != null && it.resultCode == 1) {
                id = it.data?.extras?.getInt("id")

                binding.imageContact.setImageResource(id!!)
            } else if(it.resultCode == 2) {
                println()
            }
            else {
                id = -1
                binding.imageContact.setImageResource(R.drawable.ic_profile_default)
            }
        }
    }
}