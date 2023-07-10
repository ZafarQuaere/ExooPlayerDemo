package com.prateek.exoplayerdemo

import androidx.media3.common.TrackGroup
import androidx.media3.common.TrackSelectionParameters

data class TracksData(
    val trackName: String,
    val track: TrackGroup,
    val builder: TrackSelectionParameters.Builder
)
