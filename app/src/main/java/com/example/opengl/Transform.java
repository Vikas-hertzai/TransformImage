package com.example.opengl;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES10;
import android.opengl.GLES20;
import android.os.Environment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static android.opengl.GLES20.GL_BLEND;
import static android.opengl.GLES20.GL_COLOR_BUFFER_BIT;
import static android.opengl.GLES20.GL_DEPTH_TEST;
import static android.opengl.GLES20.glClear;
import static android.opengl.GLES20.glClearColor;
import static android.opengl.GLES20.glDisable;

public class Transform {

    public Transform() {
        initializeShader();
    }

    // other url::: https://blog.jayway.com/2010/12/30/opengl-es-tutorial-for-android-part-vi-textures/ ,    https://proandroiddev.com/how-to-use-shaders-for-android-view-and-how-android-view-may-use-shaders-79eecedd26e9
    // https://xzan.medium.com/opengl-a-noobs-guide-for-android-developers-5eed724e07ad

    // reference url:  https://code.tutsplus.com/tutorials/how-to-use-android-media-effects-with-opengl-es--cms-23650
    // Source code of vertex shader
    private final String vertexShaderCode =
            "attribute vec2 aPosition;" +              //aPosition is a variable that will be bound to the FloatBuffer that contains the coordinates of the vertices
                    "uniform mat4 uMVPMatrix;"+        //model view  projection matrix
                    "attribute vec2 aTexPosition;" +   // aTexPosition is a variable that will be be bound to the FloatBuffer that contains the coordinates of the texture
                    "varying vec2 vTexPosition;" +     // vTexPosition is a varying variable, whose value is simply passed on to the fragment shader
                    "void main() {" +
                    "  gl_Position = uMVPMatrix* vec4(aPosition, 1.0, 1.0);" +     //calculate the position of the vertex
                    "  vTexPosition = aTexPosition;" +
                    "}";


    // Source code of fragment shader
    //the fragment shader is responsible for coloring. It picks up colors from the texture using the texture2D method and assigns them to the fragment using a built-in variable named gl_FragColor
    private final String fragmentShaderCode =
            "precision mediump float;" +
                    "uniform sampler2D uTexture;" +
                    "varying vec2 vTexPosition;" +
                    "void main() {" +
                    "  gl_FragColor = texture2D(uTexture, vTexPosition);" +
                    "}";


    private int vertexShader;
    private int fragmentShader;
    private int program;
    private int mMVPMatrixHandle;

    private void initializeShader(){
        // create shader
        vertexShader = GLES20.glCreateShader(GLES20.GL_VERTEX_SHADER); //create a vertex shader object and return a reference to it in the form of an int
        GLES20.glShaderSource(vertexShader, vertexShaderCode); // associate the appropriate shader code with the shader
        GLES20.glCompileShader(vertexShader); // compile shader

        fragmentShader = GLES20.glCreateShader(GLES20.GL_FRAGMENT_SHADER); ////create a fragment shader object and return a reference to it in the form of an int
        GLES20.glShaderSource(fragmentShader, fragmentShaderCode);
        GLES20.glCompileShader(fragmentShader);

        program = GLES20.glCreateProgram();   // // create empty OpenGL Program
        GLES20.glAttachShader(program, vertexShader);  // add the vertex shader to program
        GLES20.glAttachShader(program, fragmentShader);  // add the fragment shader to program

        GLES20.glLinkProgram(program);   // link the  OpenGL program to create an executable

    }



    // read Image
    static Bitmap loadImageFromStorage(String image_name) throws FileNotFoundException {


        File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        File f=new File(path, image_name);
        Bitmap b = BitmapFactory.decodeStream(new FileInputStream(f));

        return b;

    }

    // Read reference.txt file
    static List ReadReferenceFile(String file_name) throws FileNotFoundException {

        List<Float> list=new ArrayList<Float>();

        File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        File myObj = new File(path, file_name);
        Scanner myReader = new Scanner(myObj);
        while (myReader.hasNextLine()) {
            String data = myReader.nextLine();
            String[] arrOfStr = data.split(" ");
            for (int i=0; i<2; i++) {
                float val = Float.parseFloat(arrOfStr[i]);
                list.add(val);
            }
        }
        myReader.close();
        return list;

    }

    // read Warp.txt
    static List ReadWarpedFile(String file_name,int refLen) throws FileNotFoundException {

        List<Float> list=new ArrayList<Float>(20000);

        File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        File myObj = new File(path, file_name);
        Scanner myReader = new Scanner(myObj);
        while (myReader.hasNextLine()) {
            String data = myReader.nextLine();
            String[] arrOfStr = data.split(" ");
            for (int i=0; i<refLen; i++) {
                float val = Float.parseFloat(arrOfStr[i]);
                list.add(val);
            }
        }
        myReader.close();
        return list;
    }

    // read triangulation.txt
    static List ReadTriangulationFile(String file_name) throws FileNotFoundException {

        List<Integer> list=new ArrayList<Integer>();

        File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        File myObj = new File(path, file_name);
        Scanner myReader = new Scanner(myObj);
        while (myReader.hasNextLine()) {
            String data = myReader.nextLine();
            String[] arrOfStr = data.split(" ");
            for (int i=0; i<3; i++) {
                int val = Integer.parseInt(arrOfStr[i]);
                int val1 = val * 2;
                list.add(val1);
            }
        }
        myReader.close();
        return list;
    }


    public long run(int tex, float[] mvpMatrix, FloatBuffer vertex, FloatBuffer texture, int triangle_size){
        long renderedAt=System.nanoTime();
        //start=System.nanoTime();
        // Setup OpenGL state.
        glClearColor(0.0f, 0.0f, 0.0f, 1.0f); //to clear the screen.
        glDisable(GL_DEPTH_TEST);

        // draw texture
        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, 0);  //  create a named frame buffer object
        GLES20.glUseProgram(program);  // Add program to OpenGL environment
        GLES20.glDisable(GL_BLEND);   //to disable the blending of colors while rendering.

        int positionHandle = GLES20.glGetAttribLocation(program, "aPosition");  // get handle to vertex buffer position
        int textureHandle = GLES20.glGetUniformLocation(program, "uTexture");  //Set Texture Handles
        int texturePositionHandle = GLES20.glGetAttribLocation(program, "aTexPosition");  // get handle to texture buffer position

        // glVertexAttribPointer to associate the aPosition and aTexPosition handles with the verticesBuffer and the textureBuffer respectively
        GLES20.glVertexAttribPointer(texturePositionHandle, 2, GLES20.GL_FLOAT, true, 8, texture);
        GLES20.glEnableVertexAttribArray(texturePositionHandle);

        GLES20.glVertexAttribPointer(positionHandle, 2, GLES20.GL_FLOAT, true, 8, vertex);
        GLES20.glEnableVertexAttribArray(positionHandle);

        // activate and bind the texture
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, tex);
        GLES20.glUniform1i(textureHandle, 0);

        mMVPMatrixHandle = GLES20.glGetUniformLocation(program, "uMVPMatrix"); // get handle to shape's transformation matrix
        GLES20.glUniformMatrix4fv(mMVPMatrixHandle, 1, false, mvpMatrix, 0); // Apply the projection and view transformation



        glClear(GL_COLOR_BUFFER_BIT); // Clear the contents of the GLSurfaceView using glClear
        GLES20.glDrawArrays(GLES10.GL_TRIANGLES, 0, triangle_size);


        // clear buffer
        vertex.clear();


        return renderedAt;
    }


}
