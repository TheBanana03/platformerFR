package com.example.platformerfr;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Instead of using an XML layout, directly use your custom view
        CustomView customView = new CustomView(this);
        setContentView(customView);  // Set the custom view as the content view
    }
}