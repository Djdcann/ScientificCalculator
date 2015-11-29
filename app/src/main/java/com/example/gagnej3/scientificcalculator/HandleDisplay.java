package com.example.gagnej3.scientificcalculator;

import android.util.Log;
import android.widget.TextView;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

import java.math.BigDecimal;
import java.text.DecimalFormat;

/**
 * Need to figure out how to undo the plus/minus command
 * Then need to define the enter key
 * Someone needs to find a way to set the textView to blank rather than a space. The issue is
 * inside the checkSpecialCase method
 * Created by gagnej3 on 10/31/15.
 */
public class HandleDisplay {

    private String exponents = "^";
    private String DIGITS = "1234567890.";
    private String OPERATORS = "()+-*/^";
    private String TRIG = "sin|cos|tan|ln|log|abs";
    private String TRIGDEL = "n|s|g";
    private String mEquation;
    private String words = "sin(|cos(|tan(|ln(|log(|abs(|(pi)|(e)|(ans)";
    private String[] WORDS = {"(e)", "ln(", "sin(", "cos(", "tan(",  "log(", "abs(", "(pi)",  "(ans)"};
    private String CONSTANTS = "(ans)|(e)|(pi)";
    private String mSolution = "0";
    private String previousCommand;

    private String dbugMSG = "dbug HandleDisplay";

    private Solver solver = new Solver();

    private int openParenCount = 0;
    private int closeParenCount = 0;
    //Used to keep track of where the answer begins
    private int lastIndex = 0;

    private boolean isCurrentlyNegative = false;
    private boolean isDisplayingAnswer = false;

    private void resetMemberValues(){
        previousCommand = "";
        mEquation = "";
        openParenCount = 0;
        closeParenCount = 0;
        lastIndex = 0;
        isCurrentlyNegative = false;
        isDisplayingAnswer = false;
    }

    //Updates the text box to be displayed
    public TextView updateTextView(TextView currentDisplay, String newValue) {
        TextView updatedDisplay = currentDisplay;
        String currentText = currentDisplay.getText().toString();
        String updatedText = checkSpecialCase(currentText, newValue);
        updatedDisplay.setText(updatedText);

        setPreviousCommand(updatedText);
        return updatedDisplay;
    }

    /**
     * Decides what button is pressed and then calls one of the 'handle' methods
     * @param currentText current text being displayed
     * @param command value of the button pressed
     * @return returns the updated text
     */
    private String checkSpecialCase(String currentText, String command){
        String updatedText;

        if(currentText.equals("") || currentText.equals(" ")){
            resetMemberValues();
        }

        //This is only checked in the isOperator method, which is handled properly if it isn't initialized
        String lastChar = "";

        try {
            char lastCharInStr = currentText.charAt(currentText.length() - 1);
            lastChar += lastCharInStr;
        }catch (Exception e){
            Log.d(dbugMSG, "LastCharSTR out of range");
            lastChar = " ";
        }

        if(command.equals("delete")) {
            updatedText = handleDeleteValues(currentText, lastChar);
        }
        else if(isDisplayingAnswer){
            updatedText = handleNewCalculation(currentText,command,lastChar);
        }
        else if(CONSTANTS.contains(command)){
            updatedText = handleConstants(currentText, command, lastChar);
        }
        else if(command.equals("1/")) {
            updatedText = handleOneDividedBy(currentText, command, lastChar);
        }
        else if(command.contains(exponents)){
            updatedText = handleExponents(currentText, command, lastChar);
        }
        //checks for trig vals
        else if(TRIG.contains(command)) {
            updatedText = handleTrig(currentText, command, lastChar);
        }
        else if (isParenthesis(command)) {
            updatedText = handleParentheses(previousCommand, lastChar, currentText);
        }
        //Same issue as above code block
        else if (command.equals("clear")) {
            updatedText = handleClearAll();
        }
        //Places "0." in the string if decimal is pressed with: an empty string or after an operator
        else if(isDecimalPoint(command)){
            updatedText = handleDecimalPoint(command, lastChar, currentText);
        } else if (command.equals("+-")){
            updatedText = handlePlusMinus(currentText, lastChar);
        }

        //This will instantiate a new UserInput to handle the mathematical input
        //This should come before checking for a duplicate operator. "=" should end the current operation
        else if (command.equals("=") && !isDisplayingAnswer) {
            updatedText = handleEqualsButton(currentText, previousCommand,lastChar);
        }
        else if(isOperator(command)){
            updatedText = handleOperator(command,currentText,lastChar);
        }
        //If there is no special action associated with the specified command, all we need to do is append the string
        //This is where digits will be getting handled
        else{
            if((isDigit(command) || CONSTANTS.contains(command)) && currentText.equals(" ")){
                updatedText = command;
            }
            else if ((isCloseParen(lastChar)) && isDigit(command) ){
                updatedText = currentText + "*" + command;
            }
            else if(isDisplayingAnswer){
                updatedText = currentText;
            }
            else if(CONSTANTS.contains(previousCommand) && previousCommand.length() >= 3){
                updatedText = currentText + "*" + command;
            }
            else
                updatedText= currentText + command;
        }

        return updatedText;
    }

    /**
     * Figures out the previous value. This is used to group the trig values and word-constants together.
     * sets the previousCommand String. Makes it easy to delete entire trig values
     *
     * @param currentText the string being displayed to the user
     */
    private void setPreviousCommand(String currentText){
        if(currentText.equals(" ") || currentText.equals(null)){
            previousCommand = " ";
        }
        else {
            try {
                int length = currentText.length();
                int i = currentText.length() - 1;
                String previous = "";
                previous += currentText.charAt(i);

                if ((isOperator(previous) && !words.contains(previous))) {
                    previousCommand = previous;
                } else if (isDigit(previous)) {
                    previousCommand = previous;
                }
                //If the last value is a part of a word, lets search to see if we can get an entire word value for deletion purpose
                else if (words.contains(previous)) {
                    try {
                        String temp3;
                        String temp4 = " ";
                        String temp5 = " ";

                        if (length >= 5) {
                            //The max word length that I use is 5, min is 3
                            temp3 = currentText.substring(length - 3);
                            temp4 = currentText.substring(length - 4);
                            temp5 = currentText.substring(length - 5);
                        } else if (length == 4) {
                            temp3 = currentText.substring(length - 3);
                            temp4 = currentText.substring(length - 4);
                        } else {
                            temp3 = currentText.substring(length - 3);
                        }
                        Log.d(dbugMSG, "Temp3: " + temp3);
                        Log.d(dbugMSG, "Temp4: " + temp4);
                        Log.d(dbugMSG, "Temp5: " + temp5);

                        for (String word : WORDS) {
                            if (temp3.equals(word)) {
                                break;
                            } else if (temp4.equals(word)) {
                                temp3 = temp4;
                                break;
                            } else if (temp5.equals(word)) {
                                temp3 = temp5;
                                break;
                            }
                        }
                        i = currentText.length() - temp3.length();
                    } catch (Exception e) {
                        Log.d(dbugMSG, "Error");
                    }

                    previousCommand = currentText.substring(i, length);
                    Log.d(dbugMSG, "previous command set to: " + previousCommand);
                }
            } catch (Exception e) {
                Log.d(dbugMSG, "error");
            }
        }

    }

    /**
     * Gets called when the calculator is displaying an answer. Decides what to do based on user input
     * @param currentText current string being displayed to the user
     * @param command the value of the button pressed
     * @param lastChar the lastChar of currentText (String)
     * @return returns the updated text to be displayed in the TextView
     */
    private String handleNewCalculation(String currentText, String command, String lastChar) {
        String updatedText = " ";

        //if a number is pressed
        if(command.equals("ans")){
            updatedText = mSolution;
        }
        //when the calculator is displaying Incorrect Format
        else if(currentText.contains("correct")){
            updatedText = " ";
            resetMemberValues();
            updatedText = checkSpecialCase(updatedText,command);
            return updatedText;
        }
        //handle a new calculation beginning with an exponential command
        else if(command.contains("^")){
            isDisplayingAnswer = false;
            updatedText = mSolution;
            resetMemberValues();
            updatedText = checkSpecialCase(updatedText, command);
            return updatedText;
        }
        //don't change anything if the enter kety is pressed again
        else if(command.equals("=")){
            return currentText;
        }
        //format properly for the 1/ button
        else if(command.equals("1/")){
            updatedText = mSolution;
            isDisplayingAnswer = false;
            resetMemberValues();
            updatedText = checkSpecialCase(updatedText, command);
            return updatedText;
        }
        //revert back to the previous equation, don't reset any of the member values
        else if(command.equals("delete")){
            updatedText = mEquation;
            isDisplayingAnswer = false;
            return updatedText;
        }
        //
        else if(CONSTANTS.contains(command)){
            Log.d(dbugMSG,"in Constants ");
            isDisplayingAnswer = false;
            resetMemberValues();
            updatedText = checkSpecialCase(updatedText,command);
            return updatedText;
        }
        //clear the display and start the new equation with just a number
        else if(isDigit(command)){
            updatedText = command;
        }
        else if(isDecimalPoint(command)){
            isDisplayingAnswer = false;
            updatedText = checkSpecialCase(updatedText, command);
        }
        else if(command.equals("+-")){
            return currentText;
        }
        else if (TRIG.contains(command)){
            updatedText = mSolution + "*" + command + "(";
            resetMemberValues();
            openParenCount++;
            return updatedText;
        }
        else if(command.contains("1/")){
            updatedText = mSolution + "*(" + command;
            resetMemberValues();
            openParenCount++;
            return updatedText;
        }
        else if(command.equals("()")){
            updatedText = mSolution + "*" + "(";
            resetMemberValues();
            openParenCount++;
            return  updatedText;
        }
        else if(isOperator(command) && !command.equals("=")){
            updatedText = mSolution + command;
        }
        else if(command.equals("delete")){
            updatedText = mEquation;
        }
        else if(command.equals("clear")){
            updatedText = " ";
        }
        else{
            currentText = " ";
            updatedText = checkSpecialCase(currentText, command);
        }
        resetMemberValues();
        return updatedText;
    }

    /**
     * does the text formatting for ans, pi, and e
     * @param currentText the string being dispalyed to the user
     * @param command the value of the button that was pressed
     * @param lastChar the last char of the currentText
     * @return the updatedText string, this is then displayed in the TextView
     */
    private String handleConstants(String currentText, String command, String lastChar){
        String updatedText;

        setPreviousCommand(currentText);
        Log.d(dbugMSG, "previous command handle consts" + previousCommand);

        Log.d(dbugMSG, "currentText" + currentText);

        //This only happens when the text view is null
        if(currentText.equals(" ") || currentText.equals(null)){
            updatedText = "(" + command + ")";
            previousCommand = updatedText;
            openParenCount++;
            closeParenCount++;
        }
        //There was a weird error with this stuff for some reason
        else if((isDigit(lastChar)||CONSTANTS.contains(previousCommand)) && CONSTANTS.contains(command) && !previousCommand.equals("")){
            Log.d(dbugMSG,"Its in here ");
            updatedText = currentText + "*(" + command + ")";
            openParenCount++;
            closeParenCount++;
        }
        //don't update if it is after a decimal
        else if(isDecimalPoint(lastChar)){
            updatedText = currentText;
        }
        else{
            updatedText = currentText + "(" + command + ")";
        }

        return updatedText;
    }

    /**
     * Does the text formmating for the 1/x button
     * @param currentText current string being displayed to the used
     * @param command the value of the button that was pressed
     * @param lastChar the last char of the string being displayed
     * @return returns the updatedText string
     */
    private String handleOneDividedBy(String currentText, String command, String lastChar){
        String updatedText;

        if(isDigit(lastChar) || lastChar.equals("s")){
            updatedText = currentText + "*" + "(" + command;
            openParenCount++;
        }
        else{
            updatedText = currentText + "(" + command;
            openParenCount++;
        }
        return updatedText;
    }

    /**
     * Handles the text formatting of the trig values
     * @param currentText text being displayed to the user
     * @param command the value of the button that was pressed
     * @param lastChar the last char of the currentText
     * @return returns the updatedText string
     */
    private String handleTrig(String currentText, String command, String lastChar){
        String updatedText;

        setPreviousCommand(currentText);
        Log.d(dbugMSG, "Previous value in handleTrig: " + previousCommand);

        //don't allow stuff like 'sintan(', must have parenthisis so 'sin(tan(' would be acceptable
        //if(previousCommand != " " && TRIG.contains(previousCommand)) {
            //updatedText = currentText;
        //}
        //when the last value is a digit or constant, we want to put a multiplication sign in between
        if((isDigit(lastChar) || CONSTANTS.contains(previousCommand) || isCloseParen(lastChar))){
            updatedText = currentText + "*" + command + "(";
            openParenCount++;
        }
        //When it doesn't have special conditions just put it in the string normally
        else{
            updatedText = currentText + command + "(";
            openParenCount++;
        }

        return updatedText;
    }

    /**
     * Decides text formatting when an exponent is pressed
     * @param currentText string being displayed to the user
     * @param command value of the button that was pressed
     * @param lastChar last char of the string
     * @return returns the updated string
     */
    private String handleExponents(String currentText, String command, String lastChar){
        String updatedText = currentText;

        //if the text is null the only value applicable is the "e^", so don't update it otherwise
        if((currentText.equals(" ") || isOpenParen(lastChar)) && !command.equals("e^") ){
            updatedText = currentText;
        }
        else if(isCloseParen(lastChar)){
            if(command.equals("x^2")){
                updatedText = currentText + "^(2)";
                openParenCount++;
                closeParenCount++;
            } else if(command.equals("y^x")){
                updatedText = currentText + "^(";
                openParenCount++;
            } else if (command.equals("^(1/2)")){
                updatedText = currentText + command;
                openParenCount++;
                closeParenCount++;
            } else{
                updatedText = currentText + "*(e)^(";
                openParenCount+=2;
                closeParenCount++;
            }
        }
        //square the current value
        else if(command.equals("x^2") && (isDigit(lastChar) ||isDigit(previousCommand) || CONSTANTS.contains(previousCommand) )){
            updatedText = currentText + "^(2)";
            openParenCount++;
            closeParenCount++;
        }
        //as long as there is a value for y, this is allowed
        else if(command.equals("y^x") && (isDigit(lastChar) || (CONSTANTS.contains(previousCommand) && previousCommand.length() >= 3))){
            updatedText = currentText + "^(";
            openParenCount++;
        }
        //if there is a number value before e, we must place a multiplication symbol
        else if(command.equals("e^") && (isDigit(lastChar) || (CONSTANTS.contains(previousCommand) && previousCommand.length() >= 3))){
            updatedText = currentText + "*(e)^(";
            openParenCount += 2;
            closeParenCount++;
        }
        //if there is nothing special prior to the e^ command, just add it in
        else if(command.equals("e^")){
            updatedText = currentText + "(e)^(";
            openParenCount +=2;
            closeParenCount++;
        }
        else{
            updatedText = currentText + command;
            openParenCount++;
            closeParenCount++;
        }

        return updatedText;
    }

    /**
     * Decides what to do when the "=" button is pressed
     * @param currentText the current text being displayed in the text view
     * @param lastChar last value in the string
     * @return the proper output depending on the user format
     */
    private String handleEqualsButton(String currentText, String prevCommand,String lastChar){
        String updatedText;
        setPreviousCommand(currentText);
        BigDecimal bigDecimal;

        //will be used to keep answers to 8 decimal places
        DecimalFormat decimalFormat = new DecimalFormat("0.00000000");

        //This will be useful for when the user presses the delete button.
        mEquation = currentText;

        if((isOperator(lastChar) ||currentText.equals(" ") || isDecimalPoint(lastChar) ||
                currentText.contains("()")) || lastChar.equals("(") && !CONSTANTS.contains(previousCommand) ){

            updatedText = currentText + "\n" + "Incorrect Format";
        }
        else{
            //Put in the necessary number of parentheses to properly format the equation
            if(openParenCount > closeParenCount){
                int difference = openParenCount - closeParenCount;
                for (int i = 0; i < difference; i++){
                    currentText += ")";
                }
            }

            //Set the ans member variable to allow use of the ans key in the scientific calc.
            solver.setAns(mSolution);

            String solution;
            solution = solver.solve(currentText);

            //Makes sure there isn't a space in the string. Spaces in the string will not work in the solver
            //Also reformats and rounds continuous decimals
            try{
                double tempSolution = Double.parseDouble(solution);
                mSolution = decimalFormat.format(tempSolution);
                tempSolution = Double.parseDouble(mSolution);
                bigDecimal = BigDecimal.valueOf(tempSolution);
                bigDecimal.stripTrailingZeros();
                mSolution = bigDecimal.toString();
                solution = mSolution;
            } catch(Exception e){
                mSolution = solution;
            }

            //places parentheses around negative solutions
            if(solution.contains("-")){
                updatedText = currentText + '\n' + "= "  + "(" + solution + ")";
                mSolution = "(" + solution + ")";
            }
            else{
                updatedText = currentText + '\n' + "= " + solution;
            }
        }

        //is currently displaying the answer
        isDisplayingAnswer = true;
        return updatedText;
    }

    /**
     * Decides proper text formatting if an operator is pressed
     * @param command the command pressed
     * @param currentText entire string beign displayed
     * @param lastChar last char of the string in string format
     * @return the updated text
     */
    private String handleOperator(String command, String currentText, String lastChar){
        String updatedText;

        if(lastChar.equals("s") && currentText.contains("ans")){
            updatedText = currentText + command;
        }
        else if(isDecimalPoint(lastChar)){
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
        isCurrentlyNegative = false;
        previousCommand = "";
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
        } else if (containsDecimal(currentText, lastChar)){
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

        //When the string is empty
        if (lastChar.equals(" ")){
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

        //We can't let the TextView be null, it will crash the application
        if(currentText.equals("") || currentText.equals(" ") || currentText.length() == 1){
            updatedText = " ";
            return updatedText;
        }
        //remove the answer from being displayed and display the equation that was entered only
        else if(isDisplayingAnswer){
            updatedText = mEquation;
            isDisplayingAnswer = false;
        }
        //for words, we want to remove the entire word on delete
        else if(words.contains(previousCommand)){
            int prevSize = previousCommand.length();
            int size = currentText.length();
            updatedText = currentText.substring(0, size - prevSize);

            //We need to update the parencount if the word contained parentheses
            if(previousCommand.contains("(") && previousCommand.contains(")")){
                openParenCount--;
                closeParenCount--;
            } else if(previousCommand.contains("(")){
                openParenCount--;
            } else if(previousCommand.contains(")")){
                closeParenCount--;
            }
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
     * Formats text when the parentheses button is pressed
     * @param prevCommand The button pressed, in this case will be ()
     * @param lastChar The last value of the String
     * @param currentText The current string being displayed to the user
     * @return returns the updated string based on the when the parenthesis button was pressed
     */
    private String handleParentheses(String prevCommand, String lastChar, String currentText){
        String updatedText;

        //We need an opening Paren for these situations
        if ((currentText.equals(" ") || isOperator(lastChar))) {
            updatedText = currentText + "(";
            openParenCount++;
        }
        //Last value is a digit and all paren groups are paired
        else if ((isDigit(lastChar) || isCloseParen(lastChar) || CONSTANTS.contains(previousCommand)) && openParenCount == closeParenCount) {
            updatedText = currentText + "*(";
            openParenCount++;
        }
        else if((isDigit(lastChar) || isCloseParen(lastChar) || CONSTANTS.contains(previousCommand)) && (closeParenCount < openParenCount)){
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
            //treat these values the same as numbers
        else if(command.equals("e") || command.equals("pi") || command.equals("ans"))
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

    private boolean isClearCommand(String command){
        if(command.equals("clear") || command.equals("delete"))
            return true;

        return false;
    }
    //Checks if the current command is an operator or not
    public boolean isOperator (String command){
        boolean isOp;

        if(isClearCommand(command))
            isOp = false;
        else if(isDigit(command)){
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