package com.example.youtube.tab3;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.youtube.R;

import java.util.List;

// listView에 연결시켜주는 adapter
public class bookViewAdapter extends BaseAdapter {

    private Context context;
    private List<bookFinalInfo> list;
    private LayoutInflater inflate;
    private ViewHolder viewHolder;

    public bookViewAdapter(List<bookFinalInfo> list, Context context){
        this.list = list;
        this.context = context;
        this.inflate = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        if(convertView==null){
            convertView = inflate.inflate(R.layout.book_listview_layout, null);

            viewHolder = new ViewHolder();

            viewHolder.textView1 = (TextView) convertView.findViewById(R.id.hotelName);
            viewHolder.textView2 = (TextView) convertView.findViewById(R.id.hotelLoc);
            viewHolder.textView3 = (TextView) convertView.findViewById(R.id.checkIn);
            viewHolder.textView4 = (TextView) convertView.findViewById(R.id.checkOut);

            convertView.setTag(viewHolder);
        }
        else{
            viewHolder = (ViewHolder)convertView.getTag();
        }

        viewHolder.textView1.setText(list.get(position).getHotelName());
        viewHolder.textView2.setText(list.get(position).getHotelLoc());
        viewHolder.textView3.setText(list.get(position).getCheckIn());
        viewHolder.textView4.setText(list.get(position).getCheckOut());

        //viewHolder.textView2.setText(list.get(position).getEmail());

        return convertView;
    }

    class ViewHolder{
        public TextView textView1;
        public TextView textView2;
        public TextView textView3;
        public TextView textView4;
    }
}
