import java.sql.*;
import java.io.*;
import java.util.*;

public class ChangeDate {
	public static void main(String[] args) throws Exception
    {
        // register the driver 
        String sDriverName = "org.sqlite.JDBC";
        Class.forName(sDriverName);
        
        // now we set up a set of fairly basic string variables to use in the body of the code proper
        String sTempDb = "E:/rsta.db";
        String sJdbc = "jdbc:sqlite";
        String sDbUrl = sJdbc + ":" + sTempDb; 
		int iTimeout=5;
		Connection conn = DriverManager.getConnection(sDbUrl);
        
		try {
            Statement stmt = conn.createStatement();
			Statement stmt1 = conn.createStatement();
            
			try {
                stmt.setQueryTimeout(iTimeout);
                //stmt.executeUpdate( sMakeInsert );
                String sMakeSelect="select id,date from stk_info where date not like \"2%\";";
				ResultSet rs = stmt.executeQuery(sMakeSelect);
                String sMakeUpd="";
				try {
                    while(rs.next())
                        {
                            String sResult = rs.getString("date");
							String sID = rs.getString("id");
                            String sDate[]=sResult.split("-");
							String iDate=String.valueOf(Integer.parseInt(sDate[0])+1911);
							for (int i=1;i<sDate.length;i++)
							{iDate+="-"+sDate[i];}
							//{System.out.print(sDate[i]);}
						      //System.out.println(" ");
							
							//System.out.print(iDate);
							//
							sMakeUpd="update stk_info set date=\""+iDate+"\" where id="+sID+";";
							stmt1.executeUpdate(sMakeUpd);
							System.out.println("id:"+sID+",Date " +iDate+" updated"+ " ");
                        }
                } finally {
                    try { rs.close(); } catch (Exception ignore) {}
                }
				
            } finally {
                try { stmt.close(); } catch (Exception ignore) {}
            }
        } finally {
            try { conn.close(); } catch (Exception ignore) {}
        }
    }
 
}


