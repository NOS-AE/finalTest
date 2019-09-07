package org.fmod.finaltest.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import org.fmod.finaltest.bean.StaItem
import org.fmod.finaltest.widget.StaView

class StaAdapter(private var mList: ArrayList<StaItem>): RecyclerView.Adapter<StaAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(StaView(LayoutInflater.from(parent.context), parent))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(mList[position])
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    fun switchList(list: ArrayList<StaItem>) {
        mList = list
        notifyDataSetChanged()
    }

    inner class ViewHolder(private val mView: StaView): RecyclerView.ViewHolder(mView.view) {
        fun bind(item: StaItem) {
            mView.bind(item)
        }
    }
}