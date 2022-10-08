package com.bass.onlinetree;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URI;
import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Created by a.makarov on 08.08.2017.
 */
public class ManageBase {

    public Connection conn;
    public Statement statmt;
    public ResultSet resSet;
    public String rootWarPath;

    /**
     * Class Управление базой данных.
     * @author Alexey Makarov
     * @since 04.07.2018
     * @version 1.0
     */

    public ManageBase(String rootWarPath){
        this.rootWarPath = rootWarPath;
        System.out.println("root="+ rootWarPath);
    }

    public void CreateDB() throws ClassNotFoundException, SQLException
    {
        statmt = conn.createStatement();
        statmt.execute("CREATE TABLE if not exists nodes (Id TEXT PRIMARY KEY, Parent text, Text text, Children boolean);");
        resSet = statmt.executeQuery("SELECT * FROM nodes WHERE Id = 'root1';");
        if(!resSet.next())
            statmt.execute("INSERT INTO nodes VALUES ('root1','root','Root','false');");

    }

    public void WriteDB(String id, String parent, String text, boolean children) throws SQLException
    {
        resSet = statmt.executeQuery("SELECT * FROM nodes WHERE Id = '" + id + "';");

        if(resSet.next()){
            resSet = statmt.executeQuery("SELECT * FROM nodes WHERE Id = '" + id + "';");
            resSet.next();
            String parentOld = resSet.getString("Parent");
            statmt.execute("UPDATE nodes SET Parent = '" + parent +"',Text = '" + text + "' WHERE Id = '" + id + "';");
            CheckChildren(parentOld);
        }
        else {
            statmt.execute("INSERT INTO nodes VALUES ('" + id + "','" + parent + "','" + text + "','" + children + "');");
        }
        statmt.execute("UPDATE nodes SET Children = 'true' WHERE Id = '" + parent + "';");
    }

    public void DeleteDB(String id) throws SQLException
    {
        resSet = statmt.executeQuery("SELECT * FROM nodes WHERE Id = '" + id + "';");
        resSet.next();
        String parent = resSet.getString("Parent");
        statmt.execute("DELETE FROM nodes WHERE Id = '" + id + "';");
        CheckChildren(parent);
    }

    public void CheckChildren(String parent) throws SQLException{
        resSet = statmt.executeQuery("SELECT * FROM nodes WHERE Parent = '" + parent + "';");
        if(!resSet.next())
            statmt.execute("UPDATE nodes SET Children = 'false' WHERE Id = '" + parent + "';");
    }

    public JSONArray ReadDBJSON(String parent) throws ClassNotFoundException, SQLException
    {
        JSONArray al = new JSONArray();
        JSONObject map;
        resSet = statmt.executeQuery("SELECT * FROM nodes WHERE Parent = '" + parent + "';");
        while(resSet.next())
        {
            map = new JSONObject();
            map.put("id", resSet.getString("Id"));
            map.put("text", resSet.getString("Text"));
            map.put("children", resSet.getBoolean("Children")? true : false);
            al.put(map);
        }
        return al;
    }

    public void CloseDB() throws ClassNotFoundException, SQLException
    {
        conn.close();
        statmt.close();
        resSet.close();
    }

    public void baseInit() throws ClassNotFoundException, SQLException, URISyntaxException{
        conn = null;
        URI dbUri = new URI(System.getenv("DATABASE_URL"));
        String username = dbUri.getUserInfo().split(":")[0];
        String pass = dbUri.getUserInfo().split(":")[1];
        String dbUrl = "jdbc:postgresql://" + dbUri.getHost() + dbUri.getPath();
        conn = DriverManager.getConnection(dbUrl, username, pass);

        //Class.forName("org.sqlite.JDBC");
        //conn = DriverManager.getConnection("jdbc:sqlite:./../webapps/java/dist/nodes.s3db");
    }
}
