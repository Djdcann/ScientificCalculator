package com.example.gagnej3.scientificcalculator;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

/**
 * This class handles all the button widget declarations for the SimpleCalculator Fragment
 * It passes data through the Communicator interface into CalculatorMain.
 * This data then goes to the HandleDisplay class where it is interpreted based on the current
 * display
 * Created by gagnej3 on 10/28/15.
 */
public class SimpleCalulator extends Fragment implements View.OnClickListener {

    Communicator communicator;

    public boolean isSimpleFunction = true;

    //All the buttons needed for the standard calculator view
    protected Button one;
    protected Button two;
    protected Button three;
    protected Button four;
    protected Button five;
    protected Button six;
    protected Button seven;
    protected Button eight;
    protected Button nine;
    protected Button zero;
    protected Button allClear;
    protected Button divide;
    protected Button multiply;
    protected Button deleteOne;
    protected Button minus;
    protected Button plus;
    protected Button parentheses;
    protected Button equalSign;
    protected Button plusMinus;
    protected Button decimal;

    private TextView mDisplayBox;

    public SimpleCalulator(){

    }

    //Will allow the textview in the main to have a valid address
    public TextView getmDisplayBox() {
        return mDisplayBox;
    }

    //will allow us to update the textview from the main
    public void setmDisplayBox(TextView display){
        String text = display.getText().toString();
        mDisplayBox.setText(text);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //NEED THIS FOR REFERENCE TO THE CURRENT ACTIVITY
        communicator = (Communicator) getActivity();

        mDisplayBox = (TextView) getActivity().findViewById(R.id.text_view_user_input);

        one = (Button) getActivity().findViewById(R.id.one_button);
        one.setOnClickListener(this);

        two =(Button) getActivity().findViewById(R.id.two_button);
        two.setOnClickListener(this);

        three = (Button) getActivity().findViewById(R.id.three_button);
        three.setOnClickListener(this);

        four = (Button) getActivity().findViewById(R.id.four_button);
        four.setOnClickListener(this);

        five =(Button) getActivity().findViewById(R.id.five_button);
        five.setOnClickListener(this);

        six = (Button) getActivity().findViewById(R.id.six_button);
        six.setOnClickListener(this);

        seven = (Button) getActivity().findViewById(R.id.seven_button);
        seven.setOnClickListener(this);

        eight = (Button) getActivity().findViewById(R.id.eight_button);
        eight.setOnClickListener(this);

        nine = (Button) getActivity().findViewById(R.id.nine_button);
        nine.setOnClickListener(this);

        zero = (Button) getActivity().findViewById(R.id.zero_button);
        zero.setOnClickListener(this);

        allClear = (Button) getActivity().findViewById(R.id.clear_button);
        allClear.setOnClickListener(this);

        divide = (Button) getActivity().findViewById(R.id.divide_button);
        divide.setOnClickListener(this);

        multiply = (Button) getActivity().findViewById(R.id.multiply_button);
        multiply.setOnClickListener(this);

        deleteOne = (Button) getActivity().findViewById(R.id.delete_button);
        deleteOne.setOnClickListener(this);

        minus = (Button) getActivity().findViewById(R.id.minus_button);
        minus.setOnClickListener(this);

        plus = (Button) getActivity().findViewById(R.id.plus_button);
        plus.setOnClickListener(this);

        parentheses = (Button) getActivity().findViewById(R.id.parentheses_button);
        parentheses.setOnClickListener(this);

        equalSign = (Button) getActivity().findViewById(R.id.equal_button);
        equalSign.setOnClickListener(this);

        plusMinus = (Button) getActivity().findViewById(R.id.plus_minus_button);
        plusMinus.setOnClickListener(this);

        decimal = (Button) getActivity().findViewById(R.id.decimal_button);
        decimal.setOnClickListener(this);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_simple_calculator,container,false);
    }

    @Override
    public void onClick(View v) {
        selectPressedButton(v);
    }

    protected void selectPressedButton(View v){
        //Checks the id of what just got clicked
        switch(v.getId()) {
            case R.id.one_button:
                communicator.getStringData("1");
                break;
            case R.id.two_button:
                communicator.getStringData("2");
                break;
            case R.id.three_button:
                communicator.getStringData("3");
                break;
            case R.id.four_button:
                communicator.getStringData("4");
                break;
            case R.id.five_button:
                communicator.getStringData("5");
                break;
            case R.id.six_button:
                communicator.getStringData("6");
                break;
            case R.id.seven_button:
                communicator.getStringData("7");
                break;
            case R.id.eight_button:
                communicator.getStringData("8");
                break;
            case R.id.nine_button:
                communicator.getStringData("9");
                break;
            case R.id.zero_button:
                communicator.getStringData("0");
                break;
            case R.id.clear_button:
                communicator.getStringData("clear");
                break;
            case R.id.divide_button:
                communicator.getStringData("/");
                break;
            case R.id.multiply_button:
                communicator.getStringData("*");
                break;
            case R.id.delete_button:
                communicator.getStringData("delete");
                break;
            case R.id.minus_button:
                communicator.getStringData("-");
                break;
            case R.id.plus_button:
                communicator.getStringData("+");
                break;
            case R.id.parentheses_button:
                communicator.getStringData("()");
                break;
            case R.id.equal_button:
                communicator.getStringData("=");
                break;
            case R.id.plus_minus_button:
                communicator.getStringData("+-");
                break;
            case R.id.decimal_button:
                communicator.getStringData(".");
                break;
            default:
                isSimpleFunction = false;
                break;
        }
    }

}
