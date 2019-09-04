package org.fmod.finaltest.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.book_item.view.*
import org.fmod.finaltest.R
import org.fmod.finaltest.ViewHolder
import org.fmod.finaltest.bean.Book

class BookAdapter(private var mList: ArrayList<Book>): RecyclerView.Adapter<ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.book_item, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.itemView.run {
            backgroundTintList = mList[position].bgColorRes
            book_name.text = mList[position].name
        }
    }

    override fun getItemCount(): Int {
        return mList.size
    }

}