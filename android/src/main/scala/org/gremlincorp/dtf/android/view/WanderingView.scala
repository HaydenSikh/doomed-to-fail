package org.gremlincorp.dtf.android.view

import android.opengl.GLSurfaceView
import android.content.Context
import android.util.AttributeSet
import android.os.Bundle
import javax.microedition.khronos.opengles.GL10
import javax.microedition.khronos.egl.EGLConfig
import android.opengl.GLES20

class WanderingView(var context : Context) extends GLSurfaceView(context) {
    setEGLContextClientVersion(2)
    setRenderer(new WanderingRenderer)
}

class WanderingRenderer extends GLSurfaceView.Renderer {
    override def onSurfaceCreated(unused : GL10, config : EGLConfig) = {
        GLES20.glClearColor(0f, 0.78f, 0f, 1.0f)
    }

    override def onDrawFrame(unused : GL10) = {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT)
    }

    override def onSurfaceChanged(unused : GL10, width : Int, height : Int) = {
        GLES20.glViewport(0, 0, width, height)
    }
}
