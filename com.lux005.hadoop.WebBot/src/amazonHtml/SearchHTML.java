package amazonHtml;


import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SearchHTML {
	public static String Search(String content, String pattern)
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
}
