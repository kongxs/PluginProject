package fu.wanke.link;


import org.json.JSONObject;

import java.io.Serializable;

import fu.wanke.link.core.Commands;

public class MessagePage implements Serializable {

	private static final long serialVersionUID = -4746260107384254289L;

	private short command;

	private String msgBody;


	public short getCommand() {
		return command;
	}

	public void setCommand(short command) {
		this.command = command;
	}

	public String getMsgBody() {
		return msgBody;
	}

	public void setMsgBody(String msgBody) {
		this.msgBody = msgBody;
	}

	@Override
	public String toString() {
		return "command : " + getCommand() + " , msg : " + getMsgBody();
	}






	public static MessagePage mockRegister() {


		MessagePage msg = new MessagePage();
		msg.setCommand((short) 1001);

		JSONObject jsonObject;
		try {

			jsonObject = new JSONObject();
				jsonObject.put("appid", Constants.APPID);
				jsonObject.put("appSecret", Constants.APP_src);
				jsonObject.put("deviceToken", "JD2867595697247d24a8c9101b7e868");
				jsonObject.put("deviceTokenSrc", 7);
				jsonObject.put("pushEnable", 1);
				jsonObject.put("osVersion", "26");
				jsonObject.put("appVersion", "1.0.0");
				jsonObject.put("deviceType", 2);
				jsonObject.put("pkgName", "push.demo");
				jsonObject.put("uuid", "869782028853144-4c49e348eda8");
				jsonObject.put("deviceCategory", 99);
				jsonObject.put("language", "zh_cn");
				jsonObject.put("sdkVersion", "3.0.8");
				jsonObject.put("deviceBrand", "Mi Note 2");
				jsonObject.put("deviceRom", "OPR1.170623.032");

				msg.setMsgBody(jsonObject.toString());
				return msg;
		} catch (Exception e) {

		}
		return null;
	}



	public static MessagePage mockConnection() {


		try{


			MessagePage msg = new MessagePage();
			msg.setCommand((Commands.COMMAND_LINK));


			JSONObject jsonObject = new JSONObject();

			jsonObject.put("appid", Constants.APPID);
			jsonObject.put("appSecret", Constants.APP_src);
			jsonObject.put("deviceToken", "2867595697247d24a8c9101b7e868");

			msg.setMsgBody(jsonObject.toString());

			return msg;

		} catch (Exception e){

		}

		return null;
	}


	public static MessagePage mockHeart() {
		MessagePage msg = new MessagePage();
		msg.setCommand((Commands.COMMAND_HEART));
		return msg;
	}



}
