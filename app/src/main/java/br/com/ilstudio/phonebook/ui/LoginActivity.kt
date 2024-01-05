package br.com.ilstudio.phonebook.ui

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import br.com.ilstudio.phonebook.R
import br.com.ilstudio.phonebook.database.DBHelper
import br.com.ilstudio.phonebook.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val db = DBHelper(this)

        sharedPreferences = application.getSharedPreferences("login", Context.MODE_PRIVATE)
        val usernameSharedPreferences = sharedPreferences.getString("username", "")
        val passwordSharedPreferences = sharedPreferences.getString("password", "")
        val loggedSharedPreferences = sharedPreferences.getBoolean("logged", false)


        if (usernameSharedPreferences != null && passwordSharedPreferences != null) {
            if(
                usernameSharedPreferences.isNotEmpty() &&
                passwordSharedPreferences.isNotEmpty() &&
                loggedSharedPreferences
                ) {
                startActivity(Intent(this, MainActivity::class.java))
            }
        }

        binding.buttonLogin.setOnClickListener{
            val username = binding.editUsername.text.toString()
            val password = binding.editPassword.text.toString()
            val logged = binding.checkboxLogged.isChecked

            if(username.isEmpty() || password.isEmpty()) {
                Toast.makeText(
                    this,
                    getString(R.string.please_insert_all_required_fields),
                    Toast.LENGTH_SHORT
                ).show()
            }

            val response = db.login(username, password)

            if(response) {
                val editor: SharedPreferences.Editor = sharedPreferences.edit()
                editor.putString("username", username)
                editor.putString("password", password)
                editor.putBoolean("logged", logged)
                editor.apply()

                startActivity(Intent(this, MainActivity::class.java))
                finish()
            } else {
                Toast.makeText(
                    this,
                    getString(R.string.login_error),
                    Toast.LENGTH_SHORT
                ).show()

                binding.editUsername.setText("")
                binding.editPassword.setText("")
            }
        }

        binding.textRecoverPassword.setOnClickListener() {

        }

        binding.textSignup.setOnClickListener {
            startActivity(Intent(this, SignUpActivity::class.java))
        }
    }
}

