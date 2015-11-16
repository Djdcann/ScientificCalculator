package com.example.gagnej3.scientificcalculator;

import android.util.Log;
import android.widget.TextView;

/**
 * Need to figure out how to undo the plus/minus command
 * Then need to define the enter key
 * Someone needs to find a way to set the textView to blank rather than a space. The issue is
 * inside the checkSpecialCase method
 * Created by gagnej3 on 10/31/15.
 */
public class HandleDisplay {

    private int openParenCount = 0;
    private int closeParenCount = 0;
    private int lastIndex = 0;

    private String dbugMSG = "dbug HandleDisplay";
    private String mSolution;

    private boolean isCurrentlyNegative = false;
    private boolean isDisplayingAnswer = false;

    //Updates the text box to be displayed
    public TextView updateTextView(TextView currentDisplay, String newValue) {
        Log.d("dbug HandleUserInput",  "updateTextView");

        TextView updatedDisplay;// = currentDisplay;
        String currentText;

        updatedDisplay = checkSpecialCase(currentDisplay, newValue);

        return updatedDisplay;
    }

    private TextView checkSpecialCase(TextView currentDisplay, String command){
        TextView updateDisplay;
        //Gets the text displayed on the screen
        String currentText = currentDisplay.getText().toString();

        String updatedText;

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
        else if(command.equals("clear")) {
            updatedText = handleClearAll();
        }
        //Places "0." in the string if decimal is pressed with: an empty string or after an operator
        else if(isDecimalPoint(command)){
            updatedText = handleDecimalPoint(command,lastChar,currentText);
        }
        else if (command.equals("+-")){
            updatedText = handlePlusMinus(currentText,lastChar);
        }

        //This will instantiate a new UserInput to handle the mathematical input
        //This should come before checking for a duplicate operator. "=" should end the current operation
        else if (command.equals("=")) {
            updatedText = handleEqualsButton(currentText, lastChar);
        }
        else if(isOperator(command)){
            updatedText = handleOperator(command,currentText,lastChar);
        }
        //If there are no special action associated with the specified command, all we need to do is append the string
        else{
            if (isCloseParen(lastChar) && isDigit(command)){
                updatedText = currentText + "*" + command;
            }
            else
                updatedText= currentText + command;
        }

        updateDisplay = currentDisplay;
        updateDisplay.setText(updatedText);
        return updateDisplay;
    }

    /**
     * Decides what to do when the "=" button is pressed
     * @param currentText the current text being displayed in the text view
     * @param lastChar last value in the string
     * @return the proper output depending on the user format
     */
    private String handleEqualsButton(String currentText, String lastChar){
        String updatedText;

        if(isOperator(lastChar)){
            updatedText = currentText + "\n" + "Incorrect Format";
        }
        else{
            if(openParenCount > closeParenCount){
                int difference = openParenCount - closeParenCount;
                for (int i = 0; i < difference; i++){
                    currentText += ")";
                }
            }

            ComputeInput computeInput = new ComputeInput();
            String solution;
            solution = computeInput.computeSolution(currentText);
            mSolution = solution;
            updatedText = currentText + '\n' + "= "  + solution;
        }

        openParenCount = 0;
        closeParenCount = 0;
        lastIndex = currentText.length();
        isDisplayingAnswer = true;
        return updatedText;
    }

    private String handleOperator(String command, String currentText, String lastChar){
        String updatedText;

        if(isDecimalPoint(lastChar)){
            updatedText = currentText;
        }
        else if (isOperator(lastChar) || isOpenParen(lastChar)){
            updatedText = currentText;
        }
        //Does some last minute handling to the parentheses formatting
        else if(isCurrentlyNegative && !isDigit(command) && !isParenthesis(lastChar) && !isDecimalPoint(lastChar)){
            updatedText= currentText + ")" + command;
            closeParenCount++;
            isCurrentlyNegative = false;
        }
        else{
            updatedText = currentText + command;
        }

        return updatedText;
    }

    /**
     * Resets the paren count values to zero on all clear. returns a blank string.
     * @return updated text
     */
    private String handleClearAll(){
        String updatedText = " ";
        openParenCount = 0;
        closeParenCount = 0;
        isDisplayingAnswer = false;

        return updatedText;
    }

    /**
     * Decides what to do depending on when the decimal point is pressed.
     * @param command The button that was pressed by the user
     * @param lastChar The last char in the current display
     * @param currentText Current string
     * @return Updated text
     */
    private String handleDecimalPoint(String command, String lastChar, String currentText){
        String updatedText;

        if(isCloseParen(lastChar)){
            updatedText = currentText + "*(0.";
            openParenCount++;
        }
        else if(containsDecimal(currentText, lastChar)){
            updatedText = currentText;
        }
        else if((currentText.equals(" ") || isOperator(lastChar) || isParenthesis(lastChar))){
            updatedText = currentText + "0.";
        }
        //don't update it if the command and the last char are both decimals
        else if (isDecimalPoint(command) && isDecimalPoint(lastChar))
            updatedText = currentText;

        else
            updatedText = currentText + ".";


        return updatedText;
    }

    /**
     * Decides what to append to the textBox depending on what is displayed when the plus minus
     * symbol is pressed
     * @param currentText the current text being displayed
     * @param lastChar last char of the text displayed
     * @return the updated value
     */
    private String handlePlusMinus(String currentText, String lastChar){
        String updatedText = " ";

        if(isDisplayingAnswer){
            updatedText = currentText;
        }

        //When the string is empty
        else if (lastChar.equals(" ")){
            updatedText = currentText + "(-";
            openParenCount++;
            isCurrentlyNegative = true;
        }

        //Undo the negative sign if the number is already negative
        else if(isNegativeNumber(currentText)){
            Log.d(dbugMSG, "attempting to undo negative");
            updatedText = undoNegative(currentText);
        }

        //Inserts "(-" in front of a number that the user already typed in
        else if (isDigit(lastChar) || isDecimalPoint(lastChar)) {

            //First break the string in two parts depending on where the '-' needs to go
            String firstHalf;
            String secondHalf;

            for (int i = currentText.length() - 1; i >= 0; i--) {

                //Once you are at the beginning of the number
                if (!isDigit(currentText.charAt(i)) && !isDecimalPoint(currentText.charAt(i))) {

                    //Substring: begin index is inclusive, ending index is exclusive
                    firstHalf = currentText.substring(0, i + 1);
                    secondHalf = currentText.substring(i + 1, currentText.length());
                    updatedText = firstHalf + "(-" + secondHalf;

                    //Remember we need to keep track of the openParenCount
                    openParenCount++;
                    isCurrentlyNegative = true;
                    break;
                }
            }
        }

        //Don't let user put neg signs randomly
        else if(lastChar.equals(")") || lastChar.equals("-")) {
            updatedText = currentText;
        }
        else {
            updatedText = currentText + "(-";
            isCurrentlyNegative = true;
            openParenCount++;
        }

        return updatedText;
    }

    /**
     * Decides what values need to be returned and altered depending on what value is being deleted
     * @param currentText The current string being displayed
     * @param lastChar The last char of the string being displayed
     * @return Returns the updated value after deletion
     */
    private String handleDeleteValues(String currentText, String lastChar){
        String updatedText;

        if(isDisplayingAnswer){
            updatedText = currentText.substring(0,lastIndex);
            isDisplayingAnswer = false;
        }
        else if(currentText.length() == 1 || currentText.equals("")) { //If string is already null, we can't remove any values
            updatedText = " ";
        }

        //If current number is neg need to update when '-' is deleted
        else if(lastChar.equals("-") && isCurrentlyNegative){
            isCurrentlyNegative = false;
            updatedText = currentText.substring(0, currentText.length() - 1);
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

        //The updated text keeps all except the final letter of the initial string
        else{
            updatedText = currentText.substring(0, currentText.length() - 1);
        }

        //updates the value tracking negative numbers if the user goes backwards on input
        if(isDigit(lastChar)){
            if (isNegativeNumber(currentText)){
                isCurrentlyNegative = true;
            }
            else
                isCurrentlyNegative = false;
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
        String updatedText;
        //We need an opening Paren for these situations
        if (currentText.equals(" ") || isOperator(lastChar)) {
            updatedText = currentText + "(";
            openParenCount++;
        }
        //Last value is a digit and all paren groups are paired
        else if ((isDigit(lastChar) || isCloseParen(lastChar)) && openParenCount == closeParenCount) {
            updatedText = currentText + "*(";
            openParenCount++;
        }
        else if((isDigit(lastChar) || isCloseParen(lastChar) ) && (closeParenCount < openParenCount)){
            updatedText = currentText + ")";
            closeParenCount++;
        }
        else if(isOpenParen(lastChar)){
            updatedText = currentText + "(";
            openParenCount++;
        }
        //Do not let user place parenthesis if the last values is a decimal point
        else if(isDecimalPoint(lastChar)){
            updatedText = currentText;
        }
        else
            updatedText = currentText + ")";

        return updatedText;
    }

    /**
     * This should only be used when handling the +- key on the calculator
     * @param currentText current text being displayed on the TextView
     * @return a string without the negative number
     */
    private String undoNegative(String currentText){
        String updatedText;
        String firstHalf;
        String secondHalf;
        boolean isNotNegative = false;
        int i;

        for(i = currentText.length() - 1; i>= 0 ; i-- ){
            if((!isDigit(currentText.charAt(i)) && !isDecimalPoint(currentText.charAt(i)) ) && currentText.charAt(i) == '-'){
                break;
            }
            else if (isDigit(currentText.charAt(i)) || isDecimalPoint(currentText.charAt(i))){
                continue;
            }
            //If there is something else before the negative sign, sometype of error happened
            else{
                updatedText = currentText;
                return updatedText;
            }
        }
        //Breaks on i == index of negative
        //must get rid of "(-" that comes along with every negative number
        firstHalf = currentText.substring(0, i - 1);
        secondHalf = currentText.substring(i+1, currentText.length());
        openParenCount--;
        isCurrentlyNegative = false;
        updatedText = firstHalf + secondHalf;
        return updatedText;
    }

    /**
     * This should only be used if the +- number is pressed! Checks if the most recent number is negative or not
     * @param currentText Current text being displayed on the TextView
     * @return true if the number is negative
     */
    private boolean isNegativeNumber(String currentText){

        //If there is no number at end of the string there is nothing to be done here
        if(!isDigit(currentText.charAt(currentText.length() - 1)) && !isDecimalPoint(currentText.charAt(currentText.length() - 1)))
            return false;

        for (int i = currentText.length() - 1; i >=0; i--){

            if(currentText.charAt(i) == '-' ){
                return true;
            }

            else if(isDigit(currentText.charAt(i)) || isDecimalPoint(currentText.charAt(i))){
                continue;
            }
            else
                return false;
        }
        return false;
    }

    private boolean containsDecimal(String currentText, String lastChar){
        //If the current value isn't a number or decimal, it cant contain a decimal
        if(!isDigit(lastChar) && !isDecimalPoint(lastChar)){
            return false;
        }

        int size = currentText.length();
        for (int i = size - 1; i >=0; i--){
            char temp = currentText.charAt(i);

            if(isDigit(temp)){
                continue;
            }
            else if(isDecimalPoint(temp)){
                return true;
            }
            else
                return false;
        }
        return false;
    }
    private boolean isOpenParen(String command){
        if(command.equals("("))
            return true;

        return false;
    }
    private boolean isCloseParen(String command){
        if(command.equals(")"))
            return true;

        return false;
    }
    private boolean isDecimalPoint(String command){
        if(command.charAt(0) == 46)
            return true;
        else
            return false;
    }
    private boolean isDecimalPoint(char command){
        if(command == 46)
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
    private boolean isDigit(char command){
        if(command >=48 && command <=57)
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
        boolean isOp;
        if(isDigit(command)){
            isOp =  false;
        } else if(isDecimalPoint(command)){
            isOp  = false;
        } else if (isParenthesis(command)){
            isOp = false;
        } else if(command.equals("") || command.equals(" ")){
            return false;
        } else {
            isOp = true;
        }
        return isOp;
    }
}