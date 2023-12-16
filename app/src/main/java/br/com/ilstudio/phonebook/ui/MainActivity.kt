package br.com.ilstudio.phonebook.ui

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.ilstudio.phonebook.R
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
    private var ascDesc: Boolean = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = DBHelper(this)
        sharedPreferences = application.getSharedPreferences("login", Context.MODE_PRIVATE)
        editor = sharedPreferences.edit()

        binding.recycleViewConstants.layoutManager = LinearLayoutManager(applicationContext)

        loadList()

        binding.buttonLogout.setOnClickListener {
            editor.putString("username", "")
            editor.putString("password", "")
            editor.putInt("userId", 0)
            editor.apply()
            finish()
        }

        binding.buttonAdd.setOnClickListener {
            result.launch(Intent(applicationContext, NewContactActivity::class.java))
        }

        binding.buttonOrder.setOnClickListener {
            if(ascDesc) {
                binding.buttonOrder.setImageResource(R.drawable.ic_arrow_downward)
            } else {
                binding.buttonOrder.setImageResource(R.drawable.ic_arrow_upward)
            }
            ascDesc = !ascDesc
            contactList = contactList.reversed()
            placeAdapter()
        }

        result = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if(it.data != null && it.resultCode == 1) {
                loadList()
            } else if (it.data != null && it.resultCode == 0){
                Toast
                    .makeText(this, "Operation canceled", Toast.LENGTH_SHORT)
                    .show()
            }
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

    private fun loadList() {
        val username = sharedPreferences.getString("username", "")
        val password = sharedPreferences.getString("password", "")
        var user = UserModel()

        if(username != null && password != null) {
            if(username.isNotEmpty() && password.isNotBlank()) {
                user = db.getUser(username = username, password = password)
                editor.putInt("userId", user.id)
                editor.apply()
            }
        }

        contactList = db.getAllContact(user.id).sortedWith(compareBy { it.name })
        placeAdapter()
    }
}