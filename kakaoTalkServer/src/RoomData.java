

import java.io.Serializable;

import javax.swing.ImageIcon;

public class RoomData implements Serializable {
	private static final long serialVersionUID = 1L;

	private String room_id;
	private String userlist[];
	public ImageIcon roomImg;
	
	public RoomData(String room_id, String[] userlist) {
		this.room_id = room_id;
		this.userlist = userlist;
	}
	
	public String getRoom_id() {
		return room_id;
	}

	public void setRoom_id(String room_id) {
		this.room_id = room_id;
	}

	public String[] getUserlist() {
		return userlist;
	}

	public void setUserlist(String[] userlist) {
		this.userlist = userlist;
	}

}
