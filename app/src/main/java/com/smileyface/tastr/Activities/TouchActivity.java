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
import com.smileyface.tastr.Utilities.locationHandler;
import com.smileyface.tastr.Utilities.yelpDataExecutor;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Thread.sleep;


public class TouchActivity extends Activity {
    private String msg;
    private ImageView img;
    private RelativeLayout.LayoutParams layoutParams;
    yelpDataExecutor yelp = new yelpDataExecutor();
    locationHandler curLoc = new locationHandler(this);
    firebaseHandler firebase;
    private GoogleApiClient client;


    //tastrItem Queue
    public ArrayList<TastrItem> itemQueue;


    public ArrayList<String> imagePath = new ArrayList<>();
    private ArrayList<String> menuItem = new ArrayList<>();
    private ArrayList<String> restNames = new ArrayList<>();

    public void setImagePath(String path) {
        this.imagePath.add(path);
    }
    public void addMenuItem(String item){
        menuItem.add(item);
    }
    public void addRes(ArrayList<String> list){
        restNames = list;
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
        curLoc.askForlocation();
        yelp.execute(curLoc.getCurrentLat(), curLoc.getCurrentLong());
        dataLoader loader = new dataLoader();
        loader.execute();


        System.err.println("Executing Yelp Call");
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
            int i = 0;
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

                        if (i <imagePath.size()) {
                            setNewImage(imagePath.get(i));
                            i++;
                        }else{i = 0;}


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
        new downloadImageTask(img).execute(url);
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


        boolean yelpLoaded = yelp.checkHasNewData();



        boolean initialStartFlag = true;
        // Everything you want to happen OUTSIDE of the GUI thread. IE this is a background process.
        protected String doInBackground(String... params) {

            // add a local list of the restaurants found at yelp and a reference for the background thread to process.
            addRes(yelp.getRestaurants());
            // initialize firebase handler class


            // Clay's Nasty Algorithm for retrieving data from firebase

            // loop through each restaurant and add all the menu items from each one. I think we need to randomize this list later on to provide variety in the app.
            for (int i = 0; i < restNames.size(); i++) {
                firebase = new firebaseHandler("Tastr Items/" + restNames.get(i) + "/Menu"); //Change where in the database we want to search for information
                System.err.println("Adding Menu From --> " + restNames.get(i));

                firebase.readKeyFromDatabase(); // Search the database for any Menu items available and put them into a list

                // Wait for firebase to finish adding new data
                while (!firebase.isReaderDone()) {
                    try {
                        sleep(100); // wait 100 ms before checking again, saves cpu
                    } catch (InterruptedException e) {
                        e.printStackTrace(); // if there is a problem while sleeping, print out the errors encountered.
                    }
                }
                int oldMenuSize = menuItem.size(); // prevents adding duplicate menu items
                System.err.println("Menu Size So far ----->" + menuItem.size());
                menuItem.addAll(firebase.getReaderList());

                for (int k = oldMenuSize; k< menuItem.size(); k++){
                    firebase = new firebaseHandler("Tastr Items/" + restNames.get(i) + "/Menu/" + menuItem.get(k) + "/Image Path");

                    System.err.println("Tastr Items/" + restNames.get(i) + "/Menu/" + menuItem.get(k) + "/Image Path");

                    firebase.readValueFromDatabase();
                    // Wait for firebase to finish adding new data
                    while (!firebase.isReaderDone()) {
                    }
                    System.err.println("Image Paths have been added ----> " + firebase.getReaderList().get(0));

                    imagePath.addAll(firebase.getReaderList());

                }


            }

            return null;
        }//doInBackground

        // Everything you want to happen AFTER the doInBackground function is executed. Use this method to make changes to the GUI.
        @Override
        protected void onPostExecute(String result) {
            if(initialStartFlag = true && !imagePath.isEmpty()) {
                System.err.println("Trying to download image now ----> "+imagePath.get(0));

                setNewImage(imagePath.get(0));
                imagePath.remove(0);
                initialStartFlag = false;

            }

        }//On Post Execute
    }//loader class

}