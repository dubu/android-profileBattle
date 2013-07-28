package com.parse.demo;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import com.parse.GetDataCallback;
import com.parse.ParseFile;
import com.parse.ParseObject;

import java.io.InputStream;
import java.text.ParseException;
import java.util.List;

/**
 * User: kingkingdubu
 * Date: 13. 7. 28
 * Time: 오후 4:06
 */
public class ListAdapter extends BaseAdapter {

    private static final int IN_SAMPLE_SIZE = 8;
    private List<ParseObject> itemlist;
    private Activity activity;

    private int columnCount = 4;

    public ListAdapter(List<ParseObject> itemlist, Activity activity) {
        this.itemlist = itemlist;
        this.activity = activity;
    }

    @Override
    public int getCount() {
        return itemlist.size()/columnCount;
    }

    @Override
    public Object getItem(int row) {
        return itemlist.get(row);
    }

    @Override
    public long getItemId(int position) {
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public View getView(int row, View convertView, ViewGroup parent) {
        Log.e("DUBULOG", String.valueOf(row));
        if (convertView == null) {
            LayoutInflater layoutInflater = activity.getLayoutInflater();
            convertView = layoutInflater.inflate(R.layout.itemview, parent, false);
        }

        final ImageView imageView00 = (ImageView) convertView.findViewById(R.id.imageView00);
        final ImageView imageView01 = (ImageView) convertView.findViewById(R.id.imageView01);
        final ImageView imageView02 = (ImageView) convertView.findViewById(R.id.imageView02);
        final ImageView imageView03 = (ImageView) convertView.findViewById(R.id.imageView03);

        if(itemlist.size() >row*columnCount+3  ) {
            final String col00 = (String) itemlist.get(row*columnCount).get("name");
            final String col01 = (String) itemlist.get(row*columnCount+1).get("name");
            final String col02 = (String) itemlist.get(row*columnCount+2).get("name");
            final String col03 = (String) itemlist.get(row*columnCount+3).get("name");


            final ParseFile file00 = (ParseFile) itemlist.get(row*columnCount).get("profileFile");
            final ParseFile file01 = (ParseFile) itemlist.get(row*columnCount+1).get("profileFile");
            final ParseFile file02 = (ParseFile) itemlist.get(row*columnCount+2).get("profileFile");
            final ParseFile file03 = (ParseFile) itemlist.get(row*columnCount+3).get("profileFile");

            file00.getDataInBackground(new GetDataCallback() {
                @Override
                public void done(byte[] data, com.parse.ParseException e) {
                    if (e == null) {
                        // data has the bytes for the resume
                        BitmapFactory.Options options = new BitmapFactory.Options();
                        options.inSampleSize = IN_SAMPLE_SIZE;
                        Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length, options);
                        imageView00.setImageBitmap(bitmap);
                    } else {
                        // something went wrong
                    }
                }
            });

            file01.getDataInBackground(new GetDataCallback() {
                @Override
                public void done(byte[] data, com.parse.ParseException e) {
                    if (e == null) {
                        // data has the bytes for the resume
                        BitmapFactory.Options options = new BitmapFactory.Options();
                        options.inSampleSize = IN_SAMPLE_SIZE;
                        Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length, options);
                        imageView01.setImageBitmap(bitmap);
                    } else {
                        // something went wrong
                    }
                }
            });

            file02.getDataInBackground(new GetDataCallback() {
                @Override
                public void done(byte[] data, com.parse.ParseException e) {
                    if (e == null) {
                        // data has the bytes for the resume
                        BitmapFactory.Options options = new BitmapFactory.Options();
                        options.inSampleSize = IN_SAMPLE_SIZE;
                        Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length, options);
                        imageView02.setImageBitmap(bitmap);
                    } else {
                        // something went wrong
                    }
                }
            });

            file03.getDataInBackground(new GetDataCallback() {
                @Override
                public void done(byte[] data, com.parse.ParseException e) {
                    if (e == null) {
                        // data has the bytes for the resume
                        BitmapFactory.Options options = new BitmapFactory.Options();
                        options.inSampleSize = IN_SAMPLE_SIZE;
                        Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length, options);
                        imageView03.setImageBitmap(bitmap);
                    } else {
                        // something went wrong
                    }
                }
            });


        }
        return convertView;
    }
}
