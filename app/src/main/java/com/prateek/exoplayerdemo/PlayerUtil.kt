package com.prateek.exoplayerdemo

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.media3.common.C
import androidx.media3.common.PlaybackParameters
import androidx.media3.common.Player
import androidx.media3.common.TrackSelectionOverride
import androidx.media3.common.TrackSelectionParameters
import com.google.android.material.dialog.MaterialAlertDialogBuilder

import java.text.DecimalFormat
import java.util.ArrayList


object PlayerUtil {

    private var speed: Float = 1.0f
    private const val dismissDelay = 600L

    @androidx.annotation.OptIn(androidx.media3.common.util.UnstableApi::class)
    fun printQualitySelector(
        player: Player?,
        trackSelectionParameters: TrackSelectionParameters
    ): ArrayList<TracksData> {
        val trackOverrideList = ArrayList<Pair<String, TrackSelectionParameters.Builder>>()
        val dataList = ArrayList<TracksData>()

        player?.let {
            val tracks = it.currentTracks
            val groups = tracks.groups
            val trackParams = it.trackSelectionParameters
            println("Track params: $trackParams")

            //group level information
            for (tGroup in groups) {

                //Group level information
                val trackType = tGroup.type
                val trackInGroupIsSelected = tGroup.isSelected
                val trackInGroupIsSupported = tGroup.isSupported
                println("Tracks Group level: trackType $trackType   trackInGroupIsSelected $trackInGroupIsSelected  trackInGroupIsSupported $trackInGroupIsSupported ")

                val builder: TrackSelectionParameters.Builder =
                    trackSelectionParameters!!.buildUpon()

                if (tGroup.type == C.TRACK_TYPE_VIDEO) {
                    for (i in 0 until tGroup.length) {
                        // Individual track information.
                        val track = tGroup.mediaTrackGroup
                        val isSupported = tGroup.isTrackSupported(i)
                        val isSelected = tGroup.isTrackSelected(i)
                        val trackFormat = tGroup.getTrackFormat(i)
                        println("Tracks Individual level: isSupported>> $isSupported   isSelected>> $isSelected  trackFormat>> $trackFormat ")

                        builder.clearOverridesOfType(C.TRACK_TYPE_VIDEO)
                        builder.addOverride(TrackSelectionOverride(track, i))
                        val trackName = "${track.getFormat(i).width} x ${track.getFormat(i).height}"
                        println("Tracks Name of format trackName >> $trackName ")
                        val tracksData = TracksData(trackName, track, builder)
                        trackOverrideList.add(Pair(trackName, builder))
                        dataList.add(tracksData)
                    }
                }
            }
        }
        return dataList
    }


    fun showSpeedDialog(context: Context, view: View, player: Player?, speed: Float) {
        val offsetY = 200 // to create the dialog slightly above the view.

        val inflater = LayoutInflater.from(context)
        val dialogView = inflater.inflate(R.layout.speed_dialog, null)
        val btnIncrement = dialogView.findViewById<ImageButton>(R.id.plusBtn)
        val btnDecrement = dialogView.findViewById<ImageButton>(R.id.minusBtn)
        val speedText = dialogView.findViewById<TextView>(R.id.speedText)
        speedText.text = "${DecimalFormat("#.##").format(this.speed)} X"
        val builder = MaterialAlertDialogBuilder(view.context,R.style.AppTheme2).setView(dialogView)
        val dialog = builder.create()

        //to display alert dialog on top of view similar to pop up menu
        val location = IntArray(2)
        view.getLocationOnScreen(location)
        val x = location[0]
        val y = location[1]
        val newY = y - offsetY
        dialog.window.let {
            val layoutParams = WindowManager.LayoutParams()
            layoutParams.copyFrom(it?.attributes)
            layoutParams.gravity = Gravity.TOP or Gravity.START
            layoutParams.x = x
            layoutParams.y = newY
            it?.attributes = layoutParams
        }

        btnIncrement.setOnClickListener {
            changeSpeed(player, isIncrement = true)
            speedText.text = "${DecimalFormat("#.##").format(this.speed)} X"
            dismissDialogWithSomeDelay(dialog)

        }

        btnDecrement.setOnClickListener {
            changeSpeed(player, isIncrement = false)
            speedText.text = "${DecimalFormat("#.##").format(this.speed)} X"
            dismissDialogWithSomeDelay(dialog)

        }
        dialog.show()
    }


    private fun changeSpeed(player: Player?, isIncrement: Boolean) {
        if (isIncrement) {
            if (speed < 2.0f) {
                speed += 0.25f
            }
        } else {
            if (speed > 0.25f) {
                speed -= 0.25f
            }
        }
        val param = PlaybackParameters(speed)
        player?.playbackParameters = param
        player?.setPlaybackSpeed(speed)
    }

    private fun dismissDialogWithSomeDelay(dialog: AlertDialog) {
        val handler = Handler(Looper.getMainLooper())
        handler.postDelayed({
            // Execute the action.
            dialog.dismiss()
        }, dismissDelay)

    }
}