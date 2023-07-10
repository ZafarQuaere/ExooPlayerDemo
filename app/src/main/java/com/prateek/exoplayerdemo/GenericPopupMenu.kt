package com.prateek.exoplayerdemo

import android.content.Context
import android.view.MenuItem
import android.view.View
import androidx.appcompat.widget.PopupMenu

class GenericPopupMenu(
    private val context: Context,
    private val anchorView: View,
    private val items: List<TracksData>,
    private val onItemSelectedListener: OnItemSelectedListener<TracksData>
) {

    interface OnItemSelectedListener<T> {
        fun onItemSelected(item: T)
    }

    fun show() {
        val popupMenu = PopupMenu(context, anchorView)
        for ((index, item) in items.withIndex()) {
//            println("Tracks Menu >> $index >> ${items[0].trackName}")
            popupMenu.menu.add(0, index, index, item.trackName)
        }
        popupMenu.setOnMenuItemClickListener { menuItem: MenuItem ->
            val selectedItem = items[menuItem.itemId]
            onItemSelectedListener.onItemSelected(selectedItem)
            true
        }
        popupMenu.show()
    }
}