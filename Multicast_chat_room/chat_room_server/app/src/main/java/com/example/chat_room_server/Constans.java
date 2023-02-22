package com.example.chat_room_server;

public class Constans {
    public static int PORT = 9090;
    public static int TCP_PORT = 50000;
    public static String INFO="";
    public static int CMD_BROAD = 0x1001;
    public static int CMD_BRO_RESPONSE = 0x1001;

    public static void setINFO(String str){
        INFO+=str;
    }
}
