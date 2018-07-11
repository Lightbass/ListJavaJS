package com.bass.task2;

import java.io.*;
import java.util.*;
import java.lang.Math;

public class Solver
{
    public static void main(String[] args) throws InterruptedException, IOException
    {
        Map<String, Integer> words = new HashMap<String, Integer>();
        int max = 0, maxWidth = 0;
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer strTokenizer;
        strTokenizer = new StringTokenizer(in.readLine());
        while(strTokenizer.hasMoreElements()){
            String curText = strTokenizer.nextToken();
            if(words.containsKey(curText))
                words.put(curText, words.get(curText) + 1);
            else
                words.put(curText, 1);
            if(words.get(curText) > max)
                max = words.get(curText);
            if(curText.length() > maxWidth)
                maxWidth = curText.length();
        }
        for(int n = 1; n <= max; n++){
            if(words.containsValue(n))
                for(Map.Entry<String, Integer> wordsSet : words.entrySet()){
                    if(n == wordsSet.getValue()){
                        System.out.printf("%" + maxWidth + "s ", wordsSet.getKey());
                        int dots = Math.round((float)wordsSet.getValue() / max * 10);
                        for(int i = 0; i < dots ; i++)
                            System.out.print(".");
                        System.out.println();
                    }
                }
        }
    }
}