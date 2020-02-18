package com.yba.aurdinoproject.async_task;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.os.AsyncTask;

import com.yba.aurdinoproject.Interfaces.OnBluetoothListLoadedListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class SearchBluetoothDevicesAsyncTask extends AsyncTask<Void, Void, List<BluetoothDevice>> {

    private BluetoothAdapter bluetoothAdapter;
    private OnBluetoothListLoadedListener onBluetoothListLoadedListener;

    public SearchBluetoothDevicesAsyncTask(OnBluetoothListLoadedListener onBluetoothListLoadedListener) {
        this.onBluetoothListLoadedListener = onBluetoothListLoadedListener;
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    }

    @Override
    protected List<BluetoothDevice> doInBackground(Void... voids) {
        Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();
        return new ArrayList<>(pairedDevices);
    }

    @Override
    protected void onPostExecute(List<BluetoothDevice> bluetoothDevices) {
        super.onPostExecute(bluetoothDevices);
        if (bluetoothDevices.size() > 0) {
            onBluetoothListLoadedListener.onLoaded(bluetoothDevices);
        } else {
            onBluetoothListLoadedListener.onLoaded(null);
        }
    }
}
