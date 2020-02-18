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

    private  BluetoothControlHelper bluetoothControlHelper;
    private  BluetoothHelper bluetoothHelper;
    private  List<BluetoothDevice> bluetoothDeviceList;
    private  OnBluetoothListLoadedListener onBluetoothListLoadedListener;
    private  BluetoothSocket bluetoothSocket;
    private OnInputRecievedListener onInputRecievedListener;
    private AurdinoInputThread aurdinoInputThread;

    public static final String TAG = BluetoothControlHelper.class.getSimpleName();

    public BluetoothControlHelper() {
        Log.e(TAG,"Instance created");
        bluetoothDeviceList = new ArrayList<>();
        bluetoothHelper = new BluetoothHelper();
        bluetoothHelper.getBluetoothDeviceList(new OnBluetoothListLoadedListener() {
            @Override
            public void onLoaded(List<BluetoothDevice> bluetoothDeviceList) {
                getBluetoothDeviceList().addAll(bluetoothDeviceList);
                if(onBluetoothListLoadedListener != null)
                onBluetoothListLoadedListener.onLoaded(bluetoothDeviceList);
            }
        });
    }

    public void setOnBluetoothListLoadedListener(OnBluetoothListLoadedListener onBluetoothListLoadedListener) {
        this.onBluetoothListLoadedListener = onBluetoothListLoadedListener;
    }

    public void setOnInputRecievedListener(OnInputRecievedListener onInputRecievedListener) {
        this.onInputRecievedListener = onInputRecievedListener;
    }

    public BluetoothControlHelper getInstance(){
        if(bluetoothControlHelper == null){
            bluetoothControlHelper = new BluetoothControlHelper();
        }
        return bluetoothControlHelper;
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

    public void connectToDevice(final BluetoothDevice bluetoothDevice, final BluetoothConnectionListener bluetoothConnectionListener){
        ConnectBluetoothAsyncTask connectBluetoothAsyncTask = new ConnectBluetoothAsyncTask(bluetoothDevice);
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
                aurdinoInputThread = new AurdinoInputThread(bluetoothSocket, new OnInputRecievedListener() {
                    @Override
                    public void onInputReceived(int input) {
                        if(BluetoothControlHelper.this.onInputRecievedListener != null){
                            BluetoothControlHelper.this.onInputRecievedListener.onInputReceived(input);
                        }
                    }
                });
            }

            @Override
            public void onDisconnected() {
                if(bluetoothConnectionListener != null)
                bluetoothConnectionListener.onDisconnected();
            }

            @Override
            public void onConnectionFailed() {
                if(bluetoothConnectionListener != null)
                bluetoothConnectionListener.onDisconnected();
            }
        });
        connectBluetoothAsyncTask.execute();
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
                aurdinoInputThread = new AurdinoInputThread(bluetoothSocket, new OnInputRecievedListener() {
                    @Override
                    public void onInputReceived(int input) {
                        if(BluetoothControlHelper.this.onInputRecievedListener != null){
                            BluetoothControlHelper.this.onInputRecievedListener.onInputReceived(input);
                        }
                    }
                });
            }

            @Override
            public void onDisconnected() {
                if(bluetoothConnectionListener != null)
                bluetoothConnectionListener.onDisconnected();
            }

            @Override
            public void onConnectionFailed() {
                if(bluetoothConnectionListener != null)
                bluetoothConnectionListener.onDisconnected();
            }
        });
        connectBluetoothAsyncTask.execute();
    }

    public void disconnectFromDevice(final BluetoothConnectionListener bluetoothConnectionListener){
        if(bluetoothConnectionListener != null){
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
                    bluetoothConnectionListener.onDisconnected();
                }

                @Override
                public void onConnectionFailed() {

                }
            });
            disconnectBluetoothAsyncTask.execute();
        }
    }

    public void sendData(int code){
        byte[] bytes = String.valueOf(code).getBytes();
        if(getBluetoothSocket() != null){
            try {
                if(getBluetoothSocket() != null) {
                    getBluetoothSocket().getOutputStream().write(bytes);
                }else{
                    Log.e(TAG,"Bluetooth Socket is null");
                }
            }catch (IOException e){
                Log.e(TAG,"Error in sending data through bluetooth");
                e.printStackTrace();
            }
        }
    }

    public boolean isBluetoothDeviceListListLoaded(){
        return bluetoothDeviceList != null;
    }

    public  List<BluetoothDevice> getBluetoothDeviceList() {
        return bluetoothDeviceList;
    }

    public void setSelectedDevicePosition(int position){
        bluetoothHelper.setSelectedDevicePosition(position);
    }
}
