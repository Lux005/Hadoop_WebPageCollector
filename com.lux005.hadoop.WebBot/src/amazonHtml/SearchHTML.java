package amazonHtml;


import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SearchHTML {
	private static String Search(String content, String pattern)
	{
		String rs="";
		Pattern p = Pattern.compile(pattern);
		Matcher matcher = p.matcher(content);
		if (matcher.find()) {
		   rs=matcher.group(0); //prints /{item}/
		} else {
		 //   System.out.println("Match not found");
		}
		return rs;
	}
	private static List<String> ListSearch(String content, String pattern)
	{
		List<String> rs=new ArrayList<String>();
		Pattern p = Pattern.compile(pattern);
		Matcher matcher = p.matcher(content);
		while (matcher.find()) {
		   rs.add(matcher.group(0)); //prints /{item}/
		}
		return rs;
	}
	public static String SearchProductUrl(String  html)
	{
		String slink=SearchHTML.Search(html,"<link rel=\"canonical\"(.*)/>");
		String surl=SearchHTML.Search(slink, "(http:\\/\\/)[^\\s|\"|?]+");
		return surl;
	}
	public static String SearchProductId(String html)
	{
		String surl=SearchProductUrl(html);
		String sdp=SearchHTML.Search(surl, "dp/\\w+");
		sdp=sdp.replace("dp/", "");
		return sdp;
	}
	public static int SearchPageNum(String html)
	{
		int snum=0;
		List<String> ls=null;
		ls=ListSearch(html, "pageNumber=\\d*");
		if(ls!=null && ls.size()>0 )
		{
			for(String s:ls)
			{
				String ss=s.replace("pageNumber=", "");
				int sn=Integer.parseInt(ss);
				if(sn>snum)
					snum=sn;
			}
		}
		if(snum>100)
			snum=100;
		return snum;
	}
	public static List<String> SearchReviewId(String html)
	{
		List<String> ret=new ArrayList<String>();
		String rrs=" ";
		List<String> reviews=ListSearch(html,"/gp/customer-reviews/\\w+");
		for(String r:reviews)
		{
			if(r!=null&&r.length()>25)
			{
				String s=r.replace("/gp/customer-reviews/", "");
				if(s!=null&&s.length()>10&&!rrs.contains(s))
				{
					ret.add(s);
					rrs+=s+" ";
					//System.out.println(s);
				}
				
			}
		}
		return ret;
	}
	
}
