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
    private UUID uuid = UUID.fromString("cf103a14-527f-11ea-8d77-2e728ce88125");

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
    }

    @Override
    protected Void doInBackground(Void... voids) {

        try {
            if (bluetoothSocket == null || !mIsBluetoothConnected) {
                Log.e(TAG,"UUID : " + uuid);
                Log.d(TAG,"Attempting to connect to " + bluetoothDevice.getName() + " with address " + bluetoothDevice.getAddress());
                bluetoothSocket = bluetoothDevice.createInsecureRfcommSocketToServiceRecord(uuid);
                BluetoothAdapter.getDefaultAdapter().cancelDiscovery();
                bluetoothSocket.connect();
                Log.v(TAG,"Connected to " + bluetoothDevice.getName() + " with address " + bluetoothDevice.getAddress());
            }
        } catch (IOException e) {
            Log.e(TAG,"Error connecting to " + bluetoothDevice.getName() + " with address " + bluetoothDevice.getAddress());

            try {
                bluetoothSocket =(BluetoothSocket) bluetoothDevice.getClass().getMethod("createRfcommSocket", new Class[] {int.class}).invoke(bluetoothDevice,1);
                bluetoothSocket.connect();
                Log.e(TAG,"Connected Finally");
            }catch (Exception e3){
                e3.printStackTrace();
                Log.e(TAG,"Unable to connect even now to " + bluetoothDevice.getName() + " " + bluetoothDevice.getAddress());
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
