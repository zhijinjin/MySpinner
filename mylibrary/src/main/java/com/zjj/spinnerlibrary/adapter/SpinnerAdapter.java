package com.zjj.spinnerlibrary.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.zjj.spinnerlibrary.R;
import com.zjj.spinnerlibrary.model.SpinnerModel;
import com.zjj.spinnerlibrary.views.MySpinner;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by zjj on 2018/4/4.
 */

public class SpinnerAdapter  extends BaseAdapter {

    private List<SpinnerModel> list = new ArrayList<SpinnerModel>();
    private Context context;
    private int type = MySpinner.TYPE_SINGLE;
    private LayoutInflater inflater;
    //item字体大小
    private float item_text_size = 16;
    //item字体颜色
    private int item_text_color = 0xFF000000;
    //选中图片
    private int select_drawable = R.drawable.select_item2;
    //没选中图片
    private int default_drawable = R.drawable.select_item1;

    public SpinnerAdapter(Context context,int type){
        this.context = context;
        this.type = type;
        inflater = LayoutInflater.from(context);
    }

    public SpinnerAdapter setItemTextSize(float size){
        this.item_text_size = size;
        return this;
    }

    public SpinnerAdapter setItemTextColor(int color){
        this.item_text_color = color;
        return this;
    }

    public SpinnerAdapter setItemDefaultDrawable(int defaultDrawable){
        this.default_drawable = defaultDrawable;
        return this;
    }

    public SpinnerAdapter setItemSelectDrawable(int select_drawable){
        this.select_drawable = select_drawable;
        return this;
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
            convertView = inflater.inflate(R.layout.spinner_item, null);
            hodler.icon = (ImageView) convertView.findViewById(R.id.ic_img);
            hodler.title = (TextView) convertView.findViewById(R.id.title_tv);
            hodler.title.setTextSize(item_text_size);
            hodler.title.setTextColor(item_text_color);
            convertView.setTag(hodler);
        }else{
            hodler = (ViewHodler) convertView.getTag();
        }
        hodler.title.setText(option.getText());
        if(type== MySpinner.TYPE_MULTI){ //多选，图标处理
            if (option.isSelectd()) {
                hodler.icon.setImageResource(select_drawable);
            } else {
                hodler.icon.setImageResource(default_drawable);
            }
        }else{
            hodler.icon.setVisibility(View.GONE);
        }
        return convertView;
    }

    public void setType(int type){
        this.type = type;
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

    public List<Integer> getSelectPositions(){
        List<Integer> selectList = new ArrayList<Integer>();
        for(int i=0;i<list.size();i++){
            if(list.get(i).isSelectd()){
                selectList.add(i);
            }
        }
        return selectList;
    }

    class ViewHodler {
        ImageView icon;
        TextView title;
    }
}
