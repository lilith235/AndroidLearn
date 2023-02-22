package com.example.chat_room;

import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainMenu extends AppCompatActivity {

    private View view;
    private static TextView res;
    public static boolean conti = false;
    public static boolean readIt = false;
    private static EditText key;

    protected void onCreate(Bundle savedInstanceState) {
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
                DeviceInfo device = UdpSearch.start(1);
//                System.out.println("device: "+device.toString());
                Constans.setINFO("device: "+device.toString()+"\n");
                upDateView();
                //开始于 tcp 服务端通信
                if (device != null) {
                    TcpClient.bindWith(device);
                    upDateView();
                }
                upDateView();
                super.run();
            }
        }.start();
        upDateView();
//        finish();
    }

    static  public void upDateView(){
        res.setText(Constans.INFO);
    }

    public void onClick_over(View view){
        conti=true;
    }
    public void onClick_send(View view){
        Constans.setINFO("本端口发送： "+key.getText().toString()+"\n");
        Constans.sayTEXT=key.getText().toString();
        key.setText("");
        readIt=true;

    }



}
