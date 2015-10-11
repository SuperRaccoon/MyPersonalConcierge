package com.example.zekun.mypersonalconcierge;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;




public class OptionsPresentation extends ActionBarActivity  {

    Integer myBudgetInt;




    private Location myLocation;

    private String myLocationString;

    public static ArrayList<myUberClass> mySuperList;






    //MARCO'S STUFF
    static TextView label;
    String server_id = "7UB1w9eL1gz6_228W70N02oQFlU89HX5IR2Kjj5f";
    String start_latitude = "37.8668";
    String start_longitude = "-122.2536";
    Destination destinations[] = {new Destination("test_name", "test_desc","37.8545738", "-122.2918573" , ""),
            new Destination("test_name2",
                    "test_desc2","37.9045738", "-122.2818573", "" ),
            new Destination("test_name3",
                    "test_desc2","37.8045738", "-122.3018573","" ),
            new Destination("test_name4",
                    "test_desc2","37.9145738", "-122.7818573","" )};






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options_presentation);


        //START THE GOOGLE LOCATION SERVICES




        //PARSE THE USER SUBMITTED INFO FROM THE INTENT
        Intent myGivenInfo = getIntent();

        myBudgetInt = Integer.parseInt(getIntent().getStringExtra("inputBudget"));


        TextView label = (TextView) findViewById(R.id.textView);




        label.setText("This is where you can go with $" + myBudgetInt.toString() + ". You are located at ");




        //MY SAMPLES FOR TESTING
        myUberClass myUberObj1 = new myUberClass();
        myUberObj1.setDescCostName("Big C is a cool place to be you plebs", 180, "Big C", 1234);

        myUberClass myUberObj2 = new myUberClass();
        myUberObj2.setDescCostName("This place is not furd", 20, "UC Berkeley", 0000);


        ArrayList myArray = new ArrayList();

        myArray.add(myUberObj1);
        myArray.add(myUberObj2);


       //MAKE THE DISPLAY LIST


        mySuperList = myArray;
        initializeList(this, myArray);


    }



    public void initializeList(Context myContext, ArrayList<myUberClass> myList){

        ViewGroup parent = (ViewGroup) findViewById(R.id.myScrollLinear);
        parent.removeAllViews();






        /*Now that we have the ArrayList stored in the file, we iterate through it to grab each
        individual music entry to display in the ScrollView
         */

        for (int e=0;e<myList.size();e++) {

            final myUberClass myUberObj = myList.get(e);

            String myDestName = myUberObj.name;
            String myDestDesc = myUberObj.description;
            int myDestCost= myUberObj.cost;
            final int myId = myUberObj.id;

            /*Now we take care of setting it up visually

             */

            final TextView label = (TextView) findViewById(R.id.textView);

            parent = (ViewGroup) findViewById(R.id.myScrollLinear);

            View view = LayoutInflater.from(myContext).inflate(R.layout.my_options_layout, null);

            final TextView myTitleText = (TextView) view.findViewById(R.id.titleTextView);
            myTitleText.setText(myDestName);

            final TextView myTextview = (TextView) view.findViewById(R.id.displayWindow);
            myTextview.setText(myDestDesc + "\n" + "Cost: " + myDestCost);

            final TextView myIdHolder = (TextView) view.findViewById(R.id.idHolder);
            myIdHolder.setText(Integer.toString(myId));


            final Button deleteButton = (Button) view.findViewById(R.id.button);
            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mySuperList != null) {
                        for (int r = 0; r < mySuperList.size(); r++) {
                            myUberClass myCurrObj = (mySuperList.get(r));
                            if (myCurrObj.id == Integer.parseInt((String) myIdHolder.getText())) {
                                myUberClass myChosenDestObj = idObjLookup(myId);
                                label.setText("You picked " + myChosenDestObj.name);
                            }
                        }



                    }

                }

            });


            parent.addView(view);
        }
    }


    private myUberClass idObjLookup(int id){
        myUberClass myCurrObj1 = new myUberClass();
        for (int i = 0; i<mySuperList.size();i++){
            myCurrObj1 =  mySuperList.get(i);
            if (myCurrObj1.id == id){
                return myCurrObj1;
            }
        }

        return myCurrObj1;
    }


}
