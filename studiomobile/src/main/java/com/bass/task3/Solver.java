package com.bass.task3;

import java.io.*;
import java.util.*;
import java.lang.Math;

public class Solver
{
    public static void main(String[] args) throws InterruptedException, IOException
    {
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer strTokenizer;
        String[] string = in.readLine().split(" ");
        int countNum = 0, koef = 1, result = 0;
        for(int i = 0; i < string.length; i++){
            switch(string[i].charAt(0)){
                case '+':
                case '-':
                case '*':
                case '/':
                    if(string[i-2].compareTo("n") == 0) {
                        result = calc(Integer.parseInt(string[i-2*koef]), Integer.parseInt(string[i-1]), string[i].charAt(0));
                    }
                    else{
                        result = calc(Integer.parseInt(string[i-2]), Integer.parseInt(string[i-1]), string[i].charAt(0));
                    }
                    string[i-1] = "n";
                    string[i] = "" + result;

                    if(countNum > 2)
                        ++koef;
                    else
                        koef = 1;
                    --countNum;
                    break;
                default:
                    ++countNum;
            }
        }
        System.out.print(result);
    }
    public static int calc(int a, int b, char op){
        if(op == '+')
            return a+b;
        else if(op == '-')
            return a-b;
        else if(op == '*')
            return a*b;
        else if(op == '/')
            return a/b;
        else return 0;
    }
}

