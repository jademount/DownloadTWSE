import java.io.File;

public class FileRename {
	public static void main(String[] args) {      
	      File f = null;
	      File f1 = null;
	      boolean bool = false;
	      
	      try {  
	      
	         // create new File objects
	         f = new File("C:/test.txt");
	         f1 = new File("C:/testABC.txt");
	         
	         // rename file
	         bool = f.renameTo(f1);
	         
	         // print
	         System.out.print("File renamed? "+bool);
	         
	      } catch(Exception e) {
	         
	         // if any error occurs
	         e.printStackTrace();
	      }
	   }
}
