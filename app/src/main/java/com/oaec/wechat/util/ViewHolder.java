package com.oaec.wechat.util;

import android.content.Context;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import org.xutils.common.util.DensityUtil;
import org.xutils.image.ImageOptions;
import org.xutils.x;

/**
 * Created by Kevin on 2016/9/26.
 */
public class ViewHolder {
    private static final String TAG = "ViewHolder";
    //用来存放View的容器
    private SparseArray<View> mViews;
    //Item的View
    private View convertView;
    private int position;

    private ViewHolder(Context context, int layoutId, int position) {
        mViews = new SparseArray<View>();
        this.position = position;
        convertView = LayoutInflater.from(context).inflate(layoutId, null);
        convertView.setTag(this);
    }

    public static ViewHolder getInstance(Context context, View convertView, int layoutId, int position) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder(context, layoutId, position);
            convertView = holder.convertView;
        } else {
            holder = (ViewHolder) convertView.getTag();
            holder.position = position;
        }
        return holder;
    }

    public <T extends View> T getView(int viewId, Class<T> c) {
        View v = mViews.get(viewId);
        if (v == null) {
            v = convertView.findViewById(viewId);
            mViews.put(viewId, v);
        }
        return (T) v;
    }

    public View getConvertView() {
        return convertView;
    }

    /**
     * 设置TextView显示的文字
     * @param viewId
     * @param text
     * @return
     */
    public ViewHolder setText(int viewId, String text) {
        TextView view = getView(viewId, TextView.class);
        view.setText(text);
        return this;
    }

    public ViewHolder setImageUrl(int viewId, String url) {
        ImageView view = getView(viewId, ImageView.class);
        ImageOptions imageOptions = new ImageOptions.Builder()
                .setImageScaleType(ImageView.ScaleType.CENTER_CROP)
                .setSize(DensityUtil.dip2px(100f),DensityUtil.dip2px(80f))
                .setUseMemCache(true)
                .build();
        x.image().bind(view,url,imageOptions);
        return this;
    }

    public ViewHolder setOnCheckedChangeListener(int viewId, CompoundButton.OnCheckedChangeListener listener){
        CheckBox view = getView(viewId, CheckBox.class);
        view.setOnCheckedChangeListener(listener);
        return this;
    }

    public ViewHolder setChecked(int viewId,boolean checked){
        CheckBox view = getView(viewId, CheckBox.class);
        view.setChecked(checked);
        return this;
    }
}
