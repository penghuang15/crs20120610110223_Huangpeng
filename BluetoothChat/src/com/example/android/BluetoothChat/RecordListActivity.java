package com.example.android.BluetoothChat;

import com.example.android.db.DatabaseHelper;



import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

public class RecordListActivity extends Activity{
	private ArrayAdapter<String> queryArrayAdapter;
	private ListView queryListView;
	private Button button;
	protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 设置窗口界面
      //  requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setContentView(R.layout.record_list);
        DatabaseHelper dbHelper=new DatabaseHelper(RecordListActivity.this,"zhsf_db");
		SQLiteDatabase db=dbHelper.getReadableDatabase();
		//Cursor cursor=db.query("info",new String[]{"name","informations","pdate"},"name=?",new String[]{"zhouzhou"}, null,null,null);
		Cursor cursor=db.query("info",new String[]{"name","informations","pdate"},null,null, null,null,null);
		queryArrayAdapter = new ArrayAdapter<String>(RecordListActivity.this, R.layout.information);
		queryListView = (ListView) findViewById(R.id.query);
		queryListView.setAdapter(queryArrayAdapter);
    while(cursor.moveToNext())
    {
	String name=cursor.getString(cursor.getColumnIndex("name"));
	String informations=cursor.getString(cursor.getColumnIndex("informations"));
	String pdate=cursor.getString(cursor.getColumnIndex("pdate"));
	System.out.print("@name--->"+name);
    System.out.print("@informations--->"+informations);
	System.out.println("@pdate--->"+pdate);
	queryArrayAdapter.add(name+":["+informations+"]  "+pdate);


}
        
        
        

	
	
        Button returnButton=(Button)findViewById(R.id.returnButton);
        returnButton.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
            	Intent recordIntent = new Intent(RecordListActivity.this,BluetoothChat.class);
                startActivity(recordIntent);  
            }
        });
	}
}
