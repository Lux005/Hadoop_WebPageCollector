package http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.Map;

public class Client {
    /**
     * Send GET request
     * 
     * @param url
     *            request url
     * @param param
     *          name1=value1&name2=value2 
     * @return URL Result
     */
    public static String sendGet(String url, String param) {
        String result = "";
        BufferedReader in = null;
        try {
            String urlNameString = url + "?" + param;
            URL realUrl = new URL(urlNameString);
            // establish connection
            URLConnection connection = realUrl.openConnection();
            // setting connection properties
            connection.setRequestProperty("accept", "*/*");
            connection.setRequestProperty("connection", "Keep-Alive");
            connection.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            // connect
            connection.connect();
            // get header fields
            Map<String, List<String>> map = connection.getHeaderFields();
            // traversal all fields
            for (String key : map.keySet()) {
               // System.out.println(key + "--->" + map.get(key));
            }
            // use bufferedreader to get the web content
            in = new BufferedReader(new InputStreamReader(
                    connection.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            System.out.println("GET request error!" + e);
            e.printStackTrace();
        }
        // finally close all the io
        finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        return result;
    }

    /**
     * POST request
     * 
     * @param url
     *            request url
     * @param param
     *            POST param name1=value1&name2=value2
     * @return request result
     */
    public static String sendPost(String url, String param) {
        PrintWriter out = null;
        BufferedReader in = null;
        String result = "";
        try {
            URL realUrl = new URL(url);
            // establish connection
            URLConnection conn = realUrl.openConnection();
            // set connection properties
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            // Open io for POST
            conn.setDoOutput(true);
            conn.setDoInput(true);
            // get output stream
            out = new PrintWriter(conn.getOutputStream());
            // send param to output stream
            out.print(param);
            // flush output stream
            out.flush();
            // define bufferedreader to read input stream
            in = new BufferedReader(
                    new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            System.out.println("POST request error! "+e);
            e.printStackTrace();
        }
        //finally close all the io
        finally{
            try{
                if(out!=null){
                    out.close();
                }
                if(in!=null){
                    in.close();
                }
            }
            catch(IOException ex){
                ex.printStackTrace();
            }
        }
        return result;
    }    
}