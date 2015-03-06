package engine;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import javax.xml.parsers.SAXParserFactory;
 
//import com.journaldev.xml.Employee;
 
public class Main {
 
	public static PrintWriter writer,outfile,docfile;
	public static int no_of_files=1152;
	public static String path="C:\\z_SD_data\\Semester 2\\IRE\\Z_IRE\\";
	
	
    public static void main(String[] args) {
    SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
    
    long startTime = System.currentTimeMillis();
    long sTime = 0;
    path=args[0];
    try {
    	
    	// Indexer ind=new Indexer();
    	/*	
    	docfile=new PrintWriter("Documents.txt");
    	
       SAXParser saxParser = saxParserFactory.newSAXParser();
        XHandler handler = new XHandler();
        Indexer ind=new Indexer();
        writer = new PrintWriter("temp\\temp0.txt","UTF-8");
        
       //writer = new PrintWriter("index/sample.txt","UTF-8");
        
       //saxParser.parse(new File(args[0]), handler);
        saxParser.parse(new File(path+"sample_43gb.xml"), handler);
        
      //  System.out.println("TIME :"+(System.currentTimeMillis() - startTime) + " ms");
      //  System.out.println("writing indexes now");
        //indwriter.writeIndex();
        
        ind.writeIndex(null);
        writer.close();
        
        
    	
        
        */
    //  MultiIndex mi=new MultiIndex();
     //  mi.buildMultiIndex();						// Build secondary index on documents
      //  docfile.close();
        
    //	 mi.buildDocIndex();						/******** Build idnex on Documents*****************/
    	
    /*	
    	writer=new PrintWriter("index43gb.txt","UTF-8");
        ind.mergeFiles();
        writer.close();
    	*/
  //  	  MultiIndex mi=new MultiIndex();
   //       mi.buildMultiIndex();
    	
    	
    	
    	
    	
    /*		**************************** uncomment it 	*/	
    	QueryProcessor qp=new QueryProcessor();
    	
  	   sTime = System.currentTimeMillis();
  	   
  	//   qp.processQuery("gandhi");
  	   
  	   BufferedReader fbr=new BufferedReader(new InputStreamReader(System.in));
  	   
  	   int noofqueries=Integer.parseInt(fbr.readLine());
  	   
  	   for(int j=0;j<noofqueries;j++){
  		   
  		   qp.processQuery(fbr.readLine());
  		   System.out.println();   
  	   }
    	
    	//RandomAccessFile raf43=new RandomAccessFile(new File("index43gb.txt"),"r");
    	
    /*	while(true)
    	{
    	String str=br.readLine();	
    	qp.processQuery(str);
    	}
    */    
    	
    	
    /*****************	//BufferedReader check=new BufferedReader(new FileReader("index43gb.txt"));	// ************For testing purpose***********
    	RandomAccessFile check=new RandomAccessFile(new File("docindex"),"r");
    	RandomAccessFile check1=new RandomAccessFile(new File("Documents.txt"),"r");
    	long aaa=178506L;
	//	check.seek(aaa);
		check1.seek(aaa);
		//System.out.println(check.readLine());
		System.out.println(check1.readLine());
    	check.close(); 
        check1.close();
     /************************************************************/
    	 
    	 
    }
    catch(Exception e)
    {
    	e.printStackTrace();
    }
    
    
    long stopTime = System.currentTimeMillis();
    long elapsedTime = stopTime - startTime;
    
  // System.out.println("TIME (total):"+elapsedTime + " ms");
   
   elapsedTime = stopTime - sTime;
  // System.out.println("TIME (retrieval):"+elapsedTime + " ms");
   // System.out.println("Words: "+Indexer.samplewordcount);
    }
 
}