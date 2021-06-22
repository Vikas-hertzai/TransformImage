package com.example.opengl;

import android.content.Context;
import android.graphics.PixelFormat;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;

import static android.opengl.GLES20.GL_VERSION;
import static android.opengl.GLES20.glGetString;


public class OpenGlView extends GLSurfaceView {

    private final GlRenderer22 renderer;
//    private final MyRenderer renderer;

    public OpenGlView(Context context) {
        super(context);
        initOpenGlView();
//        System.out.println("version : " + glGetString(GL_VERSION));
        // set the renderer for drawing
        System.out.println("*************************************************************     in OpenGlView ********************");
        renderer = new GlRenderer22();
//        renderer = new MyRenderer();
        setRenderer(renderer);
    }

    public OpenGlView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initOpenGlView();
        System.out.println("*************************************************************     in OpenGlView 2 ********************");
        // set the renderer for drawing
        renderer = new GlRenderer22();
//        renderer = new MyRenderer();
    }

    private void initOpenGlView() {
        // create OpenGlES 2.0 context
        setEGLContextClientVersion(2);
        setPreserveEGLContextOnPause(true);
        System.out.println("*************************************************************     init OpenGlView ********************");
//        setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
//        setEGLConfigChooser(8, 8, 8, 8, 0, 0); // added 10.05
//        getHolder().setFormat(PixelFormat.TRANSLUCENT);  // // added 10.05
    }


   /* private final MyRenderer mRenderer;

    public OpenGlView(Context context) {
        super(context);
        setEGLContextClientVersion(2);// Create an OpenGL ES 2.0 context.
        mRenderer = new MyRenderer();// Set the Renderer for drawing on the GLSurfaceView
        setRenderer(mRenderer);
        // Render the view only when there is a change in the drawing data
        setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
    }  */
}
