package Main;


import java.util.ArrayList;
import java.util.List;

import FileIO.FileIO;
import amazonHtml.Amazon;
import amazonHtml.ProcessThread;
import amazonHtml.Product;
import amazonHtml.ReviewPage;
import objFile.ObjFileConverter;
public class Main {
	static Amazon amazon=new Amazon();
	static String amazonPath="Amazon/all.products";
	static String reviewPath="Amazon/reviews/";
	static String pidListPath="Amazon/list.pids";
	static String oldPidListPath="Amazon/old.pids";
	static int MULTITHREADING=5;
	static List<String> pidList=new ArrayList<String>();
	static List<String> oldPidList=new ArrayList<String>();
	public static void loadAmazon()
	{
		ObjFileConverter<Amazon> converter=new ObjFileConverter<Amazon>();
		try {
			amazon=converter.loadObj(amazonPath);
			if(amazon!=null)
				amazon.loadProducts();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		}
		if(amazon==null)
			amazon=new Amazon();
	}
	public static void saveAmazon()
	{
		if(amazon==null)
			return;
		ObjFileConverter<Amazon> converter=new ObjFileConverter<Amazon>();
		amazon.saveProducts();
		List<Product> pl=amazon.getProducts();
		amazon.setProducts(null);
		converter.saveObj(amazon, amazonPath);
		amazon.setProducts(pl);
		
	}
	private static void loadPidList()
	{
		pidList=FileIO.readFileToString(pidListPath);
		oldPidList=FileIO.readFileToString(oldPidListPath);
		
	}
	private static void savePidList()
	{
		FileIO plf=new FileIO(pidListPath);
		if(plf.isReady())
		{
			for(String s:oldPidList)
			{
				plf.println(s);
			}
			plf.close();
		}
		else
			System.out.println("Can't save old pid list to file!");
	}
	private static void processPidList()
	{
		//List<String> nPidList=new ArrayList<String>();
	
		for(String s:pidList)
		{
			if(s!=null&&!s.isEmpty()&&!oldPidList.contains(s))
			{
				amazon.addProductId(new String(s));
			}
				
		}
		List<ProcessThread> proThreads=new ArrayList<ProcessThread>();
		for(Product p:amazon.getProducts())
		{
			if(proThreads.size()<MULTITHREADING)
			{
				ProcessThread t=new ProcessThread(p,reviewPath);
				proThreads.add(t);
				t.start();
			}else
			{
				int i=-1;
				while(i<0)
				{
					for(int j=0;j<MULTITHREADING;j++)
					{
						if(!proThreads.get(j).running)
						{
							i=j;
							 proThreads.set(j,null);
							break;
						}
					}
				}
				ProcessThread t=new ProcessThread(p,reviewPath);
				proThreads.set(i, t);
				t.start();
			}
		}
	}
	public static void main(String[] args) {
		//loadAmazon();
		//amazon.addProductId("");
		//amazon.process(reviewPath);
		//saveAmazon();
		loadPidList();
		processPidList();
		savePidList();
		return;
    }
}
