package com.yba.aurdinoproject.async_task;

import android.bluetooth.BluetoothSocket;
import android.os.AsyncTask;
import android.util.Log;

import com.yba.aurdinoproject.Interfaces.BluetoothConnectionListener;
import com.yba.aurdinoproject.threads.AurdinoInputThread;

import java.io.IOException;

public class DisconnectBluetoothAsyncTask extends AsyncTask<Void, Void, Void> {

    private static final String TAG = DisconnectBluetoothAsyncTask.class.getSimpleName();
    private BluetoothSocket bluetoothSocket;
    private BluetoothConnectionListener bluetoothConnectionListener;
    private AurdinoInputThread aurdinoInputThread;
    private boolean isSiccessfullyDisconnected = true;

    public DisconnectBluetoothAsyncTask(BluetoothSocket bluetoothSocket, AurdinoInputThread aurdinoInputThread) {
        this.bluetoothSocket = bluetoothSocket;
        this.aurdinoInputThread = aurdinoInputThread;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Void doInBackground(Void... voids) {

        if(aurdinoInputThread != null){
            aurdinoInputThread.stop();
            //noinspection StatementWithEmptyBody
            while (aurdinoInputThread.isRunning());
            aurdinoInputThread = null;
        }

        try {
            bluetoothSocket.close();
        }catch (IOException e){
            e.printStackTrace();
            isSiccessfullyDisconnected = false;
            Log.e(TAG,"Error in Disconnecting...");
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        if(isSiccessfullyDisconnected && bluetoothConnectionListener != null){
            bluetoothConnectionListener.onDisconnected();
        }
    }

    public void setBluetoothConnectionListener(BluetoothConnectionListener bluetoothConnectionListener) {
        this.bluetoothConnectionListener = bluetoothConnectionListener;
    }
}
