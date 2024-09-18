package com.example.platformerfr;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Find the custom view and buttons
        CustomView customView = findViewById(R.id.myCanvasView);
        Button buttonJump = findViewById(R.id.button_jump);

        customView.setupButton(buttonJump);
    }
}
