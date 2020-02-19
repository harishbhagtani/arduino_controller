package com.yba.aurdinoproject.model_classes;

import android.util.Log;

import com.yba.aurdinoproject.Interfaces.OnAurdinoModelStatusChanged;
import com.yba.aurdinoproject.helper_classes.BluetoothControlHelper;

public class AurdinoModel {

    private static final int CODE_FORWARD = 100;
    private static final int CODE_BACKWARD = 101;
    private static final int CODE_LEFT = 200;
    private static final int CODE_RIGHT = 201;
    private static final int CODE_LIFT_UP = 300;
    private static final int CODE_LET_DOWN = 301;
    private static final int CODE_EXPAND = 400;
    private static final int CODE_CONTRACT = 401;
    private static final int CODE_STOP = 900;

    private static final String TAG = AurdinoModel.class.getSimpleName();


    private int currentStatus;

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
        bluetoothControlHelper.sendData(currentStatus = CODE_EXPAND);
        if(onAurdinoModelStatusChanged != null){
            onAurdinoModelStatusChanged.onStatusChanged(currentStatus);
        }
        printMessage();
    }

    public void contract(){
        bluetoothControlHelper.sendData(currentStatus = CODE_CONTRACT);
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
            case CODE_EXPAND:{
                Log.d(TAG,"Expanding...");
            }
            break;
            case CODE_CONTRACT:{
                Log.d(TAG,"Contacting...");
            }
            break;
            case CODE_STOP:{
                Log.d(TAG,"Stopped.");
            }
            break;
        }
    }

    public void setOnAurdinoModelStatusChanged(OnAurdinoModelStatusChanged onAurdinoModelStatusChanged) {
        this.onAurdinoModelStatusChanged = onAurdinoModelStatusChanged;
    }
}
