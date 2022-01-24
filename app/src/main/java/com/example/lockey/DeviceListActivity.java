package com.example.lockey;

import static com.example.lockey.App.channel_Id;

import android.app.Notification;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Canvas;
import android.net.wifi.WifiInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewDebug;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DeviceListActivity extends AppCompatActivity {
    //Don't worry about these! Just Bluetooth crap
    final byte delimiter = 33;
    int readBufferPosition = 0;
    final UUID uuid = UUID.fromString("815425a5-bfac-47bf-9321-c5ff980b5e11");
    BluetoothSocket mmSocket;
    BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    BluetoothDevice mDevice;
    WifiInfo wifiInfo;
    String ssid;
    String psk;
    private NotificationManagerCompat manager;
    private static final int PERIOD=5000;
    List<Device> listOld= new ArrayList<>();
    private static final String TAG = "DeviceActivity";
    private String newMACaddress;
    private RecyclerViewSimpleAdapter lockeyAdapter;
    List<Device> list= new ArrayList<>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_list);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        BluetoothAdd();

        //Add button
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MACdialog();
            }
        });
        manager=NotificationManagerCompat.from(this);
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                getAndShowDevices();
                list.clear();
            }
        }, 0, 10000);//wait 0 ms before doing the action and do it every 10000ms (10second)

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
    class Runnable implements java.lang.Runnable {

        @Override
        public void run() {
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    private void AddDeviceToUser(String mac) {
        //You guys do your thing here
        Intent intent = getIntent();
        int userid = intent.getIntExtra("userID", 0);
        UserInterface usr = ApiUtils.getUserService();
        User x = new User(userid, "no", "no", mac);
        Call<User> addDevice = usr.addDeviceToUser(x);
        addDevice.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if(response.isSuccessful()) {
                    Log.w(TAG, "Succesfully added");
                    list.clear();
                    getAndShowDevices();
                }
                else
                {
                    String message = "Problem " + response.code() + " " + response.message();
                    Log.d(TAG, message);
                }

            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.e("ADDdevicetouser", t.getMessage());
            }
        });
    }
    public void getAndShowDevices() {
        UserInterface mess = ApiUtils.getUserService();
        Intent intent = getIntent();
        int userid = intent.getIntExtra("userID", 0);
        Call<List<String>> getAllDevicesCall = mess.getDevicesForUser(userid);
        getAllDevicesCall.enqueue(new Callback<List<String>>() {
            @Override
            public void onResponse(Call<List<String>> call, Response<List<String>> response) {
                if (response.isSuccessful()) {
                    List<String> allDevicesString = response.body();
                    getallDevices(allDevicesString);
                    Log.d("getAndShowDevices", allDevicesString.toString());
                } else {
                    String message = "Problem " + response.code() + " " + response.message();
                    Log.d("getAndShowDevices", message);
                }
            }

            @Override
            public void onFailure(Call<List<String>> call, Throwable t) {
                Log.e("getAndShowDevices", t.getMessage());
            }
        });
    }

    private void getallDevices(List<String> allDevicesString)
    {
        DeviceInterface mess = ApiUtils.getDeviceService();
        for (String d:allDevicesString) {
            Call<List<Device>> getDevice = mess.getAllValuesForDevice(d);
            getDevice.enqueue(new Callback<List<Device>>() {
                @Override
                public void onResponse(Call<List<Device>> call, Response<List<Device>> response) {
                    if (response.isSuccessful()) {
                        List<Device> allDeviceReadings = response.body();
                        if(!allDeviceReadings.isEmpty()) {
                            Log.d("Notif", listOld.toString());
                            for (Device d:
                                    listOld) {
                                if (allDeviceReadings.get(allDeviceReadings.size() - 1).getId().equals(d.getId()) && !allDeviceReadings.get(allDeviceReadings.size() - 1).getTime().equals(d.getTime()) && allDeviceReadings.get(allDeviceReadings.size()-1).getIsLocked().equals(false))
                                {
                                    Notification(d.getId());
                                    listOld.clear();
                                    break;
                                }
                            }
                            list.add(allDeviceReadings.get(allDeviceReadings.size()-1));

                        }
                        Log.d("getallDevices", allDeviceReadings.toString());
                        populateRecyclerView();
                    } else {
                        String message = "Problem " + response.code() + " " + response.message();
                        Log.d("getallDevices", message);
                    }
                }

                @Override
                public void onFailure(Call<List<Device>> call, Throwable t) {
                    Log.e("getallDevices", t.getMessage());
                }
            });
        }


    }
    private void Notification(String id){
        Notification notification = new Notification.Builder(this, channel_Id)
                .setSmallIcon(R.drawable.ic_lock)
                .setContentTitle("Unlocked")
                .setContentText("Device " +id + " is unlocked")
                .build();
        manager.notify(1, notification);
    }
    private void populateRecyclerView() {
        List<Device> allDevices = list;
        listOld.addAll(list);
        RecyclerView recyclerView = findViewById(R.id.mainRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        lockeyAdapter = new RecyclerViewSimpleAdapter<>(allDevices);
        recyclerView.setAdapter(lockeyAdapter);
        lockeyAdapter.setOnItemClickListener((view, position, item) -> {
            Device msg = (Device) item;
            Log.d("device", item.toString());
            //DeviceInterface mess = ((RecyclerViewSimpleAdapter) recyclerView.getAdapter()).getItem(position);
            //Intent GoToPost = new Intent(this, TwisterActivity.class);
            //GoToPost.putExtra("Message", (Serializable) mess);
            //startActivity(GoToPost);
        });

        ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                // Row is swiped from recycler view
                final int position = viewHolder.getAdapterPosition();
                if (position >= 0) {
                    String id = ((RecyclerViewSimpleAdapter) recyclerView.getAdapter()).getItem(position).getId();
                    Intent intent = getIntent();
                    int userid = intent.getIntExtra("userID", 0);
                    deleteDeviceForUser(userid, id);
                }
                //list.clear();
                list.remove(((RecyclerViewSimpleAdapter) recyclerView.getAdapter()).getItem(position));
                lockeyAdapter.notifyItemRemoved(position);

            }


            @Override
            public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                // view the background view

                final int position = viewHolder.getAdapterPosition();
                if (position >= 0) {

                        new RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                                .addSwipeLeftBackgroundColor(ContextCompat.getColor(DeviceListActivity.this, R.color.design_default_color_error))
                                .addSwipeLeftActionIcon(R.drawable.ic_delete)
                                .create()
                                .decorate();
                        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
                    }

                }

        };
        // attaching the touch helper to recycler view
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(recyclerView);
    }
    private void deleteDeviceForUser(int userid, String deviceId) {
        UserInterface mess = ApiUtils.getUserService();
        User tbd= new User(userid, null, null, deviceId);
        Call<User> deleteDevice = mess.removeDeviceToUser(tbd);
        deleteDevice.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()) {
                    getAndShowDevices();
                    Log.d("device", "Deleted ");
                } else {
                    String message = "Problem " + response.code() + " " + response.message();
                    Log.d("device", message);
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.e("device", t.getMessage());
            }
        });
    }

    //Temporary popup for MAC address input
    private void MACdialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(DeviceListActivity.this);
        builder.setTitle("MAC address").setMessage("Input device's MAC address!");

        // Set up the input
        final EditText input = new EditText(DeviceListActivity.this);
        // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_CLASS_TEXT);
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

    private void BluetoothAdd() {

        if (!bluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, 0);
        }
        Set<BluetoothDevice> devices = bluetoothAdapter.getBondedDevices();
        if (devices != null) {
            Boolean found = false;
            for (BluetoothDevice device : devices) {
                if (device.getName().contains("Lockey")) {
                    mDevice = device;
                    found = true;
                    ssid = wifiInfo.getSSID();
                    //Inflater crap
                    RelativeLayout parent = findViewById(R.id.deviceList);        //parent layout.
                    View view = LayoutInflater.from(DeviceListActivity.this).inflate(R.layout.wifi_setup, parent, false);
                    RelativeLayout item = view.findViewById(R.id.wifiSetup);       //initialize layout & By this you can also perform any event.
                    TextView ssidText = findViewById(R.id.ssidText);
                    ssidText.setText(ssidText.getText() + wifiInfo.getSSID());
                    parent.addView(view);             //adding your inflated layout in parent layout.
                    Button okButton = findViewById(R.id.okButton);
                    Button cancelButton = findViewById(R.id.cancelButton);
                    okButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            EditText mpsk = (EditText) findViewById(R.id.passwordInput);
                            psk = mpsk.getText().toString();
                            try {
                                mmSocket = mDevice.createRfcommSocketToServiceRecord(uuid);
                                if (!mmSocket.isConnected()) {
                                    mmSocket.connect();
                                    Thread.sleep(1000);
                                }
                                OutputStream mmOutputStream = mmSocket.getOutputStream();
                                final InputStream mmInputStream = mmSocket.getInputStream();
                                waitForResponse(mmInputStream, -1);
                                mmOutputStream.write(ssid.getBytes());
                                mmOutputStream.flush();
                                waitForResponse(mmInputStream, -1);
                                mmOutputStream.write(psk.getBytes());
                                mmOutputStream.flush();
                                waitForResponse(mmInputStream, -1);
                                mmSocket.close();
                                AddDeviceToUser(device.getAddress());
                            } catch (Exception e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                            parent.removeView(view);
                        }
                    });
                    cancelButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            parent.removeView(view);
                        }
                    });
                    break;
                }
            }
            if (!found) {
                Toast.makeText(DeviceListActivity.this, "Device not found", Toast.LENGTH_SHORT).show();
            }
        }


    }

    private void waitForResponse(InputStream mmInputStream, long timeout) throws IOException {
        int bytesAvailable;

        while (true) {
            bytesAvailable = mmInputStream.available();
            if (bytesAvailable > 0) {
                byte[] packetBytes = new byte[bytesAvailable];
                byte[] readBuffer = new byte[1024];
                mmInputStream.read(packetBytes);

                for (int i = 0; i < bytesAvailable; i++) {
                    byte b = packetBytes[i];

                    if (b == delimiter) {
                        byte[] encodedBytes = new byte[readBufferPosition];
                        System.arraycopy(readBuffer, 0, encodedBytes, 0, encodedBytes.length);
                        final String data = new String(encodedBytes, "US-ASCII");
                        return;
                    } else {
                        readBuffer[readBufferPosition++] = b;
                    }
                }
            }
        }
    }

}