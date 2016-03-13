package Main;


import java.util.List;

import amazonHtml.Amazon;
import amazonHtml.Product;
import objFile.ObjFileConverter;
public class Main {
	static Amazon amazon;
	static String amazonPath="Amazon/all.products";
	public static void loadAmazon()
	{
		ObjFileConverter<Amazon> converter=new ObjFileConverter<Amazon>();
		try {
			amazon=converter.loadObj(amazonPath);
			amazon.loadProducts();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
	public static void main(String[] args) {
		loadAmazon();
		//amazon.addProduct("http://www.amazon.com/gp/product/B008D6YGE4/");
		amazon.process();
		saveAmazon();
		
		
		
		
		return;
//	List<Product> ss=new ArrayList<Product>();
//	ss.add(new Product("p1"));
//	ss.add(new Product("p2"));
//	ss.add(new Product("p3"));
//	ss.add(new Product("p4"));
//	ObjFileConverter<Product> converter=new ObjFileConverter<Product>();
//	converter.saveObjList(ss, "xxx.xml");
//		try {
//			List<Product> sn=converter.loadObjList("xxx.xml");
//			for(Product s:sn)
//			{
//			//	System.out.println(s.ss.get(0));
//			}
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	
//	
		//File.Reader.readFileByLines("",url_list);
		
        //GET
        //String s=Http.Client.sendGet("http://www.amazon.com/Tissot-T0394171105702-Stainless-Steel-Watch/product-reviews/B00842V3QG/ref=cm_cr_dp_see_all_btm?ie=UTF8&showViewpoints=1&sortBy=recent", "");
        //System.out.println(s);
      
        //POST
       // String sr=Http.Client.sendPost("http://localhost:6144/Home/RequestPostString", "key=123&v=456");
       //System.out.println(sr);
    }
}
