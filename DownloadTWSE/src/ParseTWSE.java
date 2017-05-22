import java.io.*;
import java.util.*;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.sql.*;
import java.math.BigDecimal;
// compiling using -encoding UTF-8 option
public class ParseTWSE{  
    public static void main(String args[]) throws Exception{
        //reading file line by line in Java using BufferedReader      
        Connection con=getconnection();
        con.setAutoCommit(false);
        String dailyUpdateQuery="INSERT INTO twsedaily(date,stockid,stockname,vol,deals,amount,open,high,low,close) VALUES(?,?,?,?,?,?,?,?,?,?)";
        PreparedStatement pStatement=con.prepareStatement(dailyUpdateQuery);
        InputStream fis = null;
        BufferedReader reader = null;
        String date="";
        //int date1;
        String dir="D:/StockProject/TWSE/forSample/";
        //String dir="E:/a/";
        File file=new File(dir);
        String[] twse=file.list();
        String sec="證券代號";
        try {
            for(int a=0;a<twse.length;a++){
            //System.out.println(twse[a]);
            fis = new FileInputStream(dir+twse[a]);
            reader = new BufferedReader(new InputStreamReader(fis,"Big5"));
            System.out.println("Reading File line by line using BufferedReader");
            date=twse[a].substring(4,14);
            //System.out.println(date);
            String line = reader.readLine();
            line = reader.readLine();
            line = reader.readLine();
            //System.out.println(line);
             
            while(line.length()>1&&line!=null)
            {
            if(line.contains(sec))
            {   //System.out.println(sec);
                line = reader.readLine();
               while(line.length()>1&&line!=null)
               {
                String bb[]=line.split(",");
                if (bb.length<3)break;
                //if (bb[0].length()>13 || bb[0].contains("說明")||bb[0].length()==0)break;
               for(int i=0;i<bb.length;i++){
                  bb[i]=bb[i].replace(" ","").replace("\"","").replace("=","");
                  if(bb[i].contains("--"))bb[i]="0";}
                  System.out.println(bb[1]);
                  pStatement.setString(1,date);
                  pStatement.setString(2,bb[0]);//bb[0] is stockid
                  pStatement.setString(3,bb[1]);
                  //pStatement.setString(3,new String(bb[1].getBytes("UTF8"),"UTF8") );//bb[1] is stockname
                  pStatement.setInt(4,Integer.valueOf(bb[2]));//bb[2] is vol
                  pStatement.setInt(5,Integer.valueOf(bb[3]));//bb[3] is deals
                  pStatement.setLong(6,Long.valueOf(bb[4]));//bb[4] is amount
                  pStatement.setBigDecimal(7,new BigDecimal(bb[5]));
                  pStatement.setBigDecimal(8,new BigDecimal(bb[6]));
                  pStatement.setBigDecimal(9,new BigDecimal(bb[7]));
                  pStatement.setBigDecimal(10,new BigDecimal(bb[8]));
                  pStatement.addBatch();
                  
                //DumpDailyTWSE.insert_twse(bb,date);
                line = reader.readLine();
               }
            }
            else
            line = reader.readLine();
             
        }   }
            pStatement.executeBatch();
            con.commit();
 
        } catch (FileNotFoundException ex) {
            Logger.getLogger(ParseTWSE.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ParseTWSE.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                reader.close();
                fis.close();
            } catch (IOException ex) {
                Logger.getLogger(ParseTWSE.class.getName()).log(Level.SEVERE, null, ex);
             
}
        }
}
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
}