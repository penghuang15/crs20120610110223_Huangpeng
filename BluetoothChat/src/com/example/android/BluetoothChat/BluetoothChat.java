
package com.example.android.BluetoothChat;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;




import com.example.android.db.DatabaseHelper;

import android.app.Activity;
import android.app.ActivityManager;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * This is the main Activity that displays the current chat session.
 */
public class BluetoothChat extends Activity {
    // ������Ϣ
    private static final String TAG = "BluetoothChat";
    private static final boolean D = true;

    // �� BluetoothChatService Handler��ȡ����Ϣ����
    public static final int MESSAGE_STATE_CHANGE = 1;
    public static final int MESSAGE_READ = 2;
    public static final int MESSAGE_WRITE = 3;
    public static final int MESSAGE_DEVICE_NAME = 4;
    public static final int MESSAGE_TOAST = 5;

 
    public static final String DEVICE_NAME = "device_name";
    public static final String TOAST = "toast";

  
    private static final int REQUEST_CONNECT_DEVICE = 1;
    private static final int REQUEST_ENABLE_BT = 2;

   
    private TextView mTitle;
    private ListView mConversationView;
    private EditText mOutEditText;
    private Button mSendButton;

    // �������豸������
    private String mConnectedDeviceName = null;
    // �Ự�̵߳�Array adapter
    //private ArrayAdapter<String> mConversationArrayAdapter;
    private ChatMsgViewAdapter mConversationArrayAdapter;
	private List<ChatMsgEntity> mDataArrays = new ArrayList<ChatMsgEntity>();
    // ������Ϣ��Buffer
    private StringBuffer mOutStringBuffer;
    // ��������������
    private BluetoothAdapter mBluetoothAdapter = null;
   
    private BluetoothChatService mChatService = null;

    /*onCreate()---start*/
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(D) Log.e(TAG, "+++ ON CREATE +++");

        // ���ô��ڽ���
        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.main);
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.custom_title);

        // ���ô��ڵı���
        mTitle = (TextView) findViewById(R.id.title_left_text);
        mTitle.setText(R.string.app_name);
        mTitle = (TextView) findViewById(R.id.title_right_text);

        // �õ���������������
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
     
    }
    /*onCreate()---end*/
    
    
    
    /*onStart()--start*/
    @Override
    public void onStart() {
        super.onStart();
        if(D) Log.e(TAG, "++ ON START ++");
        
        // �������û�д򿪣���ʾ��
        // setupChat() will then be called during onActivityResult
      if (!mBluetoothAdapter.isEnabled()) {
          Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
        	
        // ���� chat session
        } else {
        	if (mBluetoothAdapter.isEnabled()) {
            	  Toast.makeText(this, "�����Ѵ򿪣��밴Menu������������", Toast.LENGTH_LONG).show();                   
                }
            if (mChatService == null) setupChat();
        }
    }
    /*onStart()--end*/
    

    @Override
    public synchronized void onResume() {
        super.onResume();
        if(D) Log.e(TAG, "+ ON RESUME +");

        // Performing this check in onResume() covers the case in which BT was
        // not enabled during onStart(), so we were paused to enable it...
        // onResume() will be called when ACTION_REQUEST_ENABLE activity returns.
        if (mChatService != null) {
            if (mChatService.getState() == BluetoothChatService.STATE_NONE) {
              // �����������
              mChatService.start();
            }
        }
    }
    
    
    
    /*setupChat()--start*/
    private void setupChat() {
        Log.d(TAG, "setupChat()");

        // ��ʼ��array adapter
        mConversationArrayAdapter = new ChatMsgViewAdapter(BluetoothChat.this,mDataArrays);
        mConversationView = (ListView) findViewById(R.id.in);
        mConversationView.setAdapter(mConversationArrayAdapter);
        //��ʼ���༭��
        mOutEditText = (EditText) findViewById(R.id.edit_text_out);
        //�༭�� ������
        mOutEditText.setOnEditorActionListener(mWriteListener);       
        //mOutEditText.setText("����������");
        mTitle.setText(R.string.app_name);
        mSendButton = (Button) findViewById(R.id.button_send);
        mSendButton.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                TextView view = (TextView) findViewById(R.id.edit_text_out);
                String message = view.getText().toString();
                sendMessage(message);
            }
        });

        // ��ʼ��BluetoothChatService to perform bluetooth connections
        mChatService = new BluetoothChatService(this, mHandler);
        // ��ʼ��������Ϣ������
        mOutStringBuffer = new StringBuffer("");
    }
    /*setupChat()--end*/


    @Override
    public synchronized void onPause() {
        super.onPause();
        if(D) Log.e(TAG, "- ON PAUSE -");
    }

    @Override
    public void onStop() {
        super.onStop();
        if(D) Log.e(TAG, "-- ON STOP --");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mChatService != null) mChatService.stop();
        if(D) Log.e(TAG, "--- ON DESTROY ---");
    }
// ���ÿɼ��Է���
    private void ensureDiscoverable() {
        if(D) Log.d(TAG, "ensure discoverable");
        if (mBluetoothAdapter.getScanMode() !=
            BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE) {
            Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
            discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
            startActivity(discoverableIntent);
        }
    }
    // ����������
    private void ensureOpenbluetooth() {
        if(D) Log.d(TAG, "ensure openbluetooth");
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if(!mBluetoothAdapter.isEnabled()){
            //����һ���������������豸��intent����  				
            Intent TurnOnBtIntent=new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(TurnOnBtIntent, REQUEST_ENABLE_BT);}
    }
    //�˳�ϵͳ����
    private void ensureExit() {
        if(D) Log.d(TAG, "ensure exit");
        System.exit(0);
       /* ActivityManager activityMgr=(ActivityManager)this.getSystemService(ACTIVITY_SERVICE);
        activityMgr.restartPackage(getPackageName());*/
       
        
       
    }
    //�ر���������
    private void ensureClosebluetooth() {
        if(D) Log.d(TAG, "ensure closebluetooth");
        mBluetoothAdapter.disable();
    }
    private void sendMessage(String message) {
        // ������Ϣǰ����Ƿ�������
        if (mChatService.getState() != BluetoothChatService.STATE_CONNECTED) {
            Toast.makeText(this, R.string.not_connected, Toast.LENGTH_SHORT).show();
            return;
        }

        // �����Ϣ�Ƿ�Ϊ��
        if (message.length() > 0) {
            // ���� BluetoothChatService ������Ϣ
            byte[] send = message.getBytes();
            mChatService.write(send);

            // ����༭������
            mOutStringBuffer.setLength(0);
            mOutEditText.setText(mOutStringBuffer);
          //  mOutEditText.setTextColor(Color.BLACK);
        }
    }

    // ������Ϣ�༭�������
    private TextView.OnEditorActionListener mWriteListener =
        new TextView.OnEditorActionListener() {
        public boolean onEditorAction(TextView view, int actionId, KeyEvent event) {
            // ��key-up�¼�����ʱ��������Ϣ 
            if (actionId == EditorInfo.IME_NULL && event.getAction() == KeyEvent.ACTION_UP) {
                String message = view.getText().toString();
                sendMessage(message);
            }
            if(D) Log.i(TAG, "END onEditorAction");
            return true;
        }
    };

    // ��BluetoothChatService��÷�����Ϣ�Ӷ�����״̬
    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
//        	 long time=System.currentTimeMillis();
//             final Calendar mCalendar=Calendar.getInstance();
//             mCalendar.setTimeInMillis(time);
//             //����������Ϣ��ʱ��
//            int mHour=mCalendar.get(Calendar.HOUR);
//            int mMinuts=mCalendar.get(Calendar.MINUTE);
//            int mSecond=mCalendar.get(Calendar.SECOND);
            String pdate=null;
            switch (msg.what) {
            case MESSAGE_STATE_CHANGE:
                if(D) Log.i(TAG, "MESSAGE_STATE_CHANGE: " + msg.arg1);
                switch (msg.arg1) {
                case BluetoothChatService.STATE_CONNECTED:
                    mTitle.setText(R.string.title_connected_to);
                    mTitle.append(mConnectedDeviceName);
                    //mConversationArrayAdapter.clear();
                    break;
                case BluetoothChatService.STATE_CONNECTING:
                    mTitle.setText(R.string.title_connecting);
                    break;
                case BluetoothChatService.STATE_LISTEN:
                case BluetoothChatService.STATE_NONE:
                    mTitle.setText(R.string.title_not_connected);
                    break;
                }
                break;
            case MESSAGE_WRITE:
                byte[] writeBuf = (byte[]) msg.obj;
                String writeMessage = new String(writeBuf);
                if(writeMessage!=null)
                {
                //�����͵���Ϣ���뵽���ݿ�
                pdate=getDate();
                ContentValues values=new ContentValues();
                ChatMsgEntity entity=new ChatMsgEntity();
                values.put("name", "��");
                entity.setName("��");
                values.put("pdate",pdate);
                entity.setDate(pdate);
                values.put("informations", writeMessage);
                entity.setText(writeMessage);
                entity.setMsgType(D);
                //�������ݿ�
                DatabaseHelper insertdbHelper=new DatabaseHelper(BluetoothChat.this,"zhsf_db");
                SQLiteDatabase insertdb=insertdbHelper.getWritableDatabase();
                insertdb.insert("info", null, values);
               // mConversationArrayAdapter.add("�ҡ�"+mHour+":"+mMinuts+":"+mSecond+"��\n��" + writeMessage+"��");
                mDataArrays.add(entity);
                mConversationArrayAdapter.notifyDataSetChanged();
                mConversationView.setSelection(mConversationView.getCount() - 1);

                }
                break;
              
            case MESSAGE_READ:
                byte[] readBuf = (byte[]) msg.obj;
                String readMessage = new String(readBuf, 0, msg.arg1);
                pdate=getDate();
                if(readMessage!=null)
                {
                //�����ܵ���Ϣ���뵽���ݿ�
                ContentValues values2=new ContentValues();
                ChatMsgEntity entity=new ChatMsgEntity();
                values2.put("name", mConnectedDeviceName);
                entity.setName(mConnectedDeviceName);
                values2.put("pdate",pdate);
                entity.setDate(pdate);
                values2.put("informations", readMessage);
                entity.setText(readMessage);
                entity.setMsgType(!D);
                DatabaseHelper insertdbHelper2=new DatabaseHelper(BluetoothChat.this,"zhsf_db");
                SQLiteDatabase insertdb2=insertdbHelper2.getWritableDatabase();
                insertdb2.insert("info", null, values2);
                mDataArrays.add(entity);
                mConversationArrayAdapter.notifyDataSetChanged();
                mConversationView.setSelection(mConversationView.getCount() - 1);

                
               // mConversationArrayAdapter.add(mConnectedDeviceName+"��"+mHour+":"+mMinuts+":"+mSecond+"��\n��" + readMessage+"��");
                }
                break;
            case MESSAGE_DEVICE_NAME:
                // �����������豸������
                mConnectedDeviceName = msg.getData().getString(DEVICE_NAME);
                //�������ݿ�
                DatabaseHelper dbHelper=new DatabaseHelper(BluetoothChat.this,"zhsf_db");
                SQLiteDatabase db=dbHelper.getReadableDatabase();   
                Toast.makeText(getApplicationContext(), "����"
                               + mConnectedDeviceName+"����", Toast.LENGTH_SHORT).show();
                break;
            case MESSAGE_TOAST:
                Toast.makeText(getApplicationContext(), msg.getData().getString(TOAST),
                               Toast.LENGTH_SHORT).show();
                break;
            }
        }
    };

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(D) Log.d(TAG, "onActivityResult " + resultCode);
        switch (requestCode) {
        case REQUEST_CONNECT_DEVICE:
            // ��DeviceListActivity���������豸ʱ
            if (resultCode == Activity.RESULT_OK) {
                // �õ��豸�� MAC ��ַ
                String address = data.getExtras()
                                     .getString(DeviceListActivity.EXTRA_DEVICE_ADDRESS);
                BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(address);
                // �������Ӹ��豸
                mChatService.connect(device);
            }
            break;
        case REQUEST_ENABLE_BT:
            if (resultCode == Activity.RESULT_OK) {
                // �������ã�����chat session
                setupChat();
            } else {
                Log.d(TAG, "BT not enabled");
                Toast.makeText(this, R.string.bt_not_enabled_leaving, Toast.LENGTH_SHORT).show();
               // finish();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.option_menu, menu);
        return true;
    }
    
    //��ȡʱ��ķ���
    private String getDate() {
        Calendar c = Calendar.getInstance();

        String year = String.valueOf(c.get(Calendar.YEAR));
        String month = String.valueOf(c.get(Calendar.MONTH));
        String day = String.valueOf(c.get(Calendar.DAY_OF_MONTH) + 1);
        String hour = String.valueOf(c.get(Calendar.HOUR_OF_DAY));
        String mins = String.valueOf(c.get(Calendar.MINUTE));
        
        
        StringBuffer sbBuffer = new StringBuffer();
        sbBuffer.append(month + "-" + day + " " + hour + ":" + mins); 
        						
        						
        return sbBuffer.toString();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case R.id.scan:
            Intent serverIntent = new Intent(this, DeviceListActivity.class);
            startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE);
            return true;
        case R.id.discoverable:
            // �������ÿɼ��Է���
            ensureDiscoverable();
            return true;
        case R.id.openbluetooth:
        	ensureOpenbluetooth();
            return true;
        case R.id.closebluetooth:
        	ensureClosebluetooth();
            return true;
        case R.id.record:
        	Intent recordIntent = new Intent(BluetoothChat.this, RecordListActivity.class);
            startActivity(recordIntent);
            return true;
        case R.id.exit:
        	ensureExit();
        	return true;
        }
        return false;
    }

}