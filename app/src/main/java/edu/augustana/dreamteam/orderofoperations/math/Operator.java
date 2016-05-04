package edu.augustana.dreamteam.orderofoperations.math;

import android.graphics.Color;

import java.util.Random;

/**
 * Created by jeffreyprior on 4/13/16.
 */
public class Operator implements Comparable<Operator>{
    private static final String[] operands = {"%", "/", "*", "-", "+"};

    private String opChar;
    private int operatorIndex;
    private int operatorPriority;
    private int indexInEquation;
    private int colorCode;

    //sets a priority to each operand
    public Operator(){
        Random random = new Random();
        operatorIndex = random.nextInt(5);
        opChar = operands[operatorIndex];
        if(operatorIndex <3){
            operatorPriority = 3;
        } else {
            operatorPriority = 4;
        }
        indexInEquation = random.nextInt(4);
    }

    public Operator(int index){
        this();
        indexInEquation = index;
    }

    public String getOperatorChar(){
        return opChar;
    }

    public int getOperatorPriority(){
        return operatorPriority;
    }

    //sets priority to each operand within parentheses once an initial priority is assigned in OperandGenerator()
    public void operatorInParentheses(){
        if(operatorIndex <3){
            operatorPriority = 1;
        } else {
            operatorPriority = 2;
        }
    }

    @Override
    public int compareTo(Operator otherOperator) {
        return operatorPriority - otherOperator.getOperatorPriority();
    }

    public int getIndex(){
        return indexInEquation;
    }

    public void makeInactive(){
        indexInEquation = 4;
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof Operator) {
            return equals((Operator) other);
        } else {
            return false;
        }
    }
    public boolean equals(Operator other){
        return (opChar.equals(other.opChar)) && (indexInEquation == other.indexInEquation);
    }
}
