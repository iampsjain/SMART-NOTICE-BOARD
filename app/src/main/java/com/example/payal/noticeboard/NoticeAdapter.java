package com.example.payal.noticeboard;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;


public class NoticeAdapter extends BaseAdapter {
    Context context;
    ArrayList<StudentNotice> arrayList;

    NoticeAdapter(Context context, ArrayList<StudentNotice> arrayList) {
        this.context = context;
        this.arrayList = arrayList;

    }

    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        viewGroup = (ViewGroup) LayoutInflater.from(context).inflate(R.layout.list_design, null);
        StudentNotice st = arrayList.get(i);
        TextView tvTitle, tvDate;
        tvTitle = (TextView) viewGroup.findViewById(R.id.tvTitle);
        tvDate = (TextView) viewGroup.findViewById(R.id.tvDate);
        tvTitle.setText(st.getTitle());
        tvDate.setText(st.getDate());
        return viewGroup;


    }
}
