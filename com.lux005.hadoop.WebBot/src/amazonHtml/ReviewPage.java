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
	private String url,html;
	private int pageNum;
	private boolean isReady=false;
	private List<String> reviews;
	public ReviewPage(String url,int pageNum, String productId)
	{
		this.setPageNum(pageNum);
		this.url=url;
		
	}
	private String fetchHTML(String xurl)
	{
		return http.Client.sendGet(xurl, "");
	}
	public void update()
	{
		html=fetchHTML(url);
		reviews= SearchHTML.SearchReviewId(html);
		html=null;
		//System.out.print(".");
	}
	public void process()
	{
		if(reviews==null||reviews.size()==0)
		{
			update();
				
		}
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
	
	public List<String> getReviews() {
		return reviews;
	}
	public void setReviews(List<String> reviews) {
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
