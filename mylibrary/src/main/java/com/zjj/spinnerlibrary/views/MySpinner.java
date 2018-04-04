package com.zjj.spinnerlibrary.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.zjj.spinnerlibrary.R;
import com.zjj.spinnerlibrary.adapter.SpinnerAdapter;
import com.zjj.spinnerlibrary.model.SpinnerModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zjj on 2018/4/4.
 */

public class MySpinner extends android.support.v7.widget.AppCompatTextView implements View.OnClickListener{
    private int mStyle;
    //单选
    public static final int TYPE_SINGLE = 1;
    //多选
    public static final int TYPE_MULTI= 2;
    private ListView mList;
    private PopupWindow mPopup;
    private Context context;
    private int interval=200;
    private SpinnerAdapter adapter;
    private int selectPosition =-1;
    private List<SpinnerModel> mData;
    private LayoutInflater inflater;

    public MySpinner(Context context) {
        this(context, null);
    }

    public MySpinner(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MySpinner(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        if (!isInEditMode()) {
            this.context = context;
            initAttrs(context, attrs);
            this.setOnClickListener(this);
            adapter = new SpinnerAdapter(context,mStyle);
            inflater = LayoutInflater.from(context);
        }
    }

    private void initAttrs(Context context, AttributeSet attrs) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.MySpinner);
        try {
            mStyle = a.getInt(R.styleable.MySpinner_spinner_style, TYPE_SINGLE);
        } finally {
            a.recycle();
        }
    }

    public void setStyle(int style){
        mStyle = style;
        adapter.setType(mStyle);
    }

    public void setData(List<SpinnerModel>  mData){
        this.mData = mData;
        adapter.setList(mData);
    }

    @Override
    public void onClick(View view) {
        if(mPopup==null){
            View convertView = inflater.inflate(R.layout.spinner_layout, null);
            initConverView(convertView);
            mPopup =  new PopupWindow(convertView, this.getWidth(), LinearLayout.LayoutParams.WRAP_CONTENT);
            mPopup.setBackgroundDrawable(new BitmapDrawable());
            mPopup.setFocusable(true);
            mPopup.setOutsideTouchable(true);
            mPopup.update();
        }
        if(mPopup.isShowing()){
            mPopup.dismiss();
        }else{
            int height=this.getRootView().getHeight();
            int[] location = new int[2];
            this.getLocationInWindow(location);
            if((height-location[1]-this.getHeight())>interval){
                mPopup.showAsDropDown(this);
            }
            else{
                mPopup.showAtLocation(this, Gravity.LEFT|Gravity.BOTTOM, location[0],height-location[1]);
            }
        }
    }

    private void initConverView(View convertView){
        mList = (ListView) convertView.findViewById(R.id.listview_sp);
        LinearLayout ll = (LinearLayout) convertView.findViewById(R.id.ll);
        if(mStyle==TYPE_SINGLE){
            ll.setVisibility(View.GONE);
        }else{
            convertView.findViewById(R.id.cancel).setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    mPopup.dismiss();
                }
            });
            convertView.findViewById(R.id.confirm).setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    mPopup.dismiss();
                    onSure();
                }
            });
        }
        mList.setCacheColorHint(Color.parseColor("#00000000"));
        mList.setDivider(getResources().getDrawable(R.drawable.panel_divider));
        mList.setAdapter(adapter);
//        mList.setSelector(R.drawable.menu_selector);
        mList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                OnItemSelect(position);
            }
        });
    }

    private void onSure(){
        List<SpinnerModel> selectData = getSelectData();
        String str = "";
        for(int i=0;i<selectData.size();i++){
            if(i==0){
                str = selectData.get(i).getValue();
            }else{
                str = str +"/"+selectData.get(i).getValue();
            }
        }
        this.setText(str);
    }

    private void OnItemSelect(int position){
        switch (mStyle){
            case TYPE_SINGLE:
                selectPosition = position;
                this.setText(adapter.getItem(position).getText());
                mPopup.dismiss();
                break;
            case TYPE_MULTI:
                adapter.getItem(position).clickd();
                adapter.notifyDataSetChanged();
                break;
        }
    }

    public List<SpinnerModel> getSelectData(){
        List<SpinnerModel> selectList = new ArrayList<SpinnerModel>();
        switch (mStyle){
            case TYPE_SINGLE:
                selectList.add(mData.get(selectPosition));
                break;
            case TYPE_MULTI:
                selectList = adapter.getSelectData();
                break;
        }
        return selectList;
    }

    public void setSelectPositons(int[] posi){
        clearSelect();
        switch (mStyle){
            case TYPE_MULTI:
                for(int i=0;i<posi.length;i++){
                    mData.get(posi[i]).setSelectd(true);
                }
                onSure();
                adapter.notifyDataSetChanged();
                break;
            case TYPE_SINGLE:
                selectPosition = posi[0];
                this.setText(mData.get(selectPosition).getValue());
                break;
        }
    }

    public void clearSelect(){
        selectPosition = -1;
        for(SpinnerModel model:mData){
            model.setSelectd(false);
        }
    }

}
