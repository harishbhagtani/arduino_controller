package com.yba.aurdinoproject.threads;

import android.bluetooth.BluetoothSocket;
import android.util.Log;

import com.yba.aurdinoproject.Interfaces.OnInputRecievedListener;

import java.io.IOException;
import java.io.InputStream;

public class AurdinoInputThread implements Runnable{

    private static final String THREAD_NAME = "Aurdino Input Thread";
    private static final String TAG = AurdinoInputThread.class.getSimpleName();

    private Thread thread;
    private BluetoothSocket bluetoothSocket;
    private boolean isStopped = false;

    private OnInputRecievedListener onInputRecievedListener;

    public AurdinoInputThread(BluetoothSocket bluetoothSocket, OnInputRecievedListener onInputRecievedListener) {
        Log.d(TAG,"Instance Created");
        this.bluetoothSocket = bluetoothSocket;
        this.onInputRecievedListener = onInputRecievedListener;
        thread = new Thread(this, THREAD_NAME);
        thread.start();
    }

    @Override
    public void run() {
        Log.d(TAG,"Input Thread Started...");
        InputStream inputStream;
        int bytes;
        try {
            inputStream = bluetoothSocket.getInputStream();
            while (!isStopped){
                byte[] buffer  = new byte[256];
                bytes = inputStream.read(buffer);
                String message = new String(buffer,0,bytes);
                Log.e(TAG,"Message Received : " + message);
                if(onInputRecievedListener != null){
                    onInputRecievedListener.onInputReceived(Integer.valueOf(message));
                }
            }
            Thread.sleep(500);
        }catch (IOException e){
            Log.e(TAG,"Error in getting data buffer");
            e.printStackTrace();
        }catch (InterruptedException e){
            Log.e(TAG,"Thread Interruption Error Occurred");
            e.printStackTrace();
        }
    }

    public boolean isRunning(){
        Log.d(TAG,"Thread Rinnging : " + thread.isAlive());
        return thread.isAlive();
    }

    public void stop(){
        isStopped = true;
    }
}
