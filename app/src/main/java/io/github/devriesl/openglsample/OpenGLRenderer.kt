package io.github.devriesl.openglsample

import android.opengl.GLES30.*
import android.opengl.GLSurfaceView
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

class OpenGLRenderer: GLSurfaceView.Renderer {
    private val vertexData: FloatBuffer
    private val tableVerticesWithTriangles = floatArrayOf(
        // Triangle 1
        -0.5f, -0.5f,
        +0.5f, +0.5f,
        -0.5f, +0.5f,
        // Triangle 1
        -0.5f, -0.5f,
        +0.5f, -0.5f,
        +0.5f, +0.5f,
        // Line 1
        -0.5f, 0f,
        +0.5f, 0f,
        // Mallets
        0f, -0.25f,
        0f, +0.25f
    )

    private var program: Int = 0
    private var uColorLocation: Int = 0
    private var aPositionLocation: Int = 0

    init {
        vertexData = ByteBuffer
            .allocateDirect(tableVerticesWithTriangles.size * BYTES_PER_FLOAT)
            .order(ByteOrder.nativeOrder())
            .asFloatBuffer()

        vertexData.put(tableVerticesWithTriangles)
    }

    override fun onSurfaceCreated(p0: GL10?, p1: EGLConfig?) {
        glClearColor(0.0f, 0.0f, 0.0f, 0.0f)
        val vertexShader = GLUtil.loadShader(GL_VERTEX_SHADER, VERTEX_SHADER)
        val fragmentShader = GLUtil.loadShader(GL_FRAGMENT_SHADER, FRAGMENT_SHADER)
        program = GLUtil.linkProgram(vertexShader, fragmentShader)
        glUseProgram(program)

        uColorLocation = glGetUniformLocation(program, "u_Color")
        aPositionLocation = glGetAttribLocation(program, "a_Position")

        vertexData.position(0)
        glVertexAttribPointer(aPositionLocation, POSITION_COMPONENT_COUNT, GL_FLOAT, false, 0, vertexData)
        glEnableVertexAttribArray(aPositionLocation)
    }

    override fun onSurfaceChanged(p0: GL10?, width: Int, height: Int) {
        glViewport(0, 0, width, height)
    }

    override fun onDrawFrame(p0: GL10?) {
        glClear(GL_COLOR_BUFFER_BIT)

        glUniform4f(uColorLocation, 1.0f, 1.0f, 1.0f, 1.0f)
        glDrawArrays(GL_TRIANGLES, 0, 6)

        glUniform4f(uColorLocation, 1.0f, 0.0f, 0.0f, 1.0f)
        glDrawArrays(GL_LINES, 6, 2)

        glUniform4f(uColorLocation, 0.0f, 0.0f, 1.0f, 1.0f)
        glDrawArrays(GL_POINTS, 8, 1)

        glUniform4f(uColorLocation, 1.0f, 0.0f, 0.0f, 1.0f)
        glDrawArrays(GL_POINTS, 9, 1)
    }

    companion object {
        const val POSITION_COMPONENT_COUNT = 2
        const val BYTES_PER_FLOAT = 4

        private const val VERTEX_SHADER = "" +
                "attribute vec4 a_Position;  \n" +
                "void main(){               \n" +
                " gl_Position = a_Position;  \n" +
                " gl_PointSize = 10.0;  \n" +
                "}  \n"

        private const val FRAGMENT_SHADER = "" +
                "precision mediump float;" +
                "uniform vec4 u_Color;" +
                "void main(){" +
                "  gl_FragColor = u_Color;" +
                "}"
    }
}