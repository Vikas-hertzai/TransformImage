package com.example.opengl;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.FileNotFoundException;
/*
public class MainActivity extends AppCompatActivity {
    TextView t1;
    Button b1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        t1=  findViewById(R.id.textTV);
        b1 = findViewById(R.id.b1);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OpenGL opengl =new OpenGL();
                try {
                    opengl.run("roy.png", "triangulation.txt", "reference_points.txt", "warped_points.txt", "roy_bg.jpg");
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        });


    }
}
 */

public class MainActivity extends AppCompatActivity {

    OpenGlView openGlView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        openGlView = new OpenGlView(this);
        setContentView(openGlView);
    }
}