package org.fmod.finaltest.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import org.fmod.finaltest.bean.DealItem
import org.fmod.finaltest.widget.BatchView

class BatchAdapter(
    private var mList: ArrayList<DealItem>,
    private val checkCallback: (DealItem, Boolean)->Unit
): RecyclerView.Adapter<BatchAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(BatchView(LayoutInflater.from(parent.context), parent))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(mList[position])
    }

    override fun getItemCount(): Int {
        return mList.size
    }


    inner class ViewHolder(private val batchView: BatchView): RecyclerView.ViewHolder(batchView.view) {
        fun bind(item: DealItem) {
            batchView.bind(item, checkCallback)
        }
    }
}