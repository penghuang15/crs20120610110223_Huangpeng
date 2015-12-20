package com.example.administrator.intentcount;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by Administrator on 2015/12/16.
 */
public class Activity2 extends AppCompatActivity{
    private Button button2;
    public TextView textView2;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity2);
        //获取MainActivity中的preference
        try {
            Context mainPreference = createPackageContext("com.example.administrator.intentcount",Context.CONTEXT_IGNORE_SECURITY);
            preferences = mainPreference.getSharedPreferences("IntentCount",Context.MODE_WORLD_READABLE);
            editor=preferences.edit();
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
//        preferences=getSharedPreferences("IntentCount",MODE_PRIVATE);
//        editor=preferences.edit();
        textView2= (TextView) findViewById(R.id.textView2);
        button2 = (Button) findViewById(R.id.button2);
        textView2.setText("跳转次数：" + preferences.getInt("count", 0));
    }
    public void button2(View view){
        editor.putInt("count",preferences.getInt("count",0)+1);
        editor.commit();
        Intent intent = new Intent(Activity2.this,MainActivity.class);
        startActivity(intent);
    }
}
