package engine;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TextProcessor {
	
	void processText(StringBuilder sb,StringBuilder category,StringBuilder infobox,StringBuilder exlinks) throws Exception
	{
		
		removeDust(sb,category,infobox,exlinks);
		
		removeSpecialChar(sb,false);
		
		if(category.length()>0)
			removeSpecialChar(category,false);
		
		if(exlinks.length()>0)
			removeSpecialChar(exlinks,false);
		
		if(infobox.length()>0)
			removeSpecialChar(infobox,false);
	}
	
	void removeSpecialChar(StringBuilder sb,boolean keepdigit)
	{
		String mystr=sb.toString();
		
		String mypattern;
		
		if(keepdigit)mypattern="[^a-zA-Z0-9]+";
		else mypattern="[^a-zA-Z]+";
		
		Pattern p=Pattern.compile(mypattern);
    	Matcher m=p.matcher(mystr);
    	sb.setLength(0);
		sb.append(m.replaceAll(" "));
		
	}
	
	
	void removeDust(StringBuilder sb,StringBuilder category,StringBuilder infobox,StringBuilder exlinks) throws Exception
	{
		int len=sb.length(),i,j;
		StringBuilder word=new StringBuilder();
		StringBuilder nodust=new StringBuilder();
		char curchar,prevchar=' ';
		boolean islink=false,starstart=false; // starstart for seperating external links
		
		for(i=0;i<len;i++)
		{
			
			curchar=sb.charAt(i);
			if(curchar==' ' || curchar=='\n')
			{
			//if(i<100)System.out.println(word);
			if(word.length()>0)
				{
				
					//if(i<100)System.out.println("|"+word+"|");
					String wordstr=word.toString();
					
					if(wordstr.contains("==External"))islink=true;
					
					if(starstart==true && prevchar=='\n' && !wordstr.startsWith("*")){starstart=false; islink=false;}
					if(islink==true && starstart==false && wordstr.startsWith("*"))starstart=true;
					
					if(wordstr.equals("{{Infobox"))
					{
						//System.out.println("inside");
						islink=false;starstart=false;
						char ch,chprev;
						int opencount=1;		// To match closing of infobox
						word.setLength(0);
						chprev=' ';
						boolean toinfo=false;
							// current char
					//	System.out.println("here");
						for(j=i+1;j<len;j++,i++)	//traverse inside infobox
						{
							ch=sb.charAt(j);
							//infobox.append(ch);
							
							if(ch=='{' && chprev=='{')opencount++;	// another {{ opened
							if(ch=='}' && chprev=='}')opencount--;	// }} closed
							
							if(ch=='[' && chprev=='['){toinfo=true;}
							if(ch==']' && chprev==']'){toinfo=false;infobox.append(' ');}
							
							if(toinfo)infobox.append(ch);
							
							if(opencount==0){
						//		System.out.println(infobox.toString());
								break;}	// BREAK WHEN INFOBOX CLOSED
							
							chprev=ch;
						}
					
						i++;
					}
					else if(wordstr.contains("[[Category:")){
						islink=false;starstart=false;
						char ch,chprev=' ';
						i++;
						ch=sb.charAt(i);
						
						category.append(wordstr.substring(wordstr.indexOf(':')+1));
						category.append(' ');
						while(i<len && !(ch==']' && chprev==']')){
							category.append(ch);
							i++;
							chprev=ch;
							if(i<len)ch=sb.charAt(i);
						}
						category.append(' ');
					}
					else{						
						if(islink && starstart)
						{
							exlinks.append(word); exlinks.append(' ');
						}
						else if(!wordstr.contains("#"))
						{nodust.append(word);
						nodust.append(' ');
						}
					}
				}
				
				word.setLength(0);
				prevchar=curchar;
			}
			else {word.append(curchar);}
		}
		sb.setLength(0);
		sb.append(nodust);
	}

	
}
