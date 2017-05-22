package allProcs;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

import com.mysql.jdbc.Statement;

public class DataDownload {
	   private String address,urlParm;
	   private String destination,dir;
	   private String twsePostParm[] = {"download", "qdate", "selectType"};
	   private String twsePostData[] = {"csv", "", "ALLBUT0999"};
	   private HttpURLConnection connection;
	   private URL url; 
	   LocalDate today=LocalDate.now();
	   InputStream is;
	   
	   public LocalDate getDBMaxDate(String dbName) throws Exception{
		   Connection con=getconnection();
	       //con.setAutoCommit(false);
	       String maxDateQuery="select date_format(max(date),'%Y-%m-%d') from "+dbName+";";
	       Statement getMaxDate=(Statement) con.createStatement();
	       ResultSet rs=getMaxDate.executeQuery(maxDateQuery);
	       if(rs.next()){
	       return LocalDate.parse(rs.getString(1)).plusDays(1);}
	       else return LocalDate.now();
	       }
		
	   
	   public void downTwsedaily() throws Exception{
		   
		   DataOutputStream wr;
		   
		   address="http://www.twse.com.tw//ch/trading/exchange/MI_INDEX/MI_INDEX.php";
		   dir="D:/StockProject/TWSE/test/";
		   LocalDate maxDate=getDBMaxDate("twsedaily");
		   while (maxDate.isBefore(today)||maxDate.isEqual(today)){
		   try{
		   //to generate twsePostData[1]
			   System.out.println(maxDate.toString());
			   String datepart[]=maxDate.toString().split("-");
			   twsePostData[1]=String.valueOf(Integer.parseInt(datepart[0])-1911)+"/"+datepart[1]+"/"+datepart[2];
			   
			   for (int jx = 0; jx < twsePostParm.length; jx++) {
			      urlParm += "&" + twsePostParm[jx] + "=" + URLEncoder.encode(twsePostData[jx], "UTF-8");
			   }
		   urlParm = urlParm.substring(1);
		   url= new URL(address);
		   setConn(url);
		   wr = new DataOutputStream(connection.getOutputStream());
		   wr.writeBytes(urlParm);
		   wr.flush();
		   wr.close();
		   is = connection.getInputStream();
		   destination=dir+"A112"+maxDate.toString()+"ALLBUT0999.csv";
		   FileOutputStream fos = new FileOutputStream(destination);
		   byte[] buffer = new byte[1024];
		   for (int length; (length = is.read(buffer)) > 0; fos.write(buffer, 0, length));
		   File fi = new File(destination);
		   fos.close();
		   is.close();
		   if (fi.length() < 50 * 1024) {fi.delete();}
		   maxDate=maxDate.plusDays(1);
		   }
		   catch (Exception e) {
		       e.printStackTrace();
		        System.exit(-1);
		} finally {

		        //if (connection != null) {
		        //    connection.disconnect();
		        }}}
		    
	   
	   public void downRstadaily() throws Exception{
		   LocalDate maxDate=getDBMaxDate("rstadaily");
		   dir="D:/StockProject/OTC/Test/";
		   while (maxDate.isBefore(today)||maxDate.isEqual(today)){
		   try{
			   System.out.println(maxDate.toString());
			   String smaxDate=maxDate.toString().replace("-","/");
			   String datePart[]=smaxDate.split("/");
               int datePart1=Integer.parseInt(datePart[0])-1911;
               smaxDate=String.valueOf(datePart1)+smaxDate.substring(4);
               System.out.println(smaxDate);
			   address=String.format("http://www.gretai.org.tw/ch/stock/aftertrading/DAILY_CLOSE_quotes/stk_quote_download.php?"
	               + "l=zh-tw&d=%s&s=0,asc,0", smaxDate);        
			   connection = (HttpURLConnection) new URL(address).openConnection(); 
			   is = connection.getInputStream();
		   //smaxDate=smaxDate.replace("/","");
			   destination=dir+"RSTA"+maxDate.toString()+".csv";
	           FileOutputStream fos = new FileOutputStream(destination);
	           byte[] buffer = new byte[1024];
	           for (int length; (length = is.read(buffer)) > 0; fos.write(buffer, 0, length));
	           //File fi = new File(dir, destination.substring(filenamestart));
	           
	           fos.close();
	           is.close();
	           File fi = new File(destination);
	           //System.out.println(fi.isFile());
	           if (fi.length() < 50 * 1024) {fi.delete();
	           }
	           
	       maxDate=maxDate.plusDays(1);
	           
	           
		   }
		   catch (Exception e) {
		       e.printStackTrace();
		        System.exit(-1);
		} finally {

		        //if (connection != null) {
		        //    connection.disconnect();
		        //}
		    }
	   }
	   }
	   public void setConn(URL purl){
	   try{
	       connection = (HttpURLConnection) purl.openConnection();
		   connection.setRequestMethod("POST");
		   connection.setRequestProperty("Content-Type",
		                    "application/x-www-form-urlencoded");
		   connection.setRequestProperty("Content-Length", ""
		                    + Integer.toString(urlParm.getBytes().length));
		   connection.setRequestProperty("Content-Language", "UTF-8");
		   connection.setUseCaches(false);
		   connection.setDoInput(true);
		   connection.setDoOutput(true);}
	   catch (Exception e) {
	       e.printStackTrace();
	        System.exit(-1);
	} finally {

	        //if (connection != null) {
	        //    connection.disconnect();
	        //}
	    }
	}
	   public static void main(String[] args) throws Exception {
	       DataDownload oo = new DataDownload();   
	       //oo.downTwsedaily();
	       oo.downRstadaily();
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
	}
