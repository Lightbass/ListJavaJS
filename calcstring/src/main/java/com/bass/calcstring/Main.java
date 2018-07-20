package com.bass.calcstring;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import java.io.Serializable;

/**
 * Created by a.makarov on 16.07.2018.
 */
public class Main implements Serializable{

    private String input;
    private double result = 0;
    private Calc c;

    public Main(){
        c = new Calc();
    }
    public double getResult(){
        return result;
    }
    public void setSum(String sum){
       input = sum;
       c.stringOp(input);
       result = c.getResult();
    }
}
