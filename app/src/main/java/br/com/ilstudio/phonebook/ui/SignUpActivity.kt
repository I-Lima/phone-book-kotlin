package br.com.ilstudio.phonebook.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import br.com.ilstudio.phonebook.R
import br.com.ilstudio.phonebook.database.DBHelper
import br.com.ilstudio.phonebook.databinding.ActivitySignUpBinding

class SignUpActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignUpBinding
    private lateinit var launcher: ActivityResultLauncher<Intent>
    private var id: Int? = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val db = DBHelper(this)

        binding.buttonSignUp.setOnClickListener {
            val username = binding.editUsername.text.toString()
            val password = binding.editPassword.text.toString()
            val confirmPassword = binding.editConfirmPassword.text.toString()
            var userImageId = -1
            if(id != null) userImageId = id as Int

            if(username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                Toast.makeText(
                    this,
                    getString(R.string.please_insert_all_required_fields),
                    Toast.LENGTH_SHORT
                ).show()
            }

            if(password == confirmPassword) {
                val response = db.insertUser(username = username, password = password, imageId = userImageId)

                if(response > 0) {
                    Toast.makeText(
                        this,
                        getString(R.string.sign_up_ok),
                        Toast.LENGTH_SHORT
                    ).show()
                    finish()
                } else {
                    Toast.makeText(
                        this,
                        getString(R.string.sign_up_error),
                        Toast.LENGTH_SHORT
                    ).show()

                    binding.editUsername.setText("")
                    binding.editPassword.setText("")
                    binding.editConfirmPassword.setText("")
                }
            } else {
                Toast.makeText(
                    this,
                    getString(R.string.passwords_dont_match),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        binding.buttonAccont.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }

        binding.imageContact.setOnClickListener {
            launcher.launch(Intent(applicationContext, ContactImageSelectionActivity::class.java))
        }

        launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if(it.data != null && it.resultCode == 1) {
                id = it.data?.extras?.getInt("id")

                binding.imageContact.setImageResource(id!!)
            } else if(it.resultCode == 2) {
                println()
            } else {
                id = -1
                binding.imageContact.setImageResource(R.drawable.ic_profile_default)
            }
        }
    }
}