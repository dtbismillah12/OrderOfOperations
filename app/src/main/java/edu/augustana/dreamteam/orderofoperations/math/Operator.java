package edu.augustana.dreamteam.orderofoperations.math;

import android.graphics.Color;

import java.util.Random;

/**
 * Created by jeffreyprior on 4/13/16.
 */
public class Operator implements Comparable<Operator>{
    private static final String[] operators = {"%", "/", "*", "-", "+"};

    private String opChar;
    private int operatorIndex;
    private int operatorPriority;
    private int indexInEquation;

    /**
     * Initializes an operator with random string character from possible CS operators
     * and sets the operation priority accordingly
     */
    public Operator(){
        Random random = new Random();
        operatorIndex = random.nextInt(5);
        opChar = operators[operatorIndex];
        if(operatorIndex <3){
            operatorPriority = 3;
        } else {
            operatorPriority = 4;
        }
    }

    /**
     * Initializes an operator and sets the operators index in the equation that it belongs in
     * @param index is this operator's index in the equation it is in
     */
    public Operator(int index){
        this();
        indexInEquation = index;
    }

    /**
     * Initializes operator by setting the operator character manually and the index
     * @param opChar the operator String character
     * @param index is this operator's index in the equation it is in
     */
    public Operator(String opChar, int index){
        this(index);
        this.opChar = opChar;
    }

    /**
     * Initializes an operator that is the copy of another
     * @param op operator that is to be copied
     */
    public Operator(Operator op){
        opChar = op.opChar;
        operatorIndex = op.operatorIndex;
        operatorPriority = op.operatorPriority;
        indexInEquation = op.indexInEquation;
    }

    /**
     * Access to the operator String character
     * @return the String for operator character representation
     */
    public String getOperatorChar(){
        return opChar;
    }

    /**
     * Access to the priority of the operator compared to others
     * @return the integer for the priority to compare to other operators
     */
    public int getOperatorPriority(){
        return operatorPriority;
    }

    /**
     * Increases the priority of the operator if it is within parentheses
     */
    public void operatorInParentheses(){
        if(operatorIndex <3){
            operatorPriority = 1;
        } else {
            operatorPriority = 2;
        }
    }

    /**
     * Access to the operator's index in the equation
     * @return the integer for the index in the equation
     */
    public int getIndex(){
        return indexInEquation;
    }

    /**
     * Makes the index in equation outside of the possible indexes in equation
     * to represent as inactive (after asteroid has been hit)
     */
    public void makeInactive(){
        indexInEquation = 4;
    }

    /**
     * Compares one operator to another to say if equal or not
     * @param other another operator object to be compared to
     * @return a boolean for true or false of whether or not equal
     */
    public boolean equals(Operator other){
        return (opChar.equals(other.opChar)) && (indexInEquation == other.indexInEquation);
    }

    /**
     * Compares the priority of two operators to determine the proper order of operations
     * @param otherOperator another operator object to be compared to
     * @return integer value which is a negative number if other should be after or
     * a positive number if other should be before the current operator
     */
    @Override
    public int compareTo(Operator otherOperator) {
        return operatorPriority - otherOperator.operatorPriority;
    }

    /**
     * Overides the equals method to properly check if the object passes in
     * an instance of an operator object
     * @param other an object that is to be checked if an operator and if so if equal
     * @return true or false whether or not the other object is equal to current operator
     */
    @Override
    public boolean equals(Object other) {
        if (other instanceof Operator) {
            return equals((Operator) other);
        } else {
            return false;
        }
    }
}
