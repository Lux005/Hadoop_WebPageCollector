package amazonHtml;

import java.util.Date;

public class Review implements java.io.Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String content="";
	private Date date;
	private Customer customer;
	public Review(String content, Date date, Customer customer)
	{
		this.content=content;
		this.date=date;
		this.customer=customer;
	}
	public Customer getCustomername() {
		return customer;
	}
	public void setCustomername(Customer customer) {
		this.customer = customer;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	
		
}
