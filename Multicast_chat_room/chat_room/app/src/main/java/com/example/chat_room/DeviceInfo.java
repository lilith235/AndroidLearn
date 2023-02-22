package com.example.chat_room;

public class DeviceInfo {
    public String ip;
    public int port;
    public String data;

    public DeviceInfo(String ip, int port, String data) {
        this.ip = ip;
        this.port = port;
        this.data = data;
    }

    @Override
    public String toString() {
        return "Device{" +
                "ip='" + ip + '\'' +
                ", port=" + port +
                ", data='" + data + '\'' +
                '}'+ "\n";
    }
}
