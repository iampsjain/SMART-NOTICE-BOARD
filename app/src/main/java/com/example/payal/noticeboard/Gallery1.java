package com.example.payal.noticeboard;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.Toast;

/**
 * Created by payal on 12-Feb-17.
 */

public class Gallery1 extends AppCompatActivity {

    private Integer img[]={R.drawable.student,R.drawable.teacher,R.drawable.ab,R.drawable.student,R.drawable.ab};
    ImageView imageView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
         setContentView(R.layout.gallery);
            Gallery gallery= (Gallery) findViewById(R.id.gallery);
        gallery.setAdapter(new ImageAdapter(this));
        imageView= (ImageView) findViewById(R.id.imageView);
        gallery.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(Gallery1.this, "pic: " + i, Toast.LENGTH_SHORT).show();
                imageView.setImageResource(img[i]);
            }
        });
        }
public class ImageAdapter extends BaseAdapter{

    private Context context;
    int imageBackground;
    public ImageAdapter(Context context){
        this.context=context;
    }

    @Override
    public int getCount() {
        return img.length;
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
       ImageView imageView=new ImageView(context);
        imageView.setImageResource(img[i]);
        return imageView;
    }
}
}

