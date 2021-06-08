package com.example.opengl;

import android.opengl.GLES20;
import android.opengl.GLES32;
import android.opengl.GLSurfaceView;
import android.opengl.GLU;
import android.opengl.Matrix;
import android.util.Log;

import java.io.FileNotFoundException;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;


// reference url for projection:  https://developer.android.com/training/graphics/opengl/projection.html#projection
public class GlRenderer22 implements GLSurfaceView.Renderer{

    private final float[] mMVPMatrix = new float[16];//model view projection matrix
    private final float[] mProjectionMatrix = new float[16];//projection mastrix
    private final float[] mViewMatrix = new float[16];//view matrix
    private final float[] mMVMatrix=new float[16];//model view matrix
    private final float[] mModelMatrix=new float[16];//model  matrix

    private Triangle mTriangle;
    private float angle;
    private ImageTransform imageTransform;
    private TransformImage transformImage;
    private transform Transform;

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        System.out.println("*************************************************************     in OnSurfaceCreated 22 ********************");
        // set the background frame colour
        gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);

//        square22 = new Square22();
//        mTriangle = new Triangle();
//        imageTransform = new ImageTransform();

//        transformImage = new TransformImage();
        Transform = new transform();
    }

    public static void checkGlError(String glOperation) {
        int error;
        if ((error = GLES20.glGetError()) != GLES20.GL_NO_ERROR) {
            Log.e("MyRenderer", glOperation + ": glError " + error);
        }
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        System.out.println("*************************************************************     in OnsurfaceChange 22  ********************");
        gl.glViewport(0,0, width, height);
        gl.glClearColor(0,0,0,1);
        Matrix.orthoM(mProjectionMatrix, 0, 0, width, height, 0, 0, 1);


        float ratio = (float) width / height;
        float left=-ratio,right=ratio;
        // this projection matrix is applied to object coordinates
        // in the onDrawFrame() method
//        Matrix.frustumM(mProjectionMatrix, 0, left,right, -1.0f, 1.0f, 1.0f, 8.0f);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        System.out.println("*************************************************************     in OnSurfaceDraw  22  ********************");
        //Redraw background colour
        gl.glClear(gl.GL_COLOR_BUFFER_BIT | gl.GL_DEPTH_BUFFER_BIT);

        Matrix.setIdentityM(mMVPMatrix,0);//set the model view projection matrix to an identity matrix
        Matrix.setIdentityM(mMVMatrix,0);//set the model view  matrix to an identity matrix
        Matrix.setIdentityM(mModelMatrix,0);//set the model matrix to an identity matrix
//
//        // Set the camera position (View matrix)
        Matrix.setLookAtM(mViewMatrix, 0,
                0.0f, 0f, 1.0f,//camera is at (0,0,1)
                0f, 0f, 0f,//looks at the origin
                0f, 1f, 0.0f);//head is down (set to (0,1,0) to look from the top)


        // Calculate the projection and view transformation
        //calculate the model view matrix
        Matrix.multiplyMM(mMVMatrix,0,mViewMatrix,0,mModelMatrix,0);
        Matrix.multiplyMM(mMVPMatrix,0,mProjectionMatrix,0,mMVMatrix,0);

        Matrix.multiplyMM(mMVPMatrix, 0, mProjectionMatrix, 0, mViewMatrix, 0);


        try {
            Transform.run(mMVPMatrix,"roy.png", "triangulation.txt", "reference_points.txt", "warped_points.txt", "roy_bg.jpg");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }
}
