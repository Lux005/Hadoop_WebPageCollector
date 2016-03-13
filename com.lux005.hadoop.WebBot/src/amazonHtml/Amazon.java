package amazonHtml;

import java.util.ArrayList;
import java.util.List;

import objFile.ObjFileConverter;

public class Amazon implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2559556124735231051L;
	private List<Product> products;
	private List<String> productsPath;
	private String basePath="Amazon";
	public List<Product> getProducts() {
		return products;
	}
	public void setProducts(List<Product> products) {
		this.products = products;
	}
	public Amazon()
	{
		products=new ArrayList<Product>();
	}
	public void addProduct(String producturl)
	{
		if(products==null)
			products=new ArrayList<Product>();
		Product p=new Product(producturl);
		products.add(p);
	}
	public void process()
	{
		if(products==null)
			products=new ArrayList<Product>();
		System.out.println("process Products..");
		for(Product p: products)
		{
			p.process();
		}
	}
	public void saveProducts()
	{
		if(products==null)
			return;
		if(productsPath==null)
			productsPath=new ArrayList<String>();
		productsPath.clear();
		ObjFileConverter<Product> converter=new ObjFileConverter<Product>();
		for(Product p:products)
		{
			if(p.getProductId()==null)
				continue;
			p.saveReviewPages(basePath);
			List<ReviewPage>rpl=p.getReviewPages();
			p.setReviewPages(null);
			String file=basePath+"/"+p.getProductId()+"/0.product";
			converter.saveObj(p, file);
			p.setReviewPages(rpl);
			productsPath.add(file);
		}
		System.out.println("Products saved.."+productsPath.size());
	}
	public void loadProducts()
	{
		if(productsPath==null)
			return;
		if(products==null)
			products=new ArrayList<Product>();
		products.clear();
		ObjFileConverter<Product> converter=new ObjFileConverter<Product>();
		for(String file:productsPath)
		{
			Product rp=converter.loadObj(file);
			products.add(rp);
		}
		System.out.println("Products loaded.."+products.size());
	}
}
