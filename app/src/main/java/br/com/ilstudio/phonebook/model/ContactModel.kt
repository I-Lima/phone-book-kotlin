package br.com.ilstudio.phonebook.model

data class ContactModel(
    val id: Int = 1,
    val name: String = "",
    val email: String = "",
    val phone: String = "",
    val imageId: Int = -1
)
