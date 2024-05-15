package com.appdev.gadgetsgalaxy;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.splashscreen.SplashScreen;

import android.os.Bundle;
import android.os.Handler;

public class MainActivity extends AppCompatActivity {
    boolean keepIt = true;
    final int keepFor = 1000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SplashScreen splashScreen = SplashScreen.installSplashScreen(this);
        super.onCreate(savedInstanceState);

        splashScreen.setKeepOnScreenCondition(() -> keepIt);
        Handler handler = new Handler();
        handler.postDelayed(() -> keepIt = false, keepFor);
        setContentView(R.layout.activity_main);
    }
}