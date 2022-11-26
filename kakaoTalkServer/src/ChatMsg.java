

// ChatMsg.java ä�� �޽��� ObjectStream ��.
import java.io.Serializable;
import java.util.Calendar;

import javax.swing.ImageIcon;

class ChatMsg implements Serializable {
	private static final long serialVersionUID = 1L;
	private String id;
	private String code;
	private String data;
	public ImageIcon img;
	public ImageIcon profile;
	public String userlist;	
	public String room_id;
	public Calendar time;

	public ChatMsg(String id, String code, String msg) {
		this.id = id;
		this.code = code;
		this.data = msg;
		
		Calendar today = Calendar.getInstance();
		this.time = today;
	}

	public String getCode() {
		return code;
	}
	
	public String getData() {
		return data;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setData(String data) {
		this.data = data;
	}
	
	public void setCode(String code) {
		this.code = code;
	}

}