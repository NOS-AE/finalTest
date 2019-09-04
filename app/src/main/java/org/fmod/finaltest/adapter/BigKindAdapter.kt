package org.fmod.finaltest.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import io.reactivex.Observable
import org.fmod.finaltest.bean.BigKind
import org.fmod.finaltest.widget.KindView
import java.lang.ref.WeakReference

class BigKindAdapter(
    private val mKindList: ArrayList<BigKind>,
    private val selectedCallback: (BigKind)->Unit,
    initSelected: Int
): RecyclerView.Adapter<BigKindAdapter.ViewHolder>() {

    init {
        KindView.selectedKind = mKindList[initSelected]
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        return ViewHolder(KindView(LayoutInflater.from(parent.context), parent))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        when {
            position < mKindList.size -> {
                holder.bind(mKindList[position])
            }
        }
    }

    override fun getItemCount() = mKindList.size    //1自定义kind

    inner class ViewHolder(private val kindView: KindView): RecyclerView.ViewHolder(kindView.view){
        fun bind(kind: BigKind){
            kindView.bind(kind, selectedCallback)
        }
    }
}