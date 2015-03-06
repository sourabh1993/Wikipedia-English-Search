package engine;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.PrintWriter;

public class MultiIndex {

	
	
	String getWord(String line)				// Get the word corresponding to current line
	{
		StringBuilder stb=new StringBuilder();
		
		for(int i=0;line.charAt(i)!=':';i++)
		{
			stb.append(line.charAt(i));
		}
		
		return stb.toString();
	}
	
	void buildMultiIndex(){				// Build multilevel index
		
		long nob=0,nopb=0;  //nob=no of bytes traversed in main inverted index ; nopb=no of bytes traversed in primary index
		
		
		
		try{
			PrintWriter pws=new PrintWriter(new File("secindex1"));
			PrintWriter pwp=new PrintWriter(new File("priindex1"));
			BufferedReader br=new BufferedReader(new FileReader("index43gb.txt"));
			String line;
			String word;
			int nol=1;	// no of lines
			
			while((line=br.readLine())!=null)
			{
				word=getWord(line);
				
				pwp.write(word+":"+nob+"\n");
							
				if(nol%100==0){						
				pws.write(word+":"+nopb+"\n");
				nol=0;
				}
				
				nopb+=(word+":"+nob+"\n").length();
				nob+=line.length()+2;
				nol++;
			}
			
			br.close();
			pws.close();
			pwp.close();  
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
		
	}

	void buildDocIndex() throws Exception
	{
		PrintWriter pws=new PrintWriter(new File("docindex"));
		BufferedReader br=new BufferedReader(new FileReader("Documents.txt"));
		String line;
		int count=0;
		long bytes=0;
		
		while((line=br.readLine())!=null)
		{
			if(count%100==0)
			{
				String arr[]=line.split(":");
				pws.write(arr[0]+":"+bytes+"\n");
				count=0;
			}
			bytes+=line.length()+1;
			count++;
		}
		
		
		
		pws.close();
		br.close();
	}
}
