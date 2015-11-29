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
 * Declares and defines all the buttons in the fragment
 * Created by gagnej3 on 11/22/15.
 */
public class ScientificCalcuator extends Fragment  implements View.OnClickListener {

    Communicator communicator;

    private String logMSG = "dbug ScientificCalculator";

    private Button squareRoot;
    private Button ans;
    private Button sin;
    private Button cos;
    private Button tan;
    private Button ln;
    private Button log;
    private Button oneDividedByX;
    private Button eToTheX;
    private Button xSquared;
    private Button yToTheX;
    private Button absOfX;
    private Button pi;
    private Button exponential;

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

    public TextView mDisplayBox;

    public ScientificCalcuator(){

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

    public void onActivityCreated(Bundle savedInstanceState) {

        //Calls the onActivityCreated for the super class to initialize the
        super.onActivityCreated(savedInstanceState);

        //NEED THIS FOR REFERENCE TO THE CURRENT ACTIVITY
        communicator = (Communicator) getActivity();

        try {

            mDisplayBox = (TextView) getActivity().findViewById(R.id.text_view_user_input);

            one = (Button) getActivity().findViewById(R.id.one_button2);
            one.setOnClickListener(this);

            two = (Button) getActivity().findViewById(R.id.two_button2);
            two.setOnClickListener(this);

            three = (Button) getActivity().findViewById(R.id.three_button2);
            three.setOnClickListener(this);

            four = (Button) getActivity().findViewById(R.id.four_button2);
            four.setOnClickListener(this);

            five = (Button) getActivity().findViewById(R.id.five_button2);
            five.setOnClickListener(this);

            six = (Button) getActivity().findViewById(R.id.six_button2);
            six.setOnClickListener(this);

            seven = (Button) getActivity().findViewById(R.id.seven_button2);
            seven.setOnClickListener(this);

            eight = (Button) getActivity().findViewById(R.id.eight_button2);
            eight.setOnClickListener(this);

            nine = (Button) getActivity().findViewById(R.id.nine_button2);
            nine.setOnClickListener(this);

            zero = (Button) getActivity().findViewById(R.id.zero_button2);
            zero.setOnClickListener(this);

            allClear = (Button) getActivity().findViewById(R.id.clear_button2);
            allClear.setOnClickListener(this);

            divide = (Button) getActivity().findViewById(R.id.divide_button2);
            divide.setOnClickListener(this);

            multiply = (Button) getActivity().findViewById(R.id.multiply_button2);
            multiply.setOnClickListener(this);

            deleteOne = (Button) getActivity().findViewById(R.id.delete_button2);
            deleteOne.setOnClickListener(this);

            minus = (Button) getActivity().findViewById(R.id.minus_button2);
            minus.setOnClickListener(this);

            plus = (Button) getActivity().findViewById(R.id.plus_button2);
            plus.setOnClickListener(this);

            parentheses = (Button) getActivity().findViewById(R.id.parentheses_button2);
            parentheses.setOnClickListener(this);

            equalSign = (Button) getActivity().findViewById(R.id.equal_button2);
            equalSign.setOnClickListener(this);

            plusMinus = (Button) getActivity().findViewById(R.id.plus_minus_button2);
            plusMinus.setOnClickListener(this);

            decimal = (Button) getActivity().findViewById(R.id.decimal_button2);
            decimal.setOnClickListener(this);

            squareRoot = (Button) getActivity().findViewById(R.id.square_root_button);
            squareRoot.setOnClickListener(this);

            ans = (Button) getActivity().findViewById(R.id.ans_button);
            ans.setOnClickListener(this);

            sin = (Button) getActivity().findViewById(R.id.sin_button);
            sin.setOnClickListener(this);

            cos = (Button) getActivity().findViewById(R.id.cos_button);
            cos.setOnClickListener(this);

            tan = (Button) getActivity().findViewById(R.id.tan_button);
            tan.setOnClickListener(this);

            ln = (Button) getActivity().findViewById(R.id.ln_button);
            ln.setOnClickListener(this);

            log = (Button) getActivity().findViewById(R.id.log_button);
            log.setOnClickListener(this);

            oneDividedByX = (Button) getActivity().findViewById(R.id.one_divided_by_x_button);
            oneDividedByX.setOnClickListener(this);

            eToTheX = (Button) getActivity().findViewById(R.id.e_to_the_x_button);
            eToTheX.setOnClickListener(this);

            xSquared = (Button) getActivity().findViewById(R.id.x_squared_button);
            xSquared.setOnClickListener(this);

            yToTheX = (Button) getActivity().findViewById(R.id.y_to_the_x_button);
            yToTheX.setOnClickListener(this);

            absOfX = (Button) getActivity().findViewById(R.id.abs_of_x_button);
            absOfX.setOnClickListener(this);

            pi = (Button) getActivity().findViewById(R.id.pi_button);
            pi.setOnClickListener(this);

            exponential = (Button) getActivity().findViewById(R.id.e_button);
            exponential.setOnClickListener(this);
        } catch (Exception e){
            Log.e(logMSG,"Can't assign listeners");
        }

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_scientific_calculator,container,false);
    }


    @Override
    public void onClick(View v) {
        selectPressedButton(v);
    }

    protected void selectPressedButton(View v){

        //enters this if the value pressed is a scientific function
        switch (v.getId()){
            case R.id.square_root_button:
                communicator.getStringData("^(1/2)");
                break;
            case R.id.ans_button:
                communicator.getStringData("ans");
                break;
            case R.id.sin_button:
                communicator.getStringData("sin");
                break;
            case R.id.cos_button:
                communicator.getStringData("cos");
                break;
            case R.id.tan_button:
                communicator.getStringData("tan");
                break;
            case R.id.ln_button:
                communicator.getStringData("ln");
                break;
            case R.id.log_button:
                communicator.getStringData("log");
                break;
            case R.id.one_divided_by_x_button:
                communicator.getStringData("1/");
                break;
            case R.id.e_to_the_x_button:
                communicator.getStringData("e^");
                break;
            case R.id.x_squared_button:
                communicator.getStringData("x^2");
                break;
            case R.id.y_to_the_x_button:
                communicator.getStringData("y^x");
                break;
            case R.id.abs_of_x_button:
                communicator.getStringData("abs");
                break;
            case R.id.pi_button:
                communicator.getStringData("pi");
                break;
            case R.id.e_button:
                communicator.getStringData("e");
                break;
            case R.id.one_button2:
                communicator.getStringData("1");
                break;
            case R.id.two_button2:
                communicator.getStringData("2");
                break;
            case R.id.three_button2:
                communicator.getStringData("3");
                break;
            case R.id.four_button2:
                communicator.getStringData("4");
                break;
            case R.id.five_button2:
                communicator.getStringData("5");
                break;
            case R.id.six_button2:
                communicator.getStringData("6");
                break;
            case R.id.seven_button2:
                communicator.getStringData("7");
                break;
            case R.id.eight_button2:
                communicator.getStringData("8");
                break;
            case R.id.nine_button2:
                communicator.getStringData("9");
                break;
            case R.id.zero_button2:
                communicator.getStringData("0");
                break;
            case R.id.clear_button2:
                communicator.getStringData("clear");
                break;
            case R.id.divide_button2:
                communicator.getStringData("/");
                break;
            case R.id.multiply_button2:
                communicator.getStringData("*");
                break;
            case R.id.delete_button2:
                communicator.getStringData("delete");
                break;
            case R.id.minus_button2:
                communicator.getStringData("-");
                break;
            case R.id.plus_button2:
                communicator.getStringData("+");
                break;
            case R.id.parentheses_button2:
                communicator.getStringData("()");
                break;
            case R.id.equal_button2:
                communicator.getStringData("=");
                break;
            case R.id.plus_minus_button2:
                communicator.getStringData("+-");
                break;
            case R.id.decimal_button2:
                communicator.getStringData(".");
                break;
            default:
                break;
        }
    }
}
