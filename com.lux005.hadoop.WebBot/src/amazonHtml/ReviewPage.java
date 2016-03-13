package amazonHtml;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import objFile.ObjFileConverter;
public class ReviewPage implements java.io.Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String url,html,productId;
	private int pageNum;
	private boolean isReady=false;
	private List<Review> reviews;
	public ReviewPage(String url,int pageNum, String productId)
	{
		this.setPageNum(pageNum);
		this.url=url;
		this.productId=productId;
		
	}
	private String fetchHTML(String xurl)
	{
		return http.Client.sendGet(xurl, "");
	}

	public void process()
	{
		html=fetchHTML(url);
		if(reviews==null)
			reviews = new ArrayList<Review>();
		if(html.isEmpty())
		{
			System.out.println("Unable to Downlad Review Page!");
			return;
		}
		
		reviews.add(new Review("review_content1",new Date(),new Customer("customer1")));
		reviews.add(new Review("review_content2",new Date(),new Customer("customer2")));
		reviews.add(new Review("review_content3",new Date(),new Customer("customer3")));
		
	}

	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getHtml() {
		return html;
	}
	public void setHtml(String html) {
		this.html = html;
	}
	
	public List<Review> getReviews() {
		return reviews;
	}
	public void setReviews(List<Review> reviews) {
		this.reviews = reviews;
	}
	public boolean isReady() {
		return isReady;
	}
	public void setReady(boolean isReady) {
		this.isReady = isReady;
	}
	public int getPageNum() {
		return pageNum;
	}
	public void setPageNum(int pageNum) {
		this.pageNum = pageNum;
	}


	    
}
