package com.prateek.exoplayerdemo

import android.content.Context
import android.view.View
import android.widget.ListAdapter
import androidx.appcompat.widget.ListPopupWindow
import androidx.media3.exoplayer.ExoPlayer
import com.prateek.exoplayerdemo.adapter.AudioListAdapter
import com.prateek.exoplayerdemo.adapter.SettingMenuAdapter
import com.prateek.exoplayerdemo.adapter.SubtitleListAdapter
import com.prateek.exoplayerdemo.data.AudioTracksData
import com.prateek.exoplayerdemo.data.SettingMenuData
import com.prateek.exoplayerdemo.data.SubtitleTracksData
import java.util.ArrayList

object SettingListMenu {

    interface SettingItemClickListener1  {
        fun onSettingItemClicked(item: SettingMenuData)
        fun onAudioItemSelected(item: AudioTracksData)
        fun onSubtitleItemSelected(item: SubtitleTracksData)
    }

    fun showListPopupWindow(
        context: Context,
        anchor: View,
        items: List<SettingMenuData>,
        mPlayer: ExoPlayer?,
        listener: SettingItemClickListener1
    ) {
        val listPopupWindow: ListPopupWindow = createListPopupWindow(context, anchor, items)

        listPopupWindow.setOnItemClickListener { parent, view, position, id ->
            listPopupWindow.dismiss()
            if (items[position].title == "Audio") {
                val audioTrackList = PlaybackUtil.printAudioTrack(mPlayer)
                createAudioPopup(context, anchor, audioTrackList, listener)
            } else if (items[position].title == "Subtitles") {
                val textTrackList = PlaybackUtil.printSubtitles(mPlayer)
                createSubtitlePopup(context, anchor, textTrackList, listener)
            }
//            listener.onSettingItemClicked(items[position])
//            Toast.makeText(context, "clicked at $position item : ${items[position]}", Toast.LENGTH_SHORT)
//                .show()
        }
        listPopupWindow.isModal = true
        listPopupWindow.show()
    }

    private fun createAudioPopup(
        context: Context,
        anchor: View,
        audioTrackList: ArrayList<AudioTracksData>,
        listener: SettingItemClickListener1
    ) {
        val popup = ListPopupWindow(context)
        val adapter: ListAdapter = AudioListAdapter(audioTrackList)
        popup.anchorView = anchor
        popup.width = 340
        popup.setAdapter(adapter)

        popup.setOnItemClickListener { parent, view, position, id ->
            popup.dismiss()
            listener.onAudioItemSelected(audioTrackList[position])
        }
        popup.isModal = true
        popup.show()
    }

    private fun createSubtitlePopup(
        context: Context,
        anchor: View,
        subtitleTracksData: ArrayList<SubtitleTracksData>,
        listener: SettingItemClickListener1
    ) {
        val popup = ListPopupWindow(context)
        val adapter: ListAdapter = SubtitleListAdapter(subtitleTracksData)
        popup.anchorView = anchor
        popup.width = 380
        popup.setAdapter(adapter)
        popup.setOnItemClickListener { parent, view, position, id ->
            popup.dismiss()
            listener.onSubtitleItemSelected(subtitleTracksData[position])
        }
        popup.isModal = true
        popup.show()
    }


    private fun createListPopupWindow(
        context: Context, anchor: View,
        items: List<SettingMenuData>
    ): ListPopupWindow {
        val popup = ListPopupWindow(context)
        val adapter: ListAdapter = SettingMenuAdapter(items)
        popup.anchorView = anchor
        popup.width = 320
        popup.setAdapter(adapter)

        return popup
    }

    fun createSettingList(): List<SettingMenuData> {
        val listPopupItems: MutableList<SettingMenuData> = ArrayList<SettingMenuData>()
        listPopupItems.add(SettingMenuData("Audio", R.drawable.ic_audio))
        listPopupItems.add(SettingMenuData("Subtitles", R.drawable.ic_subtitles))
        return listPopupItems
    }
}