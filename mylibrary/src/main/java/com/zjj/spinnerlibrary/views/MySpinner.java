package com.zjj.spinnerlibrary.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

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
    //单选选中位置
    private int selectPosition =-1;
    private List<SpinnerModel> mData;
    private LayoutInflater inflater;
    //默认样式
    private View contentView;
    private int confirmTextSize;
    //pop背景
    private int spinner_pop_background_color;
    //Item分割线颜色
    private int divider_color;
    //分割线高度 dp
    private int divider_height;
    //取消
    private String text_cancel;
    //取消字体颜色
    private int text_cancel_color;
    //取消按钮背景颜色
    private int bt_cancel_bg_color;
    //确定
    private String text_srue;
    //确定字体颜色
    private int text_srue_color;
    //确定按钮背景颜色
    private int bt_srue_bg_color;
    //按钮字体大小
    private float bt_text_size;
    //item字体大小
    private float item_text_size;
    //item字体颜色
    private int item_text_color;
    //选中图片
    private int select_drawable;
    //没选中图片
    private int default_drawable;

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
            inflater = LayoutInflater.from(context);
            adapter = new SpinnerAdapter(context,mStyle);
        }
    }

    private void initAttrs(Context context, AttributeSet attrs) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.MySpinner);
        try {
            mStyle = a.getInt(R.styleable.MySpinner_spinner_style, TYPE_SINGLE);
            spinner_pop_background_color = a.getColor(R.styleable.MySpinner_spinner_pop_background_color , getResources().getColor(R.color.pop_bag));
            divider_color = a.getColor(R.styleable.MySpinner_spinner_pop__divider_color , 0);
            divider_height = a.getInt(R.styleable.MySpinner_spinner_pop__divider_height , 1);
            bt_text_size = a.getDimension(R.styleable.MySpinner_spinner_pop_bt_size,16);
            text_cancel = a.getString(R.styleable.MySpinner_spinner_cancel_text);
            text_cancel_color = a.getColor(R.styleable.MySpinner_spinner_cancel_text_color ,  0xFF000000);
            bt_cancel_bg_color = a.getColor(R.styleable.MySpinner_spinner_cancel_bg_color ,  0xFF808080);
            text_srue = a.getString(R.styleable.MySpinner_spinner_sure_text);
            text_srue_color = a.getColor(R.styleable.MySpinner_spinner_sure_text_color ,  0xFF000000);
            bt_srue_bg_color = a.getColor(R.styleable.MySpinner_spinner_sure_bg_color ,  0xFF33aadd);
            item_text_size = a.getDimension(R.styleable.MySpinner_spinner_pop_item_size , 16);
            item_text_color =  a.getColor(R.styleable.MySpinner_spinner_pop_item_text_color ,  0xFF000000);
            select_drawable = a.getResourceId(R.styleable.MySpinner_spinner_pop_select_pic,R.drawable.select_item2);
            default_drawable = a.getResourceId(R.styleable.MySpinner_spinner_pop_default_pic,R.drawable.select_item1);
        } finally {
            a.recycle();
        }
    }

    /**
     * 设置单选，多选
     * @param style
     */
    public MySpinner setStyle(int style){
        mStyle = style;
        adapter.setType(mStyle);
        return this;
    }

    /**
     * 设置数据
     * @param mData
     */
    public MySpinner setData(List<SpinnerModel>  mData){
        this.mData = mData;
        adapter.setList(this.mData);
        return this;
    }

    /**
     * 设置样式
     * @param contentView
     */
    public MySpinner setContentView(View contentView){
        this.contentView = contentView;
        return this;
    }

   public void dismiss(){
        if (mPopup!=null&&mPopup.isShowing()){
            mPopup.dismiss();
        }
   }

    @Override
    public void onClick(View view) {
        if(mPopup==null){
            if(contentView==null){//默认样式
                contentView = inflater.inflate(R.layout.spinner_layout, null);
                initConverView(contentView);
            }
            mPopup =  new PopupWindow(contentView, this.getWidth(), LinearLayout.LayoutParams.WRAP_CONTENT);
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

    /**
     * 初始化默认弹框
     * @param convertView
     */
    private void initConverView(View convertView){
        adapter.setItemTextSize(item_text_size).setItemTextColor(item_text_color)
                .setItemDefaultDrawable(default_drawable).setItemSelectDrawable(select_drawable);

        GradientDrawable background = (GradientDrawable) convertView.getBackground();
        background.setColor(spinner_pop_background_color);
        mList = (ListView) convertView.findViewById(R.id.listview_sp);
        LinearLayout ll = (LinearLayout) convertView.findViewById(R.id.ll);
        if(mStyle==TYPE_SINGLE){
            ll.setVisibility(View.GONE);
        }else{
            TextView cancel = convertView.findViewById(R.id.cancel);
            cancel.setText(text_cancel);
            cancel.setTextSize(bt_text_size);
            cancel.setTextColor(text_cancel_color);
            ((GradientDrawable)cancel.getBackground()).setColor(bt_cancel_bg_color);
            cancel.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    mPopup.dismiss();
                }
            });
            TextView srue =  convertView.findViewById(R.id.confirm);
            srue.setText(text_srue);
            srue.setTextSize(bt_text_size);
            srue.setTextColor(text_srue_color);
            ((GradientDrawable)srue.getBackground()).setColor(bt_srue_bg_color);
            srue.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    mPopup.dismiss();
                    onSure();
                }
            });
        }
        mList.setCacheColorHint(Color.parseColor("#00000000"));

        if(divider_color==0){
            mList.setDivider(getResources().getDrawable(R.drawable.panel_divider));
        }else{
            mList.setDivider(new ColorDrawable(divider_color));
            mList.setDividerHeight(dp2px(context,divider_height));
        }

        mList.setAdapter(adapter);
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
                str = selectData.get(i).getText();
            }else{
                str = str +"/"+selectData.get(i).getText();
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

    /**
     * 获取训中位置
     * @return
     */
    public List<Integer> getSelectPositions(){
        return adapter.getSelectPositions();
    }
    /**
     * 获取选中数据
     * @return
     */
    public List<SpinnerModel> getSelectData(){
        List<SpinnerModel> selectList = new ArrayList<SpinnerModel>();
        switch (mStyle){
            case TYPE_SINGLE:
                selectList.add(mData.get(selectPosition));
                break;
            case TYPE_MULTI:
                if(adapter==null){
                    for(SpinnerModel model:mData){
                        if(model.isSelectd()){
                            selectList.add(model);
                        }
                    }
                }else{
                    selectList = adapter.getSelectData();
                }
                break;
        }
        return selectList;
    }

    //通过值设置显示
    public void setTextByValues(List<String> values){
        if(values!=null&&values.size()>0){
            for(String value:values){
                for(int j=0;j<mData.size();j++){
                    SpinnerModel data = mData.get(j);
                    if(null!=value&&data.getValue().equals(value)){
                        data.setSelectd(true);
                        if(mStyle==TYPE_MULTI){
                            selectPosition = j;
                            break;
                        }
                    }
                }
            }
            adapter.setList(mData);
            adapter.notifyDataSetChanged();
            onSure();
        }
    }

    //通过值设置显示
    public void setTextByValues(String[] values){
        if(values!=null&&values.length>0){
            for(int i=0;i<values.length;i++){
                for(int j=0;j<mData.size();j++){
                    SpinnerModel data = mData.get(j);
                    if(null!=values[i]&&data.getValue().equals(values[i])){
                        data.setSelectd(true);
                        if(mStyle==TYPE_MULTI){
                            selectPosition = j;
                            break;
                        }
                    }
                }
            }
            adapter.setList(mData);
            adapter.notifyDataSetChanged();
            onSure();
        }
    }

    /**
     * 设置默认选中的位置
     * @param posi
     */
    public MySpinner setSelectPositons(int[] posi){
        clearSelect();
        switch (mStyle){
            case TYPE_MULTI:
                for(int i=0;i<posi.length;i++){
                    int position = posi[i];
                    if(position<mData.size()){
                        mData.get(position).setSelectd(true);
                    }
                }
                onSure();
                adapter.notifyDataSetChanged();
                break;
            case TYPE_SINGLE:
                selectPosition = posi[0];
                if(selectPosition<mData.size()){{
                    this.setText(mData.get(selectPosition).getText());
                }}
                break;
        }
        return this;
    }

    /**
     * 清除选中的
     */
    public void clearSelect(){
        selectPosition = -1;
        for(SpinnerModel model:mData){
            model.setSelectd(false);
        }
    }

    private int dp2px(Context context,float dpValue){
        float scale=context.getResources().getDisplayMetrics().density;
        return (int)(dpValue*scale+0.5f);
    }
}
