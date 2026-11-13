package com.teamshryne.vimo

object VimoCore {
    init {
        System.loadLibrary("vimo_jni")
    }

    external fun nativeCreate(width: Int, height: Int, sampleRate: Int, channels: Int): Long
    external fun nativeDestroy(handle: Long)
    external fun nativeExecute(
        handle: Long,
        tag: Int,
        a: Long,
        b: Long,
        c: Long,
        text: String?,
        floats: FloatArray?,
    ): Int
    external fun nativeSnapshot(handle: Long): String?
    external fun nativeStats(handle: Long): LongArray?
}

object CommandTag {
    const val PLAY = 1
    const val PAUSE = 2
    const val STOP = 3
    const val NEW_PROJECT = 55
    const val ADD_ASSET = 42
    const val ADD_TRACK = 29
    const val ADD_CLIP = 8
    const val REQUEST_SNAPSHOT = 54
}
