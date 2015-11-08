package com.example.gagnej3.scientificcalculator;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

/**
 * This is the min class. It calls the SimpleCalculator into the UI. Also acts as a middle ground
 * for handling the data from the UI to the textView
 * @author gagnej3
 */
public class CalculatorMain extends Activity implements Communicator {

    SimpleCalulator simpleCalulator;
    TextView mDisplayBox;
    HandleDisplay handleDisplay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculator_main);

         mDisplayBox = (TextView) findViewById(R.id.user_input_text_view);
         mDisplayBox.setVisibility(View.VISIBLE);

        //Instantiate a class to handle the user input
        handleDisplay = new HandleDisplay();
    }

    //Function called by the interface
    @Override
    public void getStringData(String data) {
        Log.d("dbug", "Attempting to change the text");

        //Update what the user is seeing
         mDisplayBox = handleDisplay.updateTextView( mDisplayBox, data);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_basic_calculator, menu);
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


}
