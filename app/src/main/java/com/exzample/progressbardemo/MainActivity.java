package com.exzample.progressbardemo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener,MyProgressBar.CallBack {

    private static final String TAG = "MainActivity";
    private MyProgressBar bar;
    private TextView tvTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bar=findViewById(R.id.myprogressbar);
        Button bStart=findViewById(R.id.button);
        Button bStop=findViewById(R.id.button2);
        Button bReset=findViewById(R.id.button3);
        tvTime=findViewById(R.id.tv_time);
        bStart.setOnClickListener(this);
        bStop.setOnClickListener(this);
        bReset.setOnClickListener(this);
        List<Integer> list=new ArrayList<>();
        list.add(5);
        list.add(4);
        list.add(3);
        list.add(2);
        list.add(1);

        bar.setTimeList(list);
        bar.setCallBack(this);




    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.button:
                bar.start();
                break;
            case R.id.button2:
                bar.stop();
                break;
            case R.id.button3:
                bar.reset();
                break;
        }
    }

    @Override
    public void updateTime(long time) {
        String t=String.valueOf(time);
        StringBuilder builder=new StringBuilder();
        if (t.length()<=2){
           builder.append("0.00s");
        }else if (t.length()==3){
            builder.append("0.").append(t.charAt(0)).append("0s");
        }else if (t.length()==4){
            builder.append(t.charAt(0))
                    .append(".")
                    .append(t.substring(t.length()-3,t.length()-2))
                    .append("0s");
        }else {
            builder.append(t.substring(0,t.length()-3))
                    .append(".")
                    .append(t.substring(t.length()-3,t.length()-2))
                    .append("0s");
        }
        Log.i(TAG, "updateTime: t="+t);
        Log.i(TAG, "updateTime: builder="+builder.toString());
        tvTime.setText(builder.toString());
    }
}
