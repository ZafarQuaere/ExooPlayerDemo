package com.prateek.exoplayerdemo

import android.content.Context
import android.view.View
import android.widget.ListAdapter
import androidx.appcompat.widget.ListPopupWindow
import com.prateek.exoplayerdemo.adapter.VideoListAdapter
import com.prateek.exoplayerdemo.data.VideoTracksData

object VideoTrackMenu {

    interface VideoTrackListener {
        fun onVideoItemSelected(item: VideoTracksData)
    }

    fun showVideoPopupWindow(
        context: Context,
        anchor: View,
        items: ArrayList<VideoTracksData>,
        listener: VideoTrackListener
    ) {
        val popup = ListPopupWindow(context)
        val adapter: ListAdapter = VideoListAdapter(items)
        popup.anchorView = anchor
        popup.width = 340
        popup.setAdapter(adapter)
        popup.setOnItemClickListener { parent, view, position, id ->
            listener.onVideoItemSelected(items[position])
            popup.dismiss()
        }
        popup.show()
    }
}