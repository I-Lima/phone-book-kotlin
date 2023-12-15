package br.com.ilstudio.phonebook.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import br.com.ilstudio.phonebook.R
import br.com.ilstudio.phonebook.adapter.listener.ContactOnClickListener
import br.com.ilstudio.phonebook.adapter.viewHolder.ContactViewHolder
import br.com.ilstudio.phonebook.model.ContactModel

class ContactListAdapter (
    private val contactList: List<ContactModel>,
    private val contactOnClickListener: ContactOnClickListener
): RecyclerView.Adapter<ContactViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactViewHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.row_contact, parent, false)

        return ContactViewHolder(view)
    }

    override fun getItemCount(): Int {
        return contactList.size
    }

    override fun onBindViewHolder(holder: ContactViewHolder, position: Int) {
        val contact = contactList[position]

        holder.textName.text = contact.name
        if(contact.imageId > 8) {
            holder.image.setImageResource(contact.imageId)
        } else {
            holder.image.setImageResource(R.drawable.ic_profile_default)
        }

        holder.itemView.setOnClickListener {
            contactOnClickListener.clickListener(contact)
        }
    }

}