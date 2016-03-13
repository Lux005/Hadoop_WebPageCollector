package amazonHtml;

import java.util.ArrayList;
import java.util.List;

import objFile.ObjFileConverter;

public class Product implements java.io.Serializable  {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 
	 */
	private String productUrl,productHtml,productName,productId;
	private String reviewUrl,reviewHtml;
	private boolean isReady;
	private List<ReviewPage> reviewPages;
	private List<String> reviewPagesPath;
	private int reviewPageCount,savedSize;
	public List<ReviewPage> getReviewPages() {
		return reviewPages;
	}
	public void setReviewPages(List<ReviewPage> reviewPages) {
		this.reviewPages = reviewPages;
	}

	public Product(String url)
	{
		this.productUrl=url;
		System.out.println("Fetch HTML");
		productHtml=fetchHTML(productUrl);
		System.out.println("Fetch Product ID");
		fetchProductId();
	}
	public void process()
	{
		System.out.println("Fetch Review Pages");
		fetchReviewPages();
		System.out.println("Fetch Review End");
		if(reviewPages!=null&&reviewPages.size()>0)
		{
			for(ReviewPage rp:reviewPages)
			{
				rp.process();
			}
			setReady(true);
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
		String slink=SearchHTML.Search(productHtml,"<link rel=\"canonical\"(.*)/>");
		String surl=SearchHTML.Search(slink, "(http:\\/\\/)[^\\s|\"|?]+");
		if(!surl.isEmpty())
			productUrl=surl;
		String sdp=SearchHTML.Search(surl, "dp/\\w+");
		String sdp2=SearchHTML.Search(sdp, "/\\w+");
		String sid=SearchHTML.Search(sdp2, "\\w+");
		if(!sid.isEmpty())
		{
			productId=sid;
			System.out.println("Product id="+sid);
		}
	}
	private void fetchReviewPages()
	{
		if(reviewPages==null)
			reviewPages=new ArrayList<ReviewPage>();
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
		String snum=SearchHTML.Search(reviewHtml, ">-?[1-9]\\d*</a></li><li class=\"a-last\">");
		if(snum.isEmpty())
		{
			System.out.println("Cannot find reviews!");
			reviewPageCount=0;
			return;
		}
		String snum2=SearchHTML.Search(snum, "-?[1-9]\\d*");
		if(snum2.isEmpty())
		{
			System.out.println("page number is empty!");
			return;
		}
		int r=Integer.parseInt(snum2);
		if(r>0)
			reviewPageCount=r;
		reviewPages.clear();
		for(int i=1;i<=r;i++)
		{
			String rpurl=reviewUrl+
					"/ref=cm_cr_arp_d_paging_btm_"+
					i+"?ie=UTF8&showViewpoints=1&sortBy=recent&"+
					"pageNumber="+i;
			ReviewPage rp= new ReviewPage(rpurl,i,productId);
			reviewPages.add(rp);
		}
		if(reviewPages.size()==r)
		{
			System.out.println(r+" Review Pages Updated.");
		}else
		{
			System.out.println("Review Pages Unable to Update.");
		}
			
		
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
			String file=basePath+"/"+productId.toString()+"/"+(i+1)+
					".reviewpages";
			converter.saveObj(rp, file);
			reviewPagesPath.add(file);
		}
		System.out.println("Review Pages saved.."+reviewPagesPath.size());
	}
	public void loadReviewPages(String filepath)
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
