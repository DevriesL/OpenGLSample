package io.github.devriesl.openglsample

import android.opengl.GLES30.*

object GLUtil {
    fun loadShader(type: Int, shaderCode: String): Int {
        // create a vertex shader type (GLES30.GL_VERTEX_SHADER)
        // or a fragment shader type (GLES30.GL_FRAGMENT_SHADER)
        val shaderHandle = glCreateShader(type)

        // add the source code to the shader and compile it
        glShaderSource(shaderHandle, shaderCode)
        glCompileShader(shaderHandle)

        return shaderHandle
    }

    fun linkProgram(
        vertexShaderHandle: Int,
        fragShaderHandle: Int,
        attributes: Array<String>? = null
    ): Int {
        val programHandle = glCreateProgram()
        glAttachShader(programHandle, vertexShaderHandle)
        glAttachShader(programHandle, fragShaderHandle)

        if (attributes != null) {
            for (i in attributes.indices) {
                glBindAttribLocation(programHandle, i, attributes[i])
            }
        }

        glLinkProgram(programHandle)
        glDeleteShader(vertexShaderHandle)
        glDeleteShader(fragShaderHandle)
        return programHandle
    }
}