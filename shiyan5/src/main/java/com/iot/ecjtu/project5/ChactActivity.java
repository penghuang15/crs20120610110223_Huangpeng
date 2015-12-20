package com.iot.ecjtu.project5;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class ChactActivity extends AppCompatActivity {
    private ImageView mImageView;
    private ImageView mImageView2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chact);
        mImageView = (ImageView)findViewById(R.id.imageView);
        mImageView2 = (ImageView)findViewById(R.id.imageView2);

        mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ChactActivity.this,FriendProfileActivity.class);
                startActivity(intent);
            }
        });
        mImageView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ChactActivity.this,FriendProfileActivity.class);
                startActivity(intent);
            }
        });
    }
}
