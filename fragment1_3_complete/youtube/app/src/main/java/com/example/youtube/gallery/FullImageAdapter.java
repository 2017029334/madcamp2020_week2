package com.example.youtube.gallery;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

public class FullImageAdapter extends BaseAdapter {
    private Context context;
    ArrayList<Image> arrImages;
    List<String> imageNames;

    private OnItemClickListener mListener = null;
    private OnItemLongClickListener mLongListener = null;

    //생성자
    public FullImageAdapter(){}
    public FullImageAdapter(Context c){
        context = c;
    }

    public void setImageNames(List<String> imageNames){
        this.imageNames = imageNames;
    }
    public List<String> getImageNames() { return imageNames; }

    @Override
    public int getCount() {
        return imageNames.size();
    }

    @Override
    public Object getItem(int position) {
        return arrImages.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    //클릭 리스너
    public interface OnItemClickListener
    {
        void onItemClick(View v, int pos);
    }

    //롱클릭 리스너
    public interface OnItemLongClickListener
    {
        void onItemLongClick(View v, int pos);
    }

    public void setOnItemClickListener(OnItemClickListener listener)
    {
        this.mListener = listener;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener listener)
    {
        this.mLongListener = listener;
    }

    //View
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView = new ImageView(context);
            //imageView.setImageResource(arrImages.get(position));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setLayoutParams(new GridView.LayoutParams(350, 350));
            //imageView = (ImageView) convertView;

        return imageView;
    }

}
