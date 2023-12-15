package br.com.ilstudio.phonebook.adapter.listener

import br.com.ilstudio.phonebook.model.ContactModel

class ContactOnClickListener(val clickListener: (contact: ContactModel) -> Unit) {
    fun onClick(contact: ContactModel) = clickListener
}