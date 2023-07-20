package com.prateek.exoplayerdemo

import androidx.media3.common.TrackGroup
import androidx.media3.common.TrackSelectionParameters

data class VideoTracksData(
    val trackName: String,
    val track: TrackGroup,
    val builder: TrackSelectionParameters.Builder
)

data class AudioTracksData(
    val language: String,
    val label: String,
    val id: String
)

data class SubtitleTracksData(
    val language: String,
    val label: String,
    val id: String
)

data class SettingMenuData(
    val title : String,
    val icon: Int
)