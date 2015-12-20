package com.iot.ecjtu.project7;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.iot.ecjtu.project7.model.Adapter;
import com.iot.ecjtu.project7.model.Friend;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private ListView mListView;
    private Button mDeleteButton;
    private Button mPlusButton;
    private Toast mToast;

    List<Friend> list = new ArrayList<Friend>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDeleteButton=(Button)findViewById(R.id.delete_button);
        mPlusButton = (Button)findViewById(R.id.plus_button);
        mListView = (ListView)findViewById(R.id.list);

        mPlusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               list = plusListItem();
                display(list);
            }
        });

        mDeleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteListItem();
                display(list);
            }
        });
    }

    private List<Friend> plusListItem(){
        Friend friend =new Friend();
        friend.setHeadImageId(R.drawable.pic);
        friend.setName("小明");
        friend.setRate(3);
        list.add(friend);
        return list;
    }

    private List<Friend> deleteListItem(){
        if(list.size()<1){
            if(mToast == null){
            Toast.makeText(this,"没有数据",Toast.LENGTH_SHORT).show();
            }
        }
        else{
            list.remove(list.size()-1);
        }
        return list;
    }
    private void display(List<Friend> list){
        Adapter adapter = new Adapter(this,list);
        mListView.setAdapter(adapter);

    }
}
