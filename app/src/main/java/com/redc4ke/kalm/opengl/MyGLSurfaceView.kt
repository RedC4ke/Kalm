package com.redc4ke.kalm.opengl

import android.content.Context
import android.opengl.GLSurfaceView

class MyGLSurfaceView(context: Context) : GLSurfaceView(context){

    private val renderer: MyGLRenderer

    init {

        setEGLContextClientVersion(2)

        renderer = MyGLRenderer()
        setRenderer(renderer)

        renderMode = RENDERMODE_WHEN_DIRTY

    }
}

