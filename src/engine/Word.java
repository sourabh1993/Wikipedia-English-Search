package engine;

import java.util.HashMap;
import java.util.Map;

public class Word {
	
	String lex;   // The word
	Map <String,Integer>title;		// docid vs count
	Map <String,Integer> category;	// docid vs count
	Map <String,Integer>  text;		// docid vs count
	Map <String,Integer>  infobox;	// docid vs count
	Map <String,Integer> links;		// docid vs count
	
	public Word()
	{
		title=new HashMap<String,Integer>();
		category=new HashMap<String,Integer>(); 
		text=new HashMap<String,Integer>(); 
		infobox=new HashMap<String,Integer>();
		links=new HashMap<String,Integer>();
	}

}
