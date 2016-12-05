package com.smileyface.tastr.Activities;

import android.app.Activity;
import android.content.ClipData;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;
import com.smileyface.tastr.Other.TastrItem;
import com.smileyface.tastr.R;
import com.smileyface.tastr.Utilities.CallHandler;
import com.smileyface.tastr.Utilities.DownloadImageTask;
import com.smileyface.tastr.Utilities.ItemLoader;
import com.smileyface.tastr.Utilities.LocationHandler;
import com.smileyface.tastr.Utilities.YelpDataExecutor;
import com.squareup.picasso.Picasso;

import static java.lang.Thread.sleep;


public class TouchActivity extends Activity {
    private String msg;
    private ProgressBar loadSpinner;
    private RelativeLayout.LayoutParams layoutParams;
    final static private int minHeight = 500;
    final static private int minWidth = 600;
    YelpDataExecutor yelp = new YelpDataExecutor();
    LocationHandler curLoc = new LocationHandler(this);


    private GoogleApiClient client;
    private DownloadImageTask imageLoader;
    ItemLoader itemLoader;
    TastrItem currentItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_touch);
        loadSpinner = (ProgressBar) findViewById(R.id.loadingSpinner);
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();

    }

    public void setDragProps(ImageView image, ImageView green, ImageView red) {
        image.setOnDragListener(new View.OnDragListener() {
            @Override
            public boolean onDrag(View v, DragEvent event) {
                switch (event.getAction()) {
                    default:
                        return true;
                }
            }
        });

        green.setOnDragListener(new View.OnDragListener() {
            @Override
            public boolean onDrag(View v, DragEvent event) {
                switch (event.getAction()) {
                    case DragEvent.ACTION_DROP:
                        Log.d(msg, "Drag ended");
                        if (dropEventNotHandled(event)) {
                            v.setVisibility(View.VISIBLE);
                        }
                        CharSequence options[] = new CharSequence[]{currentItem.getMenu().get(itemCounter - 1).getName(), currentItem.getAddress(), currentItem.getPhone(), currentItem.getRating()};

                        // create a google maps buffer in case the user wants to go to the restaurant found.
                        final Intent mapIntent = new Intent(android.content.Intent.ACTION_VIEW,
                                Uri.parse("google.navigation:q=an+" + currentItem.getAddress()));

                        // create an internet link buffer in case they want to read the reviews on yelp
                        Intent webIntent = new Intent(Intent.ACTION_VIEW);
                        webIntent.setData(Uri.parse("https://www.yelp.com/biz/" + currentItem.getName() + "-" + currentItem.getCity()));

                        AlertDialog.Builder builder = new AlertDialog.Builder(TouchActivity.this);
                        builder.setTitle(currentItem.getName());
                        builder.setItems(options, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which) {
                                    case 1:
                                        startActivity(mapIntent);
                                        break;
                                    case 2:
                                        CallHandler call = new CallHandler(TouchActivity.this, currentItem.getPhone());
                                        call.makePhoneCall();
                                        break;
                                    case 3:
                                        break;
                                    case 4:
                                        break;
                                }
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


        red.setOnDragListener(new View.OnDragListener() {
            int i = 0;
            @Override
            public boolean onDrag(View v, DragEvent event) {
                switch (event.getAction()) {
                    case DragEvent.ACTION_DROP:
                        showNextImage();
                        v.setVisibility(View.VISIBLE);
                        onCreate(null);
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


        image.setOnTouchListener(new View.OnTouchListener() {
            @Override

            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        Log.i("Touch Listener", "Action down triggered");
                        ClipData data = ClipData.newPlainText("", "");
                        View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(v);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            v.startDragAndDrop(data, shadowBuilder, v, 100);
                        } else //noinspection deprecation,deprecation,deprecation
                            v.startDrag(data, shadowBuilder, v, 0);
                    default:
                        Log.i("Touch Listener", "default triggered");
                        v.setVisibility(View.VISIBLE);
                }



                    return true;
                }


        });
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

    int totalItems = 0;
    int itemCounter = 0;
    int loadingError = 0;

    public void getNextRestaurant() {
        itemCounter = 0;
        currentItem = itemLoader.getNextItem();
        totalItems = currentItem.getMenu().size() - 1;
        showNextImage();
    }

    public void showNextImage() {
        ImageView yum = (ImageView) findViewById(R.id.yum);
        ImageView yuck = (ImageView) findViewById(R.id.yuck);
        ImageView img = (ImageView) findViewById(R.id.img);

        setDragProps(img, yum, yuck);
        loadSpinner = (ProgressBar) findViewById(R.id.loadingSpinner);
        loadSpinner.setVisibility(View.VISIBLE);


        //DownloadImageTask imageLoader = new DownloadImageTask(img);
        if (!currentItem.getMenu().isEmpty() && currentItem.getMenu().size() > itemCounter) {
            Picasso.with(this).load(currentItem.getMenu().get(itemCounter).getImagePath()).into(img);
            img.getLayoutParams().height = minHeight;
            img.getLayoutParams().width = minWidth;
            img.setVisibility(View.VISIBLE);
            loadSpinner.setVisibility(View.GONE);
            Log.i("Touch Activity ", "Loading New Image ---> " + currentItem.getMenu().get(itemCounter).getImagePath() + " From " + currentItem.getName());

            itemCounter++;

        } else {
            Log.i("Touch Activity ", "Can't find any more images to download from " + currentItem.getName() + ". Loading next restaurant... ");
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