package com.fx.webClient;

import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;

/**
 * 忽悠人的http 服务器
 * @author shanl
 *
 */
public class HttpServer {
    static public void start(){
        StringBuilder echo = new StringBuilder();
        echo.append("<html>");
        echo.append("<head>");
        echo.append("<title>hello</title>");
        echo.append("</head>");
        echo.append("<body>");
        echo.append("<center>");
        echo.append("<b1>Welcome to my home page.</b1>");
        echo.append("</center>");
        echo.append("</body>");
        echo.append("</html>");
        
        ServerSocket ss = null;
        int port = 8010;
        SocketAddress address = null;
        Socket connect = null;
        byte[] buffer = new byte[512];
        int readLen = 0;
        
        try{
            System.out.println("开始监听:" + port);
            address = new InetSocketAddress("localhost", port);
            ss = new ServerSocket();
            ss.bind(address);
            
            while(true){
                connect = ss.accept();
                
                do{
                    readLen = connect.getInputStream().read(buffer);
                    System.out.println(new String(buffer));
                }while(readLen == 512);
                
                connect.getOutputStream().write(echo.toString().getBytes());
                connect.close();
            }
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }
    
    public static void main(String[] args){
        start();
    }
}