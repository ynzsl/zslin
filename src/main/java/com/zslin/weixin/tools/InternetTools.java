package com.zslin.weixin.tools;

import org.json.JSONObject;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;

/**
 * 与微信平台进行交互的网络处理工具类
 * @author 钟述林
 *
 */
public class InternetTools {

	/**
	 * 处理get请求
	 * @param serverName url
	 * @param params 参数
	 * @return 返回结果
	 */
	public static String doGet(String serverName, Map<String, Object> params) {
		String result = null;
		int flag = 0;
		while(result==null && (flag++)<3) {
			try {
				URL url = new URL(rebuildUrl(serverName, params));
				HttpURLConnection conn = (HttpURLConnection) url.openConnection();
				conn.setReadTimeout(5000);
				conn.connect();
				BufferedReader reader =new BufferedReader(new InputStreamReader(conn.getInputStream(),"utf-8"));
				result = reader.readLine();
				
				//如果在POST结果中提示无效access_token，则刷新token重新获取
				if(result!=null && result.indexOf("access_token is invalid or not latest hint")>=0) {
					params.put("access_token", AccessTokenTools.getInstance().refreshAccessToken());
					url = new URL(rebuildUrl(serverName, params));
					conn = (HttpURLConnection) url.openConnection();
					conn.setReadTimeout(5000);
					conn.connect();
					reader =new BufferedReader(new InputStreamReader(conn.getInputStream(),"utf-8"));
					result = reader.readLine();
				}
			} catch (Exception e) {
				System.out.println("InternetTools.toGet 出现异常："+e.getMessage());
			}
		}
		return result;
	}
	
	/**
	 * 重新生成url
	 * @param serverName
	 * @param params
	 * @return
	 */
	private static String rebuildUrl(String serverName, Map<String, Object> params) {
		StringBuffer sb = new StringBuffer(serverName);
		if(serverName.indexOf("?")<0) {
			sb.append("?1=1");
		}
		if(params!=null) {
			for(String key : params.keySet()) {
				sb.append("&").append(key).append("=").append(params.get(key));
			}
		}
		return sb.toString();
	}

	public static final String POST = "POST";
	public static final String GET = "GET";

	/**
	 *
	 * 发起https请求并获取结果
	 *
	 *
	 *
	 * @param requestUrl
	 *            请求地址
	 *
	 * @param requestMethod
	 *            请求方式（GET、POST）
	 *
	 * @param outputStr
	 *            提交的数据
	 *
	 * @return JSONObject(通过JSONObject.get(key)的方式获取json对象的属性值)
	 */

	public static JSONObject httpRequest(String requestUrl,
										 String requestMethod, String outputStr) {
		JSONObject jsonObject = null;
		StringBuffer buffer = new StringBuffer();
		try {
			// 创建SSLContext对象，并使用我们指定的信任管理器初始化
			TrustManager[] tm = { new MyX509TrustManager() };
			SSLContext sslContext = SSLContext.getInstance("SSL", "SunJSSE");

			sslContext.init(null, tm, new java.security.SecureRandom());

			// 从上述SSLContext对象中得到SSLSocketFactory对象
			SSLSocketFactory ssf = sslContext.getSocketFactory();

			URL url = new URL(requestUrl);

			HttpsURLConnection httpUrlConn = (HttpsURLConnection) url
					.openConnection();
			httpUrlConn.setSSLSocketFactory(ssf);
			httpUrlConn.setDoOutput(true);
			httpUrlConn.setDoInput(true);
			httpUrlConn.setUseCaches(false);

			// 设置请求方式（GET/POST）
			httpUrlConn.setRequestMethod(requestMethod);
			if ("GET".equalsIgnoreCase(requestMethod))
				httpUrlConn.connect();
			// 当有数据需要提交时
			if (null != outputStr) {
				OutputStream outputStream = httpUrlConn.getOutputStream();
				// 注意编码格式，防止中文乱码
				outputStream.write(outputStr.getBytes("UTF-8"));
				outputStream.close();
			}

			// 将返回的输入流转换成字符串
			InputStream inputStream = httpUrlConn.getInputStream();

			InputStreamReader inputStreamReader = new InputStreamReader(
					inputStream, "utf-8");

			BufferedReader bufferedReader = new BufferedReader(
					inputStreamReader);

			String str = null;

			while ((str = bufferedReader.readLine()) != null) {
				buffer.append(str);
			}

			bufferedReader.close();
			inputStreamReader.close();
			// 释放资源
			inputStream.close();
			httpUrlConn.disconnect();
			jsonObject = new JSONObject(buffer.toString());
		} catch (ConnectException ce) {
		} catch (Exception e) {
		}
		return jsonObject;
	}

	//转码
	public static String urlEnodeUTF8(String str){
		String result = str;
		try {
			result = URLEncoder.encode(str,"UTF-8");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
}
