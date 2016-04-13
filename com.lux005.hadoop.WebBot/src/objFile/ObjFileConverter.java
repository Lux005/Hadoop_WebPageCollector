package objFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

public class ObjFileConverter<T> {
	public boolean saveObjList(List<T> Ts,String filename) 
	{
		try
	      {
			checkDir(filename);
	         FileOutputStream fileOut =
	         new FileOutputStream(filename);
	         ObjectOutputStream out = new ObjectOutputStream(fileOut);
	         for(T t:Ts)
	         out.writeObject(t);
	         out.writeObject(null);
	         out.close();
	         fileOut.close();
	       //System.out.printf("Serialized data is saved");
	         List<T> objList =  loadObjList(filename);  
	         if(objList.size()==Ts.size())
	        	 return true;
	        	//System.out.printf("Serialized data is saved");
	      }catch(IOException i)
	      {
	    	  System.out.println("failed to save list");
	         // i.printStackTrace();
	      }
		return false;
	}
	@SuppressWarnings("unchecked")
	public  List<T>  loadObjList(String filename)
	{
		 List<T> objList = new ArrayList<T>(); 
		  try
	      {
	         FileInputStream fileIn = new FileInputStream(filename);
	         ObjectInputStream in = new ObjectInputStream(fileIn);
	         T obj;
	         while( (obj = (T)in.readObject()) != null)   
	         {   
	          objList.add(obj);   
	         }   
	         in.close();
		     fileIn.close();
	      }catch(IOException i)
	      {
	    	  System.out.println("io: failed to load list");
	      //   i.printStackTrace();
	      }catch(ClassNotFoundException c)
	      {
	    	  System.out.println(" class not found");
	        // c.printStackTrace();
	      }
		 
		  return objList;
	}
	public boolean saveObj(T t,String filename) 
	{
		try
	      {
			checkDir(filename);
	         FileOutputStream fileOut =
	         new FileOutputStream(filename);
	         ObjectOutputStream out = new ObjectOutputStream(fileOut);
	         out.writeObject(t);
	         out.writeObject(null);
	         out.close();
	         fileOut.close();
	       //System.out.printf("Serialized data is saved");
	       T obj =  loadObj(filename);  
	         if(obj.equals(t))
	        	 return true;
	        	//System.out.printf("Serialized data is saved");
	      }catch(IOException i)
	      {
	    	  System.out.println("io: failed to save obj");
	         // i.printStackTrace();
	      }
		return false;
	}
	@SuppressWarnings("unchecked")
	public  T loadObj(String filename)
	{
		 T obj=null; 
		  try
	      {
	         FileInputStream fileIn = new FileInputStream(filename);
	         ObjectInputStream in = new ObjectInputStream(fileIn);
	         obj=(T)in.readObject();
	         in.close();
		     fileIn.close();
	      }catch(IOException i)
	      {
	    	  System.out.println("io: failed to load obj");
	         //i.printStackTrace();
	      }catch(ClassNotFoundException c)
	      {
	         System.out.println(" class not found");
	        // c.printStackTrace();
	      }
		  return obj;
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
}
