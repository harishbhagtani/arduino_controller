package com.yba.aurdinoproject.array_adapters;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.yba.aurdinoproject.R;

import java.util.List;

public class BluetoothListViewAdapter extends ArrayAdapter<BluetoothDevice> {

    private int selectedItemIndex;
    private List<BluetoothDevice> bluetoothDeviceList;


    public BluetoothListViewAdapter(@NonNull Context context, int resource) {
        super(context, resource);
        selectedItemIndex = -1;
    }

    public void setBluetoothDeviceList(List<BluetoothDevice> bluetoothDeviceList) {
        this.bluetoothDeviceList = bluetoothDeviceList;
        notifyDataSetChanged();
    }

    public int getSelectedItemIndex() {
        return selectedItemIndex;
    }

    public void setSelectedItemIndex(int selectedItemIndex) {
        this.selectedItemIndex = selectedItemIndex;
        notifyDataSetChanged();
    }

    public void unselectItem(){
        this.selectedItemIndex = -1;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return bluetoothDeviceList.size();
    }

    @Nullable
    @Override
    public BluetoothDevice getItem(int position) {
        return bluetoothDeviceList.get(position);
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View deviceView = convertView;
        if(deviceView == null){
            deviceView = LayoutInflater.from(getContext()).inflate(R.layout.bluetooth_list_item,parent,false);
        }
        TextView textViewDeviceName =  deviceView.findViewById(R.id.textViewDeviceName);
        BluetoothDevice bluetoothDevice = bluetoothDeviceList.get(position);
        textViewDeviceName.setText(bluetoothDevice.getName());
        if(selectedItemIndex != -1 && position == selectedItemIndex){
            deviceView.setBackgroundColor(Color.parseColor("#abcdef"));
        }else{
            deviceView.setBackgroundColor(Color.WHITE);
        }
        return deviceView;
    }
}
