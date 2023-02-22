package com.example.chat_room_server;

import android.os.Bundle;
import android.os.StrictMode;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class MainMenu extends AppCompatActivity {
    private  View view;
    private static TextView res;
    private static boolean conti = true;
    private static boolean readIt = false;
    private static EditText key;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

//        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
//                .detectDiskReads().detectDiskWrites().detectNetwork()
//                .penaltyLog().build());
//        StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
//                .detectLeakedSqlLiteObjects().penaltyLog().penaltyDeath()
//                .build());


        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_main);
        res = findViewById(R.id.textView);
       res .setMovementMethod(ScrollingMovementMethod.getInstance());
       key = findViewById(R.id.sayText);
    }
    public void onClick_begin(View view){
        new Thread(){
            @Override
            public void run() {
            //线程要执行的任务
                UdpProvider.start();
                TcpServer tcpServer = new TcpServer();
                //服务器发送信息

                    //noinspection ResultOfMethodCallIgnored
                    //读取管理员要发送的信息

//            BufferedReader reader = new BufferedReader(new InputStreamReader(key.getText().toString()));
                    //将string转换为inputStream
//                    String s=key.getText().toString();
//                    System.out.println(s);
//                    InputStream inputStream = new ByteArrayInputStream(s.getBytes());
//                    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                    String msg;
                    do {
                        msg = "服务器发送： "+key.getText().toString();
                        upDateView();
                        if (readIt){
                            tcpServer.sendBroad(msg);
                            readIt=false;
                        }
                    }while (conti);

                UdpProvider.stop();
                tcpServer.stop();
                upDateView();
                super.run();
            }
        }.start();
//        finish();
    }

    public void upDateView(){
            res.setText(Constans.INFO);

    }

    public void onClick_over(View view){
        conti=false;
    }
    public void onClick_send(View view){
        Constans.setINFO("服务器发送： "+key.getText().toString());
        upDateView();
        readIt=true;
        key.setText("");
    }

}
