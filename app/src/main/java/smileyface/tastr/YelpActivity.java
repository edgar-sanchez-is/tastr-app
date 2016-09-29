package smileyface.tastr;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import org.scribe.builder.api.GoogleApi;

import java.util.ArrayList;


public class YelpActivity extends AppCompatActivity implements Runnable {
    private yelpLoader load;
    private GoogleApi mMap;
    String currentLat = null;
    String currentLong = null;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_yelp);

       /* GPSTracker gpsTracker = new GPSTracker(this);
        ActivityCompat.requestPermissions(YelpAPI, new String[]{"ACCESS_FINE_LOCATION"});


        if (gpsTracker.getIsGPSTrackingEnabled())
        {
            String stringLatitude = String.valueOf(gpsTracker.latitude);
            String stringLongitude = String.valueOf(gpsTracker.longitude);
            currentLat = stringLatitude;
            currentLong = stringLongitude;
        }
        else{
            System.err.println("Warning: GPS is not enabled!");
        }*/
    }

    public void run(){
        YelpAPI test = new YelpAPI();
        Thread t = new Thread(test);

        t.start();
        android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_BACKGROUND);

        test.run();

    }




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
        int numberOfBusinesses = 0;



        private  String result = "No Response Yet";

        void setResult(String input){
            result = input;

        }

        public  String getResult(){
            return result;

        }


        protected String doInBackground(String... params) {
            android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_BACKGROUND);
            if(currentLong!= null && currentLat!= null) {
                API.setLocation(currentLat + "," + currentLong);
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



