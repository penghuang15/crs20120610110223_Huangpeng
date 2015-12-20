package com.example.administrator.canvasanimator;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;


public class MainActivity extends AppCompatActivity {
    Button button;
    Animation animationset;
    View coustomview;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        coustomview = findViewById(R.id.canvasview);
        button= (Button) findViewById(R.id.button);
        animationset = AnimationUtils.loadAnimation(MainActivity.this,R.anim.animatorset);

    }
    public void onClick(View view){
        coustomview.startAnimation(animationset);
    }
}
