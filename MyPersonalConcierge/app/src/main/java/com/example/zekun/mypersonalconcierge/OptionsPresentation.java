package com.example.zekun.mypersonalconcierge;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewManager;
import android.widget.Button;
import android.widget.TextView;

import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;


public class OptionsPresentation extends ActionBarActivity {

    Integer myBudgetInt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options_presentation);

        Intent myGivenInfo = getIntent();

        myBudgetInt = Integer.parseInt(getIntent().getStringExtra("inputBudget"));


        myUberClass myUberObj1 = new myUberClass();
        myUberObj1.setDescCostName("Big C is a cool place to be",180, "Big C");

        myUberClass myUberObj2 = new myUberClass();
        myUberObj2.setDescCostName("This place is not furd",20, "UC Berkeley");




    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_options_presentation, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void initializeList(Context myContext){

        ViewGroup parent = (ViewGroup) findViewById(R.id.myScrollLinear);
        parent.removeAllViews();



        //First we grab the file

        File myFile = new File(myFileLocation);
        ArrayList<myMusicClass> myList = new ArrayList<>();

        if(myFile.exists() && !myFile.isDirectory()) {

            try {
                FileInputStream streamIn = new FileInputStream(myFileLocation);
                ObjectInputStream objectinputstream = new ObjectInputStream(streamIn);
                ArrayList<myMusicClass> retrievedList = (ArrayList) objectinputstream.readObject();
                objectinputstream.close();
                streamIn.close();

                myList = retrievedList;
                mySuperList = myList;


            } catch (Exception e) {

                e.printStackTrace();
            }
        }




        /*Now that we have the ArrayList stored in the file, we iterate through it to grab each
        individual music entry to display in the ScrollView
         */

        for (int e=0;e<myList.size();e++) {

            myMusicClass myMusicObj = myList.get(e);

            String myArtist = myMusicObj.artist;
            String mySong = myMusicObj.song;
            String myAlbum = myMusicObj.album;
            int myId = myMusicObj.id;

            /*Now we take care of setting it up visually

             */

            parent = (ViewGroup) findViewById(R.id.myScrollLinear);

            View view = LayoutInflater.from(myContext).inflate(R.layout.basic_song_unit, null);

            final TextView myTextview = (TextView) view.findViewById(R.id.displayWindow);
            myTextview.setText("Artist: " + myArtist + "\nSong: " + mySong + "\nAlbum: " + myAlbum);

            final TextView myIdHolder = (TextView) view.findViewById(R.id.idHolder);
            myIdHolder.setText(Integer.toString(myId));

            final Button deleteButton = (Button) view.findViewById(R.id.button);
            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mySuperList != null) {
                        for (int r = 0; r < mySuperList.size(); r++) {
                            myMusicClass myCurrObj = (mySuperList.get(r));
                            if (myCurrObj.id == Integer.parseInt((String) myIdHolder.getText())) {
                                removeAtIndex(r);
                            }
                        }
                        ((ViewManager) v.getParent()).removeView(myTextview);
                        ((ViewManager) v.getParent()).removeView(deleteButton);


                    }
                    refresh();
                }

            });


            parent.addView(view);
        }
    }
}
