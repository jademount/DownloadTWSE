package allProcs;

public class Processing {

	public static void main(String[] args) throws Exception{
		// TODO Auto-generated method stub
        DataDownload dd1=new DataDownload();
        DataParse dp1=new DataParse();
        dd1.downTwsedaily();
        dd1.downRstadaily();
        dp1.parseTwseDaily();
        dp1.parseRstaDaily();
        
	}

}

