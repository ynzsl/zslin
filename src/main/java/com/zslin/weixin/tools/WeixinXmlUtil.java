package com.zslin.weixin.tools;

import com.zslin.basic.auth.tools.DateTools;
import com.zslin.weixin.dto.TempParamDto;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 处理微信的一些互动
 * @author 钟述林
 *
 */
public class WeixinXmlUtil {

	/** 获取事件模板Id */
	private static String getEventTempId() {
		return WeixinConstant.getInstance().getWeiXinConfig().getEventTempId();
	}

	/**
	 * 生成发送文本消息的XML字符串
	 * @param toUser 接收方的openid
	 * @param fromUser 开发者微信号
	 * @param content 内容
	 * @return
	 */
	public static String createTextXml(String toUser, String fromUser, String content) {
		StringBuffer sb = new StringBuffer();
		sb.append("<xml>");
		sb.append("<ToUserName><![CDATA[").append(toUser).append("]]></ToUserName>").
		   append("<FromUserName><![CDATA[").append(fromUser).append("]]></FromUserName>").
		   append("<CreateTime>").append(System.currentTimeMillis()/1000).append("</CreateTime>").
		   append("<MsgType><![CDATA[text]]></MsgType>").
		   append("<Content><![CDATA[").append(content).append("]]></Content>");
		sb.append("</xml>");
		return sb.toString();
	}
	
	/**
	 * 生成发送链接消息的XML字符串
	 * @param toUser 接收方的openid
	 * @param fromUser 开发者微信号
	 * @param content 内容
	 * @return
	 */
	public static String createUrlXml(String toUser, String fromUser, String title, String url, String content) {
		StringBuffer sb = new StringBuffer();
		sb.append("<xml>");
		sb.append("<ToUserName><![CDATA[").append(toUser).append("]]></ToUserName>").
		   append("<FromUserName><![CDATA[").append(fromUser).append("]]></FromUserName>").
		   append("<CreateTime>").append(System.currentTimeMillis()/1000).append("</CreateTime>").
		   append("<MsgType><![CDATA[link]]></MsgType>").
		   append("<Title><![CDATA[").append(title).append("]]></Title>").
		   append("<Url><![CDATA[").append(url).append("]]></Url>").
		   append("<Description><![CDATA[").append(content).append("]]></Description>");
		sb.append("</xml>");
		return sb.toString();
	}
	 
	/**
	 * 发送消息的XML字符串
	 * @param toUser 接收方的openid
	 * @param tempId 消息模板id
	 * @param url 链接地址
	 * @param topColor 顶部颜色
	 * @param paramList 参数列表
	 * @return
	 */
	public static String createTemplateMessageXml(String toUser, String tempId, String url, String topColor, List<TempParamDto> paramList) {
		StringBuffer sb = new StringBuffer();
		sb.append("{");
		
		sb.append("\"touser\":\"").append(toUser).append("\",").
		   append("\"template_id\":\"").append(tempId).append("\",");
		
		if(url!=null && !"".equals(url.trim())) {
			sb.append("\"url\":\"").append(url).append("\",");
		}
		
		sb.append("\"topcolor\":\"").append(topColor).append("\",").
		   append("\"data\":{");
		int i = 0;
		for(TempParamDto dto : paramList) {
			i++;
			sb.append("\"").append(dto.getParamName()).append("\": {\"value\":\"").append(dto.getValue()).append("\",\"color\":\"").append(dto.getColor()).append("\"}");
			if(i<paramList.size()) {sb.append(",");}
		}
		
		sb.append("}}");
		return sb.toString();
	}
	
	/**
	 * 发送消息
	 * @param toUser 接收方的openid
	 * @param tempId 消息模板id
	 * @param url 链接地址
	 * @param topColor 顶部颜色
	 * @param paramList 参数列表
	 * @return 返回发送结果
	 */
	public static String sendMessage(String toUser, String tempId, String url, String topColor, List<TempParamDto> paramList) {
		
		String tempUrl = url;
		if(tempUrl!=null && tempUrl.indexOf("{openid}")>=0) {tempUrl = tempUrl.replaceAll("\\{openid\\}", toUser);}
		
		String postUrl = "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token="+AccessTokenTools.getInstance().getAccessToken();
		String params = createTemplateMessageXml(toUser, tempId, tempUrl, topColor, paramList);
		JSONObject jsonObj = InternetTools.httpRequest(postUrl, "POST", params);
//		String res = JsonTools.getJsonParam(jsonObj.toString(), "errcode");
		String res = jsonObj==null?"":jsonObj.toString();
		if(res.indexOf("access_token is invalid or not latest hint")>=0) {
			AccessTokenTools.getInstance().refreshAccessToken(); //刷新token
			postUrl = "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token="+AccessTokenTools.getInstance().getAccessToken();
			params = createTemplateMessageXml(toUser, tempId, tempUrl, topColor, paramList);
			jsonObj = InternetTools.httpRequest(postUrl, "POST", params);
			res = jsonObj==null?"":jsonObj.toString();
		}
		return res;
	}
	
	/**
	 * 发送消息
	 * @param toUser 接收方的openid
	 * @param tempId 消息模板id
	 * @param url 链接地址
	 * @param topColor 顶部颜色
	 * @param paramList 参数列表
	 * @return 发送成功返回true，否则返回false
	 */
	public static boolean sendMsg(String toUser, String tempId, String url, String topColor, List<TempParamDto> paramList) {
		String res = sendMessage(toUser, tempId, url, topColor, paramList);
		String errcode = JsonTools.getJsonParam(res, "errcode");
		if("0".equals(errcode)) {return true;}
		else if("40003".equals(errcode)) {
			//System.out.println("无效openid："+toUser);
		} else if("40037".equals(errcode)) {
			System.out.println("无效模板Id："+tempId);
		}
		System.out.println("WeixinXmlUtil=="+res);
		return false;
	}
	
	/**
	 * 事件提醒
	 * @param title 标题
	 * @param toUser 接收用户的openid
	 * @param eventType 事件名称
	 * @param date 事件日期
	 * @param remark 备注信息
	 */
	public static boolean eventRemind(String title, String toUser, String eventType, String date, String remark, String url) {
		List<TempParamDto> paramList = new ArrayList<>();
		paramList.add(new TempParamDto("first", title, "#0000FF"));
		paramList.add(new TempParamDto("keyword1", eventType, "#888888"));
		paramList.add(new TempParamDto("keyword2", date, "#888888"));
		paramList.add(new TempParamDto("remark", remark, "#666666"));
		return sendMsg(toUser, getEventTempId(), url, "#FF0000", paramList);
	}

	/**
	 * 事件提醒
	 * @param title 标题
	 * @param toUser 接收用户的openid
	 * @param eventType 事件名称
	 * @param remark 备注信息
	 */
	public static boolean eventRemind(String title, String toUser, String eventType, String remark, String url) {
		return eventRemind(title, toUser, eventType, DateTools.formatDate(new Date()), remark, url);
	}

	/**
	 * 事件提醒
	 * @param title 标题
	 * @param toUsers 接收用户的openid
	 * @param eventType 事件名称
	 * @param remark 备注信息
	 */
	public static boolean eventRemind(List<String> toUsers, String title, String eventType, String remark, String url) {
		for(String openid : toUsers) {
			eventRemind(title, openid, eventType, remark, url);
		}
		return true;
	}
	
	/**
	 * 事件提醒
	 * @param title 标题
	 * @param toUsers 接收用户的openid
	 * @param eventType 事件名称
	 * @param date 事件日期
	 * @param remark 备注信息
	 */
	public static boolean eventRemind(List<String> toUsers, String title, String eventType, String date, String remark, String url) {
		for(String openid : toUsers) {
			eventRemind(title, openid, eventType, date, remark, url);
		}
		return true;
	}
	
	/**
	 * 平台动态报告提醒
	 * @param title 标题
	 * @param toUser 接收用户的openid
	 * @param eventType 事件名称
	 * @param date 事件日期
	 * @param remark 备注信息
	 */
	public static boolean reportRemind(String title, String toUser, String eventType, String date, String remark) {
		List<TempParamDto> paramList = new ArrayList<TempParamDto>();
		paramList.add(new TempParamDto("first", title, "#ccaa00"));
		paramList.add(new TempParamDto("keyword1", eventType, "#888888"));
		paramList.add(new TempParamDto("keyword2", date, "#888888"));
		paramList.add(new TempParamDto("remark", remark, "#666666"));
		return sendMsg(toUser, getEventTempId(), "", "#FF0000", paramList);
	}
	
	/** 群发站内信 */
	public static boolean eventLetter(String fromUser, String toUser, String remark, String url) {
		List<TempParamDto> paramList = new ArrayList<TempParamDto>();
		paramList.add(new TempParamDto("first", "收到["+fromUser+"]发来的通知", "#d05300"));
		paramList.add(new TempParamDto("keyword1", "站内信事件推送", "#888888"));
		paramList.add(new TempParamDto("keyword2", DateTools.formatDate(new Date()), "#888888"));
		paramList.add(new TempParamDto("remark", remark, "#666666"));
		return sendMsg(toUser, getEventTempId(), url, "#FF0000", paramList);
	}
}
