package com.example.ianav.roomNo;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ianav.R;

import java.util.ArrayList;

public class RoomDescriptionAdapter  extends RecyclerView.Adapter<RoomDescriptionAdapter.Viewholder> {
    private Context context;
    private static ArrayList<RoomInfo> roomInfoArrayList;
    public  RoomDescriptionInterface roomDescriptionInterface;

    // Constructor
    public RoomDescriptionAdapter(Context context, ArrayList<RoomInfo> courseModelArrayList,RoomDescriptionInterface roomDescriptionInterface) {
        this.context = context;
        this.roomInfoArrayList = courseModelArrayList;
        this.roomDescriptionInterface = roomDescriptionInterface;
    }

    // method for filtering our recyclerview items.
    public void filterList(ArrayList<RoomInfo> filterllist) {
        // below line is to add our filtered
        // list in our course array list.
        roomInfoArrayList = filterllist;
        // below line is to notify our adapter
        // as change in recycler view data.
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RoomDescriptionAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // to inflate the layout for each item of recycler view.
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.device_info, parent, false);
        Viewholder viewholder = new Viewholder(view);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                roomDescriptionInterface.onCardClicked(roomInfoArrayList.get(viewholder.getAdapterPosition()));
            }
        });
        return new Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RoomDescriptionAdapter.Viewholder holder, int position) {
        RoomInfo model = roomInfoArrayList.get(position);
        try{
            byte[] bytes = model.getDevices_img();
            Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
            holder.deviceImg.setImageBitmap(bmp);

        }catch (Exception e){
            e.printStackTrace();
        }
        holder.deviceName.setText(model.getDevices_name());
        Log.d("deviceQty", String.valueOf(model.getDevices_qty()));
        holder.deviceQty.setText(String.valueOf(model.getDevices_qty()));
    }

    @Override
    public int getItemCount() {
        return roomInfoArrayList.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder {
        private ImageView deviceImg;
        private TextView deviceName;
        private TextView deviceQty;



        public Viewholder(@NonNull View itemView) {
            super(itemView);
            deviceImg = itemView.findViewById(R.id.imgDeviceImage);
            deviceName = itemView.findViewById(R.id.tvDeviceName);
            deviceQty = itemView.findViewById(R.id.tvDeviceQty);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    roomDescriptionInterface.onCardClicked(roomInfoArrayList.get(getAdapterPosition()));
                }
            });
        }
    }
}
