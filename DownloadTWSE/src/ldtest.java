import java.time.temporal.ChronoUnit;
import java.util.*;
import java.time.*;
import java.io.*;
class ldtest
{
 
public static void main(String args[]){
LocalDate today=LocalDate.now();
LocalDate day1=LocalDate.parse("2016-12-31");
 
while(day1.isBefore(today)||day1.isEqual(today))
{
        System.out.println(day1.toString());
    day1=day1.plusDays(1);
 
}
 
}
 
 
}