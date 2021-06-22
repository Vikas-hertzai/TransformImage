package com.example.opengl;

import android.graphics.Bitmap;
import android.opengl.GLES20;
import android.opengl.GLES32;
import android.opengl.GLSurfaceView;
import android.opengl.GLU;
import android.opengl.Matrix;
import android.util.Log;

import java.io.FileNotFoundException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.List;

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

    private FloatBuffer vertex;
    private FloatBuffer texture;
    private IntBuffer index;

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
        Matrix.orthoM(mProjectionMatrix, 0, 0, 512, 683, 0, 0, 1);


        float ratio = (float) width / height;
        float left=-ratio,right=ratio;
        // this projection matrix is applied to object coordinates
        // in the onDrawFrame() method
//        Matrix.frustumM(mProjectionMatrix, 0, left,right, -1.0f, 1.0f, 1.0f, 8.0f);
    }

    int i = 0;

    @Override
    public void onDrawFrame(GL10 gl) {



        try {
            Bitmap ref_img = transform.loadImageFromStorage("roy.png");
            List<Integer> tri_idx = transform.ReadTriangulationFile("triangulation.txt");
            List<Float> ref_pts = transform.ReadReferenceFile("reference_points.txt");
            List wrp_pts = transform.ReadWarpedFile("warped_points.txt");
            float tex_scale_x = 1.0f / (float)(ref_img.getWidth()  - 1);
            float tex_scale_y = 1.0f / (float)(ref_img.getHeight() - 1);


     /*       int st = i * ref_pts.size();
            int en = st + 160;
            List<Float> wrp_pts_i = wrp_pts.subList(st, en);
            ByteBuffer bb = ByteBuffer.allocateDirect(wrp_pts_i.size() * 4);// (# of coordinate values * 4 bytes per float)
            bb.order(ByteOrder.nativeOrder());
            vertex = bb.asFloatBuffer();
            for (int vert = 0; vert < wrp_pts_i.size(); vert= vert+1) {
                vertex.put(vert, wrp_pts_i.get(vert));
            }
            vertex.position(0);

            // creating buffer for reference point
            ByteBuffer tb = ByteBuffer.allocateDirect(ref_pts.size() * 4);// (# of coordinate values * 4 bytes per float)
            tb.order(ByteOrder.nativeOrder());
            texture = tb.asFloatBuffer();
            for (int t = 0; t < ref_pts.size(); t++) {
                float ref_pt1 = ref_pts.get(t);
                float ref_pt2 = ref_pt1 * tex_scale_x;
                texture.put(t, ref_pt2);
                t = t + 1;
                float ref_pt3 = ref_pts.get(t);
                float ref_pt4 = ref_pt3 * tex_scale_y;
                texture.put(t, ref_pt4);
            }
            texture.position(0);
*/


            int st = i * ref_pts.size();
            int en = st + 160;
            List<Float> wrp_pts_i =  wrp_pts.subList(st, en);
            ByteBuffer BbCoord = ByteBuffer.allocateDirect(tri_idx.size()*2*4);
            ByteBuffer BbVertex = ByteBuffer.allocateDirect(tri_idx.size()*2*4);
            BbCoord.order(ByteOrder.nativeOrder());
            BbVertex.order(ByteOrder.nativeOrder());
            texture = BbCoord.asFloatBuffer();
            vertex = BbVertex.asFloatBuffer();
            int j = 0;
            for (int idx = 0; idx < tri_idx.size(); idx++) {
                int val = tri_idx.get(idx);
                int pt = val;
                float ref_pt1 = ref_pts.get(pt);
                float ref_pt2 = ref_pt1 * tex_scale_x;
                float ref_pt3 = ref_pts.get(pt + 1);
                float ref_pt4 = ref_pt3 * tex_scale_y;

                if (ref_pt2 < -20){
                    ref_pt2 = -20.0f;
                }
                else{
                    if (ref_pt2 > 2){
                        ref_pt2 = 2.0f;
                    }
                }

                if (ref_pt4 < -20){
                    ref_pt4 = -20.0f;
                }
                else{
                    if (ref_pt4 > 2){
                        ref_pt4 = 2.0f;
                    }
                }


                texture.put(j, ref_pt2);
                texture.put(j + 1, ref_pt4);

                float wrp_pt1 = wrp_pts_i.get(pt);
                float wrp_pt2 = wrp_pts_i.get(pt + 1);
                vertex.put(j, wrp_pt1);
                vertex.put(j + 1, wrp_pt2);
                j = j +2;
            }
            texture.position(0);
            vertex.position(0);



            // creating buffer for triangle index
            ByteBuffer dlb = ByteBuffer.allocateDirect(tri_idx.size() * 4);
            dlb.order(ByteOrder.nativeOrder());
            index = dlb.asIntBuffer();
            for (int idx = 0; idx < tri_idx.size(); idx++) {
                index.put(idx, tri_idx.get(idx));
            }
            index.position(0);
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }

//        System.out.println("  texture :  " + texture.get(62));
//        System.out.println("  texture :  " + texture.get(63));
//        System.out.println("  texture :  " + texture.get(64));
//        System.out.println("  texture :  " + texture.get(65));
//        System.out.println("  texture :  " + texture.get(66));
//        System.out.println("  texture :  " + texture.get(67));
//        for(int z = 0; z < 290; z++) {
//            System.out.println(texture.get(z));
//        }

        System.out.println("  i  :  " + i);
        i = i + 1;



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
//        Matrix.multiplyMM(mMVMatrix,0,mViewMatrix,0,mModelMatrix,0);
//        Matrix.multiplyMM(mMVPMatrix,0,mProjectionMatrix,0,mMVMatrix,0);

        Matrix.multiplyMM(mMVPMatrix, 0, mProjectionMatrix, 0, mViewMatrix, 0);


        try {
            Transform.run(mMVPMatrix, vertex, texture, index, "roy.png", "triangulation.txt", "reference_points.txt", "warped_points.txt", "roy_bg.jpg");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }


    }
}
