package com.example.lockey;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Set;
import android.view.Menu;
import android.view.MenuItem;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DeviceListActivity extends AppCompatActivity {
    private static final String TAG = "DeviceActivity";
    private String newMACaddress;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_list);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Add button
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MACdialog();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_timeline, menu);
        menu.findItem(R.id.sign_out_button).setTitle("Log Out");
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.sign_out_button:
                Intent gotoDevice = new Intent(DeviceListActivity.this, MainActivity.class);
                startActivity(gotoDevice);
                DeviceListActivity.this.finish();
                finish();
                return true; // true: menu processing done, no further actions
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void AddDeviceToUser(String mac){
        //You guys do your thing here
        Intent intent = getIntent();
        int userid = intent.getIntExtra("userID",0);
        UserInterface usr = ApiUtils.getUserService();

        User x = new User(userid,"no","no",mac);
        Call<User> addDevice = usr.addDeviceToUser(x);
        addDevice.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {

            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {

            }
        });
    }

    //Temporary popup for MAC address input
    private void MACdialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(DeviceListActivity.this);
        builder.setTitle("MAC address").setMessage("Input device's MAC address!");

        // Set up the input
        final EditText input = new EditText(DeviceListActivity.this);
        // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        builder.setView(input);

        // Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                newMACaddress = input.getText().toString();
                AddDeviceToUser(newMACaddress);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    private void BluetoothAdd(){
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (!bluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, 0);
        }
        Set<BluetoothDevice> devices = bluetoothAdapter.getBondedDevices();
        if (devices != null) {
            Boolean found=false;
            for (BluetoothDevice device : devices) {
                if (device.getName().contains("Lockey")) {
                    AddDeviceToUser(device.getAddress());
                    found=true;
                    break;
                }
            }
            if (!found){
                Toast.makeText(DeviceListActivity.this, "Device not found", Toast.LENGTH_SHORT).show();
            }
        }
    }
}