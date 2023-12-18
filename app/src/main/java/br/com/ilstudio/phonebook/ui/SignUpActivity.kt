package br.com.ilstudio.phonebook.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import br.com.ilstudio.phonebook.R
import br.com.ilstudio.phonebook.database.DBHelper
import br.com.ilstudio.phonebook.databinding.ActivitySignUpBinding

class SignUpActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignUpBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val db = DBHelper(this)

        binding.buttonSignUp.setOnClickListener {
            val username = binding.editUsername.text.toString()
            val passoword = binding.editPassword.text.toString()
            val confirmPassord = binding.editConfirmPassword.text.toString()

            if(username.isEmpty() || passoword.isEmpty() || confirmPassord.isEmpty()) {
                Toast.makeText(
                    this,
                    getString(R.string.please_insert_all_required_fields),
                    Toast.LENGTH_SHORT
                ).show()
            }

            if(passoword == confirmPassord) {
                val response = db.insertUser(username, passoword)

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
    }
}