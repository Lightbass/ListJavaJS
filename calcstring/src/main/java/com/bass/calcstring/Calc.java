package com.bass.calcstring;


/**
 * Class Простой калькулятор.
 * @author Alexey Makarov
 * @since 04.07.2018
 * @version 1.0
 */

public class Calc {
    /** Результат вычислений одного из методов  */
    private double result;
    /**
     * Метод для арифметических операций в строке
     * @param str Строка вида "100(card)-200(cinema) / 50(food) +20 +20" без скобок.
     */
    public void stringOp(String str){
        str = str.replaceAll("\\([^)]+\\)","");
        str = str.replaceAll("\\+"," + ");
        str = str.replaceAll("\\-"," - ");
        str = str.replaceAll("\\*"," * ");
        str = str.replaceAll("/"," / ");
        str = str.replaceAll("\\s{1,}"," ").trim();
        String[] mas = str.split(" ");

        double a = 0.001, b, op = 0;
        for(String s : mas){
            switch (s){
                case "+": op = 1;
                    break;
                case "-": op = 2;
                    break;
                case "*": op = 3;
                    break;
                case "/": op = 4;
                    break;
                default:
                    if(a == 0.001){
                        a = Double.parseDouble(s);
                    }
                    else{
                        b = Double.parseDouble(s);
                        a = (op == 1 ? a+b : (op == 2 ? a-b : (op == 3 ? a*b : a/b)));
                    }
            }
        }
        result = a;
    }

    /**
     * Возвращает результат математической операции для последнего выполненного метода
     * @return результат
     */
    public double getResult(){
        return result;
    }

}
