package com.fnoz;

import org.json.JSONArray;
import org.json.JSONObject;
import java.sql.*;
/**
 * Created by a.makarov on 08.08.2017.
 */
public class ManageBase {

    public static Connection conn;
    public static Statement statmt;
    public static ResultSet resSet;

    /**
     * Class Управление базой данных.
     * @author Alexey Makarov
     * @since 04.07.2018
     * @version 1.0
     */

    public static void CreateDB() throws ClassNotFoundException, SQLException
    {
        statmt = conn.createStatement();
        statmt.execute("CREATE TABLE if not exists 'nodes' ('Id' TEXT PRIMARY KEY," +
                " 'Parent' text, 'Text' text, 'Children' boolean);");
        resSet = statmt.executeQuery("SELECT * FROM nodes WHERE Id = 'root1';");
        if(!resSet.next())
            statmt.execute("INSERT INTO 'nodes' VALUES ('root1','root','Root','false');");

    }

    public static void WriteDB(String id, String parent, String text, boolean children) throws SQLException
    {
        resSet = statmt.executeQuery("SELECT * FROM nodes WHERE Id = '" + id + "';");

        if(resSet.next()){
            resSet = statmt.executeQuery("SELECT * FROM nodes WHERE Id = '" + id + "';");
            resSet.next();
            String parentOld = resSet.getString("Parent");
            statmt.execute("UPDATE 'nodes' SET Parent = '" + parent +"',Text = '" + text + "' WHERE Id = '" + id + "';");
            CheckChildren(parentOld);
        }
        else {
            statmt.execute("INSERT INTO 'nodes' VALUES ('" + id + "','" + parent + "','" + text + "','" + children + "');");
        }
        statmt.execute("UPDATE 'nodes' SET Children = 'true' WHERE Id = '" + parent + "';");
    }

    public static void DeleteDB(String id) throws SQLException
    {
        resSet = statmt.executeQuery("SELECT * FROM nodes WHERE Id = '" + id + "';");
        resSet.next();
        String parent = resSet.getString("Parent");
        statmt.execute("DELETE FROM nodes WHERE Id = '" + id + "';");
        CheckChildren(parent);
    }

    public static void CheckChildren(String parent) throws SQLException{
        resSet = statmt.executeQuery("SELECT * FROM nodes WHERE Parent = '" + parent + "';");
        if(!resSet.next())
            statmt.execute("UPDATE 'nodes' SET Children = 'false' WHERE Id = '" + parent + "';");
    }

    public static JSONArray ReadDBJSON(String parent) throws ClassNotFoundException, SQLException
    {
        JSONArray al = new JSONArray();
        JSONObject map;
        resSet = statmt.executeQuery("SELECT * FROM nodes WHERE Parent = '" + parent + "';");
        while(resSet.next())
        {
            map = new JSONObject();
            map.put("id", resSet.getString("Id"));
            map.put("text", resSet.getString("Text"));
            map.put("children", resSet.getString("Children").equals("true") ? true : false);
            al.put(map);
        }
        return al;
    }

    public static void CloseDB() throws ClassNotFoundException, SQLException
    {
        conn.close();
        statmt.close();
        resSet.close();
    }

    public static void baseInit() throws ClassNotFoundException, SQLException{
        conn = null;
        Class.forName("org.sqlite.JDBC");
        conn = DriverManager.getConnection("jdbc:sqlite:./../webapps/java/dist/nodes.s3db");
    }
}
