
package com.example.opengl;
import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.opengl.GLES10;
import android.opengl.GLES10Ext;
import android.opengl.GLES11Ext;
import android.opengl.GLES30;
import android.opengl.GLES31;
import android.opengl.GLES31Ext;
import android.opengl.GLES32;
import android.opengl.GLUtils;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
//import javax.imageio.ImageIO;

import java.io.FileInputStream;
import java.io.FileNotFoundException;  // Import this class to handle errors
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.charset.Charset;
import java.util.Scanner; // Import the Scanner class to read text files
import java.util.ArrayList;
import java.util.List;


//import static android.opengl.GLES10.GL_MODELVIEW;
//import static android.opengl.GLES10.GL_PROJECTION;
//import static android.opengl.GLES10.glColor4f;
//import static android.opengl.GLES10.glLoadIdentity;
//import static android.opengl.GLES10.glMatrixMode;
import static android.opengl.GLES10.GL_MODELVIEW;
import static android.opengl.GLES10.GL_PROJECTION;
import static android.opengl.GLES10.glColor4f;
import static android.opengl.GLES10.glLoadIdentity;
import static android.opengl.GLES10.glMatrixMode;
import static android.opengl.GLES10.glOrthox;
import static android.opengl.GLES20.GL_BLEND;
import static android.opengl.GLES20.GL_COLOR_BUFFER_BIT;
import static android.opengl.GLES20.GL_DEPTH_BUFFER_BIT;
import static android.opengl.GLES20.GL_DEPTH_TEST;
import static android.opengl.GLES20.GL_LINEAR;
import static android.opengl.GLES20.GL_LINE_LOOP;
import static android.opengl.GLES20.GL_ONE;
import static android.opengl.GLES20.GL_ONE_MINUS_SRC_ALPHA;
import static android.opengl.GLES20.GL_RGBA;
import static android.opengl.GLES20.GL_TEXTURE_2D;
import static android.opengl.GLES20.GL_TEXTURE_MAG_FILTER;
import static android.opengl.GLES20.GL_TEXTURE_MIN_FILTER;
import static android.opengl.GLES20.GL_TRIANGLES;
import static android.opengl.GLES20.GL_UNSIGNED_BYTE;
import static android.opengl.GLES20.GL_VERSION;
import static android.opengl.GLES20.glBindTexture;
import static android.opengl.GLES20.glBlendFunc;
import static android.opengl.GLES20.glClear;
import static android.opengl.GLES20.glClearColor;
import static android.opengl.GLES20.glDeleteTextures;
import static android.opengl.GLES20.glDisable;
import static android.opengl.GLES20.glEnable;
import static android.opengl.GLES20.glGenTextures;
import static android.opengl.GLES20.glGetIntegerv;
import static android.opengl.GLES20.glGetString;
import static android.opengl.GLES20.glReadPixels;
import static android.opengl.GLES20.glTexImage2D;
import static android.opengl.GLES20.glTexParameteri;
import static android.opengl.GLES20.glViewport;
//import static android.opengl.GLES32.GL_QUADS;
import static android.opengl.GLES32.GL_QUADS;
import static java.security.AccessController.getContext;

import android.content.Context;
import android.opengl.GLSurfaceView;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL;
import javax.microedition.khronos.opengles.GL10;
import javax.microedition.khronos.opengles.GL11;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import java.util.Arrays;
import java.util.Vector;
import android.opengl.GLES20.*;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

//import javax.media.opengl.*;
//import javax.media.opengl.awt.GLCanvas;
//import com.jogamp.opengl.util.*;
//import com.jogamp.opengl.util.gl2.GLUT;
//
//import java.awt.Dimension;
//
//import javax.media.opengl.glu.GLU;
//import javax.media.opengl.fixedfunc.GLMatrixFunc;
//import org.lwjgl.opengl.GL11;


public class OpenGL {
    private Context Context;
    private Square square;

    int[] tex = new int[1];

    public OpenGL() {

    }

/*
    public static byte[] getBytesFromBitmap(String image) {
//        File dir<new File(Environment.getExternalStorageDirectory().getAbsolutePath() + image);
//        BitmapFactory.Options options = new BitmapFactory.Options();
//
//        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
//
//        Bitmap bitmap = BitmapFactory.decodeFile(dir, options);
//        selected_photo.setImageBitmap(bitmap);

//        ContextWrapper cw = new ContextWrapper(getApplicationContext());
//        File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
//        File file = new File(directory, "mario" + ".png");
//        imageView.setImageDrawable(Drawable.createFromPath(file.toString()));

        String IMGpath = "/storage/emulated/0/DCIM/Camera/IMG_20151102_193132.jpg"
        File imgFile = new File(IMGpath);
        if (imgFile.Exists())
        {
            BitmapFactory.Options bmOptions = new BitmapFactory.Options();
            Bitmap bitmap = BitmapFactory.DecodeFile(imgFile.AbsolutePath, bmOptions);
            bitmap = Bitmap.CreateScaledBitmap(bitmap, 200,200, true);
            _imageview.SetImageBitmap(bitmap);
        }
        else
        {
            Log.Info("AAABL", "No file");
        }

        if (dir.exists()) {
            Log.d("path", dir.toString());
            BitmapFactory.Options options<new BitmapFactory.Options();
            options.inPreferredConfig<Bitmap.Config.ARGB_8888 ;
            Bitmap bitmap<BitmapFactory.decodeFile(String.valueOf(dir), options);

        }

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 70, stream);
        return stream.toByteArray();
    }
*/
/*  commented
    private static ByteBuffer loadImageFromStorage(String image_name) throws FileNotFoundException {

//        try {
            File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
//            File f=new File(String.valueOf(R.drawable.roy), image_name);
            File f=new File(path, image_name);
            Bitmap b = BitmapFactory.decodeStream(new FileInputStream(f));
            System.out.println("Bitmap data **** : " + b);
            int bytes = b.getByteCount();


            Bitmap bmp = b; // your bitmap
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
            byte[] byteArray = stream.toByteArray();
            try {
                stream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
//        System.out.println(Arrays.toString(byteArray));
            ByteBuffer buffer = ByteBuffer.allocate(bytes); // Create a new buffer

//            buffer.position(0);
//            b.copyPixelsToBuffer(buffer); // Move the byte data to the buffer
            buffer.put(byteArray);
            buffer.position(0);

//            ByteBuffer buffer = ByteBuffer.wrap(byteArray);
            return buffer;

//        }
//        catch (FileNotFoundException e)
//        {
//            e.printStackTrace();
//
//        }

    }
    till here
 */


    private static Bitmap loadImageFromStorage(String image_name) throws FileNotFoundException {


        File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        File f=new File(path, image_name);
        Bitmap b = BitmapFactory.decodeStream(new FileInputStream(f));

        return b;


    }


    static List ReadReferenceFile(String file_name) throws FileNotFoundException {
//        try {
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
//        } catch (FileNotFoundException e) {
//            System.out.println("An error occurred.");
//            e.printStackTrace();
//        }
    }


    static List ReadWarpedFile(String file_name) throws FileNotFoundException {
//        try {
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
//        } catch (FileNotFoundException e) {
//            System.out.println("An error occurred.");
//            e.printStackTrace();
//        }
    }


    static List ReadTriangulationFile(String file_name) throws FileNotFoundException {
//        try {
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
                list.add(val);
            }
        }
        myReader.close();
//        System.out.println("****** ReadReferenceFile :  " + list.size());
        return list;
//        } catch (FileNotFoundException e) {
//            System.out.println("An error occurred.");
//            e.printStackTrace();
//        }
    }




   public  int run(String image, String triangulation, String reference_points, String warped_points, String background_image) throws FileNotFoundException {
//        String currentPath = System.getProperty("user.dir");
//        System.out.println("user dir :  "+ Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS));
//       System.out.println("user dir ::  "+ Environment.getExternalStorageDirectory()+"/Download");
//        ByteBuffer ref_img = OpenGL.loadImageFromStorage(image);
//        ByteBuffer ref_background_img = OpenGL.loadImageFromStorage(background_image);
           Bitmap ref_img = OpenGL.loadImageFromStorage(image);
           Bitmap ref_background_img = OpenGL.loadImageFromStorage(background_image);
        System.out.println("Image part working :: ");
//        for (int i = 1; i < 500; i++) {
//           System.out.print(ref_img.get(i));
//        }
//        System.out.println(ref_img.get(22));
        List<Integer>  tri_idx = OpenGL.ReadTriangulationFile(triangulation);
        List<Float> ref_pts = OpenGL.ReadReferenceFile(reference_points);
        List  wrp_pts= OpenGL.ReadWarpedFile(warped_points);
        System.out.println("file part working :: ");
//        System.out.println(wrp_pts.get(1));
//        Toast.makeText(Context, "file working", Toast.LENGTH_SHORT ).show();
       System.out.println("*************   " + wrp_pts.size() + "   " + ref_pts.size() + "   " + tri_idx.size());
//       System.out.println("version : " + glGetString(GL_VERSION));
//       System.out.println("context : " + getContext());
//       System.out.println(glGetIntegerv());
        int width = 512;
        int height = 683;


       // Convert the image to premultiplied alpha.
//       for (int i = 0, n = width * height * 4; i < n; i += 4)
//       {
//           uint8_t * pix = ref_img + i;
//           pix[0] = pix[0] * pix[3] / 255;
//           pix[1] = pix[1] * pix[3] / 255;
//           pix[2] = pix[2] * pix[3] / 255;
//       }

//        final MyGLSurfaceView view;
//        GLuint tex = 0;
//        IntBuffer tex = IntBuffer.wrap(tex);
//        int[] tex = new int[1];
        glGenTextures(1, IntBuffer.wrap(tex));
        glBindTexture(GL_TEXTURE_2D, tex[0]);
//        System.out.println(" working till 300 :: ");
//        glTexImage2D   (GL_TEXTURE_2D, 0, GL_RGBA, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, ref_img);
        GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, ref_img, 0);
//        System.out.println(" working till 302 :: ");
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);

//        GLuint bg_tex = 0;
        int[] bg_tex = new int[1];
        glGenTextures(1, IntBuffer.wrap(bg_tex));
        glBindTexture  (GL_TEXTURE_2D, bg_tex[0]);
//        glTexImage2D   (GL_TEXTURE_2D, 0, GL_RGBA, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, ref_background_img);
        GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, ref_background_img, 0);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);

        square = new Square();
//        square.draw(tex[0]);

        // Process all the warped images.
//        long  n = wrp_pts.size() / ref_pts.size();
        int n = 2;
        int  i = 0;

        // Setup OpenGL state.
        glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
        glDisable(GL_DEPTH_TEST);
        // glEnable (GL_TEXTURE_2D);
        // glEnable (GL_BLEND);
        glBlendFunc(GL_ONE, GL_ONE_MINUS_SRC_ALPHA);
//        System.out.println("working till  glblend ");

        float tex_scale_x = 1.0f / (float)(width  - 1);
        float tex_scale_y = 1.0f / (float)(height - 1);
        System.out.println("working till 329 *************************** " +  n + " :: " + wrp_pts.size() + " :: " + ref_pts.size());
//       System.out.println("working till 329 *************************** " + tex_scale_x + "     " + tex_scale_y);

        // Start processing.

        while (i < n)
        {
            System.out.println("*************************************************************     emtering in while  ********************");
//            int width, height;
//            glfwGetFramebufferSize(window.get(), &width, &height);

//            glViewport(0, 0, width, height);
//            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
//            System.out.println("working while 329 *************************** 333 ---- " + i);

//            glMatrixMode(GL_PROJECTION);
//            System.out.println("working while 329 *************************** 333 ---- " + glGetString());
//            glLoadIdentity();
//            glOrthox(0, width, height, 0, 0, 1);

//            glMatrixMode(GL_MODELVIEW);
//            glLoadIdentity();



            // Render current mesh.

            glEnable(GL_TEXTURE_2D);
            {
//                glBindTexture(GL_TEXTURE_2D, bg_tex[0]);
////                glBegin(GL_QUADS);
//                GLES31.glBeginTransformFeedback(GL_QUADS);
//                {
//                    glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
//                    GLES20.gl
//                    glTexCoord2f(0, 0); glVertex2f(0, 0);
//                    glTexCoord2f(1, 0); glVertex2f(width, 0);
//                    glTexCoord2f(1, 1); glVertex2f(width, height);
//                    glTexCoord2f(0, 1); glVertex2f(0, height);
//                }
//                glEnd();

                glEnable (GL_BLEND);
                glBindTexture(GL_TEXTURE_2D, tex[0]);
//                glBegin(GL_TRIANGLES);
                GLES32.glBeginTransformFeedback(GL_QUADS);
                {
                    glColor4f(1.0f, 1.0f, 1.0f, 1.0f);

                    // Points for this iteration.

                    int st = i * ref_pts.size();
                    int en = st + 162;
                    List<Float> wrp_pts_i =  wrp_pts.subList(st, en);

                    // Render each
//                    for (auto idx : tri_idx)
                    for (int idx = 0; idx < tri_idx.size(); idx++)
                    {
                        int ref_pt = idx * 2;
                        int wrp_pt = idx * 2;

//                        glTexCoord2f(ref_pt[0] * tex_scale_x, ref_pt[1] * tex_scale_y);
//                        GLES10.glT
//                        glVertex2f  (wrp_pt[0]              , wrp_pt[1]              );
//                        GLES20.glVertexAttrib2f(3, wrp_pts_i.get(wrp_pt), wrp_pts_i.get(wrp_pt + 1));
//                        GL10.glTranslatef()
                    }
                }
//                glEnd();
                GLES32.glEndTransformFeedback();
                glDisable (GL_BLEND);

            }

            glDisable(GL_TEXTURE_2D);


            // Render the mesh itself.
           // show wireframe
            boolean show_wireframe = true;
            if (show_wireframe)
            {
                int st = i * ref_pts.size();
                int en = st + 160;
                List<Float> wrp_pts_i =  wrp_pts.subList(st, en);
//                System.out.println("wrp_pts_i *******    " + wrp_pts_i.get(15) + "    " + st + "    " + en);

                for (int j = 0; j < tri_idx.size(); j += 3)
                {
//                    glBegin(GL_LINE_LOOP);
                    GLES32.glBeginTransformFeedback(GL_LINE_LOOP);
                    glColor4f(1.0f, 0.0f, 0.0f, 1.0f);

                    for (int k = 0; k < 3; k++)
                    {
                       int num = j + k;
                    int  idx = tri_idx.get(num);
                    int pt  = idx * 2;
//                   wrp_pts.get(i).get(12);
//                        glVertex2f(pt[0], pt[1]);
//                        GLES20.glVertexAttrib2f(pt, wrp_pts_i.get(pt), wrp_pts_i.get(pt + 1));

                    }

//                    glEnd();
                    GLES32.glEndTransformFeedback();
                }
            }
//            Show wireframe

            // Dump the image.
            /*   Image dump
            if (dump_images)
            {
                if (output_buffer.empty())
                {
                    // Note that this is of the size of the default framebuffer size. For high-DPI devices,
                    // the resolution will be larger than the size of the image, and would have to be scaled down.
                    output_buffer.resize((size_t)width * (size_t)height * 4);
                }

                glReadPixels(0, 0, width, height, GL_RGBA, GL_UNSIGNED_BYTE, output_buffer.data());

                // Convert the image to premultiplied alpha.
                // for (int i = 0, n = ref_background_img.width * ref_background_img.height * 4; i < n; i += 4)
                // {
                //     uint8_t * pix = output_buffer.data() + i;
                //     uint8_t * background_pix = ref_background_img.data.get() + i;
                //     pix[0] += background_pix[0];
                //     pix[1] += background_pix[1];
                //     pix[2] += background_pix[2];
                // }

                char filename[64];
                snprintf(filename, sizeof(filename), "%06zu.tga", i);

//                if (stbi_write_tga(filename, width, height, 4, output_buffer.data()) == 0)
//                {
//                    throw std::runtime_error("Saving of frame #" + std::to_string(i + 1) + " failed.");
//                }
            }
 Image dump    */

            square.draw(tex[0]);
//            System.out.println("****** calling draw func : ");
//            // Advance to the next frame.
            i++;

//            glfwSwapBuffers(window.get());
//            glfwPollEvents();
        }

        // Cleanup (all the rest is done via the unique_pointers).
        glDeleteTextures(1, IntBuffer.wrap(tex));

        return 0;

//        return 0;
    }

}
