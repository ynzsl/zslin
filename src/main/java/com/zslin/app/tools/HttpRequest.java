package com.zslin.app.tools;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by 钟述林 393156105@qq.com on 2016/12/26 8:47.
 */
public class HttpRequest {

    public static String url = "http://data.zz.baidu.com/urls?site=www.zslin.com&token=8vuw9dOmN3opAHl3&type=original";

    public static String sendPost(String parameter) {
        return sendPost(url, new String []{parameter});
    }

    public static String sendPost(String [] parameters) {
        return sendPost(url, parameters);
    }

    public static String sendPost(String postUrl,String[] parameters){
        if(null == postUrl || null == parameters || parameters.length ==0){
            return null;
        }
        String result="";
        PrintWriter out=null;
        BufferedReader in=null;
        try {
            //建立URL之间的连接
            URLConnection conn=new URL(postUrl).openConnection();
            //设置通用的请求属性
            conn.setRequestProperty("Host","data.zz.baidu.com");
            conn.setRequestProperty("User-Agent", "curl/7.12.1");
            conn.setRequestProperty("Content-Length", "83");
            conn.setRequestProperty("Content-Type", "text/plain");

            //发送POST请求必须设置如下两行
            conn.setDoInput(true);
            conn.setDoOutput(true);

            //获取conn对应的输出流
            out=new PrintWriter(conn.getOutputStream());
            //发送请求参数
            String param = "";
            for(String s : parameters){
                param += s+"\n";
            }
            out.print(param.trim());
            //进行输出流的缓冲
            out.flush();
            //通过BufferedReader输入流来读取Url的响应
            in=new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            while((line=in.readLine())!= null){
                result += line;
            }

        } catch (Exception e) {
            System.out.println("发送post请求出现异常！"+e);
            e.printStackTrace();
        } finally{
            try{
                if(out != null){
                    out.close();
                }
                if(in!= null){
                    in.close();
                }

            }catch(IOException ex){
                ex.printStackTrace();
            }
        }
        return result;
    }
}
