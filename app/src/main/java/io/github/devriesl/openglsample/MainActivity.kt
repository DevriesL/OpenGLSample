package io.github.devriesl.openglsample

import android.opengl.GLSurfaceView
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class MainActivity : AppCompatActivity() {
    private lateinit var glSurfaceView: GLSurfaceView
    private var rendererSet: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        glSurfaceView = GLSurfaceView(this)
        glSurfaceView.setEGLContextClientVersion(3)
        glSurfaceView.setRenderer(OpenGLRenderer())
        glSurfaceView.renderMode = GLSurfaceView.RENDERMODE_WHEN_DIRTY
        rendererSet = true
        setContentView(glSurfaceView)
    }

    override fun onPause() {
        super.onPause()

        if (rendererSet) {
            glSurfaceView.onPause()
        }
    }

    override fun onResume() {
        super.onResume()

        if (rendererSet) {
            glSurfaceView.onResume()
        }
    }
}