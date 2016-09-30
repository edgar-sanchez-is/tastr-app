package smileyface.tastr;


import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Process;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import org.scribe.builder.api.GoogleApi;
import java.util.ArrayList;
import android.Manifest;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;


import android.location.Location;

import static java.lang.String.valueOf;


public class YelpActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,LocationListener  {
    private yelpLoader load;
    private GoogleApi mMap;
    String currentLat = null;
    String currentLong = null;
    private static final int PERMISSION_ACCESS_COARSE_LOCATION = 1;



    private static final long POLL_FREQ = 1;
    private static final long ONE_MIN = 1000 * 60;
    private static final long TWO_MIN = ONE_MIN * 2;
    private static final long FIVE_MIN = ONE_MIN * 5;
    private static final long POLLING_FREQ = 1000 * 30;
    private static final long FASTEST_UPDATE_FREQ = 1000 * 5;
    private static final float MIN_ACCURACY = 25.0f;
    private static final float MIN_LAST_READ_ACCURACY = 500.0f;


    private LocationRequest mLocationRequest;
    private Location mBestReading;

    private GoogleApiClient googleApiClient;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_yelp);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_ACCESS_COARSE_LOCATION);
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {


            mLocationRequest = LocationRequest.create();
            mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            mLocationRequest.setInterval(POLL_FREQ);
            mLocationRequest.setFastestInterval(FASTEST_UPDATE_FREQ);
            // ATTENTION: This "addApi(AppIndex.API)"was auto-generated to implement the App Indexing API.
            // See https://g.co/AppIndexing/AndroidStudio for more information.
            googleApiClient = new GoogleApiClient.Builder(this)
                    .addApi(LocationServices.API)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .build();
            googleApiClient.connect();
        }
            }





    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_ACCESS_COARSE_LOCATION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // All good!
                } else {
                    Toast.makeText(this, "Need your location!", Toast.LENGTH_SHORT).show();
                }

                break;
        }
    }




    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.i(YelpActivity.class.getSimpleName(), "Can't connect to Google Play Services!");
        System.err.println("CONNECTION TO GOOGLE FAILED");
        System.err.println("CONNECTION TO GOOGLE FAILED");
        System.err.println("CONNECTION TO GOOGLE FAILED");
        System.err.println("CONNECTION TO GOOGLE FAILED");
    }

    @Override
    public void onConnected(Bundle dataBundle) {
        System.err.println("SUCCESSFULLY CONNECTED");
        System.err.println("SUCCESSFULLY CONNECTED");
        System.err.println("SUCCESSFULLY CONNECTED");
        System.err.println("SUCCESSFULLY CONNECTED");

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_ACCESS_COARSE_LOCATION);
        }

        LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, mLocationRequest,this);
        //currentLong = mCurrentLocation.getLongitude();
        //currentLat = mCurrentLocation.getLatitude();


    }

    private Location bestLastKnownLocation(float minAccuracy, long minTime) {
        System.err.println("GOT TO METHOD: BEST LAST KNOWN LOCATION");
        System.err.println("GOT TO METHOD: BEST LAST KNOWN LOCATION");
        System.err.println("GOT TO METHOD: BEST LAST KNOWN LOCATION");
        Location bestResult = null;
        float bestAccuracy = Float.MAX_VALUE;
        long bestTime = Long.MIN_VALUE;

        // Get the best most recent location currently available
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_ACCESS_COARSE_LOCATION);
        }
        Location mCurrentLocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
        double tempLat = 0;
        tempLat = mCurrentLocation.getLatitude();
        double tempLong = 0;
        tempLong = mCurrentLocation.getLongitude();
        currentLong = valueOf(tempLong);
        System.out.println(currentLong);
        currentLat = valueOf(tempLat);
        System.out.println(currentLat);

        if (mCurrentLocation != null) {
            float accuracy = mCurrentLocation.getAccuracy();
            long time = mCurrentLocation.getTime();

            if (accuracy < bestAccuracy) {
                bestResult = mCurrentLocation;
                bestAccuracy = accuracy;
                bestTime = time;
            }
        }

        // Return best reading or null
        if (bestAccuracy > minAccuracy || bestTime < minTime) {
            return null;
        }
        else {
            return bestResult;
        }
    }

    private boolean servicesAvailable() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);

        if (ConnectionResult.SUCCESS == resultCode) {
            return true;
        } else {
            GooglePlayServicesUtil.getErrorDialog(resultCode, this, 0).show();
            return false;
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }


    public void onLocationChanged(Location location) {
        //your location code here...
        System.err.println("GOT TO METHOD: ON LOCATION CHANGED");
        System.err.println("GOT TO METHOD: ON LOCATION CHANGED");
        System.err.println("GOT TO METHOD: ON LOCATION CHANGED");
        Location newLocation = location;
        if(newLocation!=null) {
            double tempLat = 0;
            tempLat = newLocation.getLatitude();
            double tempLong = 0;
            tempLong = newLocation.getLongitude();
            currentLong = valueOf(tempLong);
            System.out.println(currentLong);
            currentLat = valueOf(tempLat);
            System.out.println(currentLat);
        }
    }

    //Action Listener for clicking the yelp button
    public void yelpClick(View v){
        changeYelpResults("Grabbing Data From Yelp");
        yelpLoader load = new yelpLoader();
        load.execute();
    }

    //Status text for what the system is currently doing. (Such as grabbing data from the Yelp API)
    private void changeYelpResults( String text){
        TextView t = (TextView) findViewById(R.id.Info_Text);
        t.setText(text);
    }
    // Text Field that will fill with Business ID's as Async Task Loader fetches them from yelp. (see post execute for loop below)
    private void listBusinesses(String text){
        TextView t = (TextView) findViewById(R.id.ID_List_Text);
        t.append(text);
    }

    private void clearBusinessResults(){
        TextView t = (TextView) findViewById(R.id.ID_List_Text);
        t.setText("");
    }




    private class yelpLoader extends AsyncTask<String, Void, String> {
        YelpAPI API = new YelpAPI();
        ArrayList<String> businessIDs = new ArrayList<>();
        int numberOfBusinesses = 1;



        private  String result = "No Response Yet";

        void setResult(String input){
            result = input;

        }

        public  String getResult(){
            return result;

        }


        protected String doInBackground(String... params) {
            Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);
            if(currentLat != null && currentLong !=null) {
                API.setLocation(currentLat,currentLong);
            }
            // Starts the API, which will automatically contact yelp and populate the business information.
            API.run();

            // Checks how many Businesses yelp found
            numberOfBusinesses = API.getNumberOfBusinessIDS();

            // Makes a copy of the array of Business ID's found in the most recent Yelp API query. Resets the array list to avoid duplicate data.
            // Also clears the previous results if this is the second time hitting the button.
            businessIDs.clear();
            businessIDs = API.getBusinessIDList();



            return null;


        }
        @Override
        protected void onPostExecute(String result) {

            // This will clear the Business results from view technically, however the user won't notice a change unless the results change on the next API Call.
            // This prevents duplicates from populating on the list as businesses are added. Feel free to comment the line out and test the program if you are confused about what I mean.
            clearBusinessResults();

            int count = 1;// for some reason i returns a two digit number and it screws up the counter as a string, so I just made a new variable for clarity.

            // This populates the list of business ID's one at a time on a new line.
            for(int i = 0; i < numberOfBusinesses; i++){
                listBusinesses("\n" + count + "->" + businessIDs.get(i));
                count ++;

            }
            changeYelpResults("Found "+ numberOfBusinesses + " Businesses! Results listed below.");
        }


    }
}











