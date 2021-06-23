package com.example.opengl;

import android.content.Context;
import android.graphics.PixelFormat;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;

import static android.opengl.GLES20.GL_VERSION;
import static android.opengl.GLES20.glGetString;


public class OpenGlView extends GLSurfaceView {

    private final GlRenderer22 renderer;

    public OpenGlView(Context context) {
        super(context);
        initOpenGlView();

        // set the renderer for drawing
        renderer = new GlRenderer22();
        setRenderer(renderer);
    }

    public OpenGlView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initOpenGlView();
        // set the renderer for drawing
        renderer = new GlRenderer22();
    }

    private void initOpenGlView() {
        // create OpenGlES 2.0 context
        setEGLContextClientVersion(2);
        setPreserveEGLContextOnPause(true);
    }

}
