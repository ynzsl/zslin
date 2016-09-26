package com.zslin.weixin.dto;

/**
 * 消息DTO对象
 *  - 主要用于WeixinController.rootPost
 *  - 解决排重问题
 * @author 钟述林 20150827
 *
 */
public class MsgDto {

	/** 操作方openid */
	private String fromUser;
	
	/** 操作时间 */
	private String cTime;
	
	/** 当前系统时间 */
	private long curTime;
	
	/** 需要发给用户的内容 */
	private String content = "";
	
	/** 是否是新对象 */
	private boolean isNew;

	public boolean getIsNew() {
		return isNew;
	}

	public void setIsNew(boolean isNew) {
		this.isNew = isNew;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	@Override
	public int hashCode() {
		return (int) curTime;
	}
	
	@Override
	public boolean equals(Object obj) {
		try {
			MsgDto dto = (MsgDto) obj;
			return dto.getFromUser().equals(fromUser) && dto.getcTime().equals(cTime);
		} catch (Exception e) {
			return false;
		}
	}
	
	public MsgDto(String fromUser, String cTime, Long curTime) {
		super();
		this.fromUser = fromUser;
		this.cTime = cTime;
		this.curTime = curTime;
	}
	
	public long getCurTime() {
		return curTime;
	}

	public void setCurTime(long curTime) {
		this.curTime = curTime;
	}

	public String getFromUser() {
		return fromUser;
	}

	public void setFromUser(String fromUser) {
		this.fromUser = fromUser;
	}

	public String getcTime() {
		return cTime;
	}

	public void setcTime(String cTime) {
		this.cTime = cTime;
	}
}
