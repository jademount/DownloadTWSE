import java.io.*;
import java.net.*;
import java.util.*; 
import java.time.*;
public class DnRSTA {
        /**
        * @param args
        */
        public static void main(String[] args) {
                // TODO Auto-generated method stub
   int[] days1=new int[3];
   int[] days2=new int[3];
   LocalDate day1,day2;
   String dir="",surl="",destination="";
   String[] stringOfDays1,stringOfDays2;                       
   HttpURLConnection connection = null;                        
   InputStream is;
   ArrayList<String> dates=new ArrayList<String>();                      
   System.setErr(System.out);
   if(args.length==3){
   dir = args[0];
   stringOfDays1=args[1].split("/");
   stringOfDays2=args[2].split("/");
   days1[0]=Integer.parseInt(stringOfDays1[0])-1911;
   days2[0]=Integer.parseInt(stringOfDays2[0])-1911;
     for (int i1=1;i1<3;i1++){    
	   days1[i1]=Integer.parseInt(stringOfDays1[i1]);
	   days2[i1]=Integer.parseInt(stringOfDays2[i1]);
       }
   day1=LocalDate.of(days1[0], days1[1], days1[2]);
   day2=LocalDate.of(days2[0], days2[1], days2[2]);
     if(day1.isBefore(day2))
       { 
       if (days2[0]>=100){
       dates.add(day2.toString().substring(1).replace("-", "/"));}
       else dates.add(day2.toString().substring(2).replace("-", "/"));
       while(day1.isBefore(day2)){
       day2=day2.minusDays(1);
       if (days2[0]>=100){
           dates.add(day2.toString().substring(1).replace("-", "/"));}
       else dates.add(day2.toString().substring(2).replace("-", "/"));
         }
       }
     else if(day1.isAfter(day2)){
    	 if (days1[0]>=100){
    	 dates.add(day1.toString().substring(1).replace("-", "/"));}
    	 else dates.add(day2.toString().substring(2).replace("-", "/")); 
	   while(day1.isAfter(day2)){ 
       day1=day1.minusDays(1);
       if (days1[0]>=100){
      	 dates.add(day1.toString().substring(1).replace("-", "/"));}
      	 else dates.add(day1.toString().substring(2).replace("-", "/"));  
         }
   }
   }
   if (args.length == 2) {
     dir = args[0];
     stringOfDays1=args[1].split("/");
     days1[0]=Integer.parseInt(stringOfDays1[0])-1911;
     for (int i1=1;i1<3;i1++){
     days1[i1]=Integer.parseInt(stringOfDays1[i1]);
     }
     day1=LocalDate.of(days1[0], days1[1], days1[2]); 
     if (days1[0]>=100){
      	 dates.add(day1.toString().substring(1).replace("-", "/"));}
      	 else dates.add(day1.toString().substring(2).replace("-", "/")); 
   }
   if (args.length == 1) {
     dir = args[0];
     day1=LocalDate.now();
     stringOfDays1=day1.toString().split("-");
     days1[0]=Integer.parseInt(stringOfDays1[0])-1911;
     for (int i1=1;i1<3;i1++){
     days1[i1]=Integer.parseInt(stringOfDays1[i1]);
     }
     day1=LocalDate.of(days1[0], days1[1], days1[2]);
     if (days1[0]>=100){
      	 dates.add(day1.toString().substring(1).replace("-", "/"));}
      	 else dates.add(day1.toString().substring(2).replace("-", "/")); 
   }
   if (args.length == 0){
     dir = "./";
     day1=LocalDate.now();
     stringOfDays1=day1.toString().split("-");
     days1[0]=Integer.parseInt(stringOfDays1[0])-1911;
     for (int i1=1;i1<3;i1++){
     days1[i1]=Integer.parseInt(stringOfDays1[i1]);
     }
     day1=LocalDate.of(days1[0], days1[1], days1[2]);
     if (days1[0]>=100){
      	 dates.add(day1.toString().substring(1).replace("-", "/"));}
      	 else dates.add(day1.toString().substring(2).replace("-", "/")); 
             }
     System.out.println(dates);
	 System.out.println(" Dir:" + dir);
    try{
        for(String sdate:dates)
                 {
 
        System.out.println("Downloading:"+sdate+" data");
        surl=String.format("http://www.gretai.org.tw/ch/stock/aftertrading/DAILY_CLOSE_quotes/stk_quote_download.php?"
                 + "l=zh-tw&d=%s&s=0,asc,0", sdate);
        sdate=sdate.replace("/","");
        destination=dir+"RSTA"+sdate+".csv";
        connection = (HttpURLConnection) new URL(surl).openConnection(); 
        is = connection.getInputStream();
        FileOutputStream fos = new FileOutputStream(destination);
        byte[] buffer = new byte[1024];
        for (int length; (length = is.read(buffer)) > 0; fos.write(buffer, 0, length));
        fos.close();
        is.close();
                  } 
				  }
     catch (Exception e) {
        e.printStackTrace();
        //  System.exit(-1);
              } 
     finally {
        if (connection != null) {
        connection.disconnect();
                       }   //}
 
        }
 
                        }
        }
