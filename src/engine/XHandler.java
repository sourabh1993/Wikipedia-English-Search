package engine;

import java.io.PrintWriter;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class XHandler extends DefaultHandler{

	static boolean title = false;
    static boolean text = false;
    static boolean id=false;
    static String idval=null,tval,titlestr;
    static StringBuilder cstr;
    static TextProcessor txp;
    static StringBuilder category,infobox,exlinks;
    static Indexer ind;
    static int pageno=0;
    int contents=0;
    
    static
    {
    	title=false;text=false;id=false;
    	txp=new TextProcessor();
    	cstr=new StringBuilder();
    	category=new StringBuilder();
    	infobox=new StringBuilder();
    	exlinks=new StringBuilder();
    	ind=new Indexer();
    	
    }
    

 
    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes)
            throws SAXException {
    	if (qName.equalsIgnoreCase("title")){title=true;cstr.setLength(0);}
	 	else if (qName.equalsIgnoreCase("text")){ text = true;cstr.setLength(0);} 
	 	else if (qName.equalsIgnoreCase("id") && idval==null ){ id = true;cstr.setLength(0);}
	 	
    }
 
 
    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
    	
    	//System.out.println(cstr);
    	if(id){idval=cstr.toString();}
    	else if(title)
    	{
    		
    		txp.removeSpecialChar(cstr,true);			//change it for including digits
    		tval=cstr.toString();
    		titlestr=cstr.toString();
    		
    		
    //		System.out.println(titlestr+"  "+pageno);
    		
    	}
    	
    	else if(qName.equalsIgnoreCase("text")){
    	
    /*	System.out.println("===================================================================");	
    	System.out.println("Title:"+tval);
    	System.out.println("Id:"+idval);
    	System.out.println("text:"+cstr.toString());
    */	
    		Main.docfile.write(idval+":"+titlestr+"\n");
    try{
    	txp.processText(cstr,category,infobox,exlinks);
    	
    	ind.BuildIndex();
    	}
    catch(Exception e)
    {
    	// Do nothing
    	e.printStackTrace();
    	System.out.println("Doing nothing");
    }

    	if(contents-900000>0)	
    	{
    		System.out.println(pageno);
    		Indexer indwriter=new Indexer();
        	indwriter.writeIndex(null);
    		Main.writer.close();
    		Main.no_of_files++;
    		contents=0;
    	  	try{	Main.writer = new PrintWriter("temp\\temp"+Main.no_of_files+".txt","UTF-8");}
    	  	catch(Exception e){System.out.println("error");}    		
    	}
    	
    	pageno++;
    	infobox.setLength(0);
    	category.setLength(0);
    	exlinks.setLength(0);
    	idval=null;
    	cstr.setLength(0);
    	}
    	
    	if (qName.equalsIgnoreCase("title"))title=false;
    	else if (qName.equalsIgnoreCase("text")){ text = false; }
    	else if (qName.equalsIgnoreCase("id")){ id = false;}//idval=cstr.toString();} 	 	
    }
 
 
    @Override
    public void characters(char ch[], int start, int length) throws SAXException {
 
    	if(text || title || id)
    	{
    	cstr.append(ch, start, length);
    	contents++;
    	}
    	
    }
}
