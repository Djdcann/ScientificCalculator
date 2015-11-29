package com.example.gagnej3.scientificcalculator;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

/**
 * This is the min class. It calls the SimpleCalculator into the UI. Also acts as a middle ground
 * for handling the data from the UI to the textView
 * @author gagnej3
 */
public class CalculatorMain extends Activity implements Communicator {

    SimpleCalulator simpleCalulator;
    ScientificCalcuator scientificCalcuator;

    TextView mDisplayBox;
    HandleDisplay handleDisplay;

    private String dbugMSG = "dbug CalculatorMain";

    FragmentManager manager;
    FragmentTransaction transaction;

    private boolean isDisplayingStandardCalc = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculator_main);

        scientificCalcuator = new ScientificCalcuator();
        simpleCalulator = new SimpleCalulator();

        int orientation  = getScreenOrientation();
        Log.d(dbugMSG, "Orientation: " + orientation);

        //handles what fragment should be placed in the view on create
        //I noticed it's less buggy if you declare a manage and transaction individually for each instance
        if(orientation == Configuration.ORIENTATION_PORTRAIT){

            if(scientificCalcuator.isVisible()){
                manager = getFragmentManager();
                transaction = manager.beginTransaction();
                transaction.remove(scientificCalcuator);
                transaction.commit();
            }
            manager = getFragmentManager();
            transaction = manager.beginTransaction();
            transaction.add(R.id.fragment_container, simpleCalulator);
            transaction.commit();
            isDisplayingStandardCalc = true;
            Log.d(dbugMSG, "In portrait ");

        }
        else{
            if (simpleCalulator.isVisible()){
                manager = getFragmentManager();
                transaction = manager.beginTransaction();
                transaction.remove(simpleCalulator);
                transaction.commit();
            }
            manager = getFragmentManager();
            transaction = manager.beginTransaction();
            transaction.add(R.id.fragment_container_landscape, scientificCalcuator);
            transaction.commit();
            isDisplayingStandardCalc = false;
        }

        if(isDisplayingStandardCalc){
            mDisplayBox = simpleCalulator.getmDisplayBox();
        }else{
            mDisplayBox = scientificCalcuator.getmDisplayBox();
        }
        //updateFragmentDisplay();

        //Instantiate a class to handle the user input
        handleDisplay = new HandleDisplay();
    }

    private void updateFragmentDisplay(){
        if(isDisplayingStandardCalc){
            //This will be necessary when the is text being displayed on the screen
            try {
                simpleCalulator.setmDisplayBox(mDisplayBox);
            } catch(Exception e){
                Log.d(dbugMSG, "Null text view");
            }

            mDisplayBox = simpleCalulator.getmDisplayBox();
        }
        else {
            try {
                scientificCalcuator.setmDisplayBox(mDisplayBox);
            } catch (Exception e){
                Log.d(dbugMSG, "Null text view");
            }
            mDisplayBox = scientificCalcuator.getmDisplayBox();
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig){
        super.onConfigurationChanged(newConfig);
        Log.d(dbugMSG, "Rotated");

        String currentText;
        int newOrientation;
        newOrientation = newConfig.orientation;


        switch(newOrientation){

            case Configuration.ORIENTATION_LANDSCAPE:
                    //before removing the fragment, get the string from it
                    try{
                        manager = getFragmentManager();
                        transaction = manager.beginTransaction();
                        transaction.remove(simpleCalulator);
                        transaction.commit();
                        Log.d(dbugMSG, "fragment removed");

                    }catch (Exception e){
                        Log.d(dbugMSG, "Error removing fragment");
                    }
                    //remove the fragment then open the rotated activity
                    Log.d(dbugMSG, "Starting next activity");

                    manager = getFragmentManager();
                    transaction = manager.beginTransaction();
                    transaction.add(R.id.fragment_container, scientificCalcuator);
                    transaction.commit();
                    isDisplayingStandardCalc = false;
                    break;

            case Configuration.ORIENTATION_PORTRAIT:
                    try {
                        manager = getFragmentManager();
                        transaction = manager.beginTransaction();
                        transaction.remove(scientificCalcuator);
                        transaction.commit();
                    } catch (Exception e){
                        Log.d(dbugMSG, "Error removing fragment");
                    }

                    manager = getFragmentManager();
                    transaction = manager.beginTransaction();
                    transaction.add(R.id.fragment_container, simpleCalulator);
                    transaction.commit();
                    isDisplayingStandardCalc = true;
                    break;

                default:
                    Log.d(dbugMSG, "Error on config change");
                    break;
        }
    }

    public int getScreenOrientation()
    {
        Display getOrient = getWindowManager().getDefaultDisplay();
        int orientation;
        if(getOrient.getWidth()==getOrient.getHeight()){
            orientation = Configuration.ORIENTATION_SQUARE;
        } else{
            if(getOrient.getWidth() < getOrient.getHeight()){
                orientation = Configuration.ORIENTATION_PORTRAIT;
            }else {
                orientation = Configuration.ORIENTATION_LANDSCAPE;
            }
        }
        return orientation;
    }

    /**
     * gets the string value that corresponds to the button pressed in one of the fragments.
     * Decides which fragment is being displayed and which text view is going to be updated
     * based on the input.
     * Calls updateTextView method.. belongs to the HandleDisplay class.
     * This is where the text formatting is done
     * @param data string value corresponding to the button pressed by the user
     */
    @Override
    public void getStringData(String data) {
        if(isDisplayingStandardCalc){
            mDisplayBox = simpleCalulator.getmDisplayBox();
        }else {
            mDisplayBox = scientificCalcuator.getmDisplayBox();
        }

        //Update what the user is seeing
        mDisplayBox = handleDisplay.updateTextView( mDisplayBox, data);

        if(isDisplayingStandardCalc){
            simpleCalulator.setmDisplayBox(mDisplayBox);
        }
        else{
            scientificCalcuator.setmDisplayBox(mDisplayBox);
        }
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
