package com.example.lockey;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.companion.AssociationRequest;
import android.companion.BluetoothDeviceFilter;
import android.companion.CompanionDeviceManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;
import android.os.ParcelUuid;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.Set;
import java.util.UUID;
import java.util.regex.Pattern;

public class DeviceListActivity extends AppCompatActivity {
    //Bluetooth stuff
    /*private BluetoothAdapter BA;
    private Set<BluetoothDevice> pairedDevices;
    ListView lv;*/
    private static final int SELECT_DEVICE_REQUEST_CODE = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_list);


        /*//Bluetooth stuff
        BA = BluetoothAdapter.getDefaultAdapter();*/

        //Add button
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*if (!BA.isEnabled()) {
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
                lv.setAdapter(adapter);*/
                CompanionDeviceManager deviceManager =
                        (CompanionDeviceManager) getSystemService(
                                Context.COMPANION_DEVICE_SERVICE
                        );

                // To skip filtering based on name and supported feature flags,
                // don't include calls to setNamePattern() and addServiceUuid(),
                // respectively. This example uses Bluetooth.
                BluetoothDeviceFilter deviceFilter =
                        new BluetoothDeviceFilter.Builder()
                                .setNamePattern(Pattern.compile(""))
                                .addServiceUuid(
                                        new ParcelUuid(new UUID(0x123abcL, -1L)), null
                                )
                                .build();

                // The argument provided in setSingleDevice() determines whether a single
                // device name or a list of device names is presented to the user as
                // pairing options.
                AssociationRequest pairingRequest = new AssociationRequest.Builder()
                        .addDeviceFilter(deviceFilter)
                        .setSingleDevice(true)
                        .build();

                // When the app tries to pair with the Bluetooth device, show the
                // appropriate pairing request dialog to the user.
                deviceManager.associate(pairingRequest,
                        new CompanionDeviceManager.Callback() {
                            @Override
                            public void onDeviceFound(IntentSender chooserLauncher) {
                                try {
                                    startIntentSenderForResult(chooserLauncher,
                                            SELECT_DEVICE_REQUEST_CODE, null, 0, 0, 0);
                                } catch (IntentSender.SendIntentException e) {
                                    // failed to send the intent
                                }
                            }

                            @Override
                            public void onFailure(CharSequence error) {
                                // handle failure to find the companion device
                            }
                        }, null);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == SELECT_DEVICE_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK && data != null) {
                BluetoothDevice deviceToPair = data.getParcelableExtra(
                        CompanionDeviceManager.EXTRA_DEVICE
                );

                if (deviceToPair != null) {
                    deviceToPair.createBond();
                    // ... Continue interacting with the paired device.
                }
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}