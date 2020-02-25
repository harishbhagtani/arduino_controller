package com.yba.aurdinoproject.helper_classes;

import android.app.Application;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.util.Log;


import com.yba.aurdinoproject.Interfaces.BluetoothConnectionListener;
import com.yba.aurdinoproject.Interfaces.OnBluetoothListLoadedListener;
import com.yba.aurdinoproject.Interfaces.OnInputRecievedListener;
import com.yba.aurdinoproject.async_task.ConnectBluetoothAsyncTask;
import com.yba.aurdinoproject.async_task.DisconnectBluetoothAsyncTask;
import com.yba.aurdinoproject.threads.AurdinoInputThread;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class BluetoothControlHelper extends Application {

    private  BluetoothHelper bluetoothHelper;
    private  List<BluetoothDevice> bluetoothDeviceList;
    private  BluetoothSocket bluetoothSocket;
    private AurdinoInputThread aurdinoInputThread;
    private boolean isConnectedToArduino = false;

    public static final String TAG = BluetoothControlHelper.class.getSimpleName();

    public BluetoothControlHelper() {
        Log.e(TAG,"Instance created");
        bluetoothDeviceList = new ArrayList<>();
        bluetoothHelper = new BluetoothHelper();
        bluetoothHelper.getBluetoothDeviceList(new OnBluetoothListLoadedListener() {
            @Override
            public void onLoaded(List<BluetoothDevice> bluetoothDeviceList) {
                getBluetoothDeviceList().addAll(bluetoothDeviceList);
                Log.v(TAG,"List Loaded Successfylly : " + BluetoothControlHelper.this.bluetoothDeviceList);
            }
        });
    }

    public void reloadBluetoothDeviceList(final OnBluetoothListLoadedListener onBluetoothListLoadedListener){
        bluetoothHelper.getBluetoothDeviceList(new OnBluetoothListLoadedListener() {
            @Override
            public void onLoaded(List<BluetoothDevice> bluetoothDeviceList) {
                getBluetoothDeviceList().addAll(bluetoothDeviceList);
                onBluetoothListLoadedListener.onLoaded(bluetoothDeviceList);
            }
        });
    }

    public  BluetoothSocket getBluetoothSocket() {
        return bluetoothSocket;
    }

    public void setBluetoothSocket(BluetoothSocket bluetoothSocket) {
        this.bluetoothSocket = bluetoothSocket;
    }

    public boolean isBluetoothEnabled(){
        return bluetoothHelper.checkForBluetooth(null);
    }

    public void connectToDevice(int position, final BluetoothConnectionListener bluetoothConnectionListener){
        ConnectBluetoothAsyncTask connectBluetoothAsyncTask = new ConnectBluetoothAsyncTask(bluetoothDeviceList.get(position));
        connectBluetoothAsyncTask.setmIsBluetoothConnected(isBluetoothEnabled());
        connectBluetoothAsyncTask.setBluetoothConnectionListener(new BluetoothConnectionListener() {
            @Override
            public void onConnectionProcessStarted() {
                if(bluetoothConnectionListener != null)
                bluetoothConnectionListener.onConnectionProcessStarted();
            }

            @Override
            public void onConnected(BluetoothSocket bluetoothSocket) {
                if(bluetoothConnectionListener != null)
                bluetoothConnectionListener.onConnected(bluetoothSocket);
                setBluetoothSocket(bluetoothSocket);
                isConnectedToArduino = true;
            }

            @Override
            public void onDisconnected() {
                if(bluetoothConnectionListener != null)
                bluetoothConnectionListener.onDisconnected();
            }

            @Override
            public void onConnectionFailed() {
                if(bluetoothConnectionListener != null)
                bluetoothConnectionListener.onConnectionFailed();
            }
        });
        connectBluetoothAsyncTask.execute();
    }

    public void startListeningForInput(final OnInputRecievedListener onInputRecievedListener){
        if(onInputRecievedListener != null && bluetoothSocket != null){
            aurdinoInputThread = new AurdinoInputThread(bluetoothSocket, new OnInputRecievedListener() {
                @Override
                public void onInputReceived(int input) {
                    onInputRecievedListener.onInputReceived(input);
                }
            });
        }
    }

    public void stopListeningForInput(){
        if(aurdinoInputThread != null && aurdinoInputThread.isRunning())
        aurdinoInputThread.stop();
    }



    public void disconnectFromDevice(final BluetoothConnectionListener bluetoothConnectionListener){
        if(bluetoothConnectionListener != null && getBluetoothSocket() != null){
            DisconnectBluetoothAsyncTask disconnectBluetoothAsyncTask = new DisconnectBluetoothAsyncTask(getBluetoothSocket(), aurdinoInputThread);
            disconnectBluetoothAsyncTask.setBluetoothConnectionListener(new BluetoothConnectionListener() {
                @Override
                public void onConnectionProcessStarted() {

                }

                @Override
                public void onConnected(BluetoothSocket bluetoothSocket) {

                }

                @Override
                public void onDisconnected() {
                    stopListeningForInput();
                    bluetoothConnectionListener.onDisconnected();
                    isConnectedToArduino = false;
                }

                @Override
                public void onConnectionFailed() {

                }
            });
            disconnectBluetoothAsyncTask.execute();
        }else{
            if(bluetoothSocket == null){
                Log.e(TAG,"Disconnection Failed : No connected device exists.");
            }
        }
    }

    public void sendData(int code){
        Log.e(TAG,"Send Data : " + code);
        byte[] bytes = String.valueOf(code).getBytes();
        if(getBluetoothSocket() != null){
            try {
                if(getBluetoothSocket() != null) {
                    getBluetoothSocket().getOutputStream().write(bytes);
                    Log.v(TAG,"Data sent successfully.");
                }else{
                    Log.e(TAG,"Bluetooth Socket is null");
                }
            }catch (IOException e){
                Log.e(TAG,"Error in sending data through bluetooth");
                e.printStackTrace();
            }
        }else{
            Log.e(TAG,"Unable to send data : Bluetooth Socket is null.");
        }
    }

    public boolean isConnectedToArduino() {
        return isConnectedToArduino;
    }

    public  List<BluetoothDevice> getBluetoothDeviceList() {
        return bluetoothDeviceList;
    }

    public void setSelectedDevicePosition(int position){
        bluetoothHelper.setSelectedDevicePosition(position);
    }
}
