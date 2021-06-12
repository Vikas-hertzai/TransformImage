package com.example.opengl;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES10;
import android.opengl.GLES20;
import android.opengl.GLUtils;
import android.os.Environment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import javax.microedition.khronos.opengles.GL10;

import static android.opengl.GLES10.glLoadIdentity;
import static android.opengl.GLES10.glMatrixMode;
import static android.opengl.GLES10.glOrthox;
import static android.opengl.GLES20.GL_BLEND;
import static android.opengl.GLES20.GL_COLOR_BUFFER_BIT;
import static android.opengl.GLES20.GL_DEPTH_BUFFER_BIT;
import static android.opengl.GLES20.GL_DEPTH_TEST;
import static android.opengl.GLES20.GL_LINEAR;
import static android.opengl.GLES20.GL_ONE;
import static android.opengl.GLES20.GL_ONE_MINUS_SRC_ALPHA;
import static android.opengl.GLES20.GL_TEXTURE_2D;
import static android.opengl.GLES20.GL_TEXTURE_MAG_FILTER;
import static android.opengl.GLES20.GL_TEXTURE_MIN_FILTER;
import static android.opengl.GLES20.glBindTexture;
import static android.opengl.GLES20.glBlendFunc;
import static android.opengl.GLES20.glClear;
import static android.opengl.GLES20.glClearColor;
import static android.opengl.GLES20.glDisable;
import static android.opengl.GLES20.glEnable;
import static android.opengl.GLES20.glGenTextures;
import static android.opengl.GLES20.glTexParameteri;
import static android.opengl.GLES20.glViewport;
import static javax.microedition.khronos.opengles.GL10.GL_MODELVIEW;
import static javax.microedition.khronos.opengles.GL10.GL_PROJECTION;

public class transform {

    public transform() {

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


    private FloatBuffer verticesBuffer;
    private FloatBuffer textureBuffer;

    private int vertexShader;
    private int fragmentShader;
    private int program;

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
    static List ReadWarpedFile(String file_name) throws FileNotFoundException {

        List<Float> list=new ArrayList<Float>(20000);

        File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        File myObj = new File(path, file_name);
        Scanner myReader = new Scanner(myObj);
        while (myReader.hasNextLine()) {
            String data = myReader.nextLine();
            String[] arrOfStr = data.split(" ");
            for (int i=0; i<160; i++) {
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


//    private FloatBuffer vertex;
//    private FloatBuffer texture;
//    private IntBuffer index;
    static final int COORDS_PER_VERTEX = 2;
    private final int mTextureCoordinateDataSize = 2;
    private int mMVPMatrixHandle;

    public void run(float[] mvpMatrix, FloatBuffer vertex, FloatBuffer texture, IntBuffer index, String image, String triangulation, String reference_points, String warped_points, String background_image) throws FileNotFoundException {

        Bitmap ref_img = transform.loadImageFromStorage(image);
        Bitmap ref_background_img = transform.loadImageFromStorage(background_image);
        List<Integer>  tri_idx = transform.ReadTriangulationFile(triangulation);
        List<Float> ref_pts = transform.ReadReferenceFile(reference_points);
        List  wrp_pts= transform.ReadWarpedFile(warped_points);

        int width = ref_img.getWidth();
        int height = ref_img.getHeight();

        // converting Image Buffer to texture
        int[] tex = new int[1];
        glGenTextures(1, IntBuffer.wrap(tex));  // initialize the array
        glBindTexture(GL_TEXTURE_2D, tex[0]);    //to activate the texture at index 0
        GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, ref_img, 0);  //map Bitmap to the texture
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);   //glTexParameteri to set various properties that decide how the texture is rendered
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);


        // for background image  // not currently using
//        int[] bg_tex = new int[1];
//        glGenTextures(1, IntBuffer.wrap(bg_tex));
//        glBindTexture  (GL_TEXTURE_2D, bg_tex[0]);
//        GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, ref_background_img, 0);
//        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
//        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);


//        int  n = wrp_pts.size() / ref_pts.size();    // number of frame
        int n = 1;  // testing with lesser number
        int  i = 0;

        // Setup OpenGL state.
        glClearColor(0.0f, 0.0f, 0.0f, 1.0f); //to clear the screen.
        glDisable(GL_DEPTH_TEST);

        float tex_scale_x = 1.0f / (float)(width  - 1);
        float tex_scale_y = 1.0f / (float)(height - 1);


        while (i < n)
        {
//            System.out.println("********************     while loop = " + i);

            // square vertex
            float vertices[] = {
                    -1f, -1f,
                    1f, -1f,
                    -1f, 1f,
                    1f, 1f,
            };

            // square textureVertices
            float textureVertices[] = {
                    0f,1f,
                    1f,1f,
                    0f,0f,
                    1f,0f
            };

            // creating buffer
            ByteBuffer buff = ByteBuffer.allocateDirect(vertices.length * 4);
            buff.order(ByteOrder.nativeOrder());
            verticesBuffer = buff.asFloatBuffer();
            verticesBuffer.put(vertices);
            verticesBuffer.position(0);

            buff = ByteBuffer.allocateDirect(textureVertices.length * 4);
            buff.order(ByteOrder.nativeOrder());
            textureBuffer = buff.asFloatBuffer();
            textureBuffer.put(textureVertices);
            textureBuffer.position(0);

            // creating buffer for warp point
//            int st = i * ref_pts.size();
//            int en = st + 160;
//            List<Float> wrp_pts_i =  wrp_pts.subList(st, en);
//            ByteBuffer bb = ByteBuffer.allocateDirect(wrp_pts_i.size() * 4);// (# of coordinate values * 4 bytes per float)
//            bb.order(ByteOrder.nativeOrder());
//            vertex = bb.asFloatBuffer();
//            for (int vert = 0; vert < wrp_pts_i.size(); vert++) {
//                vertex.put(vert, wrp_pts_i.get(vert));
//            }
//            vertex.position(0);
//
//            // creating buffer for reference point
//            ByteBuffer tb = ByteBuffer.allocateDirect(ref_pts.size() * 4);// (# of coordinate values * 4 bytes per float)
//            tb.order(ByteOrder.nativeOrder());
//            texture = tb.asFloatBuffer();
//            for (int t = 0; t < ref_pts.size(); t++) {
//                float ref_pt1 = ref_pts.get(t);
//                float ref_pt2 = ref_pt1 * tex_scale_x;
//                texture.put(t, ref_pt2);
//            }
//            texture.position(0);
//
//            // creating buffer for triangle index
//            ByteBuffer dlb = ByteBuffer.allocateDirect(tri_idx.size() * 4);
//            dlb.order(ByteOrder.nativeOrder());
//            index = dlb.asIntBuffer();
//            for (int idx = 0; idx < tri_idx.size(); idx++) {
//                index.put(idx, tri_idx.get(idx));
//            }
//            index.position(0);



//            int st = i * ref_pts.size();
//            int en = st + 160;
//            List<Float> wrp_pts_i =  wrp_pts.subList(st, en);
//            ByteBuffer BbCoord = ByteBuffer.allocateDirect(tri_idx.size()*2*4);
//            ByteBuffer BbVertex = ByteBuffer.allocateDirect(tri_idx.size()*2*4);
//            BbCoord.order(ByteOrder.nativeOrder());
//            BbVertex.order(ByteOrder.nativeOrder());
//            texture = BbCoord.asFloatBuffer();
//            vertex = BbVertex.asFloatBuffer();
//            int j = 0;
//            for (int idx = 0; idx < tri_idx.size(); idx++) {
//                int val = tri_idx.get(idx);
//                int pt = val;
//                float ref_pt1 = ref_pts.get(pt);
//                float ref_pt2 = ref_pt1 * tex_scale_x;
//                float ref_pt3 = ref_pts.get(pt + 1);
//                float ref_pt4 = ref_pt3 * tex_scale_y;
//                texture.put(j, ref_pt2);
//                texture.put(j + 1, ref_pt4);
//                float wrp_pt1 = wrp_pts_i.get(pt);
//                float wrp_pt2 = wrp_pts_i.get(pt + 1);
//                vertex.put(j, wrp_pt1);
//                vertex.put(j + 1, wrp_pt2);
//                j = j +2;
//            }
//            texture.position(0);
//            vertex.position(0);


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
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, tex[0]);
            GLES20.glUniform1i(textureHandle, 0);


            mMVPMatrixHandle = GLES20.glGetUniformLocation(program, "uMVPMatrix"); // get handle to shape's transformation matrix

            GLES20.glUniformMatrix4fv(mMVPMatrixHandle, 1, false, mvpMatrix, 0); // Apply the projection and view transformation
            GlRenderer22.checkGlError("error"); // check error
            glClear(GL_COLOR_BUFFER_BIT); // Clear the contents of the GLSurfaceView using glClear
            GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, tri_idx.size());
//            GLES20.glDrawElements(GLES20.GL_TRIANGLES, tri_idx.size(), GLES20.GL_UNSIGNED_SHORT, index); // to draw
            GLES20.glFinish();

            // clear buffer and increament i
            index.clear();
            vertex.clear();
            texture.clear();
            i++;


        }

    }

}
