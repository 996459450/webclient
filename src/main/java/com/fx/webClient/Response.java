package com.fx.webClient;

import java.io.FileInputStream;
import java.io.OutputStream;
//import com.sun.net.httpserver.

public class Response {


	private static final int BUFFER_SIZE = 1024;
//	Request request;
	OutputStream output;
	public Response(OutputStream output) {
		this.output = output;
	}
	
	public void sendstaticResource() {
		byte[] bytes = new byte[BUFFER_SIZE];
		FileInputStream  fis = null;
//		File file = new File(HttpSer)
		
	}
}
