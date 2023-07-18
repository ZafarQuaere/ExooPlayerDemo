package com.prateek.exoplayerdemo

import android.content.Context
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.PopupMenu

class SettingsMenu(
    private val context: Context,
    private val anchorView: View,
    private val items: List<String>,
    private val onItemSelectedListener: SettingItemClickListener<String>
) {

    interface SettingItemClickListener<String> {
        fun onItemSelected(item: String)
    }


    fun show() {
        val popupMenu = PopupMenu(context, anchorView)

        for ((index, item) in items.withIndex()) {
            popupMenu.menu.add(0, index, index, item)
        }

        popupMenu.setOnMenuItemClickListener { menuItem: MenuItem ->
            val selectedMenuItemIndex = menuItem.itemId
            val selectedMenuItem = items[selectedMenuItemIndex]
            onItemSelectedListener.onItemSelected(selectedMenuItem)
            Toast.makeText(context, "item id: ${menuItem.itemId}", Toast.LENGTH_LONG).show()
            if (menuItem.itemId == 2) {
                showAudioPopUp(context,anchorView)
            }
            true
        }
        popupMenu.show()
    }

    private fun showAudioPopUp(context: Context, anchorView: View) {
        val popupMenu = PopupMenu(context, anchorView)

        for ((index, item) in items.withIndex()) {
            popupMenu.menu.add(0, index, index, "$item popup")
        }

        popupMenu.setOnMenuItemClickListener { menuItem: MenuItem ->
            val selectedMenuItemIndex = menuItem.itemId
            val selectedMenuItem = items[selectedMenuItemIndex]
            onItemSelectedListener.onItemSelected(selectedMenuItem)
            Toast.makeText(context, "item id: ${menuItem.itemId}", Toast.LENGTH_LONG).show()
            if (menuItem.itemId == 2) {
                showAudioPopUp(context,anchorView)
            }
            true
        }
        popupMenu.show()
    }

}