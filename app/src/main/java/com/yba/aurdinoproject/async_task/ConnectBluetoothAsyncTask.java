package com.yba.aurdinoproject.async_task;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.AsyncTask;
import android.util.Log;

import com.yba.aurdinoproject.Interfaces.BluetoothConnectionListener;

import java.io.IOException;
import java.util.UUID;

public class ConnectBluetoothAsyncTask extends AsyncTask<Void, Void, Void> {

    private BluetoothSocket bluetoothSocket;
    private BluetoothDevice bluetoothDevice;
    private UUID uuid = UUID.fromString("fa87c0d0-afac-11de-8a39-0800200c9a66");

    private static final String TAG = ConnectBluetoothAsyncTask.class.getSimpleName();
    private boolean isConnectionSuccessful = false;

    private BluetoothConnectionListener bluetoothConnectionListener;
    private boolean mIsBluetoothConnected;

    public ConnectBluetoothAsyncTask(BluetoothDevice bluetoothDevice) {
        this.bluetoothDevice = bluetoothDevice;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        Log.d(TAG,"Bluetooth connection process started...");
        bluetoothConnectionListener.onConnectionProcessStarted();
        setmIsBluetoothConnected(false);
        BluetoothSocket tmp = null;
        try {
            Log.d(TAG,"Creating Bluetooth Socket...");
            tmp = bluetoothDevice.createRfcommSocketToServiceRecord(uuid);
            Log.d(TAG,"Bluetooth Socket Created");
        }catch (IOException e){
            Log.e(TAG,"Error in creating socket");
        }
        bluetoothSocket = tmp;
    }

    @Override
    protected Void doInBackground(Void... voids) {

        Log.d(TAG,"doInBackground() called  " + mIsBluetoothConnected);
        BluetoothAdapter.getDefaultAdapter().cancelDiscovery();

        try {
            if (!mIsBluetoothConnected) {
                Log.e(TAG,"UUID : " + uuid);
                Log.d(TAG,"Attempting to connect to " + bluetoothDevice.getName() + " with address " + bluetoothDevice.getAddress());
                bluetoothSocket.connect();
                Log.v(TAG,"Connected to " + bluetoothDevice.getName() + " with address " + bluetoothDevice.getAddress());
                isConnectionSuccessful = true;
            }
        } catch (IOException e) {
            Log.e(TAG,"Error connecting to " + bluetoothDevice.getName() + " with address " + bluetoothDevice.getAddress());

            try {
                bluetoothSocket =(BluetoothSocket) bluetoothDevice.getClass().getMethod("createRfcommSocket", new Class[] {int.class}).invoke(bluetoothDevice,1);
                bluetoothSocket.connect();
                Log.e(TAG,"Connected Finally");
            }catch (Exception e3){
                e3.printStackTrace();
                Log.e(TAG,"Unable to connect even now to " + bluetoothDevice.getName() + " at address " + bluetoothDevice.getAddress());
            }
            try {
                bluetoothSocket.close();
                Log.e(TAG,"Socket closed after error in connection");
            }catch (IOException z){
                z.printStackTrace();
                Log.e(TAG,"Error closing socket");
            }
            e.printStackTrace();
            isConnectionSuccessful = false;
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        if(isConnectionSuccessful) {
            bluetoothConnectionListener.onConnected(bluetoothSocket);
            Log.d(TAG,"Connection process finished successfully");
        }else{
            Log.e(TAG,"Connection process finished with an error");
            bluetoothConnectionListener.onConnectionFailed();
        }
    }

    public void setmIsBluetoothConnected(boolean mIsBluetoothConnected) {
        this.mIsBluetoothConnected = mIsBluetoothConnected;
    }

    public void setBluetoothConnectionListener(BluetoothConnectionListener bluetoothConnectionListener) {
        this.bluetoothConnectionListener = bluetoothConnectionListener;
    }
}
