package org.fmod.finaltest.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import org.fmod.finaltest.bean.DealItem
import org.fmod.finaltest.widget.DealView

class MainListAdapter(private var mList: ArrayList<DealItem>): RecyclerView.Adapter<MainListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        return ViewHolder(DealView(LayoutInflater.from(parent.context), parent))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(mList[position])
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    inner class ViewHolder(private val dealView: DealView): RecyclerView.ViewHolder(dealView.view) {
        fun bind(item: DealItem){
            dealView.bind(item)
        }
    }

}