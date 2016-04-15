package com.example.danyatazyeen.orderofoperations;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

/**
 * Created by jeffreyprior on 4/13/16.
 */
public class Equation {
    private Random rand;
    private StringBuilder equation;
    private boolean containsParentheses;
    private int numOfOperands;
    private Queue<OperandGenerator> operandOrder;
    private ArrayList<OperandGenerator> operands;

    public Equation(){
        rand = new Random();
        equation = new StringBuilder();
        containsParentheses = false;
        numOfOperands = 2;
        operandOrder = new LinkedList<OperandGenerator>();
        operands = new ArrayList<OperandGenerator>();
    }

    //sets the number of operands given in the equation depending how far the player has gotten in the game
    //if they have advanced at least ten rounds, parentheses will randomly be thrown into the equation
    // @param: levelsPassed gives how many rounds the player has passed
    public Equation(int levelsPassed){
        this();
        if(levelsPassed>10){
            numOfOperands = 4;
            containsParentheses = rand.nextBoolean();
        } else if(levelsPassed>5){
            numOfOperands = 3;
        }
        constructEquation();
        createProperOperandOrder();
    }

    public String toString(){
        StringBuilder spacedEquation = new StringBuilder();
        for(int i = 0; i<equation.length(); i++){
            spacedEquation.append(equation.charAt(i));
            spacedEquation.append(" ");
        }
        return spacedEquation.toString();
    }

    private void constructEquation(){
        equation.append(rand.nextInt(10));
        for(int i = 1; i<=numOfOperands; i++){
            addTerm(i-1);
        }
        if(containsParentheses){
            addParentheses();
        }
    }

    private void addTerm(int index){
        OperandGenerator genOperand = new OperandGenerator(index);
        operands.add(genOperand);
        equation.append(genOperand.getOperand());
        equation.append(rand.nextInt(10));
    }

    private void addParentheses(){
        int[] possibleOpenIndexes = {0,2,4,6};
        int[] possibleCloseIndexes = {4,6,8,10};
        int indexOfOpen = rand.nextInt(possibleOpenIndexes.length);
        int indexOfClose = indexOfOpen;
        if(possibleCloseIndexes.length-indexOfOpen-1 != 0){
            indexOfClose += rand.nextInt(possibleCloseIndexes.length-indexOfOpen-1);
        }
        equation.insert(possibleOpenIndexes[indexOfOpen], "(");
        equation.insert(possibleCloseIndexes[indexOfClose], ")");

        for(int i = possibleOpenIndexes[indexOfOpen]/2; i<(possibleCloseIndexes[indexOfClose]/2)-1; i++){
            operands.get(i).operandInParentheses();
        }
    }

    public boolean isCorrectOperand(String operand, int index){
        if(operandOrder.peek().getOperand().equals(operand) && operandOrder.peek().getIndex() == index){
            operandOrder.poll();
            return true;
        } else {
            return false;
        }
    }

    private void createProperOperandOrder(){
        Collections.sort(operands);
        for(int i=0; i<operands.size(); i++){
            operandOrder.offer(operands.get(i));
        }
    }

    public String properOrderString(){
        StringBuilder properOrder = new StringBuilder();
        while(!operandOrder.isEmpty()){
            properOrder.append(operandOrder.peek().getOperand());
            properOrder.append(",");
            properOrder.append(operandOrder.poll().getIndex());
            properOrder.append("\n");
        }
        return properOrder.toString();
    }
}
