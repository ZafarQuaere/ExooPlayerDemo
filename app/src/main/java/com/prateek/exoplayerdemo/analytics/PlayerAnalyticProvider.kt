package com.prateek.exoplayerdemo.analytics

import android.os.Handler
import androidx.media3.common.AudioAttributes
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import androidx.media3.datasource.HttpDataSource.HttpDataSourceException
import androidx.media3.datasource.HttpDataSource.InvalidResponseCodeException
import androidx.media3.exoplayer.DecoderCounters
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.analytics.AnalyticsListener
import androidx.media3.exoplayer.source.LoadEventInfo
import androidx.media3.exoplayer.source.MediaLoadData


class PlayerAnalyticProvider(player: ExoPlayer?) : AnalyticsListener {

    private val handler: Handler = Handler()
    private var player: ExoPlayer? = null

    private var currentPositionTracker: Runnable? = null
    private var lastKnownPlaybackPercent: Long = -1

    init {
        this.player = player
    }

    override fun onIsPlayingChanged(eventTime: AnalyticsListener.EventTime, isPlaying: Boolean) {
        if (isPlaying) {
            println("NagPlayerAnalyticProvider >>> onIsPlayingChanged $isPlaying")
        } else {
            // Not playing because playback is paused, ended, suppressed, or the player
            // is buffering, stopped or failed. Check player.getPlayWhenReady,
            // player.getPlaybackState, player.getPlaybackSuppressionReason and
            // player.getPlaybackError for details.
            println("NagPlayerAnalyticProvider >>> onIsPlayingChanged $isPlaying")
        }
    }

    override fun onPlaybackStateChanged(eventTime: AnalyticsListener.EventTime, state: Int) {
        if (state == Player.STATE_READY) {
            println("NagPlayerAnalyticProvider >>> Player.STATE_READY")
        } else if (state == Player.STATE_ENDED) {
            println("NagPlayerAnalyticProvider >>> Player.STATE_ENDED")
        } else if (state == Player.STATE_IDLE) {
            println("NagPlayerAnalyticProvider >>> Player.STATE_IDLE")
        } else if (state == Player.STATE_BUFFERING) {
            println("NagPlayerAnalyticProvider >>> Player.STATE_BUFFERING")
        }
    }

    override fun onPlayerError(eventTime: AnalyticsListener.EventTime, error: PlaybackException) {
        val cause: Throwable? = error.cause
        if (cause is HttpDataSourceException) {
            println("NagPlayerAnalyticProvider >>> HttpDataSourceException")
            if (cause is InvalidResponseCodeException) {
                println("NagPlayerAnalyticProvider >>> InvalidResponseCodeException")
                // Cast to InvalidResponseCodeException and retrieve the response code,
                // message and headers.
            } else {
                println("NagPlayerAnalyticProvider >>> ........")
                // Try calling httpError.getCause() to retrieve the underlying cause,
                // although note that it may be null.
            }
        }
    }

    override fun onAudioAttributesChanged(
        eventTime: AnalyticsListener.EventTime,
        audioAttributes: AudioAttributes
    ) {
        super.onAudioAttributesChanged(eventTime, audioAttributes)
    }



    override fun onAudioEnabled(
        eventTime: AnalyticsListener.EventTime,
        decoderCounters: DecoderCounters
    ) {
        super.onAudioEnabled(eventTime, decoderCounters)
    }

    override fun onEvents(player: Player, events: AnalyticsListener.Events) {
        super.onEvents(player, events)
    }


    override fun onDeviceVolumeChanged(
        eventTime: AnalyticsListener.EventTime,
        volume: Int,
        muted: Boolean
    ) {
        super.onDeviceVolumeChanged(eventTime, volume, muted)
    }

    override fun onIsLoadingChanged(eventTime: AnalyticsListener.EventTime, isLoading: Boolean) {
        super.onIsLoadingChanged(eventTime, isLoading)
    }

    override fun onLoadCompleted(
        eventTime: AnalyticsListener.EventTime,
        loadEventInfo: LoadEventInfo,
        mediaLoadData: MediaLoadData
    ) {
        super.onLoadCompleted(eventTime, loadEventInfo, mediaLoadData)
    }
}