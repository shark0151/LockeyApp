package com.example.lockey;

import androidx.appcompat.app.AppCompatActivity;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.Set;

public class DeviceListActivity extends AppCompatActivity {
    //Bluetooth stuff
    private BluetoothAdapter BA;
    private Set<BluetoothDevice> pairedDevices;
    ListView lv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_list);

        //Bluetooth stuff
        BA = BluetoothAdapter.getDefaultAdapter();

        //Add button
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!BA.isEnabled()) {
                    Intent turnOn = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                    startActivityForResult(turnOn, 0);
                }
                Intent getVisible = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
                startActivityForResult(getVisible, 0);
                pairedDevices = BA.getBondedDevices();
                ArrayList namelist = new ArrayList();
                ArrayList addresslist=new ArrayList();
                for(BluetoothDevice bt : pairedDevices) {
                    namelist.add(bt.getName());
                    addresslist.add(bt.getAddress());
                }
                final ArrayAdapter adapter = new  ArrayAdapter(DeviceListActivity.this, android.R.layout.simple_list_item_1, namelist);
                lv.setAdapter(adapter);
            }
        });
    }
}