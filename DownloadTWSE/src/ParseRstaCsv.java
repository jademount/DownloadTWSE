
import java.io.*;
import java.util.*;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.sql.*;
/**
* BufferedReader and Scanner can be used to read line by line from any File or
* console in Java
* This Java program demonstrate line by line reading using BufferedReader in Java
*
* @author Javin Paul
*/
public class ParseRstaCsv{  
    public static void main(String args[]) throws Exception{
        //reading file line by line in Java using BufferedReader      
    	Connection con=getconnection();
        con.setAutoCommit(false);
        String dailyUpdateQuery="INSERT INTO rstadaily(date,stockid,stockname,vol,deals,amount,open,high,low,close) VALUES(?,?,?,?,?,?,?,?,?,?)";
        PreparedStatement pStatement=con.prepareStatement(dailyUpdateQuery);
        InputStream fis = null;
        BufferedReader reader = null;
		String date="";
		int ydate;
		String dir="D:/StockProject/OTC/forSample/";
		File file=new File(dir);
		String[] rstas=file.list();
		
		try {
			for(int a=0;a<rstas.length;a++){
            //System.out.println(rstas[a]);
			fis = new FileInputStream(dir+rstas[a]);
            reader = new BufferedReader(new InputStreamReader(fis,"Big5"));
            System.out.println("Reading File line by line using BufferedReader");
            String line = reader.readLine();
            line = reader.readLine();
			if(line.length()>18)
            {date=line.substring(10,19).replace("/","-");
			ydate=Integer.parseInt(date.substring(0,3))+1911;
			date=String.valueOf(ydate)+"-"+date.substring(4);}
			else {date=line.substring(10,18).replace("/","-");
			ydate=Integer.parseInt(date.substring(0,2))+1911;
			date=String.valueOf(ydate)+"-"+date.substring(3);}
			
			System.out.println(date);
            line=reader.readLine();
            line=reader.readLine();
            while(line.length()>1&&line!=null)
            {
               if(line.charAt(0)!='\"')break;
               if(line.charAt(1)=='7')
               {
                    line=reader.readLine();
                    continue;
                }
    
             String bb[]=method(line);
             String dash="---";
			 if(bb[2].contains(dash)){
			 for(int i=2;i<7;i++)
			 {bb[i]=bb[7];}
		     };
			 System.out.print(bb[2]+",");
            //insert_rsta(bb,date);
			 pStatement.setString(1,date);
             pStatement.setString(2,bb[0]);//bb[0] is stockid
             pStatement.setString(3,bb[1]);
             //pStatement.setString(3,new String(bb[1].getBytes("UTF8"),"UTF8") );//bb[1] is stockname
             pStatement.setInt(4,Integer.valueOf(bb[8]));//bb[2] is vol
             pStatement.setInt(5,Integer.valueOf(bb[10]));//bb[3] is deals
             pStatement.setLong(6,Long.valueOf(bb[9]));//bb[4] is amount
             pStatement.setBigDecimal(7,new BigDecimal(bb[4]));
             pStatement.setBigDecimal(8,new BigDecimal(bb[5]));
             pStatement.setBigDecimal(9,new BigDecimal(bb[6]));
             pStatement.setBigDecimal(10,new BigDecimal(bb[2]));
             pStatement.addBatch();
			line=reader.readLine();}
            }
			pStatement.executeBatch();
            con.commit();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(ParseRstaCsv.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ParseRstaCsv.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                reader.close();
                fis.close();
            } catch (IOException ex) {
                Logger.getLogger(ParseRstaCsv.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
}

/*private static void insert_rsta(String[] aa,String date)throws Exception{
    // register the driver 
    String sDriverName = "org.sqlite.JDBC";
    Class.forName(sDriverName);
    
    // now we set up a set of fairly basic string variables to use in the body of the code proper
    String sTempDb = "rsta.db";
    String sJdbc = "jdbc:sqlite";
    String sDbUrl = sJdbc + ":" + sTempDb; 
    // which will produce a legitimate Url for SqlLite JDBC :
    // jdbc:sqlite:hello.db
    int iTimeout = 5;
    String sMakeInsert = "INSERT INTO stk_info(date,stkno,stkname,close,open,high,low,volumn,deals) VALUES(";
	sMakeInsert+="\""+date+"\",\"";
	sMakeInsert+= aa[0]+"\",\"";
	sMakeInsert+= aa[1]+"\",";
	sMakeInsert+= aa[2]+",";
	sMakeInsert+= aa[4]+",";
	sMakeInsert+= aa[5]+","+aa[6]+","+aa[8]+","+aa[10]+");";
    //String sMakeSelect = "SELECT response from daily_rsta";
    //System.out.println(sDbUrl);
    // create a database connection
    Connection conn = DriverManager.getConnection(sDbUrl);
    try {
        Statement stmt = conn.createStatement();
        
		try {
            stmt.setQueryTimeout(iTimeout);
            stmt.executeUpdate( sMakeInsert );
            /*
			ResultSet rs = stmt.executeQuery(sMakeSelect);
            try {
                while(rs.next())
                    {
                        String sResult = rs.getString("response");
                        System.out.println(sResult);
                    }
            } finally {
                try { rs.close(); } catch (Exception ignore) {}
            }
			*/
/*        } finally {
            try { stmt.close(); } catch (Exception ignore) {}
        }
    } finally {
        try { conn.close(); } catch (Exception ignore) {}
    }
}*/
public static Connection getconnection() throws Exception{
        String url="jdbc:mysql://127.0.0.1/stksample";
        String user="root";
        String password="jade1013";
        try{
        Connection conn=DriverManager.getConnection(url,user,password);
        return conn;
        //System.out.println("database connected");
        }catch(SQLException e){
        throw new IllegalStateException("can't connect the database!",e);}}

private static String[] method(String sline)
{
int a,b,i=0;
String subline1="",subline2="";
int counter1=0,counter2=0;
for (int c=0;c < sline.length();c++)
{
if (sline.charAt(c) == ',')
{counter1++;}
}
while (i < sline.length())
{
counter2++;
if (counter2 > counter1)break;
a = sline.indexOf(",", i);
//System.out.println(a);
b = a - 1;
//System.out.println(sline.substring(b,a));
if (!sline.substring(b, a).equals("\""))
{
//System.out.println("it's not \"");
subline1 = sline.substring(0, a);
subline2 = sline.substring(a).replaceFirst(",", "");
sline = subline1 + subline2;
}
i = a + 1;
}                              
String aa[]=sline.split(",");
for (int c=0;c < aa.length;c++)//removing ""
{
//int d=aa[c].length() - 1;
//System.out.println(d);
aa[c] = aa[c].substring(1, aa[c].length() - 1);
//System.out.println(aa[c]);
}
return aa;
}
}