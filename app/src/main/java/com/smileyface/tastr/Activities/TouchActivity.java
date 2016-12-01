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
import com.smileyface.tastr.Other.TastrItem;
import com.smileyface.tastr.R;
import com.smileyface.tastr.Utilities.ItemLoader;
import com.smileyface.tastr.Utilities.downloadImageTask;
import com.smileyface.tastr.Utilities.locationHandler;
import com.smileyface.tastr.Utilities.yelpDataExecutor;

import static java.lang.Thread.sleep;


public class TouchActivity extends Activity {
    private String msg;
    private ImageView img;
    private RelativeLayout.LayoutParams layoutParams;
    yelpDataExecutor yelp = new yelpDataExecutor();
    locationHandler curLoc = new locationHandler(this);

    private GoogleApiClient client;
    private downloadImageTask imageLoader;
    ItemLoader itemLoader;
    TastrItem currentItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //TODO: potential omitting list, do this after itemQueue is up and working
        //load already liked items
        //ignoreItems = loadLikes(); add to this list as food is yucked

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_touch);
        // Connect to yelp and request a list of nearby restaurants
        img = (ImageView) findViewById(R.id.img);
        img.setScaleType(ImageView.ScaleType.FIT_XY);
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
                        CharSequence options[] = new CharSequence[]{currentItem.getMenu().get(itemCount).getName(), currentItem.getAddress(), currentItem.getPhone()};

                        AlertDialog.Builder builder = new AlertDialog.Builder(TouchActivity.this);
                        builder.setTitle(currentItem.getName());
                        builder.setItems(options, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // add option to open address in google maps
                            }
                        });
                        builder.show();


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
                        showNextImage();
                        Log.d(msg, "Drag ended");
                        if (dropEventNotHandled(event)) {
                            v.setVisibility(View.VISIBLE);
                        }

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
        curLoc.askForlocation();
        yelp.execute(curLoc.getCurrentLat(), curLoc.getCurrentLong());
        itemLoader = new ItemLoader(yelp);
        itemLoader.execute();

        // Instantiate background process, connect to firebase and fill the Tastr Item queue

        // Wait for an item to be added before trying to load an image into the gui.
        TastrSync sync = new TastrSync();
        sync.execute();
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    int itemCount = 0;
    int loadingError = 0;

    public void getNextRestaurant() {
        currentItem = itemLoader.getNextItem();
        itemCount = currentItem.getMenu().size() - 1;
        showNextImage();
    }

    public void showNextImage() {
        downloadImageTask imageLoader = new downloadImageTask(img);
        if (!currentItem.getMenu().isEmpty() && currentItem.getMenu().size() > itemCount) {
            Log.i("Touch Activity ", "Loading New Image ---> " + currentItem.getMenu().get(itemCount).getImagePath());
            imageLoader.execute(currentItem.getMenu().get(itemCount).getImagePath());
            itemCount++;
        } else {
            Log.i("Touch Activity ", "Can't find any images to download, loading next restaurant... ");
            getNextRestaurant();
            }
        }


    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }

    public void getCurrentTastrItem() {

    }

    // wait for the first Tastr Item to be added
    private class TastrSync extends AsyncTask<String, Void, String> {
        protected String doInBackground(String... params) {

            while (!itemLoader.checkIfReady()) try {
                sleep(100); // wait 100 ms before checking again, saves cpu
            } catch (InterruptedException e) {
                e.printStackTrace(); // if there is a problem while sleeping, print out the errors encountered.
            }

            return null;
        }//doInBackground

        // Everything you want to happen AFTER the doInBackground function is executed. Use this method to make changes to the GUI.
        @Override
        protected void onPostExecute(String results) {
            getNextRestaurant();
        }
    }
}