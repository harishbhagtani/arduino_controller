package com.yba.aurdinoproject;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.yba.aurdinoproject.Interfaces.BluetoothConnectionListener;
import com.yba.aurdinoproject.Interfaces.OnBluetoothCheckedListener;
import com.yba.aurdinoproject.Interfaces.OnBluetoothListLoadedListener;
import com.yba.aurdinoproject.array_adapters.BluetoothListViewAdapter;
import com.yba.aurdinoproject.async_task.ConnectBluetoothAsyncTask;
import com.yba.aurdinoproject.helper_classes.BluetoothControlHelper;
import com.yba.aurdinoproject.helper_classes.BluetoothHelper;

import java.util.List;

public class BluetoothListActivity extends AppCompatActivity {

    private static final String TAG = BluetoothListActivity.class.getSimpleName();
    ListView listView;
    Button buttonSearchForDevices;
    List<BluetoothDevice> bluetoothDeviceList;
    BluetoothHelper bluetoothHelper;
    BluetoothListViewAdapter bluetoothListViewAdapter;
    BluetoothControlHelper bluetoothControlHelper;



    //Constants
    public static final int REQUEST_CODE_ENABLE_BLUETOOTH = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth_list);

        listView =  findViewById(R.id.listViewBluetoothDevices);
        buttonSearchForDevices = findViewById(R.id.buttonSearchDevices);
        bluetoothHelper = new BluetoothHelper();
        bluetoothControlHelper = (BluetoothControlHelper) getApplicationContext();

        setOnClickListeners();
        startSearchProcess();
    }

    public void message(String message){
        Toast.makeText(this,message,Toast.LENGTH_LONG).show();
    }

    private void startSearchProcess(){
//        if(!bluetoothControlHelper.isBluetoothEnabled()){
//            requestEnableBluetooth();
//        }else{
//            message("Bluetooth ON");
//        }
        bluetoothHelper.checkForBluetooth(new OnBluetoothCheckedListener() {
            @Override
            public void onChecked(int status) {
                if (status == BluetoothHelper.STATUS_ENABLED){
                    message("Bluetooth ON");
                    Log.d(TAG,"Bluetooth is ON");
                    loadBluetoothDeviceList();
                }else{
                    requestEnableBluetooth();
                    message("Bluetooth OFF");
                    Log.d(TAG,"Bluetooth is OFF");
                }
            }
        });
    }

    public void loadBluetoothDeviceList(){
        bluetoothHelper.getBluetoothDeviceList(new OnBluetoothListLoadedListener() {
            @Override
            public void onLoaded(List<BluetoothDevice> bluetoothDeviceList) {
                BluetoothListActivity.this.bluetoothDeviceList = bluetoothDeviceList;
                addBluetoothDevicesToList(bluetoothDeviceList);
                Log.e(TAG,"RECIEVED DATA : " + BluetoothListActivity.this.bluetoothDeviceList);
            }
        });


        bluetoothControlHelper.setOnBluetoothListLoadedListener(new OnBluetoothListLoadedListener() {
            @Override
            public void onLoaded(List<BluetoothDevice> bluetoothDeviceList) {
                addBluetoothDevicesToList(bluetoothDeviceList);
            }
        });
    }

    private void setOnClickListeners(){
        buttonSearchForDevices.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startSearchProcess();
            }
        });
    }


    private void addBluetoothDevicesToList(List<BluetoothDevice> bluetoothDeviceList){
        final BluetoothListViewAdapter bluetoothListViewAdapter = new BluetoothListViewAdapter(getApplicationContext(), R.layout.bluetooth_list_item);
        bluetoothListViewAdapter.setBluetoothDeviceList(bluetoothDeviceList);
        listView.setAdapter(bluetoothListViewAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                bluetoothListViewAdapter.setSelectedItemIndex(position);
//                bluetoothControlHelper.setSelectedDevicePosition(position);
                buttonSearchForDevices.setText(getString(R.string.connect));
                buttonSearchForDevices.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                bluetoothHelper.setSelectedDevicePosition(position);
                buttonSearchForDevices.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        connectToDevice(position);
                    }
                });
            }
        });
        this.bluetoothListViewAdapter = bluetoothListViewAdapter;
    }

    //TODO:Write a method to connect to the device
    public void connectToDevice(int position){
        ConnectBluetoothAsyncTask connectBluetoothAsyncTask = new ConnectBluetoothAsyncTask(bluetoothDeviceList.get(position));
        connectBluetoothAsyncTask.setmIsBluetoothConnected(true);
        connectBluetoothAsyncTask.setBluetoothConnectionListener(new BluetoothConnectionListener() {
            @Override
            public void onConnectionProcessStarted() {
                buttonSearchForDevices.setText("Connecting...");
            }

            @Override
            public void onConnected(BluetoothSocket bluetoothSocket) {
                buttonSearchForDevices.setText("Connected");
            }

            @Override
            public void onDisconnected() {

            }

            @Override
            public void onConnectionFailed() {
                buttonSearchForDevices.setText("Connection Failed");
                buttonSearchForDevices.setBackgroundColor(Color.RED);
            }
        });
        connectBluetoothAsyncTask.execute();
//        bluetoothControlHelper.connectToDevice(position, new BluetoothConnectionListener() {
//            @Override
//            public void onConnectionProcessStarted() {
//
//            }
//
//            @Override
//            public void onConnected(BluetoothSocket bluetoothSocket) {
//                BluetoothListActivity.this.onConnected();
//            }
//
//            @Override
//            public void onDisconnected() {
//
//            }
//
//            @Override
//            public void onConnectionFailed() {
//                message("Connection Failed");
//            }
//        });
    }

    public void onConnected(){
        Intent intent = new Intent();
        intent.putExtra("uuid", bluetoothDeviceList.get(bluetoothListViewAdapter.getSelectedItemIndex()));
        setResult(Activity.RESULT_OK,intent);
        finish();
    }

    private void requestEnableBluetooth(){
        Intent enableBT = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        startActivityForResult(enableBT,REQUEST_CODE_ENABLE_BLUETOOTH);
    }

    @Override
    public void onBackPressed() {
        if(bluetoothListViewAdapter.getSelectedItemIndex() != -1){
            bluetoothListViewAdapter.unselectItem();
        }else{
            super.onBackPressed();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if (requestCode == REQUEST_CODE_ENABLE_BLUETOOTH) {
            startSearchProcess();
        }

        super.onActivityResult(requestCode, resultCode, data);
    }
}
