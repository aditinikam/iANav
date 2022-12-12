package com.example.ianav;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ianav.labNo.MainActivity;
import com.example.ianav.roomNo.RoomInfo;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Iterator;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class DeviceDesc extends AppCompatActivity {

    String device_id;
    TextView device_name;
    TextView device_desc;
    String deviceQty;
    TextView device_qty;
    String deviceDesc;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_desc);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            device_id = extras.getString("Device_id");
            deviceQty = extras.getString("Device_qty");
//            Toast.makeText(this, device_id, Toast.LENGTH_SHORT).show();
        }
        device_name=findViewById(R.id.tvDeviceName);
        device_desc=findViewById(R.id.tvDeviceDesc);
        device_qty=findViewById(R.id.tvDeviceQty);
        device_qty.setText(deviceQty);
        device_name.setText(device_id);
        fetchData();
    }

    private void fetchData() {
        try{
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(getString(R.string.api)+"api/v1/devices/"+device_id)
                    .get()
                    .build();

            Log.d("Before Response device",request.toString());

            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    //wait = false;
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
                            if(key.equalsIgnoreCase("about"))
                                deviceDesc = (String) jsonObject.get(key);
                        }
                        DeviceDesc.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                device_desc.setText(deviceDesc);
                            }
                        });


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                }
            });

        }catch (Exception e){
            e.printStackTrace();
        }
    }
}