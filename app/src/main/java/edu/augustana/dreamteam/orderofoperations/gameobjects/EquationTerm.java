package edu.augustana.dreamteam.orderofoperations.gameobjects;

import java.util.Random;

import edu.augustana.dreamteam.orderofoperations.math.Operator;

/**
 * Created by jeffreyprior on 5/11/16.
 */
public class EquationTerm {
    private String term;
    private Operator operator;
    private boolean isOperator;

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

    public EquationTerm(String term){
        isOperator = false;
        this.term = term;
    }

    public boolean isOperator(){
        return isOperator;
    }

    public Operator getOperator(){
        return operator;
    }

    public String getTerm(){
        return term;
    }
}
