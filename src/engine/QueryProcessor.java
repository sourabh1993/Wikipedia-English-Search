package engine;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class LexicoComaparator implements Comparator<String>{
	 public int compare(String obj1, String obj2) {
		 
		 return obj1.compareToIgnoreCase(obj2);
	 }
}


class LexicoNumComaparator implements Comparator<String>{
	 public int compare(String obj1, String obj2) {
		 
		 int a,b;
		 a=Integer.parseInt(obj1);
		 b=Integer.parseInt(obj2);
		 
		 return a-b;
	 }
}


class WeightComaparator implements Comparator<String>{
	
	public Map<String,Long> docmap;
	
	public WeightComaparator(Map<String,Long> mmap) {
		// TODO Auto-generated constructor stub
		
		docmap=mmap;
	}
	
	@Override
	 public int compare(String obj1, String obj2) {
		 
		 long val1,val2;
		 
		 val1=docmap.get(obj1);
		 val2=docmap.get(obj2);
		 
		 if(val1>val2)return -1;
		 else if(val1==val2)return 0;		 
		 return 1;
		 
	 }
}



/********************************** Processing class Begins here ******************************************************************/

public class QueryProcessor {
	
	public List <String> iwords ;				// iwords=secondary index on index  
	public List<String> pageids; 			//  pageids=sparse index on doc vs title mapping
	public Map <String,Long> pagemap;
	public Map<String,Long> wordoffsets; 
	int tw=100,iw=50,cw=50,bw=5,lw=3;	 // Field weights
	public int nobytes=16000;
	public static Stemmer mystemmer;
	public long totaldocs=14000000;
	
	public  Map<String,Long> doclist;			// Map storing the docid vs weight 
	
	
	public Map<String,Long> sorted_map ;
		
	public QueryProcessor()
	{
		iwords=new ArrayList<String>();
		wordoffsets=new HashMap<String,Long>();
		doclist=new HashMap<String,Long>();
		pageids=new ArrayList<String>();
		pagemap=new HashMap<String,Long>();
		mystemmer=new Stemmer();
		
		try{
			BufferedReader br=new BufferedReader(new FileReader(Main.path+"\\"+"secindex1"));
			String line,arr[];
			//arr=new String[2];
			
			while((line=br.readLine())!=null)
			{
				arr=line.split(":");
				iwords.add(arr[0]);
				wordoffsets.put(arr[0], Long.parseLong(arr[1]));
			}
			
			br.close();
			
			br=new BufferedReader(new FileReader(Main.path+"\\"+"docindex"));
			
			while((line=br.readLine())!=null)
			{
				arr=line.split(":");
				pageids.add(arr[0]);
				pagemap.put(arr[0], Long.parseLong(arr[1]));				
			}
			
			br.close();
			
			
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
	}
	
	void processQuery(String queries) throws Exception
	{
		
		queries=queries.toLowerCase();
		
		String mypattern="[^a-zA-Z0-9:]+";
		Pattern p=Pattern.compile(mypattern);
    	Matcher m=p.matcher(queries);
    	queries=m.replaceAll(" ");
    	
    	
    	
		
		
		File indexfile=new File(Main.path+"\\"+"index43gb.txt");
		RandomAccessFile raf43=new RandomAccessFile(indexfile,"r");
		RandomAccessFile rafp=new RandomAccessFile(new File(Main.path+"\\"+"priindex1"),"r");
		Long ifilesize=indexfile.length();
		
		doclist=new HashMap<String,Long>();
	
		String qarr[]=queries.split("\\s+");
		
		for(String query : qarr)
		{
			
			mystemmer.add(query.toCharArray(), query.length());
			mystemmer.stem();
			query=mystemmer.toString();
			
			tw=100;iw=20;cw=20;bw=1;lw=1;
			
			if(query.contains(":"))
			{
				char place;
				place=query.charAt(0);
				query=query.substring(query.indexOf(':')+1);
				
				
				switch(place)
				{
				case 't':	tw=1000;iw=0;cw=0;bw=0;lw=0; break;
				case 'c':	tw=0;iw=0;cw=500;bw=0;lw=0; break;
				case 'i':   tw=0;iw=500;cw=0;bw=0;lw=0; break;
				case 'r':   tw=0;iw=0;cw=0;bw=50;lw=0; break;
				case 'b':   tw=0;iw=0;cw=0;bw=50;lw=0; break;
				case 'l':   tw=0;iw=0;cw=0;bw=0;lw=30; break;
				}
				
				nobytes=50000;
								
			}
			else nobytes=38000;
			
			
			
		int sindex=Collections.binarySearch(iwords, query, new LexicoComaparator());
		
		
		//System.out.println("sindex:"+sindex);
		if(sindex<0)sindex=sindex*(-1) - 2;
		
		if(sindex<0)sindex=0;
		//System.out.println(iwords.get(sindex));
		String line,arr[];
		Long offset=wordoffsets.get(iwords.get(sindex));
		
		if(sindex>0)rafp.seek(offset);
		else rafp.seek(0);
		
		while((line=rafp.readLine())!=null)
		{
			arr=line.split(":");
			
			if(arr[0].equalsIgnoreCase(query)){
				offset=Long.parseLong(arr[1]);
				break;
			}
			else if(arr[0].compareToIgnoreCase(query)>0)break;
		}
		
		raf43.seek(offset);
		
		byte barr[]=new byte[50005];//=raf43.read(10000);
		
		
		
		raf43.read(barr, 0, nobytes);
		String iline=(new String(barr)).split("\n")[0];
		String details[]=iline.split(":");
		
	//	System.out.println(iline);
		
	//	System.out.println(details[0]+"\n"+details[1]+"\n"+details[2]);
		
	//	System.out.println();
		
//		System.out.println(raf1.readLine());
		
		double dft=(double)Long.parseLong(details[1]);
		double idf=Math.log(totaldocs/dft);
		
		String poslist=details[2];
		String docs[]=poslist.split("\\|");
		
		
		
		
		StringBuilder docid=new StringBuilder();
		long weight=0;
		
	//	System.out.println("Doclength:"+docs.length);
		
		for(int i=0;i<docs.length - 1 ; i++)
		{
			weight=evalDoc(docid,docs[i]);
			weight*=idf;
			
	//		System.out.println(":"+docid);
			if(doclist.containsKey(docid.toString()))
			{
				Long val=doclist.get(docid.toString());
				val+=weight;
				doclist.put(docid.toString(),(25)*val);
			}
			else doclist.put(docid.toString(), weight);
			
			docid.setLength(0);
		}
		
	//	System.out.println("SIZE:"+doclist.size());
		
		
	}
		
		
		
		WeightComaparator wcm=new WeightComaparator(doclist);
		sorted_map = new TreeMap<String,Long>(wcm);	
		
		
		for(String key: doclist.keySet())
			sorted_map.put(key,doclist.get(key));
	
			
/*	System.out.println("SIZE:"+sorted_map.size());
	System.out.println(doclist);
	System.out.println(sorted_map);
	System.out.println("Results====================================");*/
	int i=0;	
	for(String docid : sorted_map.keySet())
	{
	//	System.out.print(docid+"  : ");
		System.out.println(getTitle(docid));
		i++;
		if(i==10)break;
	}
	}
	
	
	/************************* Function to retrieve title through docid ******************************/
	
	String getTitle(String docid) throws Exception
	{
		
    	RandomAccessFile pri=new RandomAccessFile(new File(Main.path+"\\"+"Documents.txt"),"r");
    	
    	int dindex=Collections.binarySearch(pageids,docid,new LexicoNumComaparator());
    	
    	if(dindex<0)dindex=dindex*(-1) - 2;
		
		if(dindex<0)dindex=0;
    	
    	Long offset=pagemap.get(pageids.get(dindex));
    	//System.out.println(pageids.get(dindex));
    	pri.seek(offset);
    	 	
		String line,arr[];
		
		
		while((line=pri.readLine())!=null)
		{
			arr=line.split(":");
			//if(count==0)System.out.println(line);
			
			if(arr[0].equalsIgnoreCase(docid)){pri.close();return arr[1];}
						
			if(docid.compareToIgnoreCase(arr[0])<0)break;
			
		}
		pri.close();
		return null;
	}
	
	Long evalDoc(StringBuilder docid,String posting) //throws Exception
	{
		int len=posting.length();
		long weight=0;
		int i,count=0;
		char ch=0,position=0;
		
		StringBuilder num=new StringBuilder();
		//System.out.println("Pos:"+posting);
		StringBuilder stb=new StringBuilder();
		
		for(i=0;i<len;i++)
		{
			if(Character.isDigit(posting.charAt(i))){docid.append(posting.charAt(i));}
			else break;
		}
		
	//	System.out.println("docid:"+docid+"::");
		
		for(;i<len;i++)
		{
			ch=posting.charAt(i);
			if(!Character.isDigit(ch)){ position=ch; num.setLength(0);}
			
			while(i<len && Character.isDigit(ch))
			{
				num.append(ch);
				ch=posting.charAt(i);				
				i++;
			}
			
			
			//System.out.println("num:"+num);
			
			if(num.length()>0){
				count=Integer.parseInt(num.toString());
				num.setLength(0);
				switch(position)
				{
					case 't': weight+=count*tw;break;
					case 'c': weight+=count*cw;break;
					case 'i': weight+=count*iw;break;
					case 'b': weight+=count*bw;break;
					case 'l': weight+=count*lw;break;								
				}
			}
				
				
			}
		return weight;
		
	}

}
