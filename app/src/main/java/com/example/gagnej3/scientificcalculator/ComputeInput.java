package com.example.gagnej3.scientificcalculator;

import android.util.Log;

import java.util.ArrayList;


public class ComputeInput {

    private String dbugMSG = "dbug ComputeInput";
    private double mSolution;
    private int[] operatorPrecedance;
    private ArrayList<String> equation = new ArrayList<>();
    private ArrayList<String> solvedEquation = new ArrayList<>();

    public double getmSolution(){
        return mSolution;
    }

    /**
     * This is the only method that should be called to calculate the input string.
     * Returns the simplified value in form of string.
     * @param input gets the string from the display
     * @return the solution to the string input
     */
    public String computeSolution(String input){
        String solution;
        seperateInput(input, this.equation);

        for(int i = 0; i < this.equation.size(); i++){

            String piece = this.equation.get(i);

            if(isSimplestForm(piece)){
                this.solvedEquation.add(piece);
            }
            else{
                while(!isSimplestForm(piece)){
                    piece = getSimpleForm(piece);
                }

                this.solvedEquation.add(piece);
            }
        }
        solution = solve(this.solvedEquation);

        return solution;
    }

    /**
     * Evaluates what should be done bases on the given string. If it needs ti calculate something
     * the selectOperator method is called, which in turn calls one of the calculate methods
     * @param equationPart the part of the equation in simple form to be solved
     * @return Returns a simplified value. ie [6,+,4] will yield 10
     */
    private String solve(ArrayList<String> equationPart){

        int endingIndex = 0;
        //Handles the instance where there is only a single value in the equation
        if(containsSingleValue(equationPart)){
            return equationPart.get(0);
        }

        //Gets called when it needs to handle nested negative numbers.
        if (containsOnlyNegativeNum(equationPart)){
            String temp = "";
            temp += equationPart.get(0);
            temp += equationPart.get(1);
            return temp;
        }
        else{
            //Assign the locations of each operator in the equation by precedence
            setOperatorPrecedance(equationPart);

            //Execute the operations in the order specified
            for(int j: operatorPrecedance){
                String operation;
                operation = equationPart.get(j);

                String simplified;
                simplified = selectOperation(operation, equationPart, j);

                //Update values on both sides of the operator
                equationPart.set(j-1, simplified);
                equationPart.set(j+1, simplified);

                endingIndex = j+1;
            }
        }

        //Ideally, this will work
        return equationPart.get(endingIndex);
    }

    private void setOperatorPrecedance(ArrayList<String> equation){
        int operatorCount = 0;
        for(int i = 0; i < equation.size(); i++){
            String temp = equation.get(i);
            if(isOperator(temp)){
                operatorCount++;
            }
        }
        this.operatorPrecedance = new int[operatorCount];
        int index = 0;

        //Multiplication and division should happen first
        for(int i = 0; i < equation.size(); i++){
            String temp = equation.get(i);
            if(temp.equals("*") || temp.equals("/")){
                this.operatorPrecedance[index] = i;
                index++;
            }
        }

        //addition and subtraction second
        for(int i = 0; i < equation.size(); i++){
            String temp = equation.get(i);
            if(temp.equals("+") || temp.equals("-")){
                this.operatorPrecedance[index] = i;
                index++;
            }
        }
    }

    /**
     * Decides which operation is necessary to compute next necessary value
     * @param operation The operator to be used
     * @param equation The equation array to be simplified
     * @param index index of the current value
     */
    private String selectOperation(String operation, ArrayList<String> equation, int index)
    {
        String simplified;

        System.out.println("selecting the operation");
        switch(operation)
        {
            case "+":
                simplified = calculateSum(equation, index);
                break;
            case "-":
                simplified = calculateDifference(equation, index);
                break;
            case "*":
                simplified = calculateProduct(equation, index);
                break;
            case "/":
                simplified = calculateQuotient(equation, index);
                break;
            default:
                simplified = "error";
                System.out.println("error");
                break;
        }

        return simplified;
    }

    /**
     * Calculates the quotient of 2 numbers. Returns the quotient in the form of a string
     * @param equation equation to be simplified
     * @param index current index in the array
     * @return returns the quotient
     */
    private String calculateQuotient(ArrayList<String> equation, int index){
        double num1;
        double num2;
        double quotientNum;
        String quotientStr = "";
        String word1;
        String word2;
        try{
            word1 = equation.get(index - 1);
            word2 = equation.get(index + 1);
            num1 = Double.parseDouble(word1);
            num2 = Double.parseDouble(word2);
            quotientNum = num1 / num2;
            quotientStr = Double.toString(quotientNum);

        }catch(Exception e){
            System.out.println("Error in calculateDifference");
        }

        return quotientStr;
    }

    /**
     * Calculates the product of 2 numbers, returns the product in the form of a string
     * @param equation equation to be simplified
     * @param index index in which the operator is located at
     * @return the product of two numbers
     */
    private String calculateProduct(ArrayList<String> equation, int index){
        double num1;
        double num2;
        double product;
        String productStr = "";
        String word1;
        String word2;
        try{
            word1 = equation.get(index - 1);
            word2 = equation.get(index + 1);
            num1 = Double.parseDouble(word1);
            num2 = Double.parseDouble(word2);
            product = num1 * num2;
            productStr = Double.toString(product);

        }catch(Exception e){
            System.out.println("Error in calculateDifference");
        }

        return productStr;
    }

    /**
     * Finds the difference of 2 numbers. Returns the difference in the form of a string
     * @param equation equation to be simplified
     * @param index index of the current operator
     * @return returns the difference between the two numbers
     */
    private String calculateDifference(ArrayList<String> equation, int index){
        double num1;
        double num2;
        double differenceNum;
        String differenceStr = "";
        String word1;
        String word2;
        try{
            word1 = equation.get(index - 1);
            word2 = equation.get(index + 1);
            num1 = Double.parseDouble(word1);
            num2 = Double.parseDouble(word2);
            differenceNum = num1 - num2;
            differenceStr = Double.toString(differenceNum);

        }catch(Exception e){
            System.out.println("Error in calculateDifference");
        }

        return differenceStr;
    }

    /**
     * Finds the sum of 2 numbers. Returns the sum in the form of a string
     * @param equation equation array to be simplified
     * @param index index of the current operator
     * @return calculates the sum of the two numbers
     */
    private String calculateSum(ArrayList<String> equation, int index){
        double tempSum;
        try
        {
            System.out.println("INDEX IN CALC SUM:  " + index );
            String firstNum = equation.get(index - 1);
            String secondNum = equation.get(index +1);
            double num1 = Double.parseDouble(firstNum);
            double num2 = Double.parseDouble(secondNum);

            tempSum = num1 + num2;

        }catch(NumberFormatException e) {
            System.out.println("Not a double");
            return "";
        }
        return Double.toString(tempSum);
    }

    /**
     * A recursive method that simplifies content inside nested parentheses
     * @param piece part of the equation that is not in simple form
     * @return a simplified equivalent value
     */
    private String getSimpleForm(String piece){

        ArrayList<String> temp = new ArrayList<>();
        //Fills the new ArrayList with the unsimplified value
        seperateInput(piece, temp);

        //Checks if the entire equation is in simple form
        if(isSimplestForm(temp)){
            return solve(temp);
        }

        //When there is a part of the equation that hasn't been broken down completely, lets try to break it down more
        else{
            for(int i = 0; i < temp.size(); i++){
                String newValue = temp.get(i);

                if(isSimplestForm(newValue)){
                    continue;
                } else{
                    //Calls getSimpleForm to check if the value has been completely simplified yet.
                    newValue = getSimpleForm(newValue);
                }

                temp.set(i, newValue);
            }
        }
        return solve(temp);
    }

    /**
     * Breaks down the original string into components, stores them in an array
     * The ArrayList belongs to the class itself
     * @param equation the array that the values will be stored in
     * @param input the string to be broken down
     */
    private void seperateInput(String input, ArrayList<String> equation)
    {
        int openParenCount = 0;
        int closeParentCount = 0;
        boolean inParens = false;
        boolean justClosedParen = false;
        boolean justOpenedParen = false;

        String temp = "";

        for(int i = 0; i< input.length(); i++)
        {
            char currentVal = input.charAt(i);
            //This will alert us that we are in a parentheses block

            if(isOpenParen(currentVal)){
                openParenCount++;
                if(!justOpenedParen){
                    justOpenedParen = true;
                }
                inParens = true;
            } else if(isCloseParen(currentVal)){
                closeParentCount++;
            }

            //Once there are the same number of close Parens as open, we are out of the paren block
            if(openParenCount == closeParentCount && (openParenCount != 0)){
                inParens = false;
                //allows the last paren to be counted with the string
                //other wise, inParens would always be true an format wouldn't work out
                justClosedParen = true;
            }

            if(inParens || (justClosedParen && isCloseParen(currentVal))){
                //If the first opening paren, don't store it.
                if(justOpenedParen && isOpenParen(currentVal) && openParenCount == closeParentCount + 1){
                    justOpenedParen = false;
                    continue;
                }

                else if(justClosedParen  && isCloseParen(currentVal)){
                    //Must add is here because it won't know to break;
                    equation.add(temp);
                    temp = "";
                    justClosedParen = false;
                    continue;
                }

                else{
                    temp += currentVal;
                }
                //While in a block of parentheses, we want to group this entire piece together
                justClosedParen = false;
                continue;
            }


            String checkOperator = "";
            checkOperator += input.charAt(i);
            //Add value to string per char, we don't want any spaces
            if(isOperator(checkOperator)){
                equation.add(checkOperator);
                temp = "";
                continue;
            }

            if(input.charAt(i) != 32 )
                temp += input.charAt(i);

            //check if next value is number or other symbol
            if(isReadyToSplit(input, i))
            {
                //Add the current substring to the array
                equation.add(temp);
                //clear the temp val
                temp = "";
            }
        }
    }

    /**
     * Returns true if an operator is coming up next
     * Returns false if still processing a number.
     * Returns false on decimal points
     * @param strIn current string
     * @param index
     * @return true if th string needs to split on next index
     */
    private boolean isReadyToSplit(String strIn, int index){

        //IF the current value is a decimal point or a space, don't split the string
        if(strIn.charAt(index) == 46 || strIn.charAt(index) == 32)
            return false;

        //These are the values we want to group for real numbers ie. 12.33 will be an entire string
        if((strIn.charAt(index) >= 48 && strIn.charAt(index) <= 57) || strIn.charAt(index) == 46){
            try{
                char letter = strIn.charAt(index+1);

                //Tell it not to break if it is a decimal point
                if (letter == 46)
                    return false;

                    //If the next value is not a digit nor decimal point, return true to split the string
                else if(!((letter >= 48 && letter <= 57))){
                    return true;
                }
            }catch(StringIndexOutOfBoundsException e){
                System.out.println("Array out of range");
            }
        }

        //End the last string on the last index
        if(index == strIn.length() - 1){
            return true;
        }

        if(!(strIn.charAt(index) >= 48 && strIn.charAt(index) <= 57)){
            return true;
        }

        return false;
    }

    public ArrayList<String> getEquation() { return equation;}

    public void setEquation(ArrayList<String> equation) { this.equation = equation;}

    /**
     * Only to be called in the solve method. Returns true if array contains a negative sign and a number ex. "-" "6"
     * @param equation checks if the array only contains a nested negative number
     * @return true if negative
     */
    private boolean containsOnlyNegativeNum(ArrayList<String> equation){
        String indexZero = equation.get(0);
        String indexOne = equation.get(1);
        if(indexZero.equals("-") && isNumber(indexOne)){
            return true;
        }

        return false;
    }

    private boolean containsSingleValue(ArrayList<String> equation){
        if(equation.size() == 1){
            return true;
        }

        return false;
    }

    /**
     * Checks for simple form for all values in the ArrayList
     * @param equation the equation to be checked for simple form
     * @return False if there is a non-simplified term True if all terms are simplified
     */
    private boolean isSimplestForm(ArrayList<String> equation){

        for(String part: equation){
            if(!isSimplestForm(part)){
                return false;
            }
        }

        return true;
    }

    /**
     * Checks if the piece has been broken down completely
     * @param partOfEquation string value to be checked for simple form
     * @return true is the value is only an operator or an real number. no complex terms
     */
    private boolean isSimplestForm(String partOfEquation){
        if(partOfEquation.contains("(") || partOfEquation.contains(")")){
            return false;
        }
        else if(isNumber(partOfEquation) || isOperator(partOfEquation)){
            return true;
        }

        return false;
    }


    private boolean containsParentheses(ArrayList<String> equation){
        for(String part: equation){
            if(isParenthesis(part))
                return true;
        }

        return false;
    }
    /**
     * checks if a string is a real number or not
     * @param partOfEquation value to be checked if it is a number or not
     * @return true if the value is a number
     */
    private boolean isNumber(String partOfEquation){
        try{
            double num = Double.parseDouble(partOfEquation);
        }catch (Exception e){
            return false;
        }

        return true;
    }
    private boolean containsDecimal(String currentText, String lastChar){
        //If the current value isn't a number or decimal, it cant contain a decimal
        if(!isDigit(lastChar) && !isDecimalPoint(lastChar)){
            return false;
        }

        int size = currentText.length();
        for (int i = size - 1; i >=0; i--){
            char temp = currentText.charAt(i);

            if(isDigit(temp))
                continue;
            else if (isDecimalPoint(temp))
                return true;
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
    private boolean isOpenParen(char command){
        if(command == '(')
            return true;

        return false;
    }
    private boolean isCloseParen(String command){
        if(command.equals(")"))
            return true;

        return false;
    }
    private boolean isCloseParen(char command){
        if(command == ')' )
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

    //Checks if the current command is an operator or not
    private boolean isOperator (String command){
        boolean isOp;
        if(isDigit(command)){
            return false;
        } else if(isNumber(command)){
            return false;
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