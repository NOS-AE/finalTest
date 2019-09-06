package org.fmod.finaltest.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import org.fmod.finaltest.bean.DealItem
import org.fmod.finaltest.widget.DealView

class MainListAdapter(
    private var mList: ArrayList<DealItem>,
    private val clickCallback: (DealItem)->Unit
): RecyclerView.Adapter<MainListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        return ViewHolder(DealView(LayoutInflater.from(parent.context), parent))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(mList[position])
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    fun addNewItem(item: DealItem) {
        mList.add(item)
        mList.sortByDescending {
            it.dateNum
        }
        notifyItemRangeChanged(0, itemCount)
    }

    fun modifyItem() {
        mList.sortByDescending {
            it.dateNum
        }
        notifyItemRangeChanged(0, itemCount)
    }

    inner class ViewHolder(private val dealView: DealView): RecyclerView.ViewHolder(dealView.view) {
        fun bind(item: DealItem){
            dealView.bind(item, clickCallback)
        }
    }

}