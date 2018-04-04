package com.zjj.myspinner.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.zjj.myspinner.R;
import com.zjj.spinnerlibrary.model.SpinnerModel;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by zjj on 2018/4/4.
 */

public class MyAdapter extends BaseAdapter {

    private List<SpinnerModel> list = new ArrayList<SpinnerModel>();
    private Context context;
    private LayoutInflater inflater;

    public MyAdapter(Context context,List<SpinnerModel> list){
        this.context = context;
        this.list = list;
        inflater = LayoutInflater.from(context);
    }

    public void setList(List<SpinnerModel> list){
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public SpinnerModel getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        SpinnerModel option = list.get(i);
        ViewHodler hodler;
        if(null==convertView){
            hodler = new ViewHodler();
            convertView = inflater.inflate(R.layout.spinner3_item, null);
            hodler.icon = (ImageView) convertView.findViewById(R.id.ic_img);
            hodler.title = (TextView) convertView.findViewById(R.id.title_tv);
            convertView.setTag(hodler);
        }else{
            hodler = (ViewHodler) convertView.getTag();
        }
        hodler.title.setText(option.getText());
        if (option.isSelectd()) {
            hodler.icon.setImageResource(R.drawable.select_item2);
        } else {
            hodler.icon.setImageResource(R.drawable.select_item1);
        }
        return convertView;
    }


    public void click(int i){
        SpinnerModel model = list.get(i);
        model.setSelectd(!model.isSelectd());
        notifyDataSetChanged();
    }


    public List<SpinnerModel> getSelectData(){
        List<SpinnerModel> selectList = new ArrayList<SpinnerModel>();
        for(SpinnerModel model:list){
            if(model.isSelectd()){
                selectList.add(model);
            }
        }
        return selectList;
    }

    class ViewHodler {
        ImageView icon;
        TextView title;
    }
}
