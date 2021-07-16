package com.example.opengl;

import android.graphics.Bitmap;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.GLUtils;
import android.opengl.Matrix;

import java.io.FileNotFoundException;
import java.io.IOException;
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

    private com.example.opengl.Transform Transform;

    private FloatBuffer vertex;
    private FloatBuffer texture;
    private int triangle_size;

    private boolean flag=false;

    int[] tex = new int[1];
    int im_width;
    int im_height;
    float tex_scale_x;
    float tex_scale_y;
    List<Integer> tri_idx;
    List<Float> ref_pts;
    List<Float> wrp_pts;
    int total_frame;
    long start_time = 0;
    long curr_time=0;
    long latency=0;
    long audioStarted =0;
    long start=System.nanoTime();
    long end=System.nanoTime();
    MediaPlayer mplayer;
    int audioDuration=0;
    float audioLength=0;
    float audioLatency=0;

    public long AudioPlayer(String url) throws IOException {
        mplayer = new MediaPlayer();
        long audioStarted=0;

        mplayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        try {
//            File audio_path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
//            File myObj = new File(audio_path, "M6_0.wav");
//            mplayer.setDataSource(String.valueOf(myObj));
            mplayer.setDataSource(url);
            mplayer.prepare();
            audioStarted=System.nanoTime();
            mplayer.start();


        }catch (IOException e) {
            e.printStackTrace();
        }
        return audioStarted;
    }


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
        wrp_pts = Transform.ReadWarpedFile("warped_points.txt",ref_pts.size());
        audioLength=(wrp_pts.size()/ref_pts.size())/62.5f;
        System.out.println("wrp pts size"+wrp_pts.size()/ref_pts.size()/62.5);
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

        Transform = new Transform();
        // added
        try {
            LoadTexture();
            ReadFiles();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        TextureBuffer();
        /*try {
            AudioPlayer("http://stream.mcgroce.com/examples/1626242467.228868.wav");
            start_time=System.nanoTime();
        } catch (IOException e) {
            e.printStackTrace();
        }*/
        flag=true;
        //start_time = System.nanoTime()/1000000;

    }


    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        gl.glViewport(0,0, width, height);
        gl.glClearColor(0,0,0,1);
        Matrix.orthoM(mProjectionMatrix, 0, 0, im_width, im_height, 0, 0, 1);

    }

    int i = 0;

    //float frame_rate = 62.5f;
    //float frameInterval = 1000/frame_rate;
    //float totalTime=total_frame*frameInterval;


    @Override
    public void onDrawFrame(GL10 gl) {

        if(flag)
        {
            try {
                AudioPlayer("http://stream.mcgroce.com/examples/1626170045.121975.wav");
                start_time=System.currentTimeMillis();
                audioDuration=mplayer.getDuration();
                System.out.println("length from frames : "+audioLength);
                System.out.println("legth of audio from player "+ audioDuration/1e3);
                System.out.println("freq="+(double)(wrp_pts.size()/((double)(audioDuration/1e3*ref_pts.size()))));
                audioLatency=(float)(audioDuration/1e3)-audioLength;
                flag=false;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        latency=System.nanoTime()-start;
        start=System.nanoTime();


        //System.out.println("FPS : "+1e9/latency);
        //System.out.println("count : "+counter++);
        //curr_time=System.currentTimeMillis();
        long audioTime=mplayer.getCurrentPosition();
        double timeSec=(double)(audioTime)/1e3-audioLatency;

        if(timeSec<0)
        {
            timeSec=0;
        }

        i=(int)(timeSec*((double)(wrp_pts.size()/((double)audioLength*ref_pts.size()))));
        //System.out.println("time : "+audioDuration/1e3);

        //i=(int)(timeSec*62.5);

        if(i<0)
        {
            i=0;
        }

        if(!mplayer.isPlaying())
        {
            System.out.println("Flag set true");
            flag=true;
        }

        long frameStart=System.nanoTime();
        start=System.nanoTime();
        int st = i * ref_pts.size();
        int en = st + ref_pts.size();
        if(en> wrp_pts.size())
        {
            i=0;
            st = 0;
            en = st + ref_pts.size();
            flag=true;
        }

        List<Float> wrp_pts_i = wrp_pts.subList(st, en);
        ByteBuffer BbVertex = ByteBuffer.allocateDirect(tri_idx.size() * 2 * 4);
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
            j = j + 2;
        }
        vertex.position(0);
        //i = i + 1;

        //Redraw background colour
        gl.glClear(gl.GL_COLOR_BUFFER_BIT | gl.GL_DEPTH_BUFFER_BIT);

        Matrix.setIdentityM(mMVPMatrix, 0);//set the model view projection matrix to an identity matrix
        Matrix.setIdentityM(mMVMatrix, 0);//set the model view  matrix to an identity matrix
        Matrix.setIdentityM(mModelMatrix, 0);//set the model matrix to an identity matrix

            // Set the camera position (View matrix)
        Matrix.setLookAtM(mViewMatrix, 0,
                0.0f, 0f, 1.0f,//camera is at (0,0,1)
                0f, 0f, 0f,//looks at the origin
                0f, 1f, 0.0f);//head is down (set to (0,1,0) to look from the top)

        Matrix.multiplyMM(mMVPMatrix, 0, mProjectionMatrix, 0, mViewMatrix, 0);


        end=Transform.run(tex[0], mMVPMatrix, vertex, texture, triangle_size);

        //latency=end-start;
        /*if(flag)
        {

            try {
                start_time=System.nanoTime();
                audioStarted=AudioPlayer("http://stream.mcgroce.com/examples/M6_0.wav");
                latency=audioStarted-renderedAt;
                latency=0;
            } catch (IOException e) {
                e.printStackTrace();
            }
            flag=false;
        }*/

    }
}