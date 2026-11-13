package com.teamshryne.vimo

import android.os.Bundle
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import org.json.JSONObject
import java.io.File

class EditorActivity : AppCompatActivity(), SurfaceHolder.Callback {
    private var engine: Long = 0
    private lateinit var output: TextView
    private lateinit var surfaceView: SurfaceView
    private var surfaceReady = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_editor)
        output = findViewById(R.id.output)
        surfaceView = findViewById(R.id.preview)
        surfaceView.holder.addCallback(this)

        try {
            engine = VimoCore.nativeCreate(1280, 720, 48000, 2)
            loadEffects()
            setupProject()
            log("Engine ready")
        } catch (t: Throwable) {
            log("Engine failed: ${t.message}")
        }

        findViewById<Button>(R.id.backBtn).setOnClickListener { finish() }
        findViewById<Button>(R.id.playBtn).setOnClickListener { cmd(Cmd.PLAY); log("Play") }
        findViewById<Button>(R.id.pauseBtn).setOnClickListener { cmd(Cmd.PAUSE); log("Pause") }
        findViewById<Button>(R.id.stopBtn).setOnClickListener { cmd(Cmd.STOP); log("Stop") }
        findViewById<Button>(R.id.seekStartBtn).setOnClickListener { cmd(Cmd.SEEK, c = 0); log("Seek start") }

        findViewById<Button>(R.id.newProjectBtn).setOnClickListener {
            cmd(Cmd.NEW_PROJECT)
            setupSample()
            log("New project")
        }
        findViewById<Button>(R.id.addAssetBtn).setOnClickListener {
            cmd(Cmd.ADD_ASSET, text = "demo.mp4", floats = floatArrayOf(10f, 1920f, 1080f, 1f))
            log("Asset added")
        }
        findViewById<Button>(R.id.addTrackBtn).setOnClickListener {
            cmd(Cmd.ADD_TRACK, a = 0)
            log("Track added")
        }
        findViewById<Button>(R.id.addClipBtn).setOnClickListener {
            cmd(Cmd.ADD_CLIP, a = 0, b = 0, c = 0)
            log("Clip added")
        }
        findViewById<Button>(R.id.addEffectBtn).setOnClickListener {
            cmd(Cmd.ADD_EFFECT, a = 0, b = 0, c = 0)
            log("Effect added")
        }
        findViewById<Button>(R.id.undoBtn).setOnClickListener { cmd(Cmd.UNDO); log("Undo") }
        findViewById<Button>(R.id.redoBtn).setOnClickListener { cmd(Cmd.REDO); log("Redo") }
        findViewById<Button>(R.id.addKeyframeBtn).setOnClickListener {
            cmd(Cmd.ADD_KEYFRAME, a = 0, b = 0, c = 1_000_000_000L, floats = floatArrayOf(0.5f))
            log("Keyframe added")
        }
        findViewById<Button>(R.id.addTransitionBtn).setOnClickListener {
            cmd(Cmd.ADD_TRANSITION, a = 0, b = 0, c = 0)
            log("Transition added")
        }
        findViewById<Button>(R.id.previewScaleBtn).setOnClickListener {
            cmd(Cmd.SET_PREVIEW_SCALE, a = 500)
            log("Preview scale 0.5")
        }
        findViewById<Button>(R.id.setResBtn).setOnClickListener {
            cmd(Cmd.SET_RESOLUTION, a = 1280, b = 720)
            log("Resolution 1280x720")
        }
        findViewById<Button>(R.id.exportStartBtn).setOnClickListener {
            cmd(Cmd.START_EXPORT, a = 0)
            log("Export started")
        }
        findViewById<Button>(R.id.exportPauseBtn).setOnClickListener { cmd(Cmd.EXPORT_PAUSE); log("Export paused") }
        findViewById<Button>(R.id.exportResumeBtn).setOnClickListener { cmd(Cmd.EXPORT_RESUME); log("Export resumed") }
        findViewById<Button>(R.id.exportCancelBtn).setOnClickListener { cmd(Cmd.CANCEL_EXPORT); log("Export cancelled") }
        findViewById<Button>(R.id.snapshotBtn).setOnClickListener {
            val json = VimoCore.nativeSnapshot(engine)
            log("Snapshot: ${json?.take(500) ?: "null"}")
        }
        findViewById<Button>(R.id.statsBtn).setOnClickListener {
            val s = VimoCore.nativeStats(engine)
            log("Stats: ${s?.contentToString()}")
        }
        findViewById<Button>(R.id.saveBtn).setOnClickListener {
            val path = File(filesDir, "project.vimo").absolutePath
            cmd(Cmd.SAVE_PROJECT, text = path)
            getSharedPreferences("vimo", MODE_PRIVATE).edit().putString("last_project", path).apply()
            log("Saved: $path")
        }
    }

    private fun setupProject() {
        val path = intent.getStringExtra("project_path")
        val isNew = intent.getBooleanExtra("new", false)
        val isSample = intent.getBooleanExtra("sample", false)
        when {
            path != null && File(path).exists() -> {
                cmd(Cmd.LOAD_PROJECT, text = path)
                log("Loaded: $path")
            }
            isNew || isSample -> setupSample()
        }
    }

    private fun setupSample() {
        cmd(Cmd.NEW_PROJECT)
        cmd(Cmd.SET_PROJECT_NAME, text = "Sample")
        cmd(Cmd.SET_RESOLUTION, a = 1280, b = 720)
        cmd(Cmd.SET_FPS, a = 30, b = 1)
        cmd(Cmd.ADD_ASSET, text = "sample.mp4", floats = floatArrayOf(10f, 1920f, 1080f, 1f))
        cmd(Cmd.ADD_TRACK, a = 0)
        cmd(Cmd.ADD_CLIP, a = 0, b = 0, c = 0)
        cmd(Cmd.ADD_EFFECT, a = 0, b = 0, c = 0)
    }

    private fun loadEffects() {
        try {
            val manifest = assets.open("vimo/manifests/vimo.effects.json").bufferedReader().readText()
            val shaderDir = "vimo/shaders"
            val shaders = JSONObject()
            assets.list(shaderDir)?.forEach { name ->
                val path = "$shaderDir/$name"
                shaders.put(path, assets.open(path).bufferedReader().readText())
            }
            val mismatches = VimoCore.nativeLoadEffects(engine, manifest, shaders.toString())
            log("Effects loaded, mismatches=$mismatches")
        } catch (t: Throwable) {
            log("Effects failed: ${t.message}")
        }
    }

    private fun cmd(tag: Int, a: Long = 0, b: Long = 0, c: Long = 0, text: String? = null, floats: FloatArray? = null) {
        if (engine == 0L) return
        VimoCore.nativeExecute(engine, tag, a, b, c, text, floats)
    }

    private fun log(msg: String) {
        runOnUiThread {
            output.append("\n$msg")
        }
    }

    override fun surfaceCreated(holder: SurfaceHolder) {
        if (engine == 0L) return
        val w = surfaceView.width.coerceAtLeast(1)
        val h = surfaceView.height.coerceAtLeast(1)
        val ok = VimoCore.nativeSetSurface(engine, holder.surface, w, h)
        surfaceReady = ok
        log("Surface: $ok ${w}x${h}")
    }

    override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {}

    override fun surfaceDestroyed(holder: SurfaceHolder) {
        surfaceReady = false
    }

    override fun onDestroy() {
        if (engine != 0L) {
            VimoCore.nativeDestroy(engine)
            engine = 0
        }
        super.onDestroy()
    }
}
