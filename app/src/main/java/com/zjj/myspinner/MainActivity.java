package com.zjj.myspinner;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.zjj.myspinner.adapter.MyAdapter;
import com.zjj.spinnerlibrary.model.SpinnerModel;
import com.zjj.spinnerlibrary.views.MySpinner;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private MySpinner sp_1,sp_2,sp_3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sp_1 = (MySpinner)findViewById(R.id.sp_1);
        sp_1.setData(getData());
        //默认选中单选
        sp_1.setSelectPositons(new int[]{1});

        sp_2 = (MySpinner)findViewById(R.id.sp_2);
        sp_2.setData(getData());
        //默认选中多选
        sp_2.setSelectPositons(new int[]{1,2});

        sp_3 = (MySpinner)findViewById(R.id.sp_3);
        View contentView = LayoutInflater.from(this).inflate(R.layout.spinner3_layout, null);
        sp_3.setContentView(contentView);
        final MyAdapter adapter = new MyAdapter(this,getData());
        ListView mList = (ListView) contentView.findViewById(R.id.listview_sp);
        mList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                adapter.click(i);
            }
        });
        mList.setAdapter(adapter);
        contentView.findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sp_3.dismiss();
            }
        });
        contentView.findViewById(R.id.confirm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sp_3.dismiss();
                List<SpinnerModel> selectData = adapter.getSelectData();
                Toast.makeText(MainActivity.this,selectData.size()+"",Toast.LENGTH_SHORT).show();
            }
        });
    }

    private List<SpinnerModel> getData(){
        List<SpinnerModel> mData = new ArrayList<>();
        for (int i=0;i<5;i++){
            mData.add(new SpinnerModel("选项"+i,"选项"+i));
        }
        return mData;
    }
}
