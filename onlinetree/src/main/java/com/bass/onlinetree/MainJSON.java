package com.bass.onlinetree;

import org.json.JSONArray;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;


/**
 * Class Работа с запросами.
 * @author Alexey Makarov
 * @since 04.07.2018
 * @version 1.0
 */

@WebServlet("/json")
public class MainJSON extends HttpServlet{

    ManageBase base;

    public MainJSON(){
        System.out.println("MainJSON()");

    }
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        if(base == null)
            try {
                base = new ManageBase(request.getServletPath());
                base.baseInit();
                base.CreateDB();
            }
            catch (Exception e){
                System.out.println("SOME TROUBLE " + e.getLocalizedMessage());
                e.printStackTrace();
            }

        String load = request.getParameter("load");
        try{
            Thread.sleep(2000);}
        catch(InterruptedException ie){
            ie.printStackTrace();
        }
        if(load!=null) {
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            PrintWriter out = response.getWriter();
            JSONArray json;
            try {
                json = base.ReadDBJSON(load);
                out.print(json.toString());
            }
            catch(Exception e){
                e.printStackTrace();
            }
        }
    }
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        String json = request.getParameter("create");
        if(json != null) {
            JSONObject jsonNew = new JSONObject(json);
            String id = null,text = null,parent = null;
            boolean children = false;
            try {
                id = jsonNew.getString("id");
                text = jsonNew.getString("text");
                children = jsonNew.getJSONArray("children").length() == 0 ? false : true;
                parent = jsonNew.getString("parent");
                if(parent.equals("#"))
                    parent = "root";
                base.WriteDB(id, parent, text, children);
            }
            catch (Exception sqle){

                sqle.printStackTrace();
            }
        }
        json = request.getParameter("delete");
        if(json != null){
            try {
                base.DeleteDB(json);
            }
            catch(SQLException sqle){
                sqle.printStackTrace();
            }
        }
    }
}