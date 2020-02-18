package com.yba.aurdinoproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.yba.aurdinoproject.Interfaces.OnAurdinoModelStatusChanged;
import com.yba.aurdinoproject.array_adapters.BluetoothListViewAdapter;
import com.yba.aurdinoproject.helper_classes.BluetoothControlHelper;
import com.yba.aurdinoproject.model_classes.AurdinoModel;

public class MainActivity extends AppCompatActivity {

    BluetoothListViewAdapter bluetoothDeviceArrayAdapter;

    Button buttonConnectToDevice;

    Button buttonMoveForward;
    Button buttonMoveBackward;
    Button buttonMoveLeft;
    Button buttonMoveRight;
    Button buttonLiftUp;
    Button buttonLetDown;
    Button buttonExpand;
    Button buttonContract;

    AurdinoModel aurdinoModel;

    private static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Initialization of Objects
        bluetoothDeviceArrayAdapter = new BluetoothListViewAdapter(this, R.layout.bluetooth_list_item);
        buttonConnectToDevice = findViewById(R.id.buttonConnectToBluettoothDevice);
        buttonMoveForward = findViewById(R.id.buttonUp);
        buttonMoveBackward = findViewById(R.id.buttonDown);
        buttonMoveLeft = findViewById(R.id.buttonLeft);
        buttonMoveRight = findViewById(R.id.buttonRight);
        buttonLiftUp = findViewById(R.id.buttonLift);
        buttonLetDown = findViewById(R.id.buttonLetDown);
        buttonExpand = findViewById(R.id.buttonExpand);
        buttonContract = findViewById(R.id.buttonContract);
        aurdinoModel = new AurdinoModel((BluetoothControlHelper) getApplicationContext());

        setOnClickListeners();

    }

    public void setOnClickListeners(){

        buttonConnectToDevice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, BluetoothListActivity.class);
                startActivity(intent);
            }
        });

        buttonMoveForward.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    printStackMessage("Move Forward Pressed.");
                    aurdinoModel.moveForward();
                }else{
                    printStackMessage("Button Release");
                    aurdinoModel.stop();
                }
            }
        });

        buttonMoveBackward.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    printStackMessage("Move Backward Pressed.");
                    aurdinoModel.moveBackward();
                }else{
                    printStackMessage("Button Release");
                    aurdinoModel.stop();
                }
            }
        });

        buttonMoveLeft.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    printStackMessage("Move Left Pressed.");
                    aurdinoModel.moveLeft();
                }else{
                    printStackMessage("Button Release");
                    aurdinoModel.stop();
                }
            }
        });

        buttonMoveRight.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {

                if(hasFocus){
                    printStackMessage("Move Right Pressed.");
                    aurdinoModel.moveRight();
                }else{
                    printStackMessage("Button Release");
                    aurdinoModel.stop();
                }
            }
        });

        buttonLiftUp.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {

                if(hasFocus){
                    printStackMessage("Lift Up Pressed.");
                    aurdinoModel.liftUp();
                }else{
                    printStackMessage("Button Release");
                    aurdinoModel.stop();
                }
            }
        });

        buttonLetDown.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    printStackMessage("Let Down Pressed.");
                    aurdinoModel.letDown();
                }else{
                    printStackMessage("Button Release");
                    aurdinoModel.stop();
                }
            }
        });

        buttonExpand.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {

                if(hasFocus){
                    printStackMessage("Expand Pressed.");
                    aurdinoModel.expand();
                }else{
                    printStackMessage("Button Release");
                    aurdinoModel.stop();
                }
            }
        });

        buttonContract.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    printStackMessage("Contract Pressed.");
                    aurdinoModel.contract();
                }else{
                    printStackMessage("Button Release");
                    aurdinoModel.stop();
                }
            }
        });

        aurdinoModel.setOnAurdinoModelStatusChanged(new OnAurdinoModelStatusChanged() {
            @Override
            public void onStatusChanged(int statusCode) {
                printStackMessage("Aurdino Status Changed");
            }
        });
    }

    public void printStackMessage(String message){
        Log.d(TAG, message);
    }


}
