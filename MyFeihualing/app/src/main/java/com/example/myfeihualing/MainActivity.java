package com.example.myfeihualing;
import androidx.appcompat.app.AppCompatActivity;
import android.graphics.Color;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import java.io.InputStream;
import java.util.Scanner;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }
    public void onClick_clear(View view){
        EditText key1 = findViewById(R.id.id_key);
        key1.setText("");
    }
    public void onClick_go(View view) {
        EditText key = findViewById(R.id.id_key);
        TextView res = findViewById(R.id.id_res);
        InputStream input = getResources().openRawResource((R.raw.ziyue));
        try {
            int k = 0;
            String temp = "";
            String result = "";
            Scanner scan = new Scanner(input);
            int max=100;
            while (scan.hasNext()) {
                String str = scan.nextLine();
                if (str.contains("【出处】")) {
                    temp = str;
                    continue;
                }
                if (str.contains("·")&&str.contains(key.getText().toString())) {
                    k++;
                    result += k + str + "\n" + temp + "\n";
                    max--;
                    if (max<=0) break;
                }
            }

            SpannableStringBuilder style=new SpannableStringBuilder(result);
            while( k >= 0)
            {
                int l=result.indexOf(key.getText().toString(), k);
                int r = l + key.getText().toString().length();
                if (l == -1)
                    break;
                k = l + 1;
                style.setSpan(new ForegroundColorSpan(Color.RED),l,r,Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
            }
            Toast.makeText(MainActivity.this, "一共有" + k + "句！", Toast.LENGTH_SHORT).show();
            res.setMovementMethod(ScrollingMovementMethod.getInstance());
            res.setHorizontallyScrolling(true);
            res.setText(style);
            scan.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
