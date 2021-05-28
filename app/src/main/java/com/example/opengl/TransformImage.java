package com.example.opengl;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.GLUtils;
import android.os.Environment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static android.opengl.GLES20.GL_DEPTH_TEST;
import static android.opengl.GLES20.GL_LINEAR;
import static android.opengl.GLES20.GL_ONE;
import static android.opengl.GLES20.GL_ONE_MINUS_SRC_ALPHA;
import static android.opengl.GLES20.GL_TEXTURE_2D;
import static android.opengl.GLES20.GL_TEXTURE_MAG_FILTER;
import static android.opengl.GLES20.GL_TEXTURE_MIN_FILTER;
import static android.opengl.GLES20.glBindTexture;
import static android.opengl.GLES20.glBlendFunc;
import static android.opengl.GLES20.glClearColor;
import static android.opengl.GLES20.glDisable;
import static android.opengl.GLES20.glGenTextures;
import static android.opengl.GLES20.glTexParameteri;

public class TransformImage {

    private Square square;
    public TransformImage() {

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
        List<Integer>  tri_idx = OpenGL.ReadTriangulationFile(triangulation);
        List<Float> ref_pts = OpenGL.ReadReferenceFile(reference_points);
        List  wrp_pts= OpenGL.ReadWarpedFile(warped_points);

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

        square = new Square();
        square.draw(tex[0]);

        int n = 2;
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

        }


        return 0;
    }


}


