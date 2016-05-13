package edu.augustana.dreamteam.orderofoperations.math;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

/**
 * Created by jeffreyprior on 4/13/16.
 */
public class Equation {
    private final char[] possibleOperators = {'%', '/', '*', '-', '+'};

    private Random rand;
    private boolean containsParentheses;
    private int numOfOperators;
    private Queue<Operator> operatorOrder; // Queue representation of correct operator order
    private ArrayList<Operator> operators; // All operators added to the equation
    private ArrayList<EquationTerm> terms; // All terms within the equation (includes integers)

    /**
     * Initializes a simple equation with two operators and without parentheses
     */
    public Equation(){
        rand = new Random();
        containsParentheses = false;
        numOfOperators = 2;
        operatorOrder = new LinkedList<Operator>();
        operators = new ArrayList<Operator>();
        terms = new ArrayList<EquationTerm>();
    }

    /**
     * Initializes an equation with a specified number of operators in it and adds parentheses
     * only if the number of operators is more than 3 (complexity of difficulty in game)
     * @param numOfOperators the integer value for how many operators will be in equation
     */
    public Equation(int numOfOperators){
        this();
        this.numOfOperators = numOfOperators;
        if(numOfOperators > 3){
            containsParentheses = rand.nextBoolean();
        }
        constructEquation();
        createProperOperatorOrder();
    }

    /**
     * Constructs the equation by adding terms until the number of terms in the equation is correct
     * Should be one more integer value than operators so total terms is operators * 2 + 1
     */
    private void constructEquation(){
        int termsInEquation = (numOfOperators*2)+ 1;
        for(int i = 0; i < termsInEquation; i++){
            addTerm(i);
        }
        if(containsParentheses){
            addParentheses();
        }
    }

    /**
     * Adds a single term to the equation (if odd index in the total equation then an operator so
     * numbers and operators alternate)
     * @param index integer for the index in the complete equation
     */
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

    /**
     * Adds the parentheses into the equation at the proper possible locations while increasing
     * the priority of operators within the parentheses
     */
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

    /**
     * Checks if the operator passed in is the next operator in the proper order of operators for
     * the equation and if so removes from the Queue
     * @param other Operator that is being checked
     * @return boolean for whether or not the operator is the next one to be operated
     */
    public boolean isCorrectOperator(Operator other){
        if(operatorOrder.peek().equals(other)){
            operatorOrder.poll().makeInactive();
            return true;
        } else {
            return false;
        }
    }

    /**
     * Checks if the operator queue is empty meaning that all operators have been selected in the
     * proper order
     * @return boolean for whether or not equation has been completed
     */
    public boolean correctEquation(){
        return operatorOrder.isEmpty();
    }

    /**
     * Sorts the ArrayList of operators to create the proper order that operators should be selected
     * in based on the Operator priority
     */
    private void createProperOperatorOrder(){
        ArrayList<Operator> orderedList = operators;
        Collections.sort(orderedList);
        for(int i=0; i< orderedList.size(); i++){
            operatorOrder.offer(orderedList.get(i));
        }
    }

    /**
     * For testing prints out the correct order of operators
     * @return a String of the correct order of operators along with their index in equation
     */
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

    /**
     * Gives an operator for a possible operator in the equation if the user has more than one left
     * to guess and then a one in four chance of correct operator with one left in equation
     * @return Operator object that could be in equation or not
     */
    public Operator getAnOperator(){
        if(operatorOrder.size()>1){
            return operators.get(rand.nextInt(operators.size()));
        } else {
            if(rand.nextInt(5)==0){
                return operatorOrder.peek();
            } else {
                return new Operator(String.valueOf(possibleOperators[rand.nextInt(5)]),rand.nextInt(5));
            }
        }
    }

    /**
     * Access to the ArrayList for all operators in equation
     * @return ArrayList for all operators
     */
    public ArrayList<Operator> getOperators(){
        return operators;
    }

    /**
     * Access to the full equation in ArrayList representation
     * @return an ArrayList for the equation including numbers and operators
     */
    public ArrayList<EquationTerm> getEquation(){
        return terms;
    }

    /**
     * Creates a spaced String representation of equation
     * @return a String for the equation
     */
    public String toString(){
        StringBuilder spacedEquation = new StringBuilder();
        for(int i = 0; i<terms.size(); i++){
            spacedEquation.append(terms.get(i).getTerm());
            spacedEquation.append(" ");
        }
        return spacedEquation.toString();
    }
}
