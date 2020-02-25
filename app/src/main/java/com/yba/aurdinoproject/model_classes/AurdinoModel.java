package com.yba.aurdinoproject.model_classes;

import android.bluetooth.BluetoothSocket;
import android.util.Log;

import com.yba.aurdinoproject.Interfaces.BluetoothConnectionListener;
import com.yba.aurdinoproject.Interfaces.OnAurdinoModelStatusChanged;
import com.yba.aurdinoproject.Interfaces.OnInputRecievedListener;
import com.yba.aurdinoproject.helper_classes.BluetoothControlHelper;

public class AurdinoModel {

    private static final int CODE_FORWARD = 100;
    private static final int CODE_BACKWARD = 101;
    private static final int CODE_LEFT = 200;
    private static final int CODE_RIGHT = 201;
    private static final int CODE_LIFT_UP = 300;
    private static final int CODE_LET_DOWN = 301;
    private static final int CODE_STOP = 900;
    private static final int CODE_EXPAND_SET_1 = 400;
    private static final int CODE_EXPAND_SET_2 = 401;
    private static final int CODE_EXPAND_SET_3 = 402;
    private static final int CODE_CONTRACT_SET_1 = 403;
    private static final int CODE_CONTRACT_SET_2 = 404;
    private static final int CODE_CONTRACT_SET_3 = 405;

    private static final String TAG = AurdinoModel.class.getSimpleName();


    private int currentStatus;
    private int selectedWheelSet;

    private BluetoothControlHelper bluetoothControlHelper;
    private OnAurdinoModelStatusChanged onAurdinoModelStatusChanged;

    public AurdinoModel(BluetoothControlHelper bluetoothControlHelper) {
        currentStatus = CODE_STOP;
        this.bluetoothControlHelper = bluetoothControlHelper;
        Log.d(TAG,"Instance Created");
    }

    public void moveForward(){
        bluetoothControlHelper.sendData(currentStatus = CODE_FORWARD);
        if(onAurdinoModelStatusChanged != null){
            onAurdinoModelStatusChanged.onStatusChanged(currentStatus);
        }
        printMessage();
    }

    public void moveBackward(){
        bluetoothControlHelper.sendData(currentStatus = CODE_BACKWARD);
        if(onAurdinoModelStatusChanged != null){
            onAurdinoModelStatusChanged.onStatusChanged(currentStatus);
        }
        printMessage();
    }

    public void moveLeft(){
        bluetoothControlHelper.sendData(currentStatus = CODE_LEFT);
        if(onAurdinoModelStatusChanged != null){
            onAurdinoModelStatusChanged.onStatusChanged(currentStatus);
        }
        printMessage();
    }

    public void moveRight(){
        bluetoothControlHelper.sendData(currentStatus = CODE_RIGHT);
        if(onAurdinoModelStatusChanged != null){
            onAurdinoModelStatusChanged.onStatusChanged(currentStatus);
        }
        printMessage();
    }

    public void liftUp(){
        bluetoothControlHelper.sendData(currentStatus = CODE_LIFT_UP);
        if(onAurdinoModelStatusChanged != null){
            onAurdinoModelStatusChanged.onStatusChanged(currentStatus);
        }
        printMessage();
    }

    public void letDown(){
        bluetoothControlHelper.sendData(currentStatus = CODE_LET_DOWN);
        if(onAurdinoModelStatusChanged != null){
            onAurdinoModelStatusChanged.onStatusChanged(currentStatus);
        }
        printMessage();
    }

    public void expand(){
        switch (selectedWheelSet){
            case 1:{
                bluetoothControlHelper.sendData(currentStatus = CODE_EXPAND_SET_1);
            }break;
            case 2:{
                bluetoothControlHelper.sendData(currentStatus = CODE_EXPAND_SET_2);
            }break;
            case 3:{
                bluetoothControlHelper.sendData(currentStatus = CODE_EXPAND_SET_3);
            }break;
        }
        if(onAurdinoModelStatusChanged != null){
            onAurdinoModelStatusChanged.onStatusChanged(currentStatus);
        }
        printMessage();
    }

    public void contract(){
        switch (selectedWheelSet){
            case 1:{
                bluetoothControlHelper.sendData(currentStatus = CODE_CONTRACT_SET_1);
            }break;
            case 2:{
                bluetoothControlHelper.sendData(currentStatus = CODE_CONTRACT_SET_2);
            }break;
            case 3:{
                bluetoothControlHelper.sendData(currentStatus = CODE_CONTRACT_SET_3);
            }break;
        }
        if(onAurdinoModelStatusChanged != null){
            onAurdinoModelStatusChanged.onStatusChanged(currentStatus);
        }
        printMessage();
    }

    public void stop(){
        bluetoothControlHelper.sendData(currentStatus = CODE_STOP);
        if(onAurdinoModelStatusChanged != null){
            onAurdinoModelStatusChanged.onStatusChanged(currentStatus);
        }
        printMessage();
    }

    public boolean isConnectedToAurdino(){
        return bluetoothControlHelper.isConnectedToArduino();
    }

    private void printMessage(){
        switch (currentStatus){
            case CODE_FORWARD:{
                Log.d(TAG,"Moving Forward...");
            }
            break;
            case CODE_BACKWARD:{
                Log.d(TAG,"Moving Backward...");
            }
            break;
            case CODE_LEFT:{
                Log.d(TAG,"Moving Left...");
            }
            break;
            case CODE_RIGHT:{
                Log.d(TAG,"Moving Right...");
            }
            break;
            case CODE_LIFT_UP:{
                Log.d(TAG,"Lifting Up...");
            }
            break;
            case CODE_LET_DOWN:{
                Log.d(TAG,"Letting Down...");
            }
            break;
            case CODE_EXPAND_SET_1:
            case CODE_EXPAND_SET_2:
            case CODE_EXPAND_SET_3: {
                Log.d(TAG,"Expanding wheel set " + selectedWheelSet);
            }
            break;
            case CODE_CONTRACT_SET_1:
            case CODE_CONTRACT_SET_2:
            case CODE_CONTRACT_SET_3:{
                Log.d(TAG,"Contracting wheel set " + selectedWheelSet);
            }
            break;
            case CODE_STOP:{
                Log.d(TAG,"Stopped.");
            }
            break;
        }
    }

    public void startListeningForInput(final OnInputRecievedListener onInputRecievedListener){
        bluetoothControlHelper.startListeningForInput(new OnInputRecievedListener() {
            @Override
            public void onInputReceived(int input) {
                onInputRecievedListener.onInputReceived(input);
                Log.e(TAG,"Input received : " + input);
            }
        });
    }

    public void disconnectFronAurdino(){
        bluetoothControlHelper.disconnectFromDevice(new BluetoothConnectionListener() {
            @Override
            public void onConnectionProcessStarted() {

            }

            @Override
            public void onConnected(BluetoothSocket bluetoothSocket) {

            }

            @Override
            public void onDisconnected() {
                Log.v(TAG,"Disconnected Successfully");
            }

            @Override
            public void onConnectionFailed() {

            }
        });
    }

    public void stopListeningForInput(){
        bluetoothControlHelper.stopListeningForInput();
    }

    public void setOnAurdinoModelStatusChanged(OnAurdinoModelStatusChanged onAurdinoModelStatusChanged) {
        this.onAurdinoModelStatusChanged = onAurdinoModelStatusChanged;
    }

    public void setSelectedWheelSet(int selectedWheelSet) {
        this.selectedWheelSet = selectedWheelSet;
    }

    public int getSelectedWheelSet() {
        return selectedWheelSet;
    }
}
