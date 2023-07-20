package com.prateek.exoplayerdemo

import android.content.Context
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.PopupMenu
import androidx.media3.exoplayer.ExoPlayer
import com.prateek.exoplayerdemo.data.AudioTracksData
import com.prateek.exoplayerdemo.data.SubtitleTracksData
import java.util.ArrayList

class SettingsMenu(
    private val context: Context,
    private val anchorView: View,
    private val items: List<String>,
    player: ExoPlayer?,
    private val onItemSelectedListener: SettingItemClickListener<String>
) {
val mPlayer = player
    interface SettingItemClickListener<String> {
        fun onItemSelected(item: String)
        fun onAudioItemSelected(item: AudioTracksData)
        fun onSubtitleItemSelected(item: SubtitleTracksData)
    }


    fun show() {
        val popupMenu = PopupMenu(context, anchorView)

     /*   val inflater = popupMenu.menuInflater
        inflater.inflate(R.menu.custom_menu, popupMenu.menu)*/

        for ((index, item) in items.withIndex()) {
            popupMenu.menu.add(0, index, index, item)
//            popupMenu.menu.getItem(index).title = item
//            popupMenu.menu.getItem(index).setIcon(R.drawable.ic_hd)
        }

        popupMenu.setOnMenuItemClickListener { menuItem: MenuItem ->
            val selectedMenuItemIndex = menuItem.itemId
            val selectedMenuItem = items[selectedMenuItemIndex]
            onItemSelectedListener.onItemSelected( menuItem.title.toString())
            Toast.makeText(context, "name : $selectedMenuItem item id: ${menuItem.itemId}", Toast.LENGTH_LONG).show()
            if (menuItem.itemId == 0) {
                val audioTrackList = PlaybackUtil.printAudioTrack(mPlayer)
                showAudioPopUp(context,anchorView,audioTrackList)
            }
            if (menuItem.itemId == 1) {
                val textTrackList = PlaybackUtil.printSubtitles(mPlayer)
                showSubtitlesPopUp(context,anchorView,textTrackList)
            }
            true
        }
        popupMenu.show()
    }


    private fun showAudioPopUp(
        context: Context,
        anchorView: View,
        audioTrackList: ArrayList<AudioTracksData>
    ) {
        val popupMenu = PopupMenu(context, anchorView)

        for ((index, item) in audioTrackList.withIndex()) {
            popupMenu.menu.add(0, index, index, "${item.label}")
        }

        popupMenu.setOnMenuItemClickListener { menuItem: MenuItem ->
            val selectedMenuItemIndex = menuItem.itemId
            val selectedMenuItem = audioTrackList[selectedMenuItemIndex]
            onItemSelectedListener.onAudioItemSelected(selectedMenuItem)
            Toast.makeText(context, "selectedMenuItem $selectedMenuItem item id: ${menuItem.itemId}", Toast.LENGTH_LONG).show()
            true
        }
        popupMenu.show()
    }

    private fun showSubtitlesPopUp(
        context: Context,
        anchorView: View,
        textTrackList: ArrayList<SubtitleTracksData>
    ) {
        val popupMenu = PopupMenu(context, anchorView)

        for ((index, item) in textTrackList.withIndex()) {
            popupMenu.menu.add(0, index, index, "${item.label}")
        }

        popupMenu.setOnMenuItemClickListener { menuItem: MenuItem ->
            val selectedMenuItemIndex = menuItem.itemId
            val selectedMenuItem = textTrackList[selectedMenuItemIndex]
            onItemSelectedListener.onSubtitleItemSelected(selectedMenuItem)
            Toast.makeText(context, "selectedMenuItem $selectedMenuItem item id: ${menuItem.itemId}", Toast.LENGTH_LONG).show()
            true
        }
        popupMenu.show()
    }

}