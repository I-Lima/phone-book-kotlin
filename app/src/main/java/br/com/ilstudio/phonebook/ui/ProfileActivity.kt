package br.com.ilstudio.phonebook.ui

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import br.com.ilstudio.phonebook.R
import br.com.ilstudio.phonebook.Utils
import br.com.ilstudio.phonebook.database.DBHelper
import br.com.ilstudio.phonebook.databinding.ActivityProfileBinding
import br.com.ilstudio.phonebook.model.UserModel

class ProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProfileBinding
    private lateinit var db: DBHelper
    private lateinit var editor: SharedPreferences.Editor
    private lateinit var launcher: ActivityResultLauncher<Intent>
    private var imageId: Int? = -1
    private lateinit var i: Intent
    private lateinit var user: UserModel
    private lateinit var sharedPreferences: SharedPreferences
    private val utils = Utils()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = DBHelper(this)
        sharedPreferences = applicationContext.getSharedPreferences(
            "login",
            Context.MODE_PRIVATE
        )

        i = intent
        val userId = i.extras?.getInt("id")
        val userImageId = i.extras?.getInt("imageId")
        getUser(userId!!)
        val password = user.password

        editor = sharedPreferences.edit()

        val username = user.username
        binding.editUsername.setText(username)

        if(userImageId != null && userImageId > 0) {
            binding.imageProfile.setImageResource(userImageId)
        } else {
            binding.imageProfile.setImageResource(R.drawable.ic_profile_default)
        }

        binding.buttonClose.setOnClickListener {
            i.putExtra("userImageId", imageId)
            setResult(1, i)
            finish()
        }

        binding.imageProfile.setOnClickListener {
            if(binding.imageProfile.isFocusable) {
                launcher.launch((Intent(applicationContext, ContactImageSelectionActivity::class.java)))
            }
        }

        binding.buttonDeleteAccount.setOnClickListener {
            val response = db.deleteUser(userId)

            if(response > 0) {
                Toast
                    .makeText(this, getString(R.string.delete_account_ok), Toast.LENGTH_SHORT)
                    .show()

                utils.clearData(editor)
                startActivity(Intent(this, LoginActivity::class.java))
            } else {
                Toast
                    .makeText(this, getString(R.string.delete_account_error), Toast.LENGTH_SHORT)
                    .show()
            }
        }

        binding.editProfile.setOnClickListener{
            binding.containerButtons.visibility = View.VISIBLE
            binding.editProfile.visibility = View.GONE

            binding.editPassword.setText("")

            changeEditText(true)
        }

        binding.buttonCancel.setOnClickListener {
            binding.containerButtons.visibility = View.GONE
            binding.editProfile.visibility = View.VISIBLE

            binding.editPassword.setText(R.string.password)

            changeEditText(false)

            binding.editUsername.setText(username)

            if(userImageId!! > 0) {
                binding.imageProfile.setImageResource(userImageId)
            } else {
                binding.imageProfile.setImageResource(R.drawable.ic_profile_default)
            }
        }

        binding.buttonSave.setOnClickListener {
            val name = binding.editUsername.text.toString()
            val pass = binding.editPassword.text.toString()

            val nameUser = name.takeIf { it.isNotBlank() } ?: username
            val userPass = pass.takeIf { it.isNotBlank() } ?: password
            val userImgId = imageId.takeIf { it!! > 0  } ?: userImageId

            val response = db.updateUser(
                id = userId,
                username = nameUser,
                password = userPass,
                imageId = userImgId!!
            )
            imageId = userImgId

            changeEditText(false)

            binding.containerButtons.visibility = View.GONE
            binding.editProfile.visibility = View.VISIBLE

            getUser(userId)

            println(user)

            if(response > 0) {
                Toast
                    .makeText(this, getString(R.string.update_ok), Toast.LENGTH_SHORT)
                    .show()
            } else {
                Toast
                    .makeText(this, getString(R.string.update_error), Toast.LENGTH_SHORT)
                    .show()
                finish()
            }
        }

        launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if(it.data != null && it.resultCode == 1) {
                if(it.data?.extras != null) {
                    imageId = it.data?.getIntExtra("id", 0)!!
                    binding.imageProfile.setImageResource(imageId!!)
                }

            } else if(it.resultCode == 2) {
                println()
            } else {
                imageId = -1
                binding.imageProfile.setImageResource(R.drawable.ic_profile_default)
            }
        }
    }

    private fun changeEditText(status: Boolean) {
        binding.editUsername.isEnabled = status
        binding.editPassword.isEnabled = status
        binding.imageProfile.isFocusable = status
    }

    private fun getUser(id: Int) {
        if(id > 0) {
            user = db.getUserById(id)
        }
    }
}