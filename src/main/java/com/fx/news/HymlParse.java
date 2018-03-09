package com.fx.news;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HymlParse {

	public static String getHtmlContent(URL url,String encode) {
		StringBuffer contentbuffer = new StringBuffer();
		int responesecode =-1;
		HttpURLConnection con = null;
		try {
			con = (HttpURLConnection) url.openConnection();
			con.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
			con.setConnectTimeout(60000);
			con.setReadTimeout(60000);
			responesecode = con.getResponseCode();
			if(responesecode ==-1) {
				System.out.println(url.toString()+"connection is failed");
				con.disconnect();
				return null;
			}
			if(responesecode >= 400) {
				System.out.println("请求失败：get response code"+ responesecode);
				con.disconnect();
				return null;
			}
			
			InputStream instr = con.getInputStream();
			InputStreamReader reader = new InputStreamReader(instr, encode);
			BufferedReader br = new BufferedReader(reader);
			String string = null;
			while((string = br.readLine())!=null) {
				contentbuffer.append(string);
				instr.close();
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			contentbuffer = null;
			System.out.println("error:"+url.toString());
		}finally {
			con.disconnect();
		}
		return contentbuffer.toString();
	}
	
	public static String getHtmlContent(String url,String encode) {
		if(!url.toLowerCase().startsWith("http://")) {
			url = "http://"+url;
		}
		try {
			URL rUrl = new URL(url);
			return getHtmlContent(rUrl, encode);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static void getPacture() {
		String picStr = "src=http://img1.cache.netease.com/f2e/include/common_nav/images/topapp.jpg";
		String regex = "https://[\\\\w+\\\\.?/?]+\\\\.[A-Za-z]+";
		Pattern p = Pattern.compile(regex);
		 Matcher buf_m = p.matcher(picStr);
		 System.out.println(buf_m.group());
	}
	
	public static void main(String[] args) {
		System.out.println(getHtmlContent("www.baidu.com", "GBK"));
//		getPacture();
	}
}
