package io.github.devriesl.openglsample

import android.opengl.GLES30.*
import android.opengl.Matrix.*
import android.opengl.GLSurfaceView
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

class OpenGLRenderer: GLSurfaceView.Renderer {
    private val vertexData: FloatBuffer
    private val tableVerticesWithTriangles = floatArrayOf(
        // Triangle Fan
           0f,    0f,   1f,   1f,   1f,
        -0.5f, -0.8f, 0.7f, 0.7f, 0.7f,
        +0.5f, -0.8f, 0.7f, 0.7f, 0.7f,
        +0.5f, +0.8f, 0.7f, 0.7f, 0.7f,
        -0.5f, +0.8f, 0.7f, 0.7f, 0.7f,
        -0.5f, -0.8f, 0.7f, 0.7f, 0.7f,
        // Line 1
        -0.5f,    0f,   1f,   0f,   0f,
        +0.5f,    0f,   1f,   0f,   0f,
        // Mallets
          0f,  -0.4f,   0f,   0f,   1f,
          0f,  +0.4f,   1f,   0f,   0f
    )

    private var program: Int = 0
    private var uColorLocation: Int = 0
    private var aPositionLocation: Int = 0
    private var aColorLocation: Int = 0
    private var matrix = FloatArray(16)
    private var uMatrixLocation: Int = 0

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
        aColorLocation = glGetAttribLocation(program, "a_Color")
        uMatrixLocation = glGetUniformLocation(program, "u_Matrix")

        vertexData.position(0)
        glVertexAttribPointer(aPositionLocation, POSITION_COMPONENT_COUNT, GL_FLOAT, false, STRIDE, vertexData)
        glEnableVertexAttribArray(aPositionLocation)

        vertexData.position(POSITION_COMPONENT_COUNT)
        glVertexAttribPointer(aColorLocation, COLOR_COMPONENT_COUNT, GL_FLOAT, false, STRIDE, vertexData)
        glEnableVertexAttribArray(aColorLocation)
    }

    override fun onSurfaceChanged(p0: GL10?, width: Int, height: Int) {
        glViewport(0, 0, width, height)
        if (width > height) {
            val aspectRatio = width.toFloat() / height.toFloat()
            orthoM(matrix, 0, -aspectRatio, aspectRatio, -1f, 1f, -1f, 1f)
        } else {
            val aspectRatio = height.toFloat() / width.toFloat()
            orthoM(matrix, 0, -1f, 1f, -aspectRatio, aspectRatio, -1f, 1f)
        }
    }

    override fun onDrawFrame(p0: GL10?) {
        glClear(GL_COLOR_BUFFER_BIT)
        glUniformMatrix4fv(uMatrixLocation, 1, false, matrix, 0)

        glDrawArrays(GL_TRIANGLE_FAN, 0, 6)

        glDrawArrays(GL_LINES, 6, 2)

        glDrawArrays(GL_POINTS, 8, 1)

        glDrawArrays(GL_POINTS, 9, 1)
    }

    companion object {
        const val POSITION_COMPONENT_COUNT = 2
        const val COLOR_COMPONENT_COUNT = 3
        const val BYTES_PER_FLOAT = 4
        const val STRIDE = (POSITION_COMPONENT_COUNT + COLOR_COMPONENT_COUNT) * BYTES_PER_FLOAT

        private const val VERTEX_SHADER = "" +
                "uniform mat4 u_Matrix;  \n" +
                "attribute vec4 a_Position;  \n" +
                "attribute vec4 a_Color;  \n" +
                "varying vec4 v_Color;  \n" +
                "void main(){               \n" +
                " v_Color = a_Color;  \n" +
                " gl_Position = u_Matrix * a_Position;  \n" +
                " gl_PointSize = 10.0;  \n" +
                "}  \n"

        private const val FRAGMENT_SHADER = "" +
                "precision mediump float;" +
                "varying vec4 v_Color;" +
                "void main(){" +
                "  gl_FragColor = v_Color;" +
                "}"
    }
}