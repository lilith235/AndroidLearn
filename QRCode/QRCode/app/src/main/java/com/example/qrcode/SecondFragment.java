package com.example.qrcode;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.DecodeHintType;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.NotFoundException;
import com.google.zxing.RGBLuminanceSource;
import com.google.zxing.Result;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.integration.android.IntentIntegrator;
import androidx.annotation.NonNull;
import androidx.core.text.PrecomputedTextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.qrcode.databinding.FragmentSecondBinding;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import java.util.HashMap;
import java.util.Random;

public class SecondFragment extends Fragment {

    private TextView show_txt; //显示二维码的内容
    private HashMap hashMap; //用hasmap放置二维码的参数
    private Bitmap bitmap;//声明一个bitmap对象用于放置图片;
    private Intent data;
    private FragmentSecondBinding binding;
    private TextView setA;
    // 创建IntentIntegrator对象
    IntentIntegrator intentIntegrator = IntentIntegrator.forSupportFragment(this);
    Activity a;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentSecondBinding.inflate(inflater, container, false);
        onSetA();
        onScan();
        onCreate();
        return binding.getRoot();

    }
//获取A
    public void onSetA() {
        binding.confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setA=binding.curNumber;
                Constants.A=Integer.parseInt(String.valueOf(setA.getText()));
                System.out.println(Constants.A);
            }
        });
    }

    //打开扫码请求
    public void onScan(){
        binding.scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment frg = new Fragment(0);
                // 开始扫描
                intentIntegrator.initiateScan();
                Bundle extras = data.getExtras();
                Bitmap bitmap = (Bitmap) extras.get("data");
                binding.QRCode.setImageBitmap(bitmap);
                int temps = 0;
                //区分是第一轮还是第二轮，如果是第一轮就扫描得到上一个人的S
                if (Constants.rounds == 1) {
                    //如果当前用户是普通用户就将扫码解析得到的上一个人的结果加入到当前用户的S中
                    if (Constants.id == 0)
                        temps = show_QR_code();
                    // 随机产生数B
                    Constants.B = (int) (Math.random() * (10000 - 100) + 10);
                    Constants.S = Constants.A + Constants.B + temps;
                }
//                如果是第二轮就用上一个人的S减掉当前用户的B
                else{
                    temps = show_QR_code();
                    Constants.S = temps - Constants.B;
                    //如果当前用户是发起人就直接显示结果
                    if (Constants.id == 1){
                        new  AlertDialog.Builder(a)
                                .setTitle("结束" )
                                .setMessage("最后的总数是"+Constants.S)
                                .setPositiveButton("确定" ,  null )
                                .show();
                    }

                }
                Constants.rounds=2;
            }
        });
    }

//创建二维码
    public void onCreate(){
        binding.create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                create_QR_code();
            }
        });
    }

    private int show_QR_code() {
        hashMap.put(DecodeHintType.CHARACTER_SET, "utf-8");//设置解码的字符，为utf-8
        bitmap = ((BitmapDrawable) (binding.QRCode.getDrawable())).getBitmap();
        int width = bitmap.getWidth();//现在是从那个bitmap中得到width和height
        int height = bitmap.getHeight();
        int[] pixels = new int[width * height];//新建数组，大小为width*height
        bitmap.getPixels(pixels, 0, width, 0, 0, width, height); //和什么的setPixels()方法对应
        Result result = null;//Result类主要是用于保存展示二维码的内容的
        BinaryBitmap binaryBitmap = new BinaryBitmap(new HybridBinarizer(new RGBLuminanceSource(width, height, pixels)));
        //BinaryBitmap这个类是用于反转二维码的，HybridBinarizer这个类是zxing在对图像进行二值化算法的一个类
        try {
            result = new MultiFormatReader().decode(binaryBitmap);//调用MultiFormatReader()方法的decode()，传入参数就是上面用的反转二维码的
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        if (result == null) {
            show_txt.setText("err: none string found.");
            return 0;
        } else {
            show_txt.setText(result.toString());//设置文字
            return Integer.parseInt(result.toString());
        }
    }


    private void create_QR_code() {
        hashMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);
        //定义二维码的纠错级别，为L
        hashMap.put(EncodeHintType.CHARACTER_SET, "utf-8");
        //设置字符编码为utf-8
        hashMap.put(EncodeHintType.MARGIN, 2);
        //设置margin属性为2,也可以不设置
        String contents = Constants.A+"";//根据用户输入的数字生成二维码
        BitMatrix bitMatrix = null;   //这个类是用来描述二维码的,可以看做是个布尔类型的数组
        try {
            bitMatrix = new MultiFormatWriter().encode(contents, BarcodeFormat.QR_CODE, 250, 250, hashMap);
            //调用encode()方法,第一次参数是二维码的内容，第二个参数是生二维码的类型，第三个参数是width，第四个参数是height，最后一个参数是hints属性
        } catch (WriterException e) {
            e.printStackTrace();
        }
        int width = bitMatrix.getWidth();//获取width
        int height = bitMatrix.getHeight();//获取height
        int[] pixels = new int[width * height]; //创建一个新的数组,大小是width*height
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                //通过两层循环,为二维码设置颜色
                if (bitMatrix.get(i, j)) {
                    pixels[i * width + j] = Color.BLACK;  //设置为黑色
                } else {
                    pixels[i * width + j] = Color.WHITE; //设置为白色
                }
            }
        }
        bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
        //调用Bitmap的createBitmap()，第一个参数是width,第二个参数是height,最后一个是config配置，可以设置成RGB_565
        bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
        //调用setPixels(),第一个参数就是上面的那个数组，偏移为0，x,y也都可为0，根据实际需求来,最后是width ,和height
        binding.QRCode.setImageBitmap(bitmap);
        //调用setImageBitmap()方法，将二维码设置到imageview控件里
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}