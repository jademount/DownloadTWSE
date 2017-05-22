import java.time.temporal.ChronoUnit;
import java.util.*;
import java.time.*;
import java.io.*;

class localDate
{ 
public static void main(String args[])
{ 
         
         //LocalDate ld=LocalDate.now();
         //
         //System.out.println("today is "+ld+" "+dw);
         //int a=7-dw.getValue();
         //dw=dw.plus(a);
         //System.out.println("the next Sunday will be "+ld.plusDays(a)+" "+dw);
        
		 String dir="D:/StockProject/TWSE/2016/";
		 File file=new File(dir);
         String[] rsta=file.list();
		 String stringOfDay[];
		 String weektemp;
		 int daycount=0;
		 int daycount1=0;
		 int[] days=new int[3];
		 ArrayList<String> weeks=new ArrayList<String>();
		 stringOfDay=rsta[0].substring(4,14).split("-");
		 for(int c=0;c<3;c++){
		   days[c]=Integer.parseInt(stringOfDay[c]);
		   }
		 LocalDate ldt=LocalDate.of(days[0], days[1], days[2]);
		 weektemp=ldt.toString()+",";
		 java.time.DayOfWeek dw=ldt.getDayOfWeek();
		 daycount=dw.getValue();
		 
		 for(int b=1;b<rsta.length;b++){
		   
		   //System.out.println(rsta[b].substring(4,14));
		   stringOfDay=rsta[b].substring(4,14).split("-");
		   
		   for(int e=0;e<3;e++){
			 days[e]=Integer.parseInt(stringOfDay[e]);
		   }
		   LocalDate ldt1=LocalDate.of(days[0], days[1], days[2]);
		   java.time.DayOfWeek dw1=ldt1.getDayOfWeek();
		   daycount1=dw1.getValue();
		   long between=ChronoUnit.DAYS.between(ldt,ldt1);
		   //System.out.println(between);  
		   if (daycount1>daycount && between<6){
		   weektemp+=ldt1.toString()+",";
		   ldt=ldt1;
		   daycount=daycount1;
		   }
		   else 
		   {weeks.add(weektemp);
	       weektemp=ldt1.toString()+",";
	       ldt=ldt1;
	       daycount=daycount1;
	       }
		   
			 
			 //dw[b]=ldt.getDayOfWeek();
		   }
		   weeks.add(weektemp);
		   int weekcount=weeks.size();
		   System.out.println("Number of weeks is :"+weekcount);
		   for(int d=0;d<weekcount;d++)
		   {System.out.println(weeks.get(d));}
		 //daycount+=dw[0].getValue();
		 
		 
		 
		 }
}
