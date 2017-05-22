import java.lang.*;
import java.util.*;
import java.io.*;
import java.net.*;
import java.time.*;

//2 date arguments: format yyyy-mm-dd

class DloadTWSE {
public static void main(String[] args) {
    String dir="D:/StockProject/TWSE/2004";
    String Url = "http://www.twse.com.tw//ch/trading/exchange/MI_INDEX/MI_INDEX.php";
	String destination;
	String postParm[] = {"download", "qdate", "selectType"};
    String postData[] = {"csv", "", "ALLBUT0999"};
    String urlParm="";
	
	InputStream is;
    LocalDate day1,day2;
	day1=LocalDate.parse(args[0]);
	day2=LocalDate.parse(args[1]);
	ArrayList<String> dates=new ArrayList<String>();  
	dates.add(day2.toString()); //add the last date to arraylist
	while(day1.isBefore(day2)){
       day2=day2.minusDays(1);
       dates.add(day2.toString());     
         }
	
	for(String sdate:dates){
	String year[]=sdate.split("-");
	int iYear=Integer.parseInt(year[0])-1911;
		
	postData[1] = String.valueOf(iYear)+"/"+year[1]+"/"+year[2];
	destination=dir+"/A112"+sdate+"ALLBUT0999.csv";
	try {
	
	for (int jx = 0; jx < postParm.length; jx++) {
      urlParm += "&" + postParm[jx] + "=" + URLEncoder.encode(postData[jx], "UTF-8");
    }
    urlParm = urlParm.substring(1);
    System.out.println("Downloading "+sdate+" TWSE Data;");
	System.out.println(urlParm);
	URL url = new URL(Url);
    DataOutputStream wr;
    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
    connection.setRequestMethod("POST");
    connection.setRequestProperty("Content-Type",
                    "application/x-www-form-urlencoded");
    connection.setRequestProperty("Content-Length", ""
                    + Integer.toString(urlParm.getBytes().length));
    connection.setRequestProperty("Content-Language", "UTF-8");
    connection.setUseCaches(false);
    connection.setDoInput(true);
    connection.setDoOutput(true);
    //Send request
    wr = new DataOutputStream(connection.getOutputStream());
    wr.writeBytes(urlParm);
    wr.flush();
    wr.close();  
	urlParm="";
	is = connection.getInputStream();
    FileOutputStream fos = new FileOutputStream(destination);
    byte[] buffer = new byte[1024];
    for (int length; (length = is.read(buffer)) > 0; fos.write(buffer, 0, length));
    fos.close();
    is.close();
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
}