

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Hashtable;

import javax.swing.ImageIcon;

public class RoomData implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private String room_id;
	public String room_name;
	private ArrayList<String> userlist;
	// 유저별 입장 시간
	private Hashtable<String, Calendar> enterTime = new Hashtable<String, Calendar>();
	public ImageIcon roomImg;
	
	public RoomData(String room_id, String room_name, String[] userlist, Calendar time) {
		this.room_id = room_id;
		this.room_name = room_name;
		this.userlist = new ArrayList<>(Arrays.asList(userlist));
		for (int i=0; i<userlist.length; i++) {
			this.enterTime.put(userlist[i], time);
		}
	}
	
	
	public String getRoomId() {
		return room_id;
	}

	public void setRoomId(String room_id) {
		this.room_id = room_id;
	}

	public ArrayList<String> getUserlist() {
		return userlist;
	}

	public void setUserlist(ArrayList<String> userlist) {
		this.userlist = userlist;
	}
	
	public void setEnterTime(String name, Calendar time) {
		this.enterTime.put(name, time);
	}
	
	public Calendar getEnterTime(String name) {
		return this.enterTime.get(name);
	}

}
