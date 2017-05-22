import java.util.*;

import java.sql.Statement;

import java.time.*;
import java.io.*;
import java.sql.*;

public class dailyk {
	
	public static void main(String args[]) throws Exception{
		Connection con=getconnection();
        con.setAutoCommit(false);
        String maxDateQuery="select max(date) as maxdate from twsedaily;";
		Statement selectStmt = con.createStatement();
		
		ResultSet rs = selectStmt.executeQuery(maxDateQuery);
		rs.absolute(1);
		String maxdate = rs.getDate("maxdate").toString();
		
	}

	
	

   public static Connection getconnection() throws Exception{
    String url="jdbc:mysql://127.0.0.1/stk";
    String user="root";
    String password="jade1013";
    try{
    Connection conn=DriverManager.getConnection(url,user,password);
    return conn;
    //System.out.println("database connected");
    }catch(SQLException e){
    throw new IllegalStateException("can't connect the database!",e);}}
}