package com.prateek.exoplayerdemo

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.PopupMenu
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.media3.common.*
import androidx.media3.common.util.Util
import androidx.media3.datasource.DefaultHttpDataSource
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.hls.HlsMediaSource
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity(), Player.Listener {
    private var player: ExoPlayer? = null
    private var playbackPosition = 0L
    private var playWhenReady = true
    private var isShowingTrackSelectionDialog = false
    private var trackSelectionParameters: TrackSelectionParameters? = null
    private var trackDataList = ArrayList<TracksData>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        trackSelectionParameters = TrackSelectionParameters.Builder( /* context= */this).build()

        findViewById<Button>(R.id.btnTracks).setOnClickListener {
            if (!isShowingTrackSelectionDialog) {
                isShowingTrackSelectionDialog = true

                val trackSelectionDialog = TrackSelectionDialog.createForPlayer(player)
                    { _ -> isShowingTrackSelectionDialog = false }
                trackSelectionDialog.show(supportFragmentManager,  /* tag= */null)
            }
        }
        val btnMenu: View = findViewById<Button>(R.id.btnMenu)
        btnMenu.setOnClickListener {
//            printQualitySelector(player)
//            showPopupMenu(btnMenu)
//            qualityList = printQualitySelector(player)
            trackDataList = printQualitySelector(player)
//            setUpQualityList()
            showPopupMenu(btnMenu)
//            qualityPopUp?.show()
        }
    }

    private fun showPopupMenu(btnMenu: View) {

        val popupMenu = GenericPopupMenu(this, btnMenu, trackDataList, object : GenericPopupMenu.OnItemSelectedListener<TracksData> {
            override fun onItemSelected(item: TracksData) {
               Toast.makeText(applicationContext, "Selected: ${item.trackName}", Toast.LENGTH_SHORT).show()
                player?.trackSelectionParameters =
                    player?.trackSelectionParameters
                        ?.buildUpon()
                        ?.setOverrideForType(
                            TrackSelectionOverride(item.track, /* trackIndex= */ 0)
                        )
                        ?.build()!!
            }
        })
        popupMenu.show()
    }

    private fun printQualitySelector(player: ExoPlayer?): ArrayList<TracksData> {

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

                val builder: TrackSelectionParameters.Builder = trackSelectionParameters!!.buildUpon()

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
                        val tracksData = TracksData(trackName,track,builder)
                        trackOverrideList.add(Pair(trackName,builder))
                        dataList.add(tracksData)
                    }
                }
            }
        }

//        return trackOverrideList
        return dataList
    }

    override fun onPlaybackStateChanged(playbackState: Int) {
       /* if (playbackState==Player.STATE_READY){
            qualityList = printQualitySelector(player)
            setUpQualityList()
        }*/
    }

    private fun setUpQualityList() {
//        qualityPopUp = PopupMenu(this, btnMenu)
//        qualityList.let {
//            for ((i, videoQuality) in it.withIndex()) {
////                qualityPopUp?.menu?.add(0, i, 0, videoQuality.first)
//            }
//        }
//        qualityPopUp?.setOnMenuItemClickListener { menuItem ->
//            qualityList[menuItem.itemId].let {
//               /* trackSelector!!.setParameters(
//                    trackSelector!!.getParameters()
//                        .buildUpon()
//                        .setTrackSelectionOverrides(it.second.build())
//
//                        .setTunnelingEnabled(true)
//                        .build())*/
//                Toast.makeText(applicationContext,"selected Item is:${it.first}   and builder ${it.second} :",Toast.LENGTH_LONG).show()
//                println("Tracks selected Item is:${it.first}   and builder ${it.second} ")
//                val builder = trackSelectionParameters!!.buildUpon()
//
////                builder.setOverrideForType(TrackSelectionOverride(it.second.build(), /* trackIndex= */ 0)).build()
//            }
//            true
//        }
    }
    private fun initPlayer() {
//        val mediaItem = MediaItem.fromUri("https://bitdash-a.akamaihd.net/content/MI201109210084_1/m3u8s/f08e80da-bf1d-4e3d-8899-f0f6155f6efa.m3u8")
//        val mediaItem = MediaItem.fromUri("https://devstreaming-cdn.apple.com/videos/streaming/examples/adv_dv_atmos/main.m3u8")
        val mediaItem = MediaItem.fromUri("https://test-streams.mux.dev/x36xhzz/x36xhzz.m3u8")
//        val mediaItem = MediaItem.fromUri("https://bitdash-a.akamaihd.net/content/MI201109210084_1/m3u8s/f08e80da-bf1d-4e3d-8899-f0f6155f6efa.m3u8")
        val mediaSource = HlsMediaSource.Factory(DefaultHttpDataSource.Factory()).createMediaSource(mediaItem)

        player = ExoPlayer.Builder(this).build()
        player?.playWhenReady = true
        player_exo.player = player
//        val mediaItem = MediaItem.fromUri("https://bitdash-a.akamaihd.net/content/MI201109210084_1/m3u8s/f08e80da-bf1d-4e3d-8899-f0f6155f6efa.m3u8")
//        val mediaSource = HlsMediaSource.Factory(DefaultHttpDataSource.Factory()).createMediaSource(mediaItem)
        player?.setMediaSource(mediaSource)
        player?.trackSelectionParameters = trackSelectionParameters!!
        player?.setMediaItem(mediaItem)
        player?.seekTo(playbackPosition)
        player?.playWhenReady = playWhenReady
        player?.prepare()

    }

    private fun releasePlayer() {
        player?.let {
            playbackPosition = it.currentPosition
            playWhenReady = it.playWhenReady
            it.release()
            player = null
        }
    }


    override fun onStart() {
        super.onStart()
        if (Util.SDK_INT >= 24) {
            initPlayer()
        }
    }

    override fun onStop() {
        super.onStop()
        if (Util.SDK_INT >= 24) {
            releasePlayer()
        }
    }

    override fun onResume() {
        super.onResume()
        if (Util.SDK_INT < 24) {
            initPlayer()
        }
    }
    override fun onPause() {
        super.onPause()
        if (Util.SDK_INT < 24) {
            releasePlayer()
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        releasePlayer()
    }
}