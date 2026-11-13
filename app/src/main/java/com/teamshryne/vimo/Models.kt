package com.teamshryne.vimo

data class MediaItem(val path: String)
data class TimelineClip(val trackId: Int, val clipId: Int, val startMs: Long, val endMs: Long, val asset: Int)
