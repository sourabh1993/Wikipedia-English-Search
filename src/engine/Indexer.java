package engine;


import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.TreeMap;


class PQObject
{
	Word word;
	int fpindex;
	
	public PQObject()
	{
		word=null;
		fpindex=0;
	}
}

class MyComparator implements Comparator<PQObject>{
	
	
	 public int compare(PQObject obj1, PQObject obj2) {
		 
		 return obj1.word.lex.compareToIgnoreCase(obj2.word.lex);
	 }
}

public class Indexer {
	
	public static Map <String,Word> index;
	public static StopWord wfilter;
	public static Stemmer mystemmer;
	public static Map<String,Integer> mp;
	public static int samplewordcount=0;
	public static Word outfinalword;
	public static StringBuilder fcn=new StringBuilder("");
	
	static
	{
		index=new TreeMap<String, Word>();
		wfilter=new StopWord();
		mystemmer=new Stemmer();
		mp=new HashMap<String,Integer>();
	}
	
	public void makeIndex(String wordstr,char identifier)
	{
		//System.out.println("::"+wordstr+"::");
		//Main.writer.println(wordstr);
		if(!index.containsKey(wordstr))
		{
			Word w=new Word();
			
			if(identifier=='h'){w.title.put(XHandler.idval, 1);} // word appears in title (h=heading)
			else if(identifier=='c'){w.category.put(XHandler.idval, 1);} // word appears in category
			else if(identifier=='i'){w.infobox.put(XHandler.idval, 1);} // wordi n infobox
			else if(identifier=='t'){w.text.put(XHandler.idval, 1);} // word appears in body
			else if(identifier=='l'){w.links.put(XHandler.idval, 1);} // word appears in external links
			index.put(wordstr, w);
		}
		else
		{
			Word w=index.get(wordstr);
			int val;
			if(identifier=='h')
			{
				//String docid=getLastDocId(w.title,w.hlen);
				if(w.title.containsKey(XHandler.idval)){
				val=w.title.get(XHandler.idval);
				val++;
				w.title.put(XHandler.idval, val);
				}
				else w.title.put(XHandler.idval, 1);
			}
			else if(identifier=='c')
			{
				if(w.category.containsKey(XHandler.idval)){
				val=w.category.get(XHandler.idval);
				val++;
				w.category.put(XHandler.idval, val);
				}
				else w.category.put(XHandler.idval, 1);
			}
			else if(identifier=='i')
			{
				if(w.infobox.containsKey(XHandler.idval)){
				val=w.infobox.get(XHandler.idval);
				val++;
				w.category.put(XHandler.idval, val);
				}
				else w.infobox.put(XHandler.idval, 1);
			}
			else if(identifier=='t')
			{
				if(w.text.containsKey(XHandler.idval)){
					val=w.text.get(XHandler.idval);
					val++;
					w.text.put(XHandler.idval, val);	
				}	
				else w.text.put(XHandler.idval, 1);
			}
			else if(identifier=='l')
			{
				if(w.links.containsKey(XHandler.idval)){
					val=w.links.get(XHandler.idval);
					val++;
					w.links.put(XHandler.idval, val);	
				}	
				else w.links.put(XHandler.idval, 1);
			}
			
			index.put(wordstr, w);
		}
		
	}
	
	
	public void RemoveStopWord(StringBuilder content,char identifier)
	{
		int len,i,wlen;
		char ch;
		len=content.length();
		StringBuilder myword=new StringBuilder();
		StringBuilder output=new StringBuilder();
	  
		String stemresult;
		wlen=0;
		
		for(i=0;i<len;i++)
		{
			ch=content.charAt(i);
			if(ch==' ' && wlen>0)
			{
				if(!wfilter.isStopWord(myword.toString()))
					{
						stemresult=myword.toString();
						mystemmer.add(myword.toString().toCharArray(), wlen);
						mystemmer.stem();
						stemresult=mystemmer.toString();
						output.append(stemresult+' ');
						
						makeIndex(stemresult, identifier);
						//output.append(myword.toString()+' ');
					}
				myword.setLength(0);
				wlen=0;
			}
			else if(ch!=' ')
			{
				myword.append(Character.toLowerCase(ch));
				//myword.append(ch);
				wlen++;
			}
		}
		
		content.setLength(0);
		content.append(output);		
	}
	
	public void BuildIndex()
	{
		StringBuilder title,text,category,infobox,exlink;
		category=XHandler.category;
		text=XHandler.cstr;
		title=new StringBuilder(XHandler.titlestr);
		infobox=XHandler.infobox;
		exlink=XHandler.exlinks;
		
		
		RemoveStopWord(title,'h');
		RemoveStopWord(category,'c');
		RemoveStopWord(infobox,'i');
		RemoveStopWord(text,'t');	
		RemoveStopWord(exlink,'l');
	}

	
	void writeIndex(Word word)
	{
		Word w;
		Map <String,Boolean> docmarker=new HashMap<String,Boolean>();
		StringBuilder wordline=new StringBuilder();
		long linelen=0;
		//System.out.println("total words:"+index.keySet().size());
		
		if(word!=null)index.put(word.lex, word);
		
		for(String cword : index.keySet())
		{
			w=index.get(cword);
			wordline.append(cword+":");
			//Main.writer.print(cword+":");
			
		//if(done%10==0)
		//	System.out.println("|"+cword+"|"+"  "+w.title.keySet().size()+"  "+w.category.keySet().size()+" "+w.text.keySet().size());
			
			
			for(String place : w.title.keySet())
			{
				docmarker.put(place, true);
				wordline.append(place+"-t"+w.title.get(place));
				
				if(w.category.containsKey(place))wordline.append("c"+w.category.get(place));
				if(w.infobox.containsKey(place))wordline.append("i"+w.infobox.get(place));
				if(w.text.containsKey(place))wordline.append("b"+w.text.get(place));
				if(w.links.containsKey(place))wordline.append("l"+w.links.get(place));
				
				wordline.append("|");	
				linelen+=1;
				
				if(linelen>10000)
				{
				Main.writer.print(wordline);
				wordline.setLength(0);
				linelen=0;
				}
				
			}
			
			//Main.writer.print(wordline);
			//wordline.setLength(0);
			
			for(String place: w.category.keySet())
			{
				if(!docmarker.containsKey(place))
				{
					docmarker.put(place, true);
					wordline.append(place+"-c"+w.category.get(place));
					
					if(w.infobox.containsKey(place))wordline.append("i"+w.infobox.get(place));
					if(w.text.containsKey(place))wordline.append("b"+w.text.get(place));
					if(w.links.containsKey(place))wordline.append("l"+w.links.get(place));
					
					wordline.append("|");		
				}
				
				linelen+=1;
				
				if(linelen>10000)
				{
				Main.writer.print(wordline);
				wordline.setLength(0);
				linelen=0;
				}
			}
			
		//	Main.writer.print(wordline);
		//s	wordline.setLength(0);
			
			for(String place: w.infobox.keySet())
			{
				if(!docmarker.containsKey(place))
				{
					docmarker.put(place, true);
					wordline.append(place+"-i"+w.infobox.get(place));
					
					if(w.text.containsKey(place))wordline.append("b"+w.text.get(place));
					if(w.links.containsKey(place))wordline.append("l"+w.links.get(place));
					
					wordline.append("|");		
				}
				
				linelen+=1;
				
				if(linelen>10000)
				{
				Main.writer.print(wordline);
				wordline.setLength(0);
				linelen=0;
				}
			}
			
		//	Main.writer.print(wordline);
		//	wordline.setLength(0);
			
			for(String place:w.text.keySet())
			{
				if(!docmarker.containsKey(place))
				{
					docmarker.put(place, true);
					wordline.append(place+"-b"+w.text.get(place));
					
					if(w.links.containsKey(place))wordline.append("l"+w.links.get(place));
					
					wordline.append("|");	
				}
				
				linelen+=1;
				
				if(linelen>10000)
				{
				Main.writer.print(wordline);
				wordline.setLength(0);
				linelen=0;
				}
			}
			
		//	Main.writer.print(wordline);
		//	wordline.setLength(0);
			
			for(String place:w.links.keySet())
			{
				if(!docmarker.containsKey(place))
				{
					docmarker.put(place, true);
					wordline.append(place+"-l"+w.links.get(place));
					
					wordline.append("|");	
				}
				
				linelen+=1;
				
				if(linelen>10000)
				{
				Main.writer.print(wordline);
				wordline.setLength(0);
				linelen=0;
				}
			}
			
			//System.out.println("======================================");
			//System.out.println(wordline.toString());
			Main.writer.println(wordline);
			wordline.setLength(0);
			docmarker.clear();
		//	done++;
		//	if(done%1000==0)
		//		System.out.println(done);
			
		}
		
		index.clear();
	}

	void setPQObj(PQObject pqo,String line,int index)		// Convert index line into PQ object
	{
		StringBuilder tword=new StringBuilder(); // The word
		int i=0;
		char position = 0;		// l=link t=title b=body c=category i=infobox		
		Word word=new Word();
		String id;					// Id of current doc
		pqo.fpindex=index;
		line+='\0';
		
		for(i=0;line.charAt(i)!=':';i++)tword.append(line.charAt(i));
		word.lex=tword.toString();  // The word corresponding to the current posting list
		tword.setLength(0);
		
		i++;
		while(line.charAt(i)!='\0')
		{
			while(Character.isDigit(line.charAt(i))){tword.append(line.charAt(i));i++;}
			id=tword.toString();
			tword.setLength(0);i++;
			int num=0;
			while(line.charAt(i)!='|')
			{
				if(!Character.isDigit(line.charAt(i)))position=line.charAt(i);
				i++;
				
				while(Character.isDigit(line.charAt(i))){tword.append(line.charAt(i));i++;}
				
				num=Integer.parseInt(tword.toString());
				tword.setLength(0);
				
				switch(position)
				{
				case 'c': word.category.put(id,num);break;
				case 't': word.title.put(id,num);break;
				case 'b': word.text.put(id,num);break;
				case 'i': word.infobox.put(id,num);break;
				case 'l': word.links.put(id,num);break;
				}
			}
			i++;
		}
		
		pqo.word=word;
								
	}
	
	Word mergeSameWords(List<PQObject> oneword,Word finword)
	{
		//Word finword=new Word();
		finword.lex=oneword.get(0).word.lex;
		
		//PQObject pqo=new PQObject();
		for(PQObject pqo : oneword)
		{
			for(String s : pqo.word.title.keySet())finword.title.put(s, pqo.word.title.get(s));
			
			for(String s : pqo.word.category.keySet())finword.category.put(s, pqo.word.category.get(s));
			
			for(String s : pqo.word.infobox.keySet())finword.infobox.put(s, pqo.word.infobox.get(s));
			
			for(String s : pqo.word.text.keySet())finword.text.put(s, pqo.word.text.get(s)); //System.out.println(finword.text.size()); }
			
			for(String s : pqo.word.links.keySet())finword.links.put(s, pqo.word.links.get(s));
			
		}
		return finword;
	}
	
	
	void mergeFiles() throws Exception
	{
		int nof=Main.no_of_files,i;
		
		BufferedReader br[]=new BufferedReader[nof+1];//=new BufferedReader(new FileReader("metadata.txt"));
		PriorityQueue<PQObject> pqueue=new PriorityQueue<PQObject>(new MyComparator());
		
		for(i=0;i<=nof;i++)
		{
			br[i]=new BufferedReader(new FileReader("temp\\temp"+i+".txt"));
			PQObject pqo=new PQObject();
			setPQObj(pqo,br[i].readLine(),i);
			pqueue.add(pqo);
		}
		
		List<PQObject> oneword=new ArrayList<PQObject>();
		PQObject pqobj,pqtemp;
		String linestr=null;
		
		while(!pqueue.isEmpty())
		{
			pqobj=pqueue.remove();
			oneword.add(pqobj);
			
			int fin=pqobj.fpindex;		//file index of removed object
			if((linestr=br[fin].readLine())!=null){		// read next line from file and convert to object and put into queue
				PQObject pqo=new PQObject();
				setPQObj(pqo,linestr,fin);
				pqueue.add(pqo);
			}
			
			while(true)
			{
				if(pqueue.isEmpty())break;
				
				pqtemp=pqueue.peek();
				
				if(pqtemp.word.lex.equalsIgnoreCase(pqobj.word.lex)){
					oneword.add(pqueue.remove());
					
					int find=pqtemp.fpindex; // file index of the removed object
					if((linestr=br[find].readLine())!=null){	// read next line from file and convert to object and put into queue
						PQObject pqo=new PQObject();
						setPQObj(pqo,linestr,find);
						pqueue.add(pqo);
					}
					else br[find].close();
				}
				else break;
				
				//System.out.println("stuck here");
			}
			
			Word finalword=new Word();
			mergeSameWords(oneword,finalword);
			oneword.clear();
			
			outfinalword=finalword;
			//List <String> doclist=sortWord(finalword);
			writeFinalIndex(finalword);
		}
		
	}
	
	void writeFinalIndex(Word finalword)
	{
		Word w=finalword;
		Map <String,Boolean> docmarker=new HashMap<String,Boolean>();
		List <String> doclist=new ArrayList<String>();
				
		for(String place : w.title.keySet())
		{
			docmarker.put(place, true);
			doclist.add(place);
		}
				
		for(String place: w.category.keySet())
		{
			if(!docmarker.containsKey(place))
			{
				docmarker.put(place, true);
				doclist.add(place);	
			}		
		}

		for(String place: w.infobox.keySet())
		{
			if(!docmarker.containsKey(place))
			{
				docmarker.put(place, true);
				doclist.add(place);	
			}	
		}

		for(String place:w.text.keySet())
		{
			if(!docmarker.containsKey(place))
			{
				docmarker.put(place, true);
				doclist.add(place);	
			}	
		}

		
		for(String place:w.links.keySet())
		{
			if(!docmarker.containsKey(place))
			{
				docmarker.put(place, true);
				doclist.add(place);	
			}	
		}
		
		Collections.sort(doclist, new TFComparator());
		
		Main.writer.print(finalword.lex+":"+doclist.size()+":");
		
		int doccount=0;
		
		
		for(String doc : doclist)
		{
			doccount++;
			//Main.writer.print(doc);
			fcn.append(doc);
			
			if(finalword.title.containsKey(doc))fcn.append('t'+""+finalword.title.get(doc));
			if(finalword.category.containsKey(doc))fcn.append('c'+""+finalword.category.get(doc));
			if(finalword.infobox.containsKey(doc))fcn.append('i'+""+finalword.infobox.get(doc));
			if(finalword.text.containsKey(doc))fcn.append('b'+""+finalword.text.get(doc));
			if(finalword.links.containsKey(doc))fcn.append('l'+""+finalword.links.get(doc));
			fcn.append("|");
			
			if(doccount==1000){ Main.writer.print(fcn); fcn.setLength(0);doccount=0;}
			
		}
		
		 Main.writer.println(fcn); fcn.setLength(0);doccount=0;
		 	
	}
	
}


class TFComparator implements Comparator<String> {
	
	int tw=100,iw=20,cw=20,bw=2,lw=1;		// Field Weights
	
    @Override
    public int compare(String a, String b) {
    	
    	long tfa=0,tfb=0;
    	
    	if(Indexer.outfinalword.title.containsKey(a))tfa+=Indexer.outfinalword.title.get(a) * tw;
    	if(Indexer.outfinalword.title.containsKey(b))tfb+=Indexer.outfinalword.title.get(b) * tw;
    	
    	if(Indexer.outfinalword.category.containsKey(a))tfa+=Indexer.outfinalword.category.get(a) * cw;
    	if(Indexer.outfinalword.category.containsKey(b))tfb+=Indexer.outfinalword.category.get(b) * cw;
    	
    	if(Indexer.outfinalword.infobox.containsKey(a))tfa+=Indexer.outfinalword.infobox.get(a) * iw;
    	if(Indexer.outfinalword.infobox.containsKey(b))tfb+=Indexer.outfinalword.infobox.get(b) * iw;
    	
    	if(Indexer.outfinalword.text.containsKey(a))tfa+=Indexer.outfinalword.text.get(a) * bw;
    	if(Indexer.outfinalword.text.containsKey(b))tfb+=Indexer.outfinalword.text.get(b) * bw;
    	
    	if(Indexer.outfinalword.links.containsKey(a))tfa+=Indexer.outfinalword.links.get(a) * lw;
    	if(Indexer.outfinalword.links.containsKey(b))tfb+=Indexer.outfinalword.links.get(b) * lw;
    		
        if(tfa<tfb)return 1;
        else if(tfa==tfb)return 0;
        
        return -1;
        
    }
}
