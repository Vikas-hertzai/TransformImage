package com.example.opengl;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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

import static android.opengl.GLES10.GL_MODELVIEW;
import static android.opengl.GLES10.GL_PROJECTION;
import static android.opengl.GLES10.glLoadIdentity;
import static android.opengl.GLES10.glMatrixMode;
import static android.opengl.GLES10.glOrthox;
import static android.opengl.GLES20.GL_BLEND;
import static android.opengl.GLES20.GL_COLOR_BUFFER_BIT;
import static android.opengl.GLES20.GL_DEPTH_BUFFER_BIT;
import static android.opengl.GLES20.GL_DEPTH_TEST;
import static android.opengl.GLES20.GL_INT;
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

public class TransformImage {

    private Square square;
    public TransformImage() {
        initializeBuffers();
        initializeProgram();
    }

    private final String vertexShaderCode =
            "attribute vec4 aPosition;" +
                    "attribute vec2 aTexPosition;" +
                    "varying vec2 vTexPosition;" +
                    "void main() {" +
                    "  gl_Position = aPosition;" +
                    "  vTexPosition = aTexPosition;" +
                    "}";

    private final String fragmentShaderCode =
            "precision mediump float;" +
                    "uniform sampler2D uTexture;" +
                    "varying vec2 vTexPosition;" +
                    "void main() {" +
                    "  gl_FragColor = texture2D(uTexture, vTexPosition);" +
                    "}";

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

    private void initializeProgram(){
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
                list.add(val);
            }
        }
        myReader.close();
        return list;
    }

    public  int run(String image, String triangulation, String reference_points, String warped_points, String background_image) throws FileNotFoundException {

        Bitmap ref_img = TransformImage.loadImageFromStorage(image);
        Bitmap ref_background_img = TransformImage.loadImageFromStorage(background_image);
        List<Integer>  tri_idx = TransformImage.ReadTriangulationFile(triangulation);
        List<Float> ref_pts = TransformImage.ReadReferenceFile(reference_points);
        List  wrp_pts= TransformImage.ReadWarpedFile(warped_points);

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

        int n = 10;
        int  i = 0;

        // Setup OpenGL state.
        glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
        glDisable(GL_DEPTH_TEST);
        // glEnable (GL_TEXTURE_2D);
        // glEnable (GL_BLEND);
        glBlendFunc(GL_ONE, GL_ONE_MINUS_SRC_ALPHA);

        float tex_scale_x = 1.0f / (float)(width  - 1);
        float tex_scale_y = 1.0f / (float)(height - 1);

//        square = new Square();
//        square.draw(tex[0]);

//        System.out.println("******* warp : " + wrp_pts.subList(0, 160));
//        System.out.println("******* ref : " + ref_pts);
        
        while (i < n)
        {
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
//            int en = st + 160;
//            List<Float> wrp_pts_i =  wrp_pts.subList(st, en);
//            FloatBuffer Coord;
//            FloatBuffer vertic;
//            ByteBuffer BbCoord = ByteBuffer.allocateDirect(tri_idx.size()*8);
//            ByteBuffer BbVertex = ByteBuffer.allocateDirect(tri_idx.size()*8);
//            BbCoord.order(ByteOrder.nativeOrder());
//            BbVertex.order(ByteOrder.nativeOrder());
//            Coord = BbCoord.asFloatBuffer();
//            vertic = BbCoord.asFloatBuffer();
//            int j = 0;
//            for (int idx = 0; idx < tri_idx.size(); idx++) {
//                int val = tri_idx.get(idx);
//                int pt = val * 2;
//                float ref_pt1 = ref_pts.get(pt);
//                float ref_pt2 = ref_pt1 * tex_scale_x;
//                float ref_pt3 = ref_pts.get(pt + 1);
//                float ref_pt4 = ref_pt3 * tex_scale_y;
//                Coord.put(j, ref_pt2);
//                Coord.put(j + 1, ref_pt4);
//                float wrp_pt1 = wrp_pts_i.get(pt);
//                float wrp_pt2 = wrp_pts_i.get(pt + 1);
//                vertic.put(j, wrp_pt1);
//                vertic.put(j + 1, wrp_pt2);
//                j = j +2;
//            }
//
//            System.out.println("Coord **** :  " + Coord.position() + "     **  Vertics **** : " + vertic.get(20));
//            Coord.position(0);
//            vertic.position(0);
//            BbCoord.position(0);
//            BbVertex.position(0);

//            int positionHandle = GLES20.glGetAttribLocation(program, "aPosition");
            glEnable(GL_TEXTURE_2D);
            {
//                glBindTexture(GL_TEXTURE_2D, bg_tex[0]);
                glEnable(GL_BLEND);
                glBindTexture(GL_TEXTURE_2D, tex[0]);
//                int st = i * ref_pts.size();
//                int en = st + 162;
//                List<Float> wrp_pts_i =  wrp_pts.subList(st, en);
////                GLES20.glVertexAttribPointer(positionHandle, 2, GLES20.GL_FLOAT, false, 0, (Buffer) wrp_pts_i);
////                GLES20.glEnableVertexAttribArray(positionHandle);
            }


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

            GLES20.glVertexAttribPointer(positionHandle, 2, GLES20.GL_FLOAT, false, 0, verticesBuffer);
            GLES20.glEnableVertexAttribArray(positionHandle);

            glClear(GL_COLOR_BUFFER_BIT);
            GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4);

            i++;
//            Coord.clear();
//            vertic.clear();

        }


        return 0;
    }


}


