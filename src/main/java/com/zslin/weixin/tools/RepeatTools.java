package com.zslin.weixin.tools;

import com.zslin.weixin.dto.MsgDto;

import java.util.ArrayList;
import java.util.List;

/**
 * 解决微信排重的工具类
 * @author 钟述林
 *
 */
public class RepeatTools {

	private static RepeatTools instance;
	private static Integer MAX_LENGTH = 800; //List的最大长度
	public static RepeatTools getInstance() {
		if(instance==null) {instance = new RepeatTools();}
		return instance;
	}
	
	private static List<MsgDto> msgList;

	public static MsgDto getOldMsgDto(String fromOpenid, String cTime) {
		MsgDto temp = new MsgDto(fromOpenid, cTime, System.currentTimeMillis()/1000);
		if(msgList==null) {msgList = new ArrayList<>();}
		rebuildList(); //只去掉超长的数据
		if(msgList.contains(temp)) {
			MsgDto res = msgList.get(msgList.indexOf(temp));
			res.setIsNew(false);
			return res;
		} else {temp.setIsNew(true); msgList.add(temp); return temp;}
	}
	
	//定时删除记录，最多只保留最新的50条数据
	public static void rebuildListOnTimer() {
		if(msgList!=null && msgList.size()>100) {
			for(int i=0; i<msgList.size()-50; i++) {
				msgList.remove(i);
			}
		}
	}
	
	//重新组合MsgList
	private static void rebuildList() {
		if(msgList.size()>MAX_LENGTH) {
			for(int i=0; i<msgList.size()-MAX_LENGTH; i++) { //如果list太长，则移除一部份
				msgList.remove(i);
			}
		}
	}
}
