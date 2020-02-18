package com.yba.aurdinoproject.helper_classes;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.util.Log;

import com.yba.aurdinoproject.Interfaces.OnBluetoothCheckedListener;
import com.yba.aurdinoproject.Interfaces.OnBluetoothListLoadedListener;
import com.yba.aurdinoproject.async_task.SearchBluetoothDevicesAsyncTask;

import java.util.List;

public class BluetoothHelper {

    public static final int STATUS_ENABLED = 100;
    private static final int STATUS_DISABLED = 101;

    private static final String TAG = BluetoothHelper.class.getSimpleName();

    public BluetoothHelper() {
    }

    public boolean checkForBluetooth(OnBluetoothCheckedListener onBluetoothCheckedListener){
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if(!bluetoothAdapter.isEnabled()){
            if(onBluetoothCheckedListener != null) {
                onBluetoothCheckedListener.onChecked(STATUS_DISABLED);
            }
            return false;
        }else{
            if(onBluetoothCheckedListener != null) {
                onBluetoothCheckedListener.onChecked(STATUS_ENABLED);
            }
            return true;
        }
    }

    public void setSelectedDevicePosition(int selectedDevicePosition) {
        Log.e(TAG,"Device at position " + selectedDevicePosition + " selected.");
    }

    public void getBluetoothDeviceList(final OnBluetoothListLoadedListener onBluetoothListLoadedListener){
        SearchBluetoothDevicesAsyncTask searchBluetoothDevicesAsyncTask = new SearchBluetoothDevicesAsyncTask(new OnBluetoothListLoadedListener() {
            @Override
            public void onLoaded(List<BluetoothDevice> bluetoothDeviceList) {
                if(bluetoothDeviceList != null) {
                    Log.e(TAG,"SENDING DATA : " + bluetoothDeviceList);
                    onBluetoothListLoadedListener.onLoaded(bluetoothDeviceList);
                }
            }
        });
        searchBluetoothDevicesAsyncTask.execute();
    }
}
