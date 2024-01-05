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
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.ilstudio.phonebook.R
import br.com.ilstudio.phonebook.Utils
import br.com.ilstudio.phonebook.adapter.ContactListAdapter
import br.com.ilstudio.phonebook.adapter.listener.ContactOnClickListener
import br.com.ilstudio.phonebook.database.DBHelper
import br.com.ilstudio.phonebook.databinding.ActivityMainBinding
import br.com.ilstudio.phonebook.model.ContactModel
import br.com.ilstudio.phonebook.model.UserModel

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var contactList: List<ContactModel>
    private lateinit var adapter: ContactListAdapter
    private lateinit var result: ActivityResultLauncher<Intent>
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor
    private lateinit var db: DBHelper
    private var user = UserModel()
    private var ascDesc: Boolean = true
    private var imageId: Int? = -1
    private val utils = Utils()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = DBHelper(this)
        sharedPreferences = application.getSharedPreferences("login", Context.MODE_PRIVATE)
        editor = sharedPreferences.edit()

        binding.recycleViewConstants.layoutManager = LinearLayoutManager(applicationContext)

        binding.buttonLogout.setOnClickListener {
            utils.clearData(editor)
            finish()
        }

        binding.buttonAdd.setOnClickListener {
            result.launch(Intent(applicationContext, NewContactActivity::class.java))
        }

        binding.buttonOrder.setOnClickListener {
            if (ascDesc) {
                binding.buttonOrder.setImageResource(R.drawable.ic_arrow_downward)
            } else {
                binding.buttonOrder.setImageResource(R.drawable.ic_arrow_upward)
            }
            ascDesc = !ascDesc
            contactList = contactList.reversed()
            placeAdapter()
        }

        result = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.data != null && it.resultCode == 1) {
                loadList()

                if(it.data?.extras != null) {
                    imageId = it.data?.getIntExtra("userImageId", 0)!!
                    binding.imageProfile.setImageResource(imageId!!)
                }
            } else if (it.data != null && it.resultCode == 0) {
                Toast
                    .makeText(this, getString(R.string.operation_canceled), Toast.LENGTH_SHORT)
                    .show()
            }
        }

        binding.menuContent.animate().translationX(-700f)

        binding.buttonMenuOpen.setOnClickListener {
            binding.menuContent.visibility = View.VISIBLE
            binding.menuContent.animate().translationX(0F).duration =
                resources.getInteger(android.R.integer.config_mediumAnimTime).toLong()
        }

        binding.buttonMenuClose.setOnClickListener {
            binding.menuContent.animate().translationX(-binding.menuContent.measuredWidth.toFloat()).duration =
                resources.getInteger(android.R.integer.config_mediumAnimTime).toLong()
        }

        binding.buttonProfile.setOnClickListener {
            val intent = Intent(applicationContext, ProfileActivity::class.java)
            intent.putExtra("id", user.id)
            intent.putExtra("imageId", user.imageId)
            result.launch(intent)
        }
    }


    override fun onResume() {
        super.onResume()

        getUser()
        loadList()

        binding.textMenuName.text = user.username

        if(user.imageId > 0) {
            binding.imageProfile.setImageResource(user.imageId)
        } else {
            binding.imageProfile.setImageResource(R.drawable.ic_profile_default)
        }
    }
    private fun placeAdapter() {
        adapter = ContactListAdapter(contactList, ContactOnClickListener {contact ->
            val intent = Intent(applicationContext, ContactDetailActivity::class.java)
            intent.putExtra("id", contact.id)
            result.launch(intent)
        })
        binding.recycleViewConstants.adapter = adapter
    }

    private fun getUser() {
        val username = sharedPreferences.getString("username", "")
        val password = sharedPreferences.getString("password", "")

        if(username != null && password != null) {
            if(username.isNotEmpty() && password.isNotBlank()) {
                user = db.getUser(username = username, password = password)
                editor.putInt("userId", user.id)
                editor.apply()
            }
        }
    }
    private fun loadList() {
        contactList = db.getAllContact(user.id).sortedWith(compareBy { it.name })
        placeAdapter()
    }
}