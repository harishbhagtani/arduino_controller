package com.yba.aurdinoproject;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.yba.aurdinoproject.Interfaces.OnAurdinoModelStatusChanged;
import com.yba.aurdinoproject.Interfaces.OnInputRecievedListener;
import com.yba.aurdinoproject.helper_classes.BluetoothControlHelper;
import com.yba.aurdinoproject.model_classes.AurdinoModel;

public class MainActivity extends AppCompatActivity {

    Button buttonConnectToDevice;
    Button buttonMoveForward;
    Button buttonMoveBackward;
    Button buttonMoveLeft;
    Button buttonMoveRight;
    Button buttonLiftUp;
    Button buttonLetDown;
    Button buttonExpand;
    Button buttonContract;

    TextView textViewDisplayMessage;
    TextView textViewConnectionStatus;

    RadioGroup radioGroup;

    AurdinoModel aurdinoModel;

    private static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Initialization of Objects

        buttonConnectToDevice = findViewById(R.id.buttonConnectToBluettoothDevice);
        buttonMoveForward = findViewById(R.id.buttonUp);
        buttonMoveBackward = findViewById(R.id.buttonDown);
        buttonMoveLeft = findViewById(R.id.buttonLeft);
        buttonMoveRight = findViewById(R.id.buttonRight);
        buttonLiftUp = findViewById(R.id.buttonLift);
        buttonLetDown = findViewById(R.id.buttonLetDown);
        buttonExpand = findViewById(R.id.buttonExpand);
        buttonContract = findViewById(R.id.buttonContract);
        textViewDisplayMessage = findViewById(R.id.textViewDisplayMessage);
        textViewConnectionStatus = findViewById(R.id.textViewConnectionStatus);
        radioGroup = findViewById(R.id.radioGroupWheelSet);
        aurdinoModel = new AurdinoModel((BluetoothControlHelper) getApplicationContext());

        setOnClickListeners();
        aurdinoModel.setSelectedWheelSet(1);

    }

    @SuppressLint("ClickableViewAccessibility")
    public void setOnClickListeners(){

        buttonConnectToDevice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, BluetoothListActivity.class);
                startActivity(intent);
            }
        });

        buttonMoveForward.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    printStackMessage("Move Forward Pressed.");
                    aurdinoModel.moveForward();
                }else if(event.getAction() == MotionEvent.ACTION_UP){
                    printStackMessage("Button Released");
                    aurdinoModel.stop();
                }
                return false;
            }

        });

        buttonMoveBackward.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    printStackMessage("Move Backward Pressed.");
                    aurdinoModel.moveBackward();
                }else if(event.getAction() == MotionEvent.ACTION_UP){
                    printStackMessage("Button Released");
                    aurdinoModel.stop();
                }
                return false;
            }

        });

        buttonMoveLeft.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    printStackMessage("Move Left Pressed.");
                    aurdinoModel.moveLeft();
                }else if(event.getAction() == MotionEvent.ACTION_UP){
                    printStackMessage("Button Released");
                    aurdinoModel.stop();
                }
                return false;
            }

        });

        buttonMoveRight.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    printStackMessage("Move Right Pressed.");
                    aurdinoModel.moveRight();
                }else if(event.getAction() == MotionEvent.ACTION_UP){
                    printStackMessage("Button Released");
                    aurdinoModel.stop();
                }
                return false;
            }

        });

        buttonLiftUp.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    printStackMessage("Lift Up Pressed.");
                    aurdinoModel.liftUp();
                }else if(event.getAction() == MotionEvent.ACTION_UP){
                    printStackMessage("Button Released");
                    aurdinoModel.stop();
                }
                return false;
            }

        });

        buttonLetDown.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    printStackMessage("Let Down Pressed.");
                    aurdinoModel.letDown();
                }else if(event.getAction() == MotionEvent.ACTION_UP){
                    printStackMessage("Button Released");
                    aurdinoModel.stop();
                }
                return false;
            }
        });

        buttonExpand.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    printStackMessage("Expand Wheel Set "+ aurdinoModel.getSelectedWheelSet() + " Pressed.");
                    aurdinoModel.expand();
                }else if(event.getAction() == MotionEvent.ACTION_UP){
                    printStackMessage("Button Released");
                    aurdinoModel.stop();
                }
                return false;
            }
        });

        buttonContract.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    printStackMessage("Contract Wheel Set " + aurdinoModel.getSelectedWheelSet() + " Pressed.");
                    aurdinoModel.contract();
                }else if(event.getAction() == MotionEvent.ACTION_UP){
                    printStackMessage("Button Released");
                    aurdinoModel.stop();
                }
                return false;
            }

        });

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.radioButtonWheelSet1:{
                        aurdinoModel.setSelectedWheelSet(1);
                    }break;
                    case R.id.radioButtonWheelSet2:{
                        aurdinoModel.setSelectedWheelSet(2);
                    }break;
                    case R.id.radioButtonWheelSet3:{
                        aurdinoModel.setSelectedWheelSet(3);
                    }break;
                }

                Log.d(TAG,"Wheel set " + aurdinoModel.getSelectedWheelSet() + " selected");

            }
        });

        aurdinoModel.setOnAurdinoModelStatusChanged(new OnAurdinoModelStatusChanged() {
            @Override
            public void onStatusChanged(int statusCode) {
                //printStackMessage("Aurdino Status Changed");
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(aurdinoModel.isConnectedToAurdino()){
            textViewConnectionStatus.setBackgroundColor(getResources().getColor(R.color.green));
            textViewConnectionStatus.setText("Connected");
            buttonConnectToDevice.setText("Disconnect and Exit");
            buttonConnectToDevice.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    aurdinoModel.disconnectFronAurdino();
                    finish();
                }
            });
            buttonConnectToDevice.setBackgroundColor(getResources().getColor(R.color.red));
//            aurdinoModel.startListeningForInput(new OnInputRecievedListener() {
//                @Override
//                public void onInputReceived(int input) {
//                    printStackMessage("Input Received : " + input);
//                }
//            });
        }else{
            buttonConnectToDevice.setText("CONNECT TO A DEVICE");
            buttonConnectToDevice.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(MainActivity.this, BluetoothListActivity.class);
                    startActivity(intent);
                }
            });
            buttonConnectToDevice.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            textViewConnectionStatus.setBackgroundColor(getResources().getColor(R.color.red));
            textViewConnectionStatus.setText("Not Connected");
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        aurdinoModel.stopListeningForInput();
    }

    public void printStackMessage(String message){
        textViewDisplayMessage.setText(message);
        Log.d(TAG, message);
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        aurdinoModel.disconnectFronAurdino();
        super.onDestroy();
    }
}
