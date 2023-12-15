package br.com.ilstudio.phonebook.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import br.com.ilstudio.phonebook.R
import br.com.ilstudio.phonebook.databinding.ActivityContactImageSelectionBinding

class ContactImageSelectionActivity : AppCompatActivity() {
    private lateinit var binding: ActivityContactImageSelectionBinding
    private lateinit var i: Intent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityContactImageSelectionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        i = intent

        binding.imageProfile1.setOnClickListener { sendId(R.drawable.profile1) }
        binding.imageProfile2.setOnClickListener { sendId(R.drawable.profile2) }
        binding.imageProfile3.setOnClickListener { sendId(R.drawable.profile3) }
        binding.imageProfile4.setOnClickListener { sendId(R.drawable.profile4) }
        binding.imageProfile5.setOnClickListener { sendId(R.drawable.profile5) }
        binding.imageProfile6.setOnClickListener { sendId(R.drawable.profile6) }
        binding.imageProfile7.setOnClickListener { sendId(R.drawable.profile7) }
        binding.imageProfile8.setOnClickListener { sendId(R.drawable.profile8) }
        binding.imageProfile9.setOnClickListener { sendId(R.drawable.profile9) }
        binding.imageProfile10.setOnClickListener { sendId(R.drawable.profile10) }
        binding.imageProfile11.setOnClickListener { sendId(R.drawable.profile11) }
        binding.imageProfile12.setOnClickListener { sendId(R.drawable.profile12) }
        binding.buttonRemove.setOnClickListener { sendId(R.drawable.ic_profile_default)}

        binding.buttonClose.setOnClickListener {
            setResult(2, i)
            finish()
        }
    }

    private fun sendId(id: Int) {
        i.putExtra("id", id)
        setResult(1, i)
        finish()
    }
}