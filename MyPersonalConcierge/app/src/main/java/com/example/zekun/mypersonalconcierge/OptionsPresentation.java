package com.example.zekun.mypersonalconcierge;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

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
        myUberObj1.setDescCostName("Big C is a cool place to be you plebs",180, "Big C",1234);

        myUberClass myUberObj2 = new myUberClass();
        myUberObj2.setDescCostName("This place is not furd",20, "UC Berkeley",0000);


        ArrayList myArray = new ArrayList();

        myArray.add(myUberObj1);
        myArray.add(myUberObj2);

        initializeList(this, myArray);


    }

    /*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_options_presentation, menu);
        return true;
    }
    */

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        /*
        if (id == R.id.action_settings) {
            return true;
        }
        */

        return super.onOptionsItemSelected(item);
    }

    public void initializeList(Context myContext, ArrayList<myUberClass> myList){

        ViewGroup parent = (ViewGroup) findViewById(R.id.myScrollLinear);
        parent.removeAllViews();






        /*Now that we have the ArrayList stored in the file, we iterate through it to grab each
        individual music entry to display in the ScrollView
         */

        for (int e=0;e<myList.size();e++) {

            myUberClass myUberObj = myList.get(e);

            String myDestName = myUberObj.name;
            String myDestDesc = myUberObj.description;
            int myDestCost= myUberObj.cost;
            int myId = myUberObj.id;

            /*Now we take care of setting it up visually

             */

            parent = (ViewGroup) findViewById(R.id.myScrollLinear);

            View view = LayoutInflater.from(myContext).inflate(R.layout.my_options_layout, null);

            final TextView myTitleText = (TextView) view.findViewById(R.id.titleTextView);
            myTitleText.setText(myDestName);

            final TextView myTextview = (TextView) view.findViewById(R.id.displayWindow);
            myTextview.setText(myDestDesc + "\n" + "Cost: " + myDestCost);


            final Button deleteButton = (Button) view.findViewById(R.id.button);
            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }

            });


            parent.addView(view);
        }
    }
}
