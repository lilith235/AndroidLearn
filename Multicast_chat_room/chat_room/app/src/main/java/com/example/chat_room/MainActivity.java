package com.example.chat_room;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        if (android.os.Build.VERSION.SDK_INT > 9) {
//            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
//            StrictMode.setThreadPolicy(policy);
//        }
//
//        DeviceInfo device = UdpSearch.start(1);
//        //System.out.println("就离谱，你给我打哪去了 ");
//        //System.out.println("device: "+device.toString());
//        //开始于 tcp 服务端通信
//        if (device != null) {
//            TcpClient.bindWith(device);
//        }

    }

    public void onClick_start(View view){

        Intent intent=new Intent(MainActivity.this,MainMenu.class);
//        finish();
        startActivity(intent);

    }

    public void onClick_quit(View view){
        android.os.Process.killProcess(android.os.Process.myPid()); //获取PID
        System.exit(0);   //常规java、c#的标准退出法，返回值为0代表正常退出
    }

}
