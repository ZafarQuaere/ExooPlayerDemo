package com.prateek.exoplayerdemo

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageButton
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
    private var speed: Float = 1.0f
    private var selectedItem: TracksData? = null
    var popupMenu: GenericPopupMenu? = null

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

        val btnMenu: View = findViewById<ImageButton>(R.id.exo_quality)
        btnMenu.setOnClickListener {
            trackDataList = PlayerUtil.printQualitySelector(player, trackSelectionParameters!!)
            val selectedTrack = if (selectedItem == null) trackDataList[0] else selectedItem
            showPopupMenu(btnMenu, selectedTrack!!)
        }

        // speed control
        val btnSpeed: View = findViewById<ImageButton>(R.id.exo_playback_speed)
        btnSpeed.setOnClickListener {
            val speedDialog = PlayerUtil.showSpeedDialog(this,btnSpeed,player,speed)
        }
    }


    private fun showPopupMenu(btnMenu: View, selectedTrack: TracksData) {
          popupMenu = GenericPopupMenu(this, btnMenu, trackDataList, selectedTrack, object : GenericPopupMenu.OnItemSelectedListener<TracksData> {
            override fun onItemSelected(item: TracksData) {
               Toast.makeText(applicationContext, "Selected: ${item.trackName}", Toast.LENGTH_SHORT).show()
                for ((index, data) in trackDataList.withIndex()) {
                    if (trackDataList[index].trackName == item.trackName) {
                        selectedItem = data
                        player?.trackSelectionParameters =
                            player?.trackSelectionParameters
                                ?.buildUpon()
                                ?.setOverrideForType(
                                    TrackSelectionOverride(item.track, /* trackIndex= */ index)
                                )
                                ?.build()!!
                    }
                }
            }
        })
        popupMenu?.show()
    }

    private fun initPlayer() {
//        val mediaItem = MediaItem.fromUri("https://devstreaming-cdn.apple.com/videos/streaming/examples/adv_dv_atmos/main.m3u8")
        val mediaItem = MediaItem.fromUri("https://bitdash-a.akamaihd.net/content/MI201109210084_1/m3u8s/f08e80da-bf1d-4e3d-8899-f0f6155f6efa.m3u8")
//        val mediaItem = MediaItem.fromUri("https://test-streams.mux.dev/x36xhzz/x36xhzz.m3u8")
        val mediaSource = HlsMediaSource.Factory(DefaultHttpDataSource.Factory()).createMediaSource(mediaItem)

        player = ExoPlayer.Builder(this).build()
        player?.playWhenReady = true
        player_exo.player = player

        player?.setMediaSource(mediaSource)
        player?.trackSelectionParameters = trackSelectionParameters!!
        player?.trackSelectionParameters?.forceHighestSupportedBitrate //for highest quality support
        player?.setMediaItem(mediaItem)
        player?.setPlaybackSpeed(speed)
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