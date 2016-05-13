package edu.augustana.dreamteam.orderofoperations.math;

import java.util.Random;

import edu.augustana.dreamteam.orderofoperations.math.Operator;

/**
 * Created by jeffreyprior on 5/11/16.
 */
public class EquationTerm {
    private String term;
    private Operator operator;
    private boolean isOperator;

    /**
     * Creates a term to be placed in an equation (could be an operator or integer)
     * @param isOperator boolean whether or not term should be an operator
     * @param rand random object to utilize same random as other class
     * @param index integer for the index in the equation the operator belongs
     */
    public EquationTerm(boolean isOperator, Random rand, int index){
        this.isOperator = isOperator;
        if(isOperator){
            Operator op = new Operator(index);
            operator = op;
            term = operator.getOperatorChar();
        } else {
            term = String.valueOf(rand.nextInt(10));
        }
    }

    /**
     * Creates a term in an equation that is only to be an integer value so
     * not an operator
     * @param term
     */
    public EquationTerm(String term){
        isOperator = false;
        this.term = term;
    }

    /**
     * Access to whether or not term is an operator
     * @return true or false for whether or not the term is an operator
     */
    public boolean isOperator(){
        return isOperator;
    }

    /**
     * Access to the operator for the term if it is an operator
     * @return operator object if operator and null if not
     */
    public Operator getOperator(){
        if(isOperator){
            return operator;
        } else {
            return null;
        }
    }

    /**
     * Access to the character of the term
     * @return the String value of the term regardless of operator or not
     */
    public String getTerm(){
        return term;
    }
}