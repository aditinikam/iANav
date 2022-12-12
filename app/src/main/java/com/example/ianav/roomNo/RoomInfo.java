package com.example.ianav.roomNo;

public class RoomInfo {
    private String room_no;
    private String room_type;
    private String devices_name;
    private String devices_desc;
    private int devices_qty;
    private byte[] devices_img;

    public RoomInfo() {
    }

    public RoomInfo(String room_no, String room_type, String devices_name, String devices_desc, int devices_qty, byte[] devices_img) {
        this.room_no = room_no;
        this.room_type = room_type;
        this.devices_name = devices_name;
        this.devices_desc = devices_desc;
        this.devices_qty = devices_qty;
        this.devices_img = devices_img;
    }

    public RoomInfo(String devices_name, byte[] devices_img) {
        this.devices_name = devices_name;
        this.devices_img = devices_img;
    }

    public RoomInfo(String devices_name, int devices_qty, byte[] devices_img) {
        this.devices_name = devices_name;
        this.devices_qty = devices_qty;
        this.devices_img = devices_img;
    }

    public RoomInfo(String room_no, String room_type) {
        this.room_no = room_no;
        this.room_type = room_type;
    }

    public String getRoom_no() {
        return room_no;
    }

    public void setRoom_no(String room_no) {
        this.room_no = room_no;
    }

    public String getRoom_type() {
        return room_type;
    }

    public void setRoom_type(String room_type) {
        this.room_type = room_type;
    }

    public String getDevices_name() {
        return devices_name;
    }

    public void setDevices_name(String devices_name) {
        this.devices_name = devices_name;
    }

    public String getDevices_desc() {
        return devices_desc;
    }

    public void setDevices_desc(String devices_desc) {
        this.devices_desc = devices_desc;
    }

    public int getDevices_qty() {
        return devices_qty;
    }

    public void setDevices_qty(int devices_qty) {
        this.devices_qty = devices_qty;
    }

    public byte[] getDevices_img() {
        return devices_img;
    }

    public void setDevices_img(byte[] devices_img) {
        this.devices_img = devices_img;
    }
}
