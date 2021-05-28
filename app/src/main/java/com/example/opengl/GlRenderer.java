package com.example.opengl;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;

import java.io.FileNotFoundException;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import static android.opengl.GLES20.GL_COLOR_BUFFER_BIT;
import static android.opengl.GLES20.GL_DEPTH_BUFFER_BIT;

public class GlRenderer implements GLSurfaceView.Renderer {
    private Triangle mTriangle;
    private Square22   mSquare;
    private OpenGL openGL;
    private Square square;


    public static int loadShader(int type, String shaderCode){

        // create a vertex shader type (GLES20.GL_VERTEX_SHADER)
        // or a fragment shader type (GLES20.GL_FRAGMENT_SHADER)
        int shader = GLES20.glCreateShader(type);
        System.out.println("*************************************************************     LoadShader ********************");
        // add the source code to the shader and compile it
        GLES20.glShaderSource(shader, shaderCode);
        GLES20.glCompileShader(shader);

        return shader;
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        System.out.println("*************************************************************     in OnSurfaceCreated ********************");
        // set the background frame colour
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
        // initialize a triangle
//        mTriangle = new Triangle();
        // initialize a square
        mSquare = new Square22();

        openGL = new OpenGL();
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        System.out.println("*************************************************************     in OnsurfaceChange ********************");
        GLES20.glViewport(0,0, width, height);
        GLES20.glClearColor(0,0,0,1);
//        float[] projectionMatrix = new float[16];                         // for 2.0
//        Matrix.orthoM(projectionMatrix, 0, 0, width, height, 0, 0, 1);    // for 2.0
        gl.glMatrixMode(GL10.GL_PROJECTION);
        gl.glLoadIdentity();
        gl.glOrthox(0, width, height, 0, 0, 1);
        gl.glMatrixMode(gl.GL_MODELVIEW);
        gl.glLoadIdentity();
//        try {
//            openGL.run("roy.png", "triangulation.txt", "reference_points.txt", "warped_points.txt", "roy_bg.jpg");
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        System.out.println("*************************************************************     in OnSurfaceDraw ********************");
        //Redraw background colour
        GLES20.glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
//        mTriangle.draw();
//        mSquare.draw();
        try {
            openGL.run("roy.png", "triangulation.txt", "reference_points.txt", "warped_points.txt", "roy_bg.jpg");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
