package com.example.ianav;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ianav.roomNo.RoomDescriptionAdapter;
import com.example.ianav.roomNo.RoomDescriptionInterface;
import com.example.ianav.roomNo.RoomInfo;

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

public class RoomDescription extends AppCompatActivity implements RoomDescriptionInterface {

    private RecyclerView recyclerView;
    private ArrayList<RoomInfo> roomInfoArrayList;
    private ProgressBar progressBar;
    private RoomDescriptionAdapter roomDescriptionAdapter;
    private TextView labNo;
    String lab_no;
    String lab_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room_description);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            lab_no = extras.getString("Lab No");
            lab_name = extras.getString("Lab Name");
//            Toast.makeText(this, lab_name, Toast.LENGTH_SHORT).show();
        }
        recyclerView = findViewById(R.id.rv_device);
        progressBar = findViewById(R.id.progressBarMon);
        labNo = findViewById(R.id.tv_room_no);
        labNo.setText("Lab No: " + lab_name);
        roomInfoArrayList = new ArrayList<>();
        fetchData();
        roomDescriptionAdapter = new RoomDescriptionAdapter(RoomDescription.this, roomInfoArrayList, this) {

        };
        recyclerView.setAdapter(roomDescriptionAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(RoomDescription.this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
    }

    private void fetchData() {

        try{
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(getString(R.string.api)+"api/v1/rooms/"+lab_no)
                    .get()
                    .build();

            Log.d("Before Response",request.toString());

            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    //wait = false;

                    progressBar.setVisibility(View.GONE);
                    e.printStackTrace();
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    try {
                        JSONObject jsonObject = new JSONObject(response.body().string());
                        //JSONArray jsonArray = jsonObject.getJSONArray();
//                        JSONArray jsonArray = new JSONArray(response.body().string());
                        Log.d("jsonarray",""+jsonObject);
                        Iterator<String> keys = jsonObject.keys();
                        while (keys.hasNext()){
                            String key = keys.next();
                            Log.d(""+key,""+jsonObject.get(key));
                            int no= (int) jsonObject.get(key);
                            byte[] img={};
                            RoomInfo obj = new RoomInfo(key,no,img);
                            roomInfoArrayList.add(obj);
                        }
                        RoomDescription.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                progressBar.setVisibility(View.GONE);
                                roomDescriptionAdapter.notifyDataSetChanged();
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

            progressBar.setVisibility(View.GONE);
            e.printStackTrace();
        }
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_item, menu);

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
        ArrayList<RoomInfo> filteredlist = new ArrayList<>();

        // running a for loop to compare elements.
        for (RoomInfo item : roomInfoArrayList) {
            // checking if the entered string matched with any item of our recycler view.
            if (item.getDevices_name().toLowerCase().contains(text.toLowerCase())) {
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
            roomDescriptionAdapter.filterList(filteredlist);
        }
    }

    @Override
    public void onCardClicked(RoomInfo mInfo) {
        Toast.makeText(getApplicationContext(), "Device clicked:" + mInfo.getDevices_name(), Toast.LENGTH_SHORT).show();
        Intent i = new Intent(RoomDescription.this, DeviceDesc.class);
        String s = String.valueOf(mInfo.getDevices_qty());
        i.putExtra("Device_id", mInfo.getDevices_name());
        i.putExtra("Device_qty",s);
        startActivity(i);
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }

}