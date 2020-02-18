package com.yba.aurdinoproject.Interfaces;


import android.bluetooth.BluetoothDevice;


import java.util.List;

public interface OnBluetoothListLoadedListener {
    void onLoaded(List<BluetoothDevice> bluetoothDeviceList);
}
