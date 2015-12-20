package com.example.administrator.intentcount;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {
   private Button button;
    private TextView textView;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        preferences=/*MainActivity.this.*/getSharedPreferences("IntentCount",MODE_PRIVATE);
        editor=preferences.edit();
        button = (Button)findViewById(R.id.button);
        textView = (TextView) findViewById(R.id.textView);
        textView.setText("跳转次数："+preferences.getInt("count",0));

    }
    public void button1(View view){
        editor.putInt("count",preferences.getInt("count",0)+1);
        editor.commit();
        Intent intent = new Intent(MainActivity.this,Activity2.class);
        startActivity(intent);
    }
}
