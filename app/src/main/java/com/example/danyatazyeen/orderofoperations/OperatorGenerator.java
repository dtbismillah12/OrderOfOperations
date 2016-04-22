package com.example.danyatazyeen.orderofoperations;

import android.graphics.Color;

import java.util.Random;

/**
 * Created by jeffreyprior on 4/13/16.
 */
public class OperatorGenerator implements Comparable<OperatorGenerator>{
    private final String[] operands = {"%", "/", "x", "-", "+"};

    private Random random;
    private String currentOperand;
    private int operatorIndex;
    private int operatorPriority;
    private int indexInEquation;

    //sets a priority to each operand
    public OperatorGenerator(int levelsPassed){
        random = new Random();
        operatorIndex = random.nextInt(5);
        currentOperand = operands[operatorIndex];
        if(operatorIndex <3){
            operatorPriority = 3;
        } else {
            operatorPriority = 4;
        }
        if(levelsPassed>10){
            indexInEquation = random.nextInt(4);
        } else if(levelsPassed>5){
            indexInEquation = random.nextInt(3);
        } else {
            indexInEquation = random.nextInt(2);
        }
    }

    public OperatorGenerator(int levelsPassed, int index){
        this(levelsPassed);
        indexInEquation = index;
    }

    public String getOperator(){
        return currentOperand;
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
    public int compareTo(OperatorGenerator otherOperand) {
        return operatorPriority - otherOperand.getOperatorPriority();
    }

    public int getIndex(){
        return indexInEquation;
    }
}
