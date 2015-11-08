package com.example.gagnej3.scientificcalculator;

import android.util.Log;
import android.widget.TextView;

/**
 *
 * Someone needs to find a way to set the textView to blank rather than a space. The issue is
 * inside the checkSpecialCase method
 * Created by gagnej3 on 10/31/15.
 */
public class HandleDisplay {
    private int openParenCount = 0;
    private int closeParenCount = 0;
    private String dbugMSG = "dbug HandleDisplay";

    //Updates the text box to be displayed
    public TextView updateTextView(TextView currentDisplay, String newValue) {
        Log.d("dbug HandleUserInput",  "updateTextView");

        TextView updatedDisplay;// = currentDisplay;
        String currentText;

        updatedDisplay = checkSpecialCase(currentDisplay, newValue);

        return updatedDisplay;
    }


    private TextView checkSpecialCase(TextView currentDisplay, String command){
        TextView updateDisplay = currentDisplay;
        //Gets the text displayed on the screen
        String currentText = currentDisplay.getText().toString();

        String updatedText = " ";

        //This is only checked in the isOperator method, which is handled properly if it isn't initialized
        String lastChar = "";

        try {
            char lastCharInStr = currentText.charAt(currentText.length() - 1);
            lastChar += lastCharInStr;
        }catch (Exception e){
            Log.d(dbugMSG, "LastCharSTR out of range");
        }

        //need to figure out how to set the text view to null rather than just a space: " "
        if(command.equals("delete")) {
            updatedText = handleDeleteValues(currentText,lastChar);
        }
        else if(isParenthesis(command)){
            updatedText = handleParentheses(command,lastChar, currentText);
        }
        //Same issue as above code block
        else if( command.equals("clear")) {
            updatedText = " ";
        }
        //Places "0." in the string if decimal is pressed with: an empty string or after an operator
        else if(  isDecimalPoint(command) &&  (currentText.equals(" ") || isOperator(lastChar)) ){
            updatedText = currentText + "0.";
        }

        //This will instantiate a new UserInput to handle the mathematical input
        //This should come before checking for a duplicate operator. "=" should end the current operation
        else if (command.equals("=")) {
            if(isOperator(lastChar)){
                updatedText = currentText + "\n Incorrect Format";
            }
            //CALL THE EXECUTE COMMAND METHOD
        }

        else if (isDuplicateOperator(command, currentText)){
            updatedText = currentText;
        }

        //If there are no special action associated with the specified command
        //all we need to do is append the string
        else{
            updatedText= currentText + command;
        }

        updateDisplay.setText(updatedText);
        return updateDisplay;
    }

    /**
     * Decides what values need to be returned and altered depending on what value is being deleted
     * @param currentText The current string being displayed
     * @param lastChar The last char of the string being displayed
     * @return Returns the updated value after deletion
     */
    private String handleDeleteValues(String currentText, String lastChar){
        String updatedText = " ";

        //if(currentText.contains("Incorrect Format")){
            //This needs to be defined
        //}
        if(currentText.length() == 1 || currentText.equals(null)) { //If string is already null, we can't remove any values
            updatedText = " ";
        }
        //Because we use a count to track what paren. we need, this must be updated when using the delete key
        else if (isParenthesis(lastChar)){
            if(lastChar.equals("(")){
                openParenCount--;
                updatedText = currentText.substring(0, currentText.length() - 1);
            } else{
                closeParenCount--;
                updatedText = currentText.substring(0, currentText.length() - 1);
            }
        }
        else{   //The updated text keeps all except the final letter of the initial string
            updatedText = currentText.substring(0, currentText.length() - 1);
        }

        return updatedText;
    }

    /**
     *
     * @param command The button pressed, in this case will be ()
     * @param lastChar The last value of the String
     * @param currentText The current string being displayed to the user
     * @return returns the updated string based on the when the parenthesis button was pressed
     */
    private String handleParentheses(String command, String lastChar, String currentText){
        String updatedText = "";
        //We need an opening Paren for these situations
        if(currentText.equals(" ") || isOperator(lastChar)) {
            updatedText = currentText + "(";
            openParenCount++;
        }
        //Last value is a digit and all paren groups are paired
        else if ((isDigit(lastChar) || isParenthesis(lastChar)) && openParenCount == closeParenCount) {
            updatedText = currentText + "*(";
            openParenCount++;
        }
        else if((isDigit(lastChar) || isParenthesis(lastChar) )&& (closeParenCount < openParenCount)){
            updatedText = currentText + ")";
            closeParenCount++;
        }
        else
            updatedText = currentText + ")";

        return updatedText;
    }

    /**
     * Will prevent the user form entering multiple operators
     * @param command Contains the value of the button pressed by the user
     * @param currentText Contains the current content being displayed in the text view
     * @return returns true if the last value of Current text is an Operator and the command is an operator
     *          returns false otherwise
     */
    public boolean isDuplicateOperator(String command, String currentText) {
        char lastChar = currentText.charAt(currentText.length() - 1);
        String lastCharStr = "" + lastChar;

        //consecutive digits are fine
        if(isDigit(command)){
            return false;
        }
        //We don't want multiple decimal points
        if(isDecimalPoint(command) && isDecimalPoint(lastCharStr)){
            return true;
        }
        //We don't want multiple operators
        if(isOperator(command) && isOperator(lastCharStr)){
            return true;
        }
        return false;
    }

    private boolean isDecimalPoint(String command){
        if(command.charAt(0) == 46)
            return true;
        else
            return false;
    }
    private boolean isDigit(String command){
        if(command.charAt(0) >=48 && command.charAt(0) <=57)
            return true;
        else
            return false;
    }
    private boolean isParenthesis(String command){
        if (command.equals("(") || command.equals(")") || command.equals("()"))
            return true;
        else
            return false;
    }
    //private boolean

    //Checks if the current command is an operator or not
    public boolean isOperator (String command){
        boolean isOp = false;
        if(isDigit(command)){
            isOp =  false;
        } else if(isDecimalPoint(command)){
            isOp  = false;
        } else if (isParenthesis(command)){
            isOp = false;
        } else if(command.equals(null) || command.equals(" ")){
            return false;
        } else {
            isOp = true;
        }
        return isOp;
    }

}