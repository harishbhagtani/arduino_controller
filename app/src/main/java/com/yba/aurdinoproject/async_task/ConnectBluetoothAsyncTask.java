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
    private UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

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
        bluetoothConnectionListener.onConnectionProcessStarted();
    }

    @Override
    protected Void doInBackground(Void... voids) {

        try {
            if (bluetoothSocket == null || !mIsBluetoothConnected) {
                Log.e("TAG","UUID : " + bluetoothDevice.getUuids()[0].getUuid());
                bluetoothSocket = bluetoothDevice.createRfcommSocketToServiceRecord(uuid);
                BluetoothAdapter.getDefaultAdapter().cancelDiscovery();
                bluetoothSocket.connect();
            }
        } catch (IOException e) {
            Log.e(TAG,"Connection Error");
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
        }else{
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
