package com.project.sql;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.derby.drda.NetworkServerControl;

public class DerbyConnection {
    private static Connection conn = null;
    private static NetworkServerControl server = null;
    private static Statement stmt = null;
	//private static String dbURL = "jdbc:derby:F:/DevTools/workspace/MyDB;create=true";
    //private static String dbURL = "jdbc:derby:MyDB;create=true";
    private static String dbURL = "jdbc:derby://localhost:1527/MyDb;create=true";
	
	public static void connect() throws SQLException {
		try {
            server = new NetworkServerControl(InetAddress.getByName("localhost"), 1527);
            server.start(null);
        } catch (UnknownHostException e1) {
            e1.printStackTrace();
        } catch (Exception e1) {
            e1.printStackTrace();
        }
		try {
            Class.forName("org.apache.derby.jdbc.EmbeddedDriver").newInstance();
            conn = DriverManager.getConnection(dbURL); 
            stmt = conn.createStatement();
        } catch (Exception except) {
            except.printStackTrace();
        }
		 try {
			 String projTable = "CREATE TABLE project (" + 
			 		"projID int NOT NULL PRIMARY KEY GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1)," + 
			 		"projName VARCHAR(50), startDate DATE, endDate DATE)";
			 
             stmt.execute(projTable);
             stmt.close();
         } catch (Exception e) {
             System.out.println("Project table already exist");
         }
		 try {
			 String taskTable = "CREATE TABLE task (" + 
			 		"	taskID int NOT NULL PRIMARY KEY GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1)," + 
			 		"	projID int references project(projID)," + 
			 		"	taskName VARCHAR(50)," + 
			 		"	startDate DATE," + 
			 		"	endDate DATE)";
			 
             stmt.execute(taskTable);
             stmt.close();
         } catch (Exception e) {
        	 System.out.println("Task table already exist");
         }
		 try {
			 String taskDependencyTable = "CREATE TABLE task_dependency (" +
						"taskID int NOT NULL," +
						"taskDependentID int NOT NULL," +
						"CONSTRAINT taskDependentID_ref FOREIGN KEY (taskDependentID) REFERENCES task(taskID))";					
			 
             stmt.execute(taskDependencyTable);
             stmt.close();
         } catch (Exception e) {
        	 System.out.println("Task Dependency table already exist");
         }
	}
	
	public static Connection getConnection() {
		return conn;
	}
	
	public static void voidConnection() {
		 try
	        {
	            if (conn != null) {
	                DriverManager.getConnection(dbURL);
	                conn.close();
	            }           
	        }
	        catch (SQLException sqlExcept) {
	            
	        }
	}
}
