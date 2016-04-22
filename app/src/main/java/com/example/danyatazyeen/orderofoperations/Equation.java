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
    private int numOfOperators;
    private Queue<OperatorGenerator> operatorOrder;
    private ArrayList<OperatorGenerator> operators;

    public Equation(){
        rand = new Random();
        equation = new StringBuilder();
        containsParentheses = false;
        numOfOperators = 2;
        operatorOrder = new LinkedList<OperatorGenerator>();
        operators = new ArrayList<OperatorGenerator>();
    }

    //sets the number of operators given in the equation depending how far the player has gotten in the game
    //if they have advanced at least ten rounds, parentheses will randomly be thrown into the equation
    // @param: levelsPassed gives how many rounds the player has passed
    public Equation(int levelsPassed){
        this();
        if(levelsPassed>10){
            numOfOperators = 4;
            containsParentheses = rand.nextBoolean();
        } else if(levelsPassed>5){
            numOfOperators = 3;
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
        for(int i = 1; i<= numOfOperators; i++){
            addTerm(i-1);
        }
        if(containsParentheses){
            addParentheses();
        }
    }

    private void addTerm(int index){
        OperatorGenerator genOperator = new OperatorGenerator(index);
        operators.add(genOperator);
        equation.append(genOperator.getOperator());
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
            operators.get(i).operatorInParentheses();
        }
    }

    public boolean isCorrectOperand(String operand, int index){
        if(operatorOrder.peek().getOperator().equals(operand) && operatorOrder.peek().getIndex() == index){
            operatorOrder.poll();
            return true;
        } else {
            return false;
        }
    }

    private void createProperOperandOrder(){
        Collections.sort(operators);
        for(int i=0; i< operators.size(); i++){
            operatorOrder.offer(operators.get(i));
        }
    }

    public String properOrderString(){
        StringBuilder properOrder = new StringBuilder();
        while(!operatorOrder.isEmpty()){
            properOrder.append(operatorOrder.peek().getOperator());
            properOrder.append(",");
            properOrder.append(operatorOrder.poll().getIndex());
            properOrder.append("\n");
        }
        return properOrder.toString();
    }

    public boolean correctEquation(){
        return operatorOrder.isEmpty();
    }
}
