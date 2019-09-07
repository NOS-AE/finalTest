package org.fmod.finaltest.widget

import android.widget.TextView
import androidx.appcompat.widget.Toolbar

class BatchViewController(
    private val titleView: Toolbar,
    private val moveToView: TextView,
    private val deleteView: TextView,
    private val exportView: TextView
) {

    fun updateView(oldCount: Int, newCount: Int) {

        val title = "已选择${newCount}项"
        titleView.title = title

        if(newCount == 0) {
            moveToView.isEnabled = false
            deleteView.isEnabled = false
            exportView.isEnabled = false
        } else if(oldCount == 0 && newCount == 1) {
            moveToView.isEnabled = true
            deleteView.isEnabled = true
            exportView.isEnabled = true
        }

    }

}