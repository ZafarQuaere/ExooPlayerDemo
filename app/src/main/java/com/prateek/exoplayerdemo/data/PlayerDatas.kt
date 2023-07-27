package com.prateek.exoplayerdemo.data

import androidx.media3.common.TrackGroup
import androidx.media3.common.TrackSelectionParameters

data class VideoTracksData(
    val trackName: String,
    val track: TrackGroup,
    val builder: TrackSelectionParameters.Builder,
    val isSelected: Boolean,
    val index: Int
)

data class AudioTracksData(
    val language: String,
    val label: String,
    val id: String,
    val isSelected: Boolean
)

data class SubtitleTracksData(
    val language: String,
    val label: String,
    val id: String,
    val isSelected: Boolean
)

data class SettingMenuData(
    val title : String,
    val icon: Int
)

data class ListPopupItem(val title: String, val imgRes: Int)

data class SpeedData(val speed: Float, var isSelected : Boolean = false)