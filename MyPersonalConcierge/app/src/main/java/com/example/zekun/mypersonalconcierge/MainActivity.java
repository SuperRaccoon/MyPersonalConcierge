package com.example.zekun.mypersonalconcierge;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;



public class MainActivity extends ActionBarActivity {

    private EditText myBudget;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        myBudget = (EditText) findViewById(R.id.BudgetInputEditText);



        Button CalculateButton = (Button) findViewById(R.id.myButton);
        CalculateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToShowPage();


            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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

    public void goToShowPage(){
        Intent myIntentInfo= new Intent(this, OptionsPresentation.class);
        String  myBudgetInt= myBudget.getText().toString();
        myIntentInfo.putExtra("inputBudget", myBudgetInt);
        startActivity(myIntentInfo);

    }
}
