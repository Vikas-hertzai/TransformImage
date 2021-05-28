package com.example.opengl;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.GLU;
import android.opengl.Matrix;

import java.io.FileNotFoundException;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;



public class GlRenderer22 implements GLSurfaceView.Renderer{

//    private Square22 square22;
    private Triangle mTriangle;
    private float angle;
    private ImageTransform imageTransform;
    private TransformImage transformImage;

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        System.out.println("*************************************************************     in OnSurfaceCreated 22 ********************");
        // set the background frame colour
        gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
        // Enable Smooth Shading, default not really needed.
//        gl.glShadeModel(GL10.GL_SMOOTH);
//        // Depth buffer setup.
//        // Depth buffer setup.
//        gl.glClearDepthf(1.0f);
//        // Enables depth testing.
//        gl.glEnable(GL10.GL_DEPTH_TEST);
//        // The type of depth testing to do.
//        gl.glDepthFunc(GL10.GL_LEQUAL);
//        // Really nice perspective calculations.
//        gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT,
//                GL10.GL_NICEST);


//        square22 = new Square22();
//        mTriangle = new Triangle();
        imageTransform = new ImageTransform();
//        transformImage = new TransformImage();
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        System.out.println("*************************************************************     in OnsurfaceChange 22  ********************");
        gl.glViewport(0,0, width, height);
//        gl.glClearColor(0,0,0,1);
//        float[] projectionMatrix = new float[16];                         // for 2.0
//        Matrix.orthoM(projectionMatrix, 0, 0, width, height, 0, 0, 1);    // for 2.0
        gl.glMatrixMode(gl.GL_PROJECTION);
        gl.glLoadIdentity();
        gl.glOrthox(0, width, height, 0, 0, 1);
        gl.glMatrixMode(gl.GL_MODELVIEW);
        gl.glLoadIdentity();

        // Select the projection matrix
//        gl.glMatrixMode(GL10.GL_PROJECTION);
//        // Reset the projection matrix
//        gl.glLoadIdentity();
//        // Calculate the aspect ratio of the window
//        GLU.gluPerspective(gl, 45.0f,
//                (float) width / (float) height,
//                0.1f, 100.0f);
//        // Select the modelview matrix
//        gl.glMatrixMode(GL10.GL_MODELVIEW);
//        // Reset the modelview matrix
//        gl.glLoadIdentity();
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        System.out.println("*************************************************************     in OnSurfaceDraw  22  ********************");
        //Redraw background colour
        gl.glClear(gl.GL_COLOR_BUFFER_BIT | gl.GL_DEPTH_BUFFER_BIT);
        try {
            imageTransform.run("roy.png", "triangulation.txt", "reference_points.txt", "warped_points.txt", "roy_bg.jpg");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        /*  test for square22/triangle
        float x = 0.2f;
        float y = 0.3f;

        gl.glLoadIdentity();
        // Translates 10 units into the screen.
        gl.glTranslatef(x + 0.1f, y + 0.2f, -10);

        // SQUARE A
        // Save the current matrix.
        gl.glPushMatrix();
        // Rotate square A counter-clockwise.
        gl.glRotatef(angle, 0, 0, 1);
        // Draw square A.
        mTriangle.draw(gl);
        // Restore the last matrix.
        gl.glPopMatrix();

        // SQUARE B
        // Save the current matrix
        gl.glPushMatrix();
        // Rotate square B before moving it, making it rotate around A.
        gl.glRotatef(-angle, 0, 1, 1);
        // Move square B.
        gl.glTranslatef(x + 0.9f, y + 0.5f, 0);
        // Scale it to 50% of square A
        gl.glScalef(.5f, .5f, .5f);
        // Draw square B.
        mTriangle.draw(gl);

        // SQUARE C
        // Save the current matrix
        gl.glPushMatrix();
        // Make the rotation around B
        gl.glRotatef(-angle*50, 1, 0, 1);
        gl.glTranslatef(0.1f, 1, 0);
        // Scale it to 50% of square B
        gl.glScalef(.5f, .5f, .5f);
        // Rotate around it's own center.
        gl.glRotatef(angle*10, 0, 0, 1);
        // Draw square C.
        mTriangle.draw(gl);

        // Restore to the matrix as it was before C.
        gl.glPopMatrix();
        // Restore to the matrix as it was before B.
        gl.glPopMatrix();

        // Increse the angle.
        angle++;

         */
    }
}
