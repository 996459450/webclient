package com.fx.news;

import java.awt.List;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Robot {

	
	public static ArrayList<String> getpage(String urls) {
		ArrayList<String> page = new ArrayList<String>();
		URL url = null;
		URLConnection urlconn = null;
        BufferedReader br = null;
        BufferedWriter bw = null;
        PrintWriter pw = null;
        FileOutputStream of = null;
//        String regex = "http://[\\w+\\.?/?]+\\.[A-Za-z]+";
       
        try {
            url = new URL(urls);
            urlconn = url.openConnection();
//            pw = new PrintWriter(new FileWriter("D:/SiteURL.txt"), true);
            br = new BufferedReader(new InputStreamReader(
                    urlconn.getInputStream(),"GBK"));
            of = new FileOutputStream("out.dat");
            bw  = new BufferedWriter(new OutputStreamWriter(of, "utf-8"));
            String buf = null;
            while ((buf = br.readLine()) != null) {
            	bw.write(buf+"\n");
            	page.add(buf);
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                br.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return page;
	}
	
	
	public static void getPicture(ArrayList<String> page) {
		
		 String regex = "http://[\\w+\\.?/?]+\\.jpg+";//url匹配规则
	     Pattern p = Pattern.compile(regex);
	     System.out.println("照片：");
		for (int i = 0; i < page.size(); i++) {
			try {
				Matcher buf_m = p.matcher(page.get(i));
	             while (buf_m.find()) {
	             	
	             	if(buf_m.toString().contains(".jpg")) {
	             		System.out.println("\t"+buf_m.group());
	             		writeFile(buf_m,"image/10"+i+".jpg");
	             		
	             	}
	             }
			} catch (Exception e) {
				System.out.println("ERROR.....:抓取错误");
			}
			 
		}
	}
	
	public static byte[] readInputStream(InputStream inStream) throws Exception{  
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();  
        //创建一个Buffer字符串  
        byte[] buffer = new byte[1024];  
        //每次读取的字符串长度，如果为-1，代表全部读取完毕  
        int len = 0;  
        //使用一个输入流从buffer里把数据读取出来  
        while( (len=inStream.read(buffer)) != -1 ){  
            //用输出流往buffer里写入数据，中间参数代表从哪个位置开始读，len代表读取的长度  
            outStream.write(buffer, 0, len);  
        }  
        //关闭输入流  
        inStream.close();  
        //把outStream里的数据写入内存  
        return outStream.toByteArray();  
    }  
	
	public static void writeFile(Matcher buf_m,String paths) throws Exception {
		
		
		URL url = new URL(buf_m.group());  
        //打开链接  
        HttpURLConnection conn = (HttpURLConnection)url.openConnection();  
        //设置请求方式为"GET"  
        conn.setRequestMethod("GET");  
        //超时响应时间为5秒  
        conn.setConnectTimeout(5 * 1000);  
        //通过输入流获取图片数据  
        InputStream inStream = conn.getInputStream();  
        //得到图片的二进制数据，以二进制封装得到数据，具有通用性  
        byte[] data = readInputStream(inStream);  
        //new一个文件对象用来保存图片，默认保存当前工程根目录  
        File imageFile = new File(paths);  
        //创建输出流  
        FileOutputStream outStream = new FileOutputStream(imageFile);  
        //写入数据  
        outStream.write(data);  
        //关闭输出流  
        outStream.close();  
	}
	
	public static void getnews(ArrayList<String> page) {
//		new Map<String , String>();
		HashMap<String, String> map = new HashMap<String,String>();
//      String regex = "http://[\\w+\\.?/?]+\\.[A-Za-z]+";
		// <p>据悉，在社交媒体推特等网络上也相继出现有关投稿。有网民称，也曾看到该物体有几次爆发性发光。</p>
		String regex = "<p>[\\u4e00-\\u9fa5]+.*\\。";//url匹配规则
		//<h1>日本各地夜空目击光球：发出强烈光亮 形似流星_金羊网新闻</h1>
		String regex1 = "<h1>[\\u4e00-\\u9fa5]+.*</h1>";
	     Pattern p = Pattern.compile(regex);
	     Pattern p1 = Pattern.compile(regex1);
	     for (int i = 0; i < page.size(); i++) {
				try {
					Matcher buf_m = p.matcher(page.get(i));
					Matcher buf_m1 = p1.matcher(page.get(i));
		             while (buf_m.find()) {
		            	 String str = buf_m.group().substring(3, buf_m.group().length());
		             		System.out.println(str);
//		             		map.
		             }
		             while (buf_m1.find()) {
		            	 if (!map.containsKey(buf_m1.find())) {
							map.put(buf_m1.toString(), "");
						}
		            	 String str1 = buf_m1.group().substring(4, buf_m1.group().length());
		            	 String str2 = str1.substring(0, str1.length()-5);
		             		System.out.println("标题："+str2);
		             }
		        
				} catch (Exception e) {
					System.out.println("ERROR.....:抓取错误");
				}
				 
			}
	     System.out.println("successed");
	}
	
	public static void main(String[] args) {
		String url = "http://news.163.com/17/1122/15/D3RT43VC00014AEE.html";
		ArrayList<String> page = getpage(url);
//		getPicture(page);
		getnews(page);
		
	}
	
}
