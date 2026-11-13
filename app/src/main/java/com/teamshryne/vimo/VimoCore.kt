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
    external fun nativeSetSurface(handle: Long, surface: Any, width: Int, height: Int): Boolean
    external fun nativeLoadEffects(handle: Long, manifest: String, shaders: String): Int
}

object Cmd {
    const val PLAY = 1
    const val PAUSE = 2
    const val STOP = 3
    const val SEEK = 4
    const val SET_RATE = 5
    const val UNDO = 6
    const val REDO = 7
    const val ADD_CLIP = 8
    const val REMOVE_CLIP = 9
    const val SET_PARAM = 10
    const val LOAD_PROJECT = 11
    const val SAVE_PROJECT = 12
    const val START_EXPORT = 13
    const val CANCEL_EXPORT = 14
    const val THERMAL = 16
    const val MEMORY = 17
    const val SET_PREVIEW_SCALE = 18
    const val MOVE_CLIP = 19
    const val TRIM_CLIP = 20
    const val SPLIT_CLIP = 21
    const val SET_CLIP_ENABLED = 22
    const val SET_CLIP_OPACITY = 23
    const val SET_CLIP_BLEND = 24
    const val SET_CLIP_TRANSFORM = 25
    const val SET_CLIP_VOLUME = 26
    const val SET_CLIP_SPEED = 27
    const val SET_CLIP_MUTED = 28
    const val ADD_TRACK = 29
    const val REMOVE_TRACK = 30
    const val SET_TRACK_ENABLED = 31
    const val SET_TRACK_MUTED = 32
    const val SET_TRACK_SOLO = 33
    const val SET_TRACK_LOCKED = 34
    const val SET_TRACK_VOLUME = 35
    const val ADD_EFFECT = 36
    const val REMOVE_EFFECT = 37
    const val SET_EFFECT_ENABLED = 38
    const val ADD_KEYFRAME = 39
    const val REMOVE_KEYFRAME = 40
    const val SET_KEYFRAME_INTERP = 41
    const val ADD_ASSET = 42
    const val REMOVE_ASSET = 43
    const val ADD_TRANSITION = 44
    const val REMOVE_TRANSITION = 45
    const val SET_PROJECT_NAME = 46
    const val SET_RESOLUTION = 47
    const val SET_FPS = 48
    const val SET_AUTOSAVE_DIR = 49
    const val RECOVER_AUTOSAVE = 50
    const val SET_LOOP = 51
    const val EXPORT_PAUSE = 52
    const val EXPORT_RESUME = 53
    const val REQUEST_SNAPSHOT = 54
    const val NEW_PROJECT = 55
}
