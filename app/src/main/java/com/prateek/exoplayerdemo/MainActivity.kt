package com.prateek.exoplayerdemo

import android.os.Build
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
import com.prateek.exoplayerdemo.analytics.PlayerAnalyticProvider
import com.prateek.exoplayerdemo.data.AudioTracksData
import com.prateek.exoplayerdemo.data.SettingMenuData
import com.prateek.exoplayerdemo.data.SpeedData
import com.prateek.exoplayerdemo.data.SubtitleTracksData
import com.prateek.exoplayerdemo.data.VideoTracksData
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity(), Player.Listener {
    private var player: ExoPlayer? = null
    private var playbackPosition = 0L
    private var playWhenReady = true
    private var isShowingTrackSelectionDialog = false
    private var trackSelectionParameters: TrackSelectionParameters? = null
    private var trackDataList:MutableList<VideoTracksData> = mutableListOf()
    private var speed: Float = 1.0f
    private var selectedItem: VideoTracksData? = null
    var popupMenu: VideoTracksMenu? = null
    private var selectedSpeedData = SpeedData(1.0f,true)

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

        val btnVideoTrack: View = findViewById<ImageButton>(R.id.exo_quality)

        btnVideoTrack.setOnClickListener {
            trackDataList = PlaybackUtil.printQualitySelector(player, trackSelectionParameters!!)
//            val selectedTrack1 = VideoTracksData("Auto", TrackGroup(), trackSelectionParameters!!.buildUpon()!!,true,0)
            val selectedTrack = if (selectedItem == null) trackDataList[0] else selectedItem
//            trackDataList.add(0,selectedTrack1)
            showVideoTracksMenu(btnVideoTrack, selectedTrack!!)
//            showVideoListMenu(btnVideoTrack,trackDataList)
        }

        // speed control
        val btnSpeed: View = findViewById<ImageButton>(R.id.exo_playback_speed)
        btnSpeed.setOnClickListener {
//            val speedDialog = PlaybackUtil.showSpeedDialog(this,btnSpeed,player,speed)
            showSpeedMenu(btnSpeed)
        }

        findViewById<ImageButton>(R.id.exoSettings).also {
            it.setOnClickListener {
                showSettingListMenu(it)
            }
        }

        findViewById<ImageButton>(R.id.imgBtnMuteUnmute).also { it ->
            it.setOnClickListener {
                muteUnmuteFeature(it as ImageButton)
            }
        }
    }

    private fun muteUnmuteFeature(muteBtn: ImageButton) {
        player?.volume = if (player?.volume == 0f) 1f else 0f
        if (player?.volume == 0f) {
            muteBtn.apply {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    foreground = getDrawable(R.drawable.ic_mutes)
                } else {
                    background = getDrawable(R.drawable.ic_mutes)
                }
            }
        } else {
            muteBtn.apply {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    foreground = getDrawable(R.drawable.ic_unmute)
                } else {
                    background = getDrawable(R.drawable.ic_unmute)
                }
            }
        }
    }

    private fun showVideoListMenu(view: View, videoTracks: ArrayList<VideoTracksData>) {
        VideoTrackMenu.showVideoPopupWindow(this, view, videoTracks,  object : VideoTrackMenu.VideoTrackListener {
            override fun onVideoItemSelected(item: VideoTracksData) {
                player?.trackSelectionParameters =
                    player?.trackSelectionParameters
                        ?.buildUpon()
                        ?.setOverrideForType(
                            TrackSelectionOverride(item.track, /* trackIndex= */ item.index)
                        )
                        ?.build()!!
            }
        })
    }

    private fun showSpeedMenu(btnSpeed: View) {
        val speedList = SpeedMenu.createSpeedListData()
        SpeedMenu.showSpeedPopMenu(
            this,
            btnSpeed,
            speedList,
            selectedSpeedData,
            object : SpeedMenu.SpeedItemClickListener {
                override fun onSpeedItemClick(item: SpeedData) {
                    selectedSpeedData = item
                    Toast.makeText(this@MainActivity, "speed is ${item.speed}", Toast.LENGTH_SHORT)
                        .show()
                    val param = PlaybackParameters(item.speed)
                    player?.playbackParameters = param
                    player?.setPlaybackSpeed(item.speed)
                }
            })
    }

    private fun showSettingListMenu(btnSettingMenu: View) {
        val settingsItem = SettingListMenu.createSettingList()
        SettingListMenu.showListPopupWindow(
            this,
            btnSettingMenu,
            settingsItem,
            player as ExoPlayer,
            object :
                SettingListMenu.SettingItemClickListener1 {
                override fun onSettingItemClicked(item: SettingMenuData) {}

                override fun onAudioItemSelected(item: AudioTracksData) {
                    player?.trackSelectionParameters =
                        player?.trackSelectionParameters?.buildUpon()
                            ?.setPreferredAudioLanguage(item.language)?.build()!!
                }

                override fun onSubtitleItemSelected(item: SubtitleTracksData) {
                    player?.trackSelectionParameters =
                        player?.trackSelectionParameters?.buildUpon()
                            ?.setPreferredTextLanguage(item.language)?.build()!!
                }
            })
    }

    private fun showSettingPopupMenu(btnSettingMenu: ImageButton) {
        val items = listOf("Audio", "Subtitles")
        val settingMenu = SettingsMenu(this,btnSettingMenu,items,player,object :
            SettingsMenu.SettingItemClickListener<String> {
            override fun onItemSelected(item: String) {
//                player?.trackSelectionParameters =
//                    player?.trackSelectionParameters?.buildUpon()?.setPreferredTextLanguage(item)?.build()!!
            }

            override fun onAudioItemSelected(item: AudioTracksData) {
                player?.trackSelectionParameters =
                    player?.trackSelectionParameters?.buildUpon()
                        ?.setPreferredAudioLanguage(item.language)?.build()!!
            }

            override fun onSubtitleItemSelected(item: SubtitleTracksData) {
                player?.trackSelectionParameters =
                    player?.trackSelectionParameters?.buildUpon()
                        ?.setPreferredTextLanguage(item.language)?.build()!!
            }

        })
        settingMenu.show()
    }


    private fun showVideoTracksMenu(btnMenu: View, selectedTrack: VideoTracksData) {
          popupMenu = VideoTracksMenu(this, btnMenu, trackDataList, selectedTrack, object : VideoTracksMenu.OnItemSelectedListener {
            override fun onItemSelected(item: VideoTracksData) {
//               Toast.makeText(applicationContext, "Selected: ${item.trackName}", Toast.LENGTH_SHORT).show()
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
//        val mediaItem = MediaItem.fromUri("https://bitdash-a.akamaihd.net/content/MI201109210084_1/m3u8s/f08e80da-bf1d-4e3d-8899-f0f6155f6efa.m3u8")
        val mediaItem = MediaItem.fromUri("https://test-streams.mux.dev/x36xhzz/x36xhzz.m3u8")
        val mediaSource = HlsMediaSource.Factory(DefaultHttpDataSource.Factory()).createMediaSource(mediaItem)

        player = ExoPlayer.Builder(this).build()
        player?.addAnalyticsListener(PlayerAnalyticProvider(player))
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
//        player?.addAnalyticsListener(TestFairyExoPlayerAnalyticsListener(player))
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

    override fun onEvents(player: Player, events: Player.Events) {
        super.onEvents(player, events)
    }
}