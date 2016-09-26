package com.smileyface.tastr;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;


public class YelpActivity extends AppCompatActivity implements Runnable{
    private yelpLoader load;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.smileyface.tastr.R.layout.activity_yelp);



    }
    public void run(){
        YelpAPI test = new YelpAPI();
        Thread t = new Thread(test);

        t.start();
        android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_BACKGROUND);

        test.run();

    }

      public void yelpClick(View v){


        changeYelpResults("Grabbing Data");
          yelpLoader load = new yelpLoader();
          load.execute();


    }


    private void changeYelpResults( String text){

        TextView t = (TextView) findViewById(com.smileyface.tastr.R.id.ResturauntID);
        t.setText(text);

    }

   private class yelpLoader extends AsyncTask<String, Void, String> {
       YelpAPI API = new YelpAPI();
       String yelpText = "Nothing Yet";



        private  String result = "No Response Yet";

        void setResult(String input){
            result = input;

        }

        public  String getResult(){
            return result;

        }


        protected String doInBackground(String... params) {
            android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_BACKGROUND);

            API.run();
           yelpText = API.getYelpInfo();



            return null;


        }
       @Override
       protected void onPostExecute(String result) {

           changeYelpResults(yelpText);
       }


    }



}