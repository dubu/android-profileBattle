package com.parse.photobattle;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.List;

/**
 * User: kingkingdubu
 * Date: 13. 7. 28
 * Time: 오후 4:01
 */
public class ListActivity extends android.app.ListActivity

{
    private static final int ACTIVITY_CREATE = 0;
    private static final int ACTIVITY_EDIT = 1;
    private static final int ACTIVITY_PHOTO = 2;
    public Dialog progressDialog;
    private Activity activity;
    private List<ParseObject> todos;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        activity = this;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.album);

        TextView empty = (TextView) findViewById(android.R.id.empty);
        empty.setVisibility(View.INVISIBLE);

        new RemoteDataTask().execute();
        registerForContextMenu(getListView());

        Button confirmButton = (Button) findViewById(R.id.my);
        confirmButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                openMy();

//                Bundle bundle = new Bundle();
//                Intent intent = new Intent();
//                intent.putExtras(bundle);
//                setResult(RESULT_OK, intent);
//                finish();


            }
        });

        Button reloadButton = (Button) findViewById(R.id.btn_reload);
        reloadButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                new RemoteDataTask().execute();
            }
        });

    }

    private void openMy() {
        Intent i = new Intent(this, BattleActivity.class);
        startActivityForResult(i, 100);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        new RemoteDataTask().execute();
    }

    private class RemoteDataTask extends AsyncTask<Void, Void, Void> {
        // Override this method to do custom remote calls
        protected Void doInBackground(Void... params) {
            // Gets the current list of todos in sorted order
            ParseQuery query = new ParseQuery("BattlePhoto");
            //query.orderByDescending("point");
            query.orderByDescending("_updated_at");

            try {
                todos = query.find();
            } catch (ParseException e) {

            }
            return null;
        }

        @Override
        protected void onPreExecute() {

            ListActivity.this.progressDialog = ProgressDialog.show(ListActivity.this, "",
                    "Loading...", true);

            super.onPreExecute();
        }

        @Override
        protected void onProgressUpdate(Void... values) {

            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(Void result) {

            Log.e("DUBULOG adapter", "adapper!!");
            ListActivity.this.progressDialog.dismiss();
            setListAdapter(new ListAdapter(todos, activity));

            TextView empty = (TextView) findViewById(android.R.id.empty);
            empty.setVisibility(View.VISIBLE);
        }
    }


}
