package FileIO;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class FileIO {
	PrintWriter out;
	private boolean isReady=false;
	public FileIO(String filename)
	{
		checkDir(filename);
		try {
			out = new PrintWriter( filename);
			setReady(true);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			setReady(false);
		}
	}
	public void close()
	{
		if(out!=null)
			out.close();
	}
	public void println(String c)
	{
		if(c!=null&&out!=null)
		    out.println( c );	
		out.flush();
	}
	static private void checkDir(String filepath)
	{
		File theFile=new File(filepath);
		String parentDir=theFile.getParent();
		if(parentDir==null||parentDir.length()<1)
			return;
		//System.out.println("file:"+theFile+"    path:"+parentDir);
		File theDir = new File(parentDir);
		checkDir(parentDir);
		  // if the directory does not exist, create it
		  if (!theDir.exists())
		  {
		    theDir.mkdir();
		  }
		
	}
	static public List<String> readFileToString(String filename)
	{
		BufferedReader br = null;
		List<String> ss=new ArrayList<String>();
		try {
			br = new BufferedReader(new FileReader(filename));
		    String line = br.readLine();

		    while (line != null) {
		    	ss.add(line);
		        line = br.readLine();
		    }
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		} finally {
				try {
					if(br!=null)
						br.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					//e.printStackTrace();
				}
		}
		return ss;
	}
	public boolean isReady() {
		return isReady;
	}
	public void setReady(boolean isReady) {
		this.isReady = isReady;
	}
	
}
