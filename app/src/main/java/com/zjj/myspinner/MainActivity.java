package com.zjj.myspinner;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.zjj.spinnerlibrary.model.SpinnerModel;
import com.zjj.spinnerlibrary.views.MySpinner;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private MySpinner sp_1,sp_2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sp_1 = (MySpinner)findViewById(R.id.sp_1);
        sp_1.setData(getData());
//        sp_1.setSelectPositons(new int[]{1});

        sp_2 = (MySpinner)findViewById(R.id.sp_2);
        sp_2.setData(getData());
//        sp_2.setSelectPositons(new int[]{1,2});
    }

    private List<SpinnerModel> getData(){
        List<SpinnerModel> mData = new ArrayList<>();
        for (int i=0;i<5;i++){
            mData.add(new SpinnerModel("选项"+i,"选项"+i));
        }
        return mData;
    }
}
