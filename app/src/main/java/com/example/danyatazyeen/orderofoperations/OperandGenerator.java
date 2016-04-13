package com.example.danyatazyeen.orderofoperations;

import java.util.Random;

/**
 * Created by jeffreyprior on 4/13/16.
 */
public class OperandGenerator implements Comparable<OperandGenerator>{
    private final String[] operands = {"%", "/", "x", "-", "+"};

    private Random random;
    private String currentOperand;
    private int operandIndex;
    private int operandPriority;
    private int indexInEquation;

    public OperandGenerator(){
        random = new Random();
        operandIndex = random.nextInt(5);
        currentOperand = operands[operandIndex];
        if(operandIndex<3){
            operandPriority = 3;
        } else {
            operandPriority = 4;
        }
        indexInEquation = -1;
    }

    public OperandGenerator(int index){
        this();
        indexInEquation = index;
    }

    public String getOperand(){
        return currentOperand;
    }

    public int getOperandPriority(){
        return operandPriority;
    }

    public void operandInParentheses(){
        if(operandIndex<3){
            operandPriority = 1;
        } else {
            operandPriority = 2;
        }
    }

    @Override
    public int compareTo(OperandGenerator otherOperand) {
        return operandPriority - otherOperand.getOperandPriority();
    }

    public int getIndex(){
        return indexInEquation;
    }
}
