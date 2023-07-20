package com.prateek.exoplayerdemo

import android.content.Context
import android.view.MenuItem
import android.view.View
import androidx.appcompat.widget.PopupMenu
import com.prateek.exoplayerdemo.data.VideoTracksData

class VideoTracksMenu(
    private val context: Context,
    private val anchorView: View,
    private val items: List<VideoTracksData>,
    private var selectedItem: VideoTracksData,
    private val onItemSelectedListener: OnItemSelectedListener
) {

    private var selectedItemIndex: Int = -1
    interface OnItemSelectedListener {
        fun onItemSelected(item: VideoTracksData)
    }

    fun show() {
        val popupMenu = PopupMenu(context, anchorView)
        for ((index, item) in items.withIndex()) {
            if (item.trackName == selectedItem.trackName) {
                selectedItemIndex = index
            }
            popupMenu.menu.add(0, index, index, item.trackName)
        }

        popupMenu.setOnMenuItemClickListener { menuItem: MenuItem ->
            val selectedMenuItemIndex = menuItem.itemId
            val selectedMenuItem = items[selectedMenuItemIndex]
            if (selectedMenuItem != selectedItem) {
                selectedItemIndex = selectedMenuItemIndex
                onItemSelectedListener.onItemSelected(selectedMenuItem)
            }
            true
        }

        popupMenu.menu.setGroupCheckable(0,true,true)
        popupMenu.menu.getItem(selectedItemIndex).isChecked = true

        popupMenu.show()
    }

}