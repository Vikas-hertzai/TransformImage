package com.example.opengl;

import android.graphics.Bitmap;
import android.opengl.GLES20;
import android.opengl.GLES32;
import android.opengl.GLSurfaceView;
import android.opengl.GLU;
import android.opengl.GLUtils;
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

import static android.opengl.GLES20.GL_LINEAR;
import static android.opengl.GLES20.GL_TEXTURE_2D;
import static android.opengl.GLES20.GL_TEXTURE_MAG_FILTER;
import static android.opengl.GLES20.GL_TEXTURE_MIN_FILTER;
import static android.opengl.GLES20.glBindTexture;
import static android.opengl.GLES20.glGenTextures;
import static android.opengl.GLES20.glTexParameteri;


// reference url for projection:  https://developer.android.com/training/graphics/opengl/projection.html#projection
public class GlRenderer22 implements GLSurfaceView.Renderer{

    private final float[] mMVPMatrix = new float[16];//model view projection matrix
    private final float[] mProjectionMatrix = new float[16];//projection mastrix
    private final float[] mViewMatrix = new float[16];//view matrix
    private final float[] mMVMatrix=new float[16];//model view matrix
    private final float[] mModelMatrix=new float[16];//model  matrix

    private transform Transform;

    private FloatBuffer vertex;
    private FloatBuffer texture;
    private int triangle_size;

    int[] tex = new int[1];
    int im_width;
    int im_height;
    float tex_scale_x;
    float tex_scale_y;
    List<Integer> tri_idx;
    List<Float> ref_pts;
    List<Float> wrp_pts;
    int total_frame;


    public void LoadTexture()throws FileNotFoundException{
        Bitmap ref_img = Transform.loadImageFromStorage("roy.png");
        im_width = ref_img.getWidth();
        im_height = ref_img.getHeight();
        tex_scale_x = 1.0f / (float)(im_width - 1);
        tex_scale_y = 1.0f / (float)(im_height - 1);

        glGenTextures(1, IntBuffer.wrap(tex));  // initialize the array
        glBindTexture(GL_TEXTURE_2D, tex[0]);    //to activate the texture at index 0
        GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, ref_img, 0);  //map Bitmap to the texture
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);   //glTexParameteri to set various properties that decide how the texture is rendered
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
    }

    public void ReadFiles()throws FileNotFoundException{
        tri_idx = Transform.ReadTriangulationFile("triangulation.txt");
        ref_pts = Transform.ReadReferenceFile("reference_points.txt");
        wrp_pts = Transform.ReadWarpedFile("warped_points.txt");
        triangle_size = tri_idx.size();
        total_frame = wrp_pts.size() / ref_pts.size();    // number of frame
    }

    public void TextureBuffer(){
        ByteBuffer BbCoord = ByteBuffer.allocateDirect(tri_idx.size()*2*4);
        BbCoord.order(ByteOrder.nativeOrder());
        texture = BbCoord.asFloatBuffer();
        int indx = 0;
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

            texture.put(indx, ref_pt2);
            texture.put(indx + 1, ref_pt4);

            indx = indx + 2;
        }

        texture.position(0);

    }


    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        // set the background frame colour
        gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);

        Transform = new transform();
        // added
        try {
            LoadTexture();
            ReadFiles();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        TextureBuffer();

    }


    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        gl.glViewport(0,0, width, height);
        gl.glClearColor(0,0,0,1);
        Matrix.orthoM(mProjectionMatrix, 0, 0, im_width, im_height, 0, 0, 1);

    }

    int i = 0;

    @Override
    public void onDrawFrame(GL10 gl) {

//        if (i > total_frame){
//            break;
//        }

        int st = i * ref_pts.size();
        int en = st + 160;
        List<Float> wrp_pts_i =  wrp_pts.subList(st, en);
        ByteBuffer BbVertex = ByteBuffer.allocateDirect(tri_idx.size()*2*4);
        BbVertex.order(ByteOrder.nativeOrder());
        vertex = BbVertex.asFloatBuffer();
        int j = 0;
        for (int idx = 0; idx < tri_idx.size(); idx++) {
            int val = tri_idx.get(idx);
            int pt = val;

            float wrp_pt1 = wrp_pts_i.get(pt);
            float wrp_pt2 = wrp_pts_i.get(pt + 1);
            vertex.put(j, wrp_pt1);
            vertex.put(j + 1, wrp_pt2);
            j = j +2;
        }
        vertex.position(0);

        System.out.println("  i  :  " + i);
        i = i + 1;

        //Redraw background colour
        gl.glClear(gl.GL_COLOR_BUFFER_BIT | gl.GL_DEPTH_BUFFER_BIT);

        Matrix.setIdentityM(mMVPMatrix,0);//set the model view projection matrix to an identity matrix
        Matrix.setIdentityM(mMVMatrix,0);//set the model view  matrix to an identity matrix
        Matrix.setIdentityM(mModelMatrix,0);//set the model matrix to an identity matrix

        // Set the camera position (View matrix)
        Matrix.setLookAtM(mViewMatrix, 0,
                0.0f, 0f, 1.0f,//camera is at (0,0,1)
                0f, 0f, 0f,//looks at the origin
                0f, 1f, 0.0f);//head is down (set to (0,1,0) to look from the top)

        Matrix.multiplyMM(mMVPMatrix, 0, mProjectionMatrix, 0, mViewMatrix, 0);

        Transform.run(tex[0], mMVPMatrix, vertex, texture, triangle_size);

    }
}
