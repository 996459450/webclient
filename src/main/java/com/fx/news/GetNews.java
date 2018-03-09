package com.fx.news;

import java.io.IOException;
import java.net.MalformedURLException;

import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.SgmlPage;
import com.gargoylesoftware.htmlunit.WebClient;

public class GetNews {

//	private Webcl
//	private static WebClient webClient =new WebClient();
	public static void main(String[] args) {
		String url = "http://news.163.com/17/1122/15/D3RT43VC00014AEE.html";
		getUrlList(url);
	}

	public static void getUrlList(String url) {
		
		WebClient webClient = new WebClient();
		
		webClient.getOptions().setCssEnabled(false);
        webClient.setJavaScriptTimeout(300000);
        webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);
        webClient.getOptions().setThrowExceptionOnScriptError(false);
        webClient.getOptions().setPrintContentOnFailingStatusCode(false);
        webClient.addRequestHeader("Host", "http://news.163.com/17/1122/15/D3RT43VC00014AEE.html");
        
		System.out.println("url:"+url);
		try {
			SgmlPage page = webClient.getPage(url);
			System.out.println(page);
		} catch (FailingHttpStatusCodeException e) {
			e.printStackTrace();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
