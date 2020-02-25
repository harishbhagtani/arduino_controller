package com.yba.aurdinoproject;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

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
import com.yba.aurdinoproject.Interfaces.OnBluetoothListLoadedListener;
import com.yba.aurdinoproject.array_adapters.BluetoothListViewAdapter;
import com.yba.aurdinoproject.helper_classes.BluetoothControlHelper;

import java.util.List;


public class BluetoothListActivity extends AppCompatActivity {

    private static final String TAG = BluetoothListActivity.class.getSimpleName();

    ListView listView;
    Button buttonSearchForDevices;
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
        bluetoothControlHelper = (BluetoothControlHelper) getApplicationContext();

        setOnClickListeners();
        startSearchProcess();
    }

    public void message(String message){
        Toast.makeText(this,message,Toast.LENGTH_LONG).show();
    }

    private void startSearchProcess(){
        if(!bluetoothControlHelper.isBluetoothEnabled()){
            requestEnableBluetooth();
        }else{
            message("Bluetooth ON");
            loadBluetoothDeviceList();
        }
    }

    public void loadBluetoothDeviceList(){
        if(bluetoothControlHelper.getBluetoothDeviceList().size() > 0){
            Log.v(TAG,"List loaded successfully : " + bluetoothControlHelper.getBluetoothDeviceList());
            addBluetoothDevicesToList();
        }else{
            Log.e(TAG,"Paired device list is zero");
            message("No Paired Device Available");
        }
    }

    private void setOnClickListeners(){
        buttonSearchForDevices.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startSearchProcess();
            }
        });
    }


    private void addBluetoothDevicesToList(){
        final BluetoothListViewAdapter bluetoothListViewAdapter = new BluetoothListViewAdapter(getApplicationContext(), R.layout.bluetooth_list_item);
        bluetoothListViewAdapter.setBluetoothDeviceList(bluetoothControlHelper.getBluetoothDeviceList());
        listView.setAdapter(bluetoothListViewAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                bluetoothListViewAdapter.setSelectedItemIndex(position);
                bluetoothControlHelper.setSelectedDevicePosition(position);
                buttonSearchForDevices.setText("Connect to : " + bluetoothControlHelper.getBluetoothDeviceList().get(position).getName());
                buttonSearchForDevices.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
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

    public void connectToDevice(int position){
        bluetoothControlHelper.connectToDevice(position, new BluetoothConnectionListener() {
            @Override
            public void onConnectionProcessStarted() {
                buttonSearchForDevices.setText("Connecting...");
            }

            @Override
            public void onConnected(BluetoothSocket bluetoothSocket) {
                buttonSearchForDevices.setText("Connected");
                BluetoothListActivity.this.finish();
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
    }

    private void requestEnableBluetooth(){
        Intent enableBT = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        startActivityForResult(enableBT,REQUEST_CODE_ENABLE_BLUETOOTH);
    }

    @Override
    public void onBackPressed() {
        if(bluetoothListViewAdapter.getSelectedItemIndex() != -1){
            bluetoothListViewAdapter.unselectItem();
            buttonSearchForDevices.setText("Search Again");
            buttonSearchForDevices.setBackgroundColor(getResources().getColor(R.color.blue));
            buttonSearchForDevices.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startSearchProcess();
                }
            });
        }else{
            super.onBackPressed();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if (requestCode == REQUEST_CODE_ENABLE_BLUETOOTH) {
            if(bluetoothControlHelper.getBluetoothDeviceList().size() == 0){
                bluetoothControlHelper.reloadBluetoothDeviceList(new OnBluetoothListLoadedListener() {
                    @Override
                    public void onLoaded(List<BluetoothDevice> bluetoothDeviceList) {
                        startSearchProcess();
                    }
                });
            }else{
                startSearchProcess();
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }
}
