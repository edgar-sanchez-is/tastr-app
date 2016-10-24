package com.smileyface.tastr.Activities;

import android.app.Activity;
import android.content.ClipData;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;
import com.smileyface.tastr.R;
import com.smileyface.tastr.Other.TastrItem;
import com.smileyface.tastr.Utilities.downloadImageTask;
import com.smileyface.tastr.Utilities.firebaseHandler;

import java.util.ArrayList;
import java.util.List;


public class TouchActivity extends Activity {
    private String msg;
    private ImageView img;
    private RelativeLayout.LayoutParams layoutParams;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;


    //tastrItem Queue
    public List<TastrItem> itemQueue;


    public ArrayList<String> imagePath = new ArrayList<>();
    public void setImagePath(ArrayList<String> imagePath) {
        this.imagePath = imagePath;
    }

    //potential temporary list of items to ignore that have been yucked or previously liked
    //public List<TastrItem> ignoreItemsIDs;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //TODO: setup request tastr items from firebase
        //int requestAmount = 15; trying not to hardcode 15 incase of modular request size in the future
        //TODO: make "firebase" instantiation available in the scope of the touch module so that we can request and show items
        //TODO: make a function that will return a list of TastrItems
        //itemQueue = firebaseHandler.requestNewItems(requestAmount);

        //load already liked items TODO: potential omitting list, do this after itemQueue is up and working
        //ignoreItems = loadLikes(); add to this list as food is yucked

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_touch);

        // Connect to database and get Tastr Items
        dataLoader loader = new dataLoader();
        loader.execute();

        img = (ImageView) findViewById(R.id.imageView);
        ImageView yum = (ImageView) findViewById(R.id.yum);
        ImageView yuck = (ImageView) findViewById(R.id.yuck);



        img.setOnDragListener(new View.OnDragListener() {
            @Override
            public boolean onDrag(View v, DragEvent event) {
                switch (event.getAction()) {
                    case DragEvent.ACTION_DRAG_STARTED:
                        layoutParams = (RelativeLayout.LayoutParams) v.getLayoutParams();
                        Log.d(msg, "Action is DragEvent.ACTION_DRAG_STARTED");

                        // Do nothing
                        return true;


                    case DragEvent.ACTION_DRAG_ENTERED:
                        Log.d(msg, "Action is DragEvent.ACTION_DRAG_ENTERED");
                        int x_cord = (int) event.getX();
                        int y_cord = (int) event.getY();
                        img.setVisibility(View.VISIBLE);
                        return true;

                    case DragEvent.ACTION_DRAG_EXITED:
                        Log.d(msg, "Action is DragEvent.ACTION_DRAG_EXITED");
                        x_cord = (int) event.getX();
                        y_cord = (int) event.getY();
                        layoutParams.leftMargin = x_cord;
                        layoutParams.topMargin = y_cord;
                        v.setLayoutParams(layoutParams);
                        img.setVisibility(View.VISIBLE);
                        return true;

                    case DragEvent.ACTION_DRAG_LOCATION:
                        Log.d(msg, "Action is DragEvent.ACTION_DRAG_LOCATION");
                        v.setVisibility(View.VISIBLE);
                        return true;


                    case DragEvent.ACTION_DRAG_ENDED:
                        v.setVisibility(View.VISIBLE);
                        break;


                    case DragEvent.ACTION_DROP:
                        Log.d(msg, "ACTION_DROP event");


                        return true;

                    default:
                        return true;
                }
                return false;
            }


        });

        yum.setOnDragListener(new View.OnDragListener() {
            @Override
            public boolean onDrag(View v, DragEvent event) {
                switch (event.getAction()) {
                    case DragEvent.ACTION_DRAG_STARTED:
                        layoutParams = (RelativeLayout.LayoutParams) v.getLayoutParams();
                        Log.d(msg, "Action is DragEvent.ACTION_DRAG_STARTED");

                        // Do nothing
                        return true;


                    case DragEvent.ACTION_DRAG_ENTERED:
                        Log.d(msg, "Action is DragEvent.ACTION_DRAG_ENTERED");
                        int x_cord = (int) event.getX();
                        int y_cord = (int) event.getY();
                        return true;

                    case DragEvent.ACTION_DRAG_EXITED:
                        Log.d(msg, "Action is DragEvent.ACTION_DRAG_EXITED");
                        x_cord = (int) event.getX();
                        y_cord = (int) event.getY();
                        layoutParams.leftMargin = x_cord;
                        layoutParams.topMargin = y_cord;
                        v.setLayoutParams(layoutParams);
                        return true;

                    case DragEvent.ACTION_DRAG_LOCATION:
                        Log.d(msg, "Action is DragEvent.ACTION_DRAG_LOCATION");
                        break;

                    case DragEvent.ACTION_DROP:

                        Log.d(msg, "Drag ended");
                        if (dropEventNotHandled(event)) {
                            v.setVisibility(View.VISIBLE);
                        }

                        new AlertDialog.Builder(TouchActivity.this)
                                .setTitle("Collision Detected")
                                .setMessage("Yum!")
                                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        // continue with delete
                                    }
                                })
                                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {

                                    }
                                })
                                .setIcon(android.R.drawable.ic_dialog_alert)
                                .show();
                        onCreate(null);


                        break;


                    default:
                        return true;
                }

                return false;
            }
        });


        yuck.setOnDragListener(new View.OnDragListener() {
            @Override
            public boolean onDrag(View v, DragEvent event) {
                switch (event.getAction()) {
                    case DragEvent.ACTION_DRAG_STARTED:
                        layoutParams = (RelativeLayout.LayoutParams) v.getLayoutParams();
                        Log.d(msg, "Action is DragEvent.ACTION_DRAG_STARTED");

                        // Do nothing
                        return true;


                    case DragEvent.ACTION_DRAG_ENTERED:
                        Log.d(msg, "Action is DragEvent.ACTION_DRAG_ENTERED");
                        int x_cord = (int) event.getX();
                        int y_cord = (int) event.getY();
                        return true;

                    case DragEvent.ACTION_DRAG_EXITED:
                        Log.d(msg, "Action is DragEvent.ACTION_DRAG_EXITED");
                        x_cord = (int) event.getX();
                        y_cord = (int) event.getY();
                        layoutParams.leftMargin = x_cord;
                        layoutParams.topMargin = y_cord;
                        v.setLayoutParams(layoutParams);
                        return true;

                    case DragEvent.ACTION_DRAG_LOCATION:
                        Log.d(msg, "Action is DragEvent.ACTION_DRAG_LOCATION");
                        break;

                    case DragEvent.ACTION_DROP:

                        Log.d(msg, "Drag ended");
                        if (dropEventNotHandled(event)) {
                            v.setVisibility(View.VISIBLE);
                        }


                        new AlertDialog.Builder(TouchActivity.this)
                                .setTitle("Collision Detected")
                                .setMessage("Yuck!")
                                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        // continue with delete
                                    }
                                })
                                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        // do nothing
                                    }
                                })
                                .setIcon(android.R.drawable.ic_dialog_alert)
                                .show();
                        onCreate(null);

                        break;


                    default:
                        return true;
                }

                return false;
            }
        });


        img.setOnTouchListener(new View.OnTouchListener() {
            @Override

            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    ClipData data = ClipData.newPlainText("", "");
                    View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(img);

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        img.startDragAndDrop(data, shadowBuilder, img, 100);
                    } else //noinspection deprecation,deprecation,deprecation
                        img.startDrag(data, shadowBuilder, img, 100);


                    return true;
                } else {
                    return false;
                }
            }

        });
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    private boolean dropEventNotHandled(DragEvent dragEvent) {
        return !dragEvent.getResult();
    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("Touch Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());

    }

    public void setNewImage(String url){
        new downloadImageTask((ImageView) img)
                .execute("https://firebasestorage.googleapis.com/v0/b/unt-team-project.appspot.com/o/Bagheri%E2%80%99s%20Restaurant%2FFettuccine%20Alfredo.jpg?alt=media&token=c85b230a-9c53-4c5e-89db-1823bb760a03");
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }

    private class dataLoader extends AsyncTask<String, Void, String> {
        firebaseHandler firebase = new firebaseHandler("Tastr Items/Bagheri's Restaurant/Menu");
        boolean inititalStartFlag = true;
        // Everything you want to happen OUTSIDE of the GUI thread.
        protected String doInBackground(String... params) {
            System.out.println("Now Starting Background Task inside TouchActivity");
            firebase.readFromDatabase();
            // Wait for the database to actually get  some information.
            while(!firebase.isReaderDone()){}
           setImagePath(firebase.getReaderList());
            return null;
        }//doInBackground

        // Everything you want to happen AFTER the doInBackground function is executed. Use this method to make changes to the GUI.
        @Override
        protected void onPostExecute(String result) {
            if(inititalStartFlag = true) {
                ArrayList<String> tmp = firebase.getReaderList();
                setNewImage("");
                inititalStartFlag = false;
            }


        }//On Post Execute

    }//loader class

}