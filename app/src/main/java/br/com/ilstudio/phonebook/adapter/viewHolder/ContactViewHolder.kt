package br.com.ilstudio.phonebook.adapter.viewHolder

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import br.com.ilstudio.phonebook.R

class ContactViewHolder(view: View): RecyclerView.ViewHolder(view) {
    val image: ImageView = view.findViewById(R.id.image_contact)
    val textName: TextView = view.findViewById(R.id.text_name)


}