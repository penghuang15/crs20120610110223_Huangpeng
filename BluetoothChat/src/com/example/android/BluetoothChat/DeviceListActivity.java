
package com.example.android.BluetoothChat;

import java.util.Set;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class DeviceListActivity extends Activity {
    private static final String TAG = "DeviceListActivity";
    private static final boolean D = true;
    public static String EXTRA_DEVICE_ADDRESS = "device_address";
    private BluetoothAdapter mBtAdapter;
    private ArrayAdapter<String> mPairedDevicesArrayAdapter;
    private ArrayAdapter<String> mNewDevicesArrayAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // ���ô��ڽ���
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setContentView(R.layout.device_list);
        setResult(Activity.RESULT_CANCELED);
        // ��ʼ������������ť
        Button scanButton = (Button) findViewById(R.id.button_scan);
        scanButton.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                doDiscovery();
                v.setVisibility(View.GONE);
            }
        });

        // ��ʼ��������б�����豸�б�
        mPairedDevicesArrayAdapter = new ArrayAdapter<String>(this, R.layout.device_name);
        mNewDevicesArrayAdapter = new ArrayAdapter<String>(this, R.layout.device_name);

        ListView pairedListView = (ListView) findViewById(R.id.paired_devices);
        pairedListView.setAdapter(mPairedDevicesArrayAdapter);
        pairedListView.setOnItemClickListener(mDeviceClickListener);

  
        ListView newDevicesListView = (ListView) findViewById(R.id.new_devices);
        newDevicesListView.setAdapter(mNewDevicesArrayAdapter);
        newDevicesListView.setOnItemClickListener(mDeviceClickListener);

        // �������豸ʱ��ʼע��㲥
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        this.registerReceiver(mReceiver, filter);

        // ���������ʱ��ʼע��㲥
        filter = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        this.registerReceiver(mReceiver, filter);
        mBtAdapter = BluetoothAdapter.getDefaultAdapter();

        // ��ȡ������豸
        Set<BluetoothDevice> pairedDevices = mBtAdapter.getBondedDevices();

        // �����������豸����ӵ�������б���������
        if (pairedDevices.size() > 0) {
            findViewById(R.id.title_paired_devices).setVisibility(View.VISIBLE);
            for (BluetoothDevice device : pairedDevices) {
                mPairedDevicesArrayAdapter.add("�ǳƣ�"+device.getName() + "\nMAC��ַ��" + device.getAddress());
            }
        } else {
            String noDevices = getResources().getText(R.string.none_paired).toString();
            mPairedDevicesArrayAdapter.add(noDevices);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mBtAdapter != null) {
            mBtAdapter.cancelDiscovery();
        }
        this.unregisterReceiver(mReceiver);
    }

    private void doDiscovery() {
        if (D) Log.d(TAG, "doDiscovery()");

        // ���������������ɼ�
        setProgressBarIndeterminateVisibility(true);
        setTitle(R.string.scanning);
        findViewById(R.id.title_new_devices).setVisibility(View.VISIBLE);
        if (mBtAdapter.isDiscovering()) {
            mBtAdapter.cancelDiscovery();
        }
        mBtAdapter.startDiscovery();
    }

    private OnItemClickListener mDeviceClickListener = new OnItemClickListener() {
        public void onItemClick(AdapterView<?> av, View v, int arg2, long arg3) {
        	if(mBtAdapter.isEnabled()){
            // ȡ������
            mBtAdapter.cancelDiscovery();
            //  ���豸��MAC��ַ�л�ȡ��ַ���17���ֽ� 
            String info = ((TextView) v).getText().toString();
            String address = info.substring(info.length() - 17);
            Intent intent = new Intent();
            intent.putExtra(EXTRA_DEVICE_ADDRESS, address);
            // ��ص��������� result�������� Activity
            setResult(Activity.RESULT_OK, intent);
            finish();
        	}else{  Toast.makeText(DeviceListActivity.this, "��ȷ�������Ѿ���", Toast.LENGTH_LONG).show();  }
            
        }
    };
    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            // ������һ���豸ʱ
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                // ��Intent�л�� BluetoothDevice ����
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                // ������豸û����ԣ�����ӵ����豸�б���
                if (device.getBondState() != BluetoothDevice.BOND_BONDED) {
                    mNewDevicesArrayAdapter.add(device.getName() + "\n" + device.getAddress());
                }
            // ����������ʱ���� Activity ����
            } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                setProgressBarIndeterminateVisibility(false);
                setTitle(R.string.select_device);
                if (mNewDevicesArrayAdapter.getCount() == 0) {
                	
                	 Button scanButton = (Button) findViewById(R.id.button_scan);
                	 scanButton.setVisibility(View.VISIBLE);
                	 scanButton.setText("�ٴ�����");
                     scanButton.setOnClickListener(new OnClickListener() {
                         public void onClick(View v) {
                             doDiscovery();
                             v.setVisibility(View.GONE);
                         }
                     });
                  String noDevices = getResources().getText(R.string.none_found).toString();
                    mNewDevicesArrayAdapter.add(noDevices);
                   
                	
                }
            }
        }
    };

}
