package com.example.sleepforawhile;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;
import android.content.DialogInterface;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {


    private static int timeDelay=30;
    private static int m=0;
    private static MediaPlayer mu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mu = MediaPlayer.create(this, R.raw.music00);

        AlertDialog alertdialog = new AlertDialog.Builder(this).setTitle("提示")
        .setIcon(ContextCompat.getDrawable(this,R.mipmap.mainicon))
        .setMessage("启动后，应用将会在您入睡后开始计时，时间结束后将您唤醒\n您可以通过剧烈摇晃手机来改变闹钟时间")
        .setPositiveButton("启动", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startIT();
            }
        })
        .setNeutralButton("退出",new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which) {
                android.os.Process.killProcess(android.os.Process.myPid()); //获取PID
                System.exit(0);   //常规java、c#的标准退出法，返回值为0代表正常退出
            }
        })
        .create();
        alertdialog.show();
    }
    public void startIT(){
        SensorManager sm = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        Sensor s = sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        SensorEventListener el = new SensorEventListener() {
            int tt=0;
            long time0=0;
            @Override
            public void onSensorChanged(SensorEvent e) {
                if(e.sensor.getType() == Sensor.TYPE_ACCELEROMETER){
                    tt++;
                    float x = e.values[0];
                    float y = e.values[1];
                    float z = e.values[2];
                    String tips;
                    if ((Math.abs(x)>12||Math.abs(y)>12||Math.abs(z)>12))
                    {
                        time0 = System.currentTimeMillis()/1000;////获得系统的时间，单位为毫秒,转换为秒
//                        String tips=tt+" 动:"+x+"/"+y+"/"+z +" @ "+time0;
                        tips = "用户未进入睡眠状态，取消计时";
                        m=0;
                        Toast.makeText(MainActivity.this, tips, Toast.LENGTH_SHORT).show();
                        Log.v("xyz", tips);

                        if((Math.abs(x)>25||Math.abs(y)>25||Math.abs(z)>25)){
                            timeDelay=60;
                            Toast.makeText(MainActivity.this, "时间已改为60分钟", Toast.LENGTH_SHORT).show();
                        }

                    }
                }
            }
            @Override
            public void onAccuracyChanged(Sensor sensor, int i) {
                Toast.makeText(MainActivity.this, "开始", Toast.LENGTH_SHORT).show();
            }
        };
        sm.registerListener(el,s,SensorManager.SENSOR_DELAY_UI);

        // 在 Android上界面元素不能在定时器的响应函数里更新。
        final Handler flush = new Handler() {
            public void handleMessage(Message msg) {
                ((TextView)findViewById(R.id.id_timer)).setText("已经过 "+msg.what+" 分钟");
            }
        };
        class Every1m extends TimerTask {
//            int m=0;
            @Override
            public void run() {
                m++;
                flush.sendEmptyMessage(m);

                if(m==timeDelay){

                    //mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                    mu.setLooping(true);
                    mu.start();
                }

            }
        }
        Timer timer1m = new Timer();
        timer1m.schedule(new Every1m(), 3*1000, 1*60*1000);//执行循环任务，三个参数分别为任务内容，执行开始时间，多长时间执行一次，在子线程中执行






    }

}
