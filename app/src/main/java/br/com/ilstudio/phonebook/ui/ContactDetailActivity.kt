package br.com.ilstudio.phonebook.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import br.com.ilstudio.phonebook.R
import br.com.ilstudio.phonebook.database.DBHelper
import br.com.ilstudio.phonebook.databinding.ActivityContactDetailBinding
import br.com.ilstudio.phonebook.model.ContactModel

class ContactDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityContactDetailBinding
    private lateinit var contactModel: ContactModel
    private lateinit var launcher: ActivityResultLauncher<Intent>
    private var imageId: Int? = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityContactDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val db = DBHelper(this)
        val i = intent
        val itemId = i.extras?.getInt("id")

        if(itemId != null) {
            contactModel = db.getContact(itemId)

            binding.editName.setText(contactModel.name)
            binding.editEmail.setText(contactModel.email)
            binding.editPhone.setText(contactModel.phone)

            if(contactModel.imageId > 0) {
                binding.imageContact.setImageResource(contactModel.imageId)
            } else {
                binding.imageContact.setImageResource(R.drawable.ic_profile_default)
            }
        } else {
            Toast
                .makeText(this, "Contact Detail Error", Toast.LENGTH_SHORT)
                .show()

            finish()
        }

        binding.buttonEdit.setOnClickListener {
            binding.containerButtons1.visibility = View.GONE
            binding.containerButtons2.visibility = View.VISIBLE

            changeEditText(true)
        }

        binding.buttonSave.setOnClickListener {
            val name = binding.editName.text.toString()
            val email = binding.editEmail.text.toString()
            val phone = binding.editPhone.text.toString()

            val response = db.updateContact(
                id = contactModel.id,
                name = name,
                email = email,
                phone = phone,
                imageId = imageId!!
            )

            changeEditText(false)

            binding.containerButtons1.visibility = View.VISIBLE
            binding.containerButtons2.visibility = View.GONE

            if(response > 0) {
                Toast
                    .makeText(this, getString(R.string.update_ok), Toast.LENGTH_SHORT)
                    .show()

                setResult(1, i)
            } else {
                Toast
                    .makeText(this, getString(R.string.update_error), Toast.LENGTH_SHORT)
                    .show()

                setResult(0, i)
                finish()
            }
        }

        binding.buttonRemove.setOnClickListener {
            val response = db.deleteContact(contactModel.id)

            if(response > 0) {
                Toast
                    .makeText(this, getString(R.string.delete_ok), Toast.LENGTH_SHORT)
                    .show()

                setResult(1, i)
                finish()
            } else {
                Toast
                    .makeText(this, getString(R.string.delete_error), Toast.LENGTH_SHORT)
                    .show()

                setResult(0, i)
                finish()
            }
        }

        binding.buttonCancel.setOnClickListener {
            binding.containerButtons1.visibility = View.VISIBLE
            binding.containerButtons2.visibility = View.GONE

            changeEditText(false)

            binding.editName.setText(contactModel.name)
            binding.editEmail.setText(contactModel.email)
            binding.editPhone.setText(contactModel.phone)

            if(contactModel.imageId > 0) {
                binding.imageContact.setImageResource(contactModel.imageId)
            } else {
                binding.imageContact.setImageResource(R.drawable.ic_profile_default)
            }
        }

        binding.buttonClose.setOnClickListener { finish() }

        binding.imageContact.setOnClickListener {
            if(binding.imageContact.isFocusable) {
                launcher.launch((Intent(applicationContext, ContactImageSelectionActivity::class.java)))
            }
        }

        launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if(it.data != null && it.resultCode == 1) {
                if(it.data?.extras != null) {
                    imageId = it.data?.getIntExtra("id", 0)!!
                    binding.imageContact.setImageResource(imageId!!)
                }

            } else if(it.resultCode == 2) {
                println()
            } else {
                imageId = -1
                binding.imageContact.setImageResource(R.drawable.ic_profile_default)
            }
        }
    }

    private fun changeEditText(status: Boolean) {
        binding.editName.isEnabled = status
        binding.editEmail.isEnabled = status
        binding.editPhone.isEnabled = status
        binding.imageContact.isFocusable = status
    }

}