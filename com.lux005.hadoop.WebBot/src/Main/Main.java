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
	static String amazonPath="Amazon/all.products";//amazon object file
	static String reviewPath="Amazon/reviews/";//reviews for products
	static String pidListPath="Amazon/list.pids";//product id list to be processed
	static String historyPidListPath="Amazon/history.pids";//processed product id list
	static int MULTITHREADING=5;//number of process threads
	static List<String> pidList=new ArrayList<String>();
	static List<String> historyPidList=new ArrayList<String>();
	//load saved amazon class object
	//tested but not used
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
	//save amazon class object
	//tested but not used
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
	//load all the product id from the list file
	//newList: items to be processed.
	//oldList: items have been processed.
	private static void loadPidList()
	{
		pidList=FileIO.readFileToString(pidListPath);
		historyPidList=FileIO.readFileToString(historyPidListPath);
		
	}
	
	//process each product id using multithreads
	private static void processPidList()
	{
		//List<String> nPidList=new ArrayList<String>();
	
		for(String s:pidList)
		{
			if(s!=null&&!s.isEmpty()&&!historyPidList.contains(s))
			{
				amazon.addProductId(new String(s));
			}
				
		}
		FileIO plf=new FileIO(historyPidListPath);
		if(!plf.isReady())
		{
			System.out.println("Can't open file to save old pid list!");
			return;
		}
		for(String s:historyPidList)
		{
			plf.println(s);
		}
		
		
		List<ProcessThread> proThreads=new ArrayList<ProcessThread>();
		List<String> threadPids=new ArrayList<String>();
		for(Product p:amazon.getProducts())
		{
			if(proThreads.size()<MULTITHREADING)
			{
				if(p.getProductId()!=null && p.getProductId().length()>5)
				{
					ProcessThread t=new ProcessThread(p,reviewPath);
					proThreads.add(t);
					threadPids.add(p.getProductId());
					t.start();
				}
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
							historyPidList.add(threadPids.get(j));
							if(proThreads.get(j).success)
								plf.println(threadPids.get(j));
							proThreads.set(j,null);
							break;
						}
					}
				}
				ProcessThread t=new ProcessThread(p,reviewPath);
				threadPids.set(i,p.getProductId());
				proThreads.set(i, t);
				t.start();
			}
		}
		while(proThreads.size()>0)
		{
			for(int j=0;j<proThreads.size();j++)
			{
				if(proThreads.get(j)!=null)
				{
					if(!proThreads.get(j).running)
					{
						historyPidList.add(threadPids.get(j));
						if(proThreads.get(j).success)
							plf.println(threadPids.get(j));
						proThreads.remove(j);
						break;
					}
				}else
				{
					proThreads.remove(j);
				}
			}
		}
		if(plf!=null)
			plf.close();
	}
	public static void main(String[] args) {
		//loadAmazon();
		//amazon.addProductId("");
		//amazon.process(reviewPath);
		//saveAmazon();
		loadPidList();
		processPidList();
		return;
    }
}
