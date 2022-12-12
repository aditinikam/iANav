package com.example.ianav.labNo;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.ianav.R;
import com.example.ianav.RoomDescription;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity implements MyRecyclerViewAdapter.ItemClickListener {

    private static final int REQUEST_ENABLE_BT = -1;
    MyRecyclerViewAdapter adapter;
    FloatingActionButton floatingActionButton;
    ArrayList<String> labNo;
    ArrayList<String> labId;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        floatingActionButton = findViewById(R.id.floatingActionButton);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent launchIntent = getPackageManager().getLaunchIntentForPackage("com.DefaultCompany.Navigation");
                if (launchIntent != null) {
                    startActivity(launchIntent);
                } else {
                    Toast.makeText(MainActivity.this, "There is no package available in android", Toast.LENGTH_LONG).show();
                }
            }
        });
        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH)) {
            Toast.makeText(this, "bluetooth_not_supported", Toast.LENGTH_SHORT).show();
            finish();
        }
// Use this check to determine whether BLE is supported on the device. Then
// you can selectively disable BLE-related features.
        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            Toast.makeText(this, "ble_not_supported", Toast.LENGTH_SHORT).show();
            finish();
        }
        BluetoothManager bluetoothManager = getSystemService(BluetoothManager.class);
        BluetoothAdapter bluetoothAdapter = bluetoothManager.getAdapter();
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.BLUETOOTH_CONNECT) == PackageManager.PERMISSION_DENIED)
        {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S)
            {
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.BLUETOOTH_CONNECT}, 2);
            }
        }
        if (!bluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
//                return;
            }
//            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        }
        labNo = new ArrayList<>();
        labId = new ArrayList<>();
//        labNo.add("B001");
//        labNo.add("B002");
//        labNo.add("B003");
//        labNo.add("A001");
//        labNo.add("A002");
//        labNo.add("A003");
//        labNo.add("B001");
//        labNo.add("B002");
//        labNo.add("B003");
//        labNo.add("A001");
//        labNo.add("A002");
//        labNo.add("A003");

        fetchData();
        // set up the RecyclerView
        RecyclerView recyclerView = findViewById(R.id.rvLabNo);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new MyRecyclerViewAdapter(this, labNo);
        adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);
    }


    private void fetchData() {
        try{
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(getString(R.string.api)+"api/v1/rooms")
                    .get()
                    .build();

            Log.d("Before Response",request.toString());

            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    //wait = false;

                    //progressBar.setVisibility(View.GONE);
                    e.printStackTrace();
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    try {
                        JSONObject jsonObject = new JSONObject(response.body().string());
                        Iterator<String> keys = jsonObject.keys();

                        while(keys.hasNext()) {
                            String key = keys.next();
                            if (jsonObject.get(key) instanceof JSONObject) {
                                JSONObject  keyvalue = (JSONObject) jsonObject.get(key);
                                Log.d("key: "+ key , " value: "+ keyvalue);
                                Iterator<String> names = keyvalue.keys();
                                while (names.hasNext()){
                                    String name=names.next();
                                    Log.d(""+name,""+keyvalue.get(name));
                                    if(name.equalsIgnoreCase("name")) {
                                        Log.d(""+name,""+keyvalue.get(name));
                                        labNo.add((String) keyvalue.get(name));
                                        labId.add(key);
                                        Log.d("labNo",labNo.toString());
                                    }
                                }
                            }
                        }
                        MainActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
//                                progressBar.setVisibility(View.GONE);
                                Log.d("name", String.valueOf(labNo));
                                Log.d("labId",labId.toString());
                                adapter.notifyDataSetChanged();
                            }
                        });
                        //wait = false;


                    } catch (JSONException e) {

                        //progressBar.setVisibility(View.GONE);
                        //wait = false;
                        e.printStackTrace();
                    }


                }
            });

        }catch (Exception e){
            //wait = false;

//            progressBar.setVisibility(View.GONE);
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_item,menu);

        // below line is to get our inflater
        //MenuInflater inflater = getMenuInflater();

        // inside inflater we are inflating our menu file.
        // inflater.inflate(R.menu.menu_item, menu);

        // below line is to get our menu item.
        MenuItem searchItem = menu.findItem(R.id.actionSearch);

        // getting search view of our item.
        SearchView searchView = (SearchView) searchItem.getActionView();

        // below line is to call set on query text listener method.
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // inside on query text change method we are
                // calling a method to filter our recycler view.
                filter(newText);
                return false;
            }
        });

        return true;
    }

    private void filter(String text) {
        // creating a new array list to filter our data.
        ArrayList<String> filteredlist = new ArrayList<>();

        // running a for loop to compare elements.
        for (String item : labNo) {
            // checking if the entered string matched with any item of our recycler view.
            if (item.toLowerCase().contains(text.toLowerCase())) {
                // if the item is matched we are
                // adding it to our filtered list.
                filteredlist.add(item);
            }
        }
        if (filteredlist.isEmpty()) {
            // if no item is added in filtered list we are
            // displaying a toast message as no data found.
            Toast.makeText(this, "No Data Found..", Toast.LENGTH_SHORT).show();
        } else {
            // at last we are passing that filtered
            // list to our adapter class.
            adapter.filterList(filteredlist);
        }
    }

    @Override
    public void onItemClick(View view, int position) {
        Intent intent=new Intent(MainActivity.this, RoomDescription.class);
        intent.putExtra("Lab No",labId.get(position));
        intent.putExtra("Lab Name",adapter.getItem(position));
        startActivity(intent);
        Toast.makeText(this, "You clicked " + adapter.getItem(position) + " on row number " + labId.get(position), Toast.LENGTH_SHORT).show();
    }
}