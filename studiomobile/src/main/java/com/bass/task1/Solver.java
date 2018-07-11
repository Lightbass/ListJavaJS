package com.bass.task1;

import java.io.*;
import java.util.*;

/* Для хранения чисел я использовал long, поскольку предел int[-2^31;2^31-1],
   но он не подходит. В требованиях к задаче, указан диапазон числа[0; 2^32] */

class HashTable {
    public List[] values;
    private int divNum;

    public HashTable(int divNum){
        this.divNum = divNum;
        values = new List[divNum];
    }

    public void Insert(long newValue){
        long val = newValue;
        int valdiv = (int)(newValue % divNum);
        if(values[valdiv] == null){
            values[valdiv] = new List();
        }
        values[valdiv].Insert(val);
    }

    public String toString(){
        StringBuilder res = new StringBuilder();
        for(int i = 0; i < divNum; i++){
            String lineres = i + ": ";
            if(values[i] != null)
                lineres += values[i] + "\n";
            else lineres += "\n";
            res.append(lineres);
        }
        return res.toString();
    }
}

class List{
    private ListNode head;
    private ListNode tail;

    public void Insert(long value){
        ListNode cur = new ListNode();
        cur.Insert(value);

        if(head == null){
            head = cur;
            tail = cur;
        }
        else{
            tail.next = cur;
            tail = cur;
        }

    }

    public String toString(){
        String result = "";
        ListNode cur1 = head;
        while(cur1.next!=null){
            result += cur1.value + " ";
            cur1 = cur1.next;
        }
        result += cur1.value;
        return result;
    }
}


class ListNode {
    long value;
    ListNode next;

    public void Insert(long newValue){
        value = newValue;
    }
}


public class Solver
{
    public static void main(String[] args) throws InterruptedException, IOException
    {
        HashTable ht;
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer strTokenizer;
        strTokenizer = new StringTokenizer(in.readLine());
        int size = Integer.parseInt(strTokenizer.nextToken());
        if(size > 0)
            ht = new HashTable(size);
        else{
            System.out.print("Wrong number N<=0");
            return;
        }
        strTokenizer = new StringTokenizer(in.readLine());
        while(strTokenizer.hasMoreElements()){
            ht.Insert(Long.parseLong(strTokenizer.nextToken()));
        }
        System.out.print(ht);
    }
}




