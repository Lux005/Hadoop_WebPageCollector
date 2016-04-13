package amazonHtml;

import java.util.ArrayList;
import java.util.List;

import FileIO.FileIO;
import objFile.ObjFileConverter;

public class Product implements java.io.Serializable  {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 
	 */
	private String productUrl,productHtml,productId;
	public String getProductUrl() {
		return productUrl;
	}
	public void setProductUrl(String productUrl) {
		this.productUrl = productUrl;
	}
	public String getProductHtml() {
		return productHtml;
	}
	public void setProductHtml(String productHtml) {
		this.productHtml = productHtml;
	}
	private String reviewUrl,reviewHtml;
	private boolean isReady;
	private List<ReviewPage> reviewPages;
	private List<String> reviewPagesPath;
	public int savedReviews=0;
	public List<ReviewPage> getReviewPages() {
		return reviewPages;
	}
	public void setReviewPages(List<ReviewPage> reviewPages) {
		this.reviewPages = reviewPages;
	}

	public Product(String url)
	{
		this.productUrl=url;

	}
	public void update()
	{
		System.out.println("Fetch HTML");
		productHtml=fetchHTML(productUrl);
		System.out.println("Fetch Product ID");
		fetchProductId();
		System.out.println("Fetch Review Pages");
		fetchReviewPages();
		System.out.println("Fetch Review End");
		if(reviewPages!=null&&reviewPages.size()>0)
		{
			for(ReviewPage rp:reviewPages)
			{
				rp.update();
			}
			System.out.println("Review Pages downloaded!");
			setReady(true);
		}
	}
	public void process(String reviewPath)
	{
		
		if(productId==null||productId.isEmpty()||reviewPages==null||reviewPages.size()==0)
		{
			update();
		}
		else{
			if(reviewPages!=null&&reviewPages.size()>0)
			{
				for(ReviewPage rp:reviewPages)
				{
					rp.process();
				}
				System.out.println("Review Pages processeded!");
				setReady(true);
			}
		}
		 saveReviews(reviewPath);
	}
	private void saveReviews(String reviewPath)
	{
		if(productId!=null&&!productId.isEmpty())
		{
			FileIO file=new FileIO(reviewPath+productId+".reviews");
			if(file.isReady())
			{
				for(ReviewPage rp:reviewPages)
				{
					//System.out.println(rp.getReviews().size());
					for(String r:rp.getReviews())
					{
						savedReviews++;
						//System.out.println(pid+" "+r);
						file.println(productId+" "+r);
					}
				}
			}
			file.close();
			
		}
	}
	private String fetchHTML(String xurl)
	{
		return http.Client.sendGet(xurl, "");
	}
	private void fetchProductId()
	{
		if(productHtml.isEmpty())
		{
			System.out.println("Unable to Downlad Product Page!");
			return;
		}
		productUrl=SearchHTML.SearchProductUrl(productHtml);
		productId=SearchHTML.SearchProductId(productHtml);
		if(!productId.isEmpty())
		{
			System.out.println("Product id="+productId);
		}
		else
		{
			System.out.println("Unable to Fetch Product ID!");
		}
	}
	private void fetchReviewPages()
	{
		if(reviewPages==null)
			reviewPages=new ArrayList<ReviewPage>();
		else
			reviewPages.clear();
		if(productUrl.isEmpty())
			return;
		reviewUrl=productUrl.replace("/dp/", "/product-reviews/");
		reviewHtml=fetchHTML(reviewUrl);
		if(reviewHtml.isEmpty())
		{
			System.out.println("Unable to Downlad Review Page List!");
			return;
		}
		//System.out.println("reviewHtml!"+reviewHtml);
		int snum=SearchHTML.SearchPageNum(reviewHtml);
		if(snum<1)
		{
			System.out.println("page number is empty!");
			return;
		}
		reviewPages.clear();
		if(snum>0)
		{
			for(int i=1;i<=snum;i++)
			{
				String rpurl=reviewUrl+
						"/ref=cm_cr_arp_d_paging_btm_"+
						i+"?ie=UTF8&showViewpoints=1&sortBy=recent&"+
						"pageNumber="+i;
				ReviewPage rp= new ReviewPage(rpurl,i,productId);
				reviewPages.add(rp);
			}
		}
		if(reviewPages.size()==snum)
		{
			System.out.println(snum+ " Review Pages Updated.");
		}else
		{
			System.out.println("Review Pages Unable to Update.");
		}
		reviewHtml=null;
	}
	public void saveReviewPages(String basePath)
	{
		if(reviewPages==null)
			return;
		if(reviewPagesPath==null)
			reviewPagesPath=new ArrayList<String>();
		reviewPagesPath.clear();
		ObjFileConverter<ReviewPage> converter=new ObjFileConverter<ReviewPage>();
		for(int i=0;i<reviewPages.size();i++)
		{
			ReviewPage rp=reviewPages.get(i);
			rp.setHtml("");
			String file=basePath+"/"+productId.toString()+"/"+(i+1)+
					".reviewpages";
			converter.saveObj(rp, file);
			reviewPagesPath.add(file);
		}
		System.out.println("Review Pages saved.."+reviewPagesPath.size());
	}
	public void loadReviewPages()
	{
		if(reviewPagesPath==null)
			return;
		if(reviewPages==null)
			reviewPages=new ArrayList<ReviewPage>();
		reviewPages.clear();
		ObjFileConverter<ReviewPage> converter=new ObjFileConverter<ReviewPage>();
		for(String file:reviewPagesPath)
		{
			ReviewPage rp=converter.loadObj(file);
			reviewPages.add(rp);
		}
		System.out.println("Review Pages loaded.."+reviewPages.size());
	}
	public String getProductId() {
		return productId;
	}
	public void setProductId(String productId) {
		this.productId = productId;
	}
	public boolean isReady() {
		return isReady;
	}
	public void setReady(boolean isReady) {
		this.isReady = isReady;
	}
}
