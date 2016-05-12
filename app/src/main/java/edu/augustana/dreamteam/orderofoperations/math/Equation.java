package edu.augustana.dreamteam.orderofoperations.math;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

import edu.augustana.dreamteam.orderofoperations.gameobjects.EquationTerm;

/**
 * Created by jeffreyprior on 4/13/16.
 */
public class Equation {
    private final char[] possibleOperators = {'%', '/', '*', '-', '+'};

    private Random rand;
    private boolean containsParentheses;
    private int numOfOperators;
    private Queue<Operator> operatorOrder;
    private ArrayList<Operator> operators;
    private ArrayList<EquationTerm> terms;

    //TODO: get the game logic stuff out (and into gameArena)
    //   Equation should think in terms of number of operators / complexity (contains parens?)
    //   Equation should provide methods for getting THE correct operation, or an incorrect
    //    operation (which may or may not be in the equation at all).

    public Equation(){
        rand = new Random();
        containsParentheses = false;
        numOfOperators = 2;
        operatorOrder = new LinkedList<Operator>();
        operators = new ArrayList<Operator>();
        terms = new ArrayList<EquationTerm>();
    }

    //sets the number of operators given in the equation depending how far the player has gotten in the game
    //if they have advanced at least ten rounds, parentheses will randomly be thrown into the equation
    // @param: levelsPassed gives how many rounds the player has passed
    public Equation(int numOfOperators){
        this();
        this.numOfOperators = numOfOperators;
        if(numOfOperators > 3){
            containsParentheses = rand.nextBoolean();
        }
        constructEquation();
        createProperOperatorOrder();
    }

    public ArrayList<EquationTerm> getEquation(){
        return terms;
    }

    public String toString(){
        StringBuilder spacedEquation = new StringBuilder();
        for(int i = 0; i<terms.size(); i++){
            spacedEquation.append(terms.get(i).getTerm());
            spacedEquation.append(" ");
        }
        return spacedEquation.toString();
    }

    private void constructEquation(){
        int termsInEquation = (numOfOperators*2)+ 1;
        for(int i = 0; i < termsInEquation; i++){
            addTerm(i);
        }
        if(containsParentheses){
            addParentheses();
        }
    }

    private void addTerm(int index){
        boolean isOperator = false;
        if(index % 2 != 0){
            isOperator = true;
        }
        EquationTerm term = new EquationTerm(isOperator, rand, index/2);
        if(isOperator){
            operators.add(term.getOperator());
        }
        terms.add(term);
    }

    private void addParentheses(){
        int[] possibleOpenIndexes = {0,2,4,6};
        int[] possibleCloseIndexes = {4,6,8,10};
        int indexOfOpen = rand.nextInt(possibleOpenIndexes.length);
        int indexOfClose = indexOfOpen;
        if(possibleCloseIndexes.length-indexOfOpen-1 != 0){
            indexOfClose += rand.nextInt(possibleCloseIndexes.length-indexOfOpen-1);
        }
        terms.add(possibleOpenIndexes[indexOfOpen], new EquationTerm("("));
        terms.add(possibleCloseIndexes[indexOfClose], new EquationTerm(")"));

        for(int i = possibleOpenIndexes[indexOfOpen]/2; i<(possibleCloseIndexes[indexOfClose]/2)-1; i++){
            operators.get(i).operatorInParentheses();
        }
    }

    public boolean isCorrectOperator(Operator other){
        if(operatorOrder.peek().equals(other)){
            operatorOrder.poll().makeInactive();
            return true;
        } else {
            return false;
        }
    }

    private void createProperOperatorOrder(){
        ArrayList<Operator> orderedList = operators;
        Collections.sort(orderedList);
        for(int i=0; i< orderedList.size(); i++){
            operatorOrder.offer(orderedList.get(i));
        }
    }

    public String properOrderString(){
        StringBuilder properOrder = new StringBuilder();
        while(!operatorOrder.isEmpty()){
            properOrder.append(operatorOrder.peek().getOperatorChar());
            properOrder.append(",");
            properOrder.append(operatorOrder.poll().getIndex());
            properOrder.append("\n");
        }
        return properOrder.toString();
    }

    public boolean correctEquation(){
        return operatorOrder.isEmpty();
    }

    public Operator getAnOperator(){
        if(operatorOrder.size()>1){
            return operators.get(rand.nextInt(operators.size()));
        } else {
            if(rand.nextInt(5)==0){
                return operators.get(rand.nextInt(operators.size()));
            } else {
                return operatorOrder.peek();
            }
        }
    }

    public ArrayList<Operator> getOperators(){
        return operators;
    }
}
