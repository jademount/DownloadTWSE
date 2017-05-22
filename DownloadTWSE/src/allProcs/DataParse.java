package allProcs;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.mysql.jdbc.Statement;

public class DataParse {
	LocalDate today=LocalDate.now();
	String dir;
	InputStream fis = null;
	BufferedReader reader = null;
	String dailyUpdateQuery;
	PreparedStatement pStatement;
	Connection con;
	String date="";
	LocalDate maxDate;
	
	public void parseTwseDaily() throws Exception{
        //reading file line by line in Java using BufferedReader      
        con=getconnection();
        con.setAutoCommit(false);
        dailyUpdateQuery="INSERT INTO twsedaily(date,stockid,stockname,vol,deals,amount,open,high,low,close) VALUES(?,?,?,?,?,?,?,?,?,?)";
        pStatement=con.prepareStatement(dailyUpdateQuery);
        //int date1;
        dir="D:/StockProject/TWSE/Test/";
        //String dir="E:/a/";
        //File file=new File(dir);
        //String[] twse=file.list();
        String sec="證券代號";
        maxDate=getDBMaxDate("twsedaily");
        //System.out.println(maxDate.toString());
        if(maxDate.isEqual(today))return;
        //try {
        while (maxDate.isBefore(today)||maxDate.isEqual(today)){
            //for(int a=0;a<twse.length;a++){
            //System.out.println(twse[a]);
        	System.out.println(maxDate.toString());
        	File fi=new File(dir+"A112"+maxDate.toString()+"ALLBUT0999.csv");
        	if(!fi.exists()){
            	System.out.println("There is no data for "+maxDate.toString());
        		maxDate=maxDate.plusDays(1);
            	continue;
        	}
            System.out.println("Parsig "+fi.toString()+" now");
            fis = new FileInputStream(fi);
            reader = new BufferedReader(new InputStreamReader(fis,"Big5"));
            System.out.println("Reading "+fi.toString()+" line by line using BufferedReader");
            //date=twse[a].substring(4,14);
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
                  if(bb[i].contains("--"))bb[i]="0";
                  }
                  //System.out.println(bb[1]);
                  pStatement.setString(1,maxDate.toString());
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
        }   
            maxDate=maxDate.plusDays(1);
            
            }
            pStatement.executeBatch();
            con.commit();
            if(reader!=null&&fis!=null)
            	{reader.close();
            fis.close();}
        /*} 
	catch (FileNotFoundException ex) {
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
        }*/
}
	
	public void parseRstaDaily() throws Exception{
	System.out.println("Starting parsing Rsta data");
	con=getconnection();
    con.setAutoCommit(false);
    dailyUpdateQuery="INSERT INTO rstadaily(date,stockid,stockname,vol,deals,amount,open,high,low,close) VALUES(?,?,?,?,?,?,?,?,?,?)";
    pStatement=con.prepareStatement(dailyUpdateQuery);
    //int ydate;
	dir="D:/StockProject/OTC/test/";
	//File file=new File(dir);
	//String[] rstas=file.list();
	maxDate=getDBMaxDate("rstadaily");
	if(maxDate.isEqual(today))return;
    //try {
    while (maxDate.isBefore(today)||maxDate.isEqual(today)){
	//try {
		//for(int a=0;a<rstas.length;a++){
        //System.out.println(rstas[a]);
    	File fi=new File(dir+"RSTA"+maxDate.toString()+".csv");
        if(!fi.exists()){
        	System.out.println("There is no data for "+maxDate.toString());
    		maxDate=maxDate.plusDays(1);
        	continue;
    	}
        System.out.println("Parsig "+fi.toString()+" now");
    	fis = new FileInputStream(fi);
        reader = new BufferedReader(new InputStreamReader(fis,"Big5"));
        System.out.println("Reading File line by line using BufferedReader");
        String line = reader.readLine();
        line = reader.readLine();
		line=reader.readLine();
        line=reader.readLine();
        while(line.length()>1&&line!=null)
        {
           if(line.charAt(0)!='\"')break;
           
           if(line.charAt(1)=='7'){
                line=reader.readLine();
                continue;
            }

         String bb[]=method(line);
         String dash="---";
		 for(int i=2;i<7;i++){
			 if(bb[i].contains(dash))		 
		     {bb[i]=bb[7];}
	     };
		 //System.out.print(bb[2]+",");
        //insert_rsta(bb,date);
		 pStatement.setString(1,maxDate.toString());
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
        maxDate=maxDate.plusDays(1);    
    }
		pStatement.executeBatch();
        con.commit();
    /*} catch (FileNotFoundException ex) {
        Logger.getLogger(ParseRstaCsv.class.getName()).log(Level.SEVERE, null, ex);
    } catch (IOException ex) {
        Logger.getLogger(ParseRstaCsv.class.getName()).log(Level.SEVERE, null, ex);
    } finally {
        try {*/
        if(reader!=null&&fis!=null)
    	{
        	reader.close();
            fis.close();
            }
        /*} catch (IOException ex) {
            Logger.getLogger(ParseRstaCsv.class.getName()).log(Level.SEVERE, null, ex);
        }
    }*/
}
    public static Connection getconnection() throws Exception{
    String url="jdbc:mysql://127.0.0.1/stktest";
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
      		aa[c] = aa[c].substring(1, aa[c].length() - 1);
            //System.out.println(aa[c]);
    	}
    	return aa;
    	}	
    public LocalDate getDBMaxDate(String dbName) throws Exception{
		   Connection con=getconnection();
	       //con.setAutoCommit(false);
	       String maxDateQuery="select date_format(max(date),'%Y-%m-%d') from "+dbName+";";
	       Statement getMaxDate=(Statement) con.createStatement();
	       ResultSet rs=getMaxDate.executeQuery(maxDateQuery);
	       rs.next();
	       return LocalDate.parse(rs.getString(1)).plusDays(1);
	       }
		
    
   public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
    	DataParse oo = new DataParse();   
	       oo.parseTwseDaily();
	       oo.parseRstaDaily();
	}

}
