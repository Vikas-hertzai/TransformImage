package com.example.opengl;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.GLES32;
import android.opengl.GLUtils;
import android.os.Environment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static android.opengl.GLES10.GL_MODELVIEW;
import static android.opengl.GLES10.GL_PROJECTION;
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

public class ImageTransform {

    private Square square;
//    private android.content.Context Context;
    public ImageTransform() {

    }

//    private final String vertexShaderCode =
//            "attribute vec4 aPosition;" +
//                    "attribute vec2 aTexPosition;" +
//                    "varying vec2 vTexPosition;" +
//                    "void main() {" +
//                    "  gl_Position = aPosition;" +
//                    "  vTexPosition = aTexPosition;" +
//                    "}";
//

    private final String vertexShaderCode =
            "attribute vec2 a_TexCoordinate;" +
                    "varying vec2 v_TexCoordinate;" +
                    "uniform mat4 uMVPMatrix;" +
                    "attribute vec4 vPosition;" +
                    "void main() {" +
                    "  gl_Position = vPosition * uMVPMatrix;" +
                    "v_TexCoordinate = a_TexCoordinate;" +
                    "}";

//    private final String vertexShaderCode =
//            "attribute vec3 aVertexPosition;"+//vertex of an object
//                    " attribute vec4 aVertexColor;"+//the colour  of the object
//                    "     uniform mat4 uMVPMatrix;"+//model view  projection matrix
//                    "    varying vec4 vColor;"+//variable to be accessed by the fragment shader
//                    "    void main() {" +
//                    "        gl_Position = uMVPMatrix* vec4(aVertexPosition, 1.0);"+//calculate the position of the vertex
//                    "        vColor=aVertexColor;}";//get the colour from the application program


    //    private final String vertexShaderCode =
//            "attribute vec4 vertexPosition;"+
//        "attribute vec2 vertexTextureCord;"+
//                "varying vec2 textureCord;"+
//                "uniform mat4 projection;"+
//                "uniform mat4 modelView;"+
//                "void main()"+
//                "{"+
//                "    gl_Position = projection * modelView * vertexPosition;"+
//                "    textureCord = vertexTextureCord;"+
//                "}";

/*
    private final String fragmentShaderCode =
            "precision mediump float;" +
                    "uniform sampler2D uTexture;" +
                    "varying vec2 vTexPosition;" +
                    "void main() {" +
                    "  gl_FragColor = texture2D(uTexture, vTexPosition);" +
                    "}";
*/

    private final String fragmentShaderCode =
            "precision mediump float;" +
                    "uniform vec4 vColor;" +
                    "uniform sampler2D u_Texture;" +
                    "varying vec2 v_TexCoordinate;" +
                    "void main() {" +
                    "gl_FragColor = texture2D(u_Texture, v_TexCoordinate) * vColor;" +
                    "}";


/*
    private float vertices[] = {
            -1f, -1f,
            1f, -1f,
            -1f, 1f,
            1f, 1f,
    };

//    private float vertices[] = {
//            0f, 0f,
//            51f, 0f,
//            51f, 683f,
//            0f, 68f,
//    };

    private float textureVertices[] = {
            0f,1f,
            1f,1f,
            0f,0f,
            1f,0f
    };

    private FloatBuffer verticesBuffer;
    private FloatBuffer textureBuffer;

    private void initializeBuffers(){
        System.out.println("*************************************************************     in IitializeBuffers ********************");
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
    }

    private int vertexShader;
    private int fragmentShader;
    private int program;

 */

    /*private void initializeProgram(){
        System.out.println("*************************************************************     in initializeProgram********************");
        vertexShader = GLES20.glCreateShader(GLES20.GL_VERTEX_SHADER);
        GLES20.glShaderSource(vertexShader, vertexShaderCode);
        GLES20.glCompileShader(vertexShader);

        fragmentShader = GLES20.glCreateShader(GLES20.GL_FRAGMENT_SHADER);
        GLES20.glShaderSource(fragmentShader, fragmentShaderCode);
        GLES20.glCompileShader(fragmentShader);

        program = GLES20.glCreateProgram();
        GLES20.glAttachShader(program, vertexShader);
        GLES20.glAttachShader(program, fragmentShader);

        GLES20.glLinkProgram(program);
    }
*/


    private static Bitmap loadImageFromStorage(String image_name) throws FileNotFoundException {

        File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        File f=new File(path, image_name);
        Bitmap b = BitmapFactory.decodeStream(new FileInputStream(f));

        return b;

    }


    static List ReadReferenceFile(String file_name) throws FileNotFoundException {

        List<Float> list=new ArrayList<Float>();

        File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        File myObj = new File(path, file_name);
//            System.out.println(" **** File size in bytes " + myObj.list());
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
//            System.out.println("****** ReadReferenceFile :  " + list.size());
        return list;
    }


    static List ReadWarpedFile(String file_name) throws FileNotFoundException {

        List<Float> list=new ArrayList<Float>(20000);

        File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        File myObj = new File(path, file_name);
//            System.out.println(" **** File size in bytes " + myObj.list());
        Scanner myReader = new Scanner(myObj);
        while (myReader.hasNextLine()) {
            String data = myReader.nextLine();
//            List<Float> list1=new ArrayList<Float>();
            String[] arrOfStr = data.split(" ");
            for (int i=0; i<160; i++) {
                float val = Float.parseFloat(arrOfStr[i]);
                list.add(val);
            }
//            list.add(list1);
        }
        myReader.close();
//        System.out.println("****** ReadReferenceFile :  " + list.size());
        return list;

    }


    static List ReadTriangulationFile(String file_name) throws FileNotFoundException {

        List<Integer> list=new ArrayList<Integer>();

        File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        File myObj = new File(path, file_name);
//            System.out.println(" **** File size in bytes " + myObj.list());
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
//        System.out.println("****** ReadReferenceFile :  " + list.size());
        return list;

    }


    private FloatBuffer vertexBuffer;
    private FloatBuffer textureBuffer;
    private IntBuffer indexBuffer;
    private int program;
    private int mPositionHandle,PositionHandle, mColorHandle, mTextureUniformHandle;
    private int mMVPMatrixHandle;
    // number of coordinates per vertex in this array
    static final int COORDS_PER_VERTEX = 2;
    private int vertexCount;// number of vertices
    private final int vertexStride = COORDS_PER_VERTEX * 4; // 4 bytes per vertex
    private int mTextureCoordinateHandle;
    private final int mTextureCoordinateDataSize = 2;
    private int mTextureDataHandle;
    private IntBuffer index;
    float color[] = { 0.0f, 0.0f, 0.0f, 1.0f };




    public int run(float[] mvpMatrix, String image, String triangulation, String reference_points, String warped_points, String background_image) throws FileNotFoundException {

//        FloatBuffer vertexBuffer;
//        IntBuffer indexBuffer;
//        int program;
//        int mPositionHandle,mColorHandle;
//        int mMVPMatrixHandle;
//        // number of coordinates per vertex in this array
//        final int COORDS_PER_VERTEX = 2;
//        int vertexCount;// number of vertices
//        final int vertexStride = COORDS_PER_VERTEX * 4; // 4 bytes per vertex

        Bitmap ref_img = ImageTransform.loadImageFromStorage(image);
        Bitmap ref_background_img = ImageTransform.loadImageFromStorage(background_image);
        List<Integer>  tri_idx = ImageTransform.ReadTriangulationFile(triangulation);
        List<Float> ref_pts = ImageTransform.ReadReferenceFile(reference_points);
        List  wrp_pts= ImageTransform.ReadWarpedFile(warped_points);
        System.out.println("ref img **** : " + ref_img.getHeight());

        int width = 512;
        int height = 683;

        int[] tex = new int[1];
        glGenTextures(1, IntBuffer.wrap(tex));
        glBindTexture(GL_TEXTURE_2D, tex[0]);
        GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, ref_img, 0);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);

        int[] bg_tex = new int[1];
        glGenTextures(1, IntBuffer.wrap(bg_tex));
        glBindTexture  (GL_TEXTURE_2D, bg_tex[0]);
        GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, ref_background_img, 0);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);

//        square = new Square();
//        square.draw(tex[0]);
//        ImageTransform.draw(tex[0]);

    /*
        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, 0);
        GLES20.glUseProgram(program);
        GLES20.glDisable(GLES20.GL_BLEND);

        int positionHandle = GLES20.glGetAttribLocation(program, "aPosition");
        int textureHandle = GLES20.glGetUniformLocation(program, "uTexture");
        int texturePositionHandle = GLES20.glGetAttribLocation(program, "aTexPosition");

        GLES20.glVertexAttribPointer(texturePositionHandle, 2, GLES20.GL_FLOAT, false, 0, textureBuffer);
        GLES20.glEnableVertexAttribArray(texturePositionHandle);

        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, tex[0]);
        GLES20.glUniform1i(textureHandle, 0);

        GLES20.glVertexAttribPointer(positionHandle, 2, GLES20.GL_FLOAT, false, 0, verticesBuffer);
        GLES20.glEnableVertexAttribArray(positionHandle);

        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4);

     */

        // Process all the warped images.
//        long  n = wrp_pts.size() / ref_pts.size();
        int n = 10;
        int  i = 0;

        // Setup OpenGL state.
//        glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
//        glDisable(GL_DEPTH_TEST);
//        // glEnable (GL_TEXTURE_2D);
//        // glEnable (GL_BLEND);
//        glBlendFunc(GL_ONE, GL_ONE_MINUS_SRC_ALPHA);

        float tex_scale_x = 1.0f / (float)(width  - 1);
        float tex_scale_y = 1.0f / (float)(height - 1);

//        square = new Square();
//        square.draw(tex[0]);

//        System.out.println("******* warp : " + wrp_pts.subList(0, 160));
//        System.out.println("******* ref : " + ref_pts);

        while (i < n) {
//            glViewport(0, 0, width, height);
//            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
//            System.out.println("working while  ***************************  ---- " + i);
//
//            glMatrixMode(GL_PROJECTION);
//            glLoadIdentity();
//            glOrthox(0, width, height, 0, 0, 1);
//
//            glMatrixMode(GL_MODELVIEW);
//            glLoadIdentity();

//            int st = i * ref_pts.size();
//            int en = st + 162;
//            List<Float> wrp_pts_i =  wrp_pts.subList(st, en);
//            FloatBuffer Coord;
//            FloatBuffer vertic;
//            ByteBuffer BbCoord = ByteBuffer.allocate(tri_idx.size()*4);
//            ByteBuffer BbVertex = ByteBuffer.allocate(tri_idx.size()*4);
//            BbCoord.order(ByteOrder.nativeOrder());
//            BbVertex.order(ByteOrder.nativeOrder());
//            Coord = BbCoord.asFloatBuffer();
//            vertic = BbCoord.asFloatBuffer();
//            for (int idx = 0; idx < tri_idx.size(); idx++) {
//                int val = tri_idx.get(idx);
//                int pt = val * 2;
//                float ref_pt1 = ref_pts.get(pt);
//                float ref_pt2 = ref_pt1 * tex_scale_x;
//                float ref_pt3 = ref_pts.get(pt + 1);
//                float ref_pt4 = ref_pt3 * tex_scale_y;
//                Coord.put(ref_pt2);
//                Coord.put(ref_pt4);
//                float wrp_pt1 = wrp_pts_i.get(pt);
//                float wrp_pt2 = wrp_pts_i.get(pt + 1);
//                vertic.put(wrp_pt1);
//                vertic.put(wrp_pt2);
//            }

//            Coord.position(0);
//            vertic.position(0);

//            Float[] texture = ref_pts.toArray(new Float[0]);

            glEnable(GL_TEXTURE_2D);
            {
                glEnable(GL_BLEND);
                glBindTexture(GL_TEXTURE_2D, tex[0]);
            }

            int st = i * ref_pts.size();
            int en = st + 160;
            List<Float> wrp_pts_i =  wrp_pts.subList(st, en);
            ByteBuffer bb = ByteBuffer.allocateDirect(wrp_pts_i.size() * 4);// (# of coordinate values * 4 bytes per float)
            bb.order(ByteOrder.nativeOrder());
            vertexBuffer = bb.asFloatBuffer();
            for (int vert = 0; vert < wrp_pts_i.size(); vert++) {
                vertexBuffer.put(vert, wrp_pts_i.get(vert));
            }
            vertexBuffer.position(0);
            vertexCount=wrp_pts_i.size()/COORDS_PER_VERTEX;

            ByteBuffer tb = ByteBuffer.allocateDirect(ref_pts.size() * 4);// (# of coordinate values * 4 bytes per float)
            tb.order(ByteOrder.nativeOrder());
            textureBuffer = tb.asFloatBuffer();
            int j = 0;
            for (int texture = 0; texture < ref_pts.size(); texture++) {
                float ref_pt1 = ref_pts.get(texture);
                float ref_pt2 = ref_pt1 * tex_scale_x;
                textureBuffer.put(texture, ref_pt2);
            }
            textureBuffer.position(0);

            ByteBuffer dlb = ByteBuffer.allocateDirect(tri_idx.size()*4);
            dlb.order(ByteOrder.nativeOrder());
            index = dlb.asIntBuffer();
            for (int idx = 0; idx < tri_idx.size(); idx++) {
                index.put(idx, tri_idx.get(idx));
            }
            index.position(0);

            // prepare shaders and OpenGL program
            int vertexShader = MyRenderer.loadShader(GLES32.GL_VERTEX_SHADER, vertexShaderCode);
            int fragmentShader = MyRenderer.loadShader(GLES32.GL_FRAGMENT_SHADER, fragmentShaderCode);

            program = GLES32.glCreateProgram();             // create empty OpenGL Program
            GLES32.glAttachShader(program, vertexShader);   // add the vertex shader to program
            GLES32.glAttachShader(program, fragmentShader); // add the fragment shader to program
            GLES20.glBindAttribLocation(program, tex[0], "a_TexCoordinate");
            GLES32.glLinkProgram(program);                  // link the  OpenGL program to create an executable
            GLES32.glUseProgram(program);// Add program to OpenGL environment

            // get handle to vertex shader's vPosition member
//            mPositionHandle = GLES32.glGetAttribLocation(program, "v_TexCoordinate");
            // Enable a handle to the triangle vertices
//            GLES32.glEnableVertexAttribArray(mPositionHandle);

            // get handle to shape's transformation matrix
//            mMVPMatrixHandle = GLES32.glGetUniformLocation(program, "uMVPMatrix");
//            MyRenderer.checkGlError("glGetUniformLocation");

            //draw

            GLES32.glUseProgram(program);//use the object's shading programs
            PositionHandle = GLES32.glGetAttribLocation(program, "vPosition");
            //Enable a handle to the triangle vertices
            GLES32.glEnableVertexAttribArray(PositionHandle);
            //Prepare the triangle coordinate data
            GLES32.glVertexAttribPointer(PositionHandle, COORDS_PER_VERTEX, GLES32.GL_FLOAT, false, vertexStride, vertexBuffer);
            //Get Handle to Fragment Shader's vColor member
            mColorHandle = GLES32.glGetUniformLocation(program, "vColor");
            GLES20.glUniform4fv(mColorHandle, 1, color, 0);
            //Set Texture Handles and bind Texture
            mTextureUniformHandle = GLES32.glGetUniformLocation(program, "u_Texture");
            mTextureCoordinateHandle = GLES32.glGetAttribLocation(program, "a_TexCoordinate");
            //Set the active texture unit to texture unit 0.
            GLES32.glActiveTexture(GLES32.GL_TEXTURE0);
            //Bind the texture to this unit.
            GLES32.glBindTexture(GLES32.GL_TEXTURE_2D, tex[0]);
            //Tell the texture uniform sampler to use this texture in the shader by binding to texture unit 0.
            GLES32.glUniform1i(mTextureUniformHandle, 0);
            //Pass in the texture coordinate information
            GLES32.glVertexAttribPointer(mTextureCoordinateHandle, mTextureCoordinateDataSize, GLES32.GL_FLOAT, false, 8, textureBuffer);
            GLES20.glEnableVertexAttribArray(mTextureCoordinateHandle);
            //Get Handle to Shape's Transformation Matrix
            mMVPMatrixHandle = GLES32.glGetUniformLocation(program, "uMVPMatrix");
//            MyRenderer.checkGlError("glUniformMatrix4fv");
            //Apply the projection and view transformation
            GLES32.glUniformMatrix4fv(mMVPMatrixHandle, 1, false, mvpMatrix, 0);
            //Draw the triangle
            GLES32.glDrawElements(GLES32.GL_TRIANGLE_STRIP, tri_idx.size(), GLES20.GL_UNSIGNED_SHORT, index);

            //Disable Vertex Array
            GLES20.glDisableVertexAttribArray(PositionHandle);



//            MyRenderer.checkGlError("glUniformMatrix4fv");
//            //set the attribute of the vertex to point to the vertex buffer
//            GLES32.glVertexAttribPointer(mPositionHandle, COORDS_PER_VERTEX, GLES32.GL_FLOAT, false, vertexStride, vertexBuffer);
//            GLES32.glVertexAttribPointer(mColorHandle, COORDS_PER_VERTEX,
//                    GLES32.GL_FLOAT, false, colorStride, colorBuffer);
//            // Draw the 3D character A
//            GLES32.glDrawElements(GLES32.GL_TRIANGLES,CharIndex.length,GLES32.GL_UNSIGNED_INT,indexBuffer);

//
//            int positionHandle = GLES20.glGetAttribLocation(program, "aPosition");
//            glEnable(GL_TEXTURE_2D);
//            {
////                glBindTexture(GL_TEXTURE_2D, bg_tex[0]);
//                glEnable(GL_BLEND);
//                glBindTexture(GL_TEXTURE_2D, tex[0]);
//                int st = i * ref_pts.size();
//                int en = st + 162;
//                List<Float> wrp_pts_i =  wrp_pts.subList(st, en);
////                GLES20.glVertexAttribPointer(positionHandle, 2, GLES20.GL_FLOAT, false, 0, (Buffer) wrp_pts_i);
////                GLES20.glEnableVertexAttribArray(positionHandle);
//            }

/*
            // draw texture
            GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, 0);
            GLES20.glUseProgram(program);
            GLES20.glDisable(GL_BLEND);

            int positionHandle = GLES20.glGetAttribLocation(program, "aPosition");
            int textureHandle = GLES20.glGetUniformLocation(program, "uTexture");
            int texturePositionHandle = GLES20.glGetAttribLocation(program, "aTexPosition");

            GLES20.glVertexAttribPointer(texturePositionHandle, 2, GLES20.GL_FLOAT, false, 0, textureBuffer);
            GLES20.glEnableVertexAttribArray(texturePositionHandle);

            GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, tex[0]);
            GLES20.glUniform1i(textureHandle, 0);

            GLES20.glVertexAttribPointer(positionHandle, 2, GLES20.GL_FLOAT, false, 0, vertexBuffer);
            GLES20.glEnableVertexAttribArray(positionHandle);

            glClear(GL_COLOR_BUFFER_BIT);
            GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4);
*/
            i++;
        }

    return 0;
    }



}
