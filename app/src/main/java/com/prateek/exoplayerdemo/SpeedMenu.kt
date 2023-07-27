package com.prateek.exoplayerdemo

import android.content.Context
import android.view.View
import androidx.appcompat.widget.ListPopupWindow
import com.prateek.exoplayerdemo.adapter.SpeedMenuAdapter
import com.prateek.exoplayerdemo.data.SpeedData

object SpeedMenu {

    interface SpeedItemClickListener {
        fun onSpeedItemClick(item: SpeedData)
    }

    fun showSpeedPopMenu(
        context: Context,
        anchor: View,
        speedList: List<SpeedData>,
        selectedItem: SpeedData,
        listener: SpeedItemClickListener
    ) {
        val popupWindow = ListPopupWindow(context)
        for (item in speedList) {
            if (item.speed == selectedItem.speed) {
                item.isSelected = true
            }
        }
        val adapter = SpeedMenuAdapter(speedList)
        popupWindow.anchorView = anchor
        popupWindow.width = 250
        popupWindow.setAdapter(adapter)

        popupWindow.setOnItemClickListener { parent, view, position, id ->
            popupWindow.dismiss()
            val item = speedList[position]
            item.isSelected = !item.isSelected
           adapter.updateItemSelection(position, item.isSelected)
            listener.onSpeedItemClick(speedList[position])
        }
        popupWindow.isModal = true
        popupWindow.show()
    }


    fun createSpeedListData(): List<SpeedData> {
        return mutableListOf<SpeedData>(
            SpeedData(0.25f),
            SpeedData(0.5f),
            SpeedData(0.75f),
            SpeedData(1.0f),
            SpeedData(1.25f),
            SpeedData(1.5f),
            SpeedData(1.75f),
            SpeedData(2.0f)
        )
    }
}