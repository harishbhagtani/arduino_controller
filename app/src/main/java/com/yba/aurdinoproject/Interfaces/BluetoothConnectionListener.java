package com.yba.aurdinoproject.Interfaces;

import android.bluetooth.BluetoothSocket;

public interface BluetoothConnectionListener {
    void onConnectionProcessStarted();
    void onConnected(BluetoothSocket bluetoothSocket);
    void onDisconnected();
    void onConnectionFailed();
}
