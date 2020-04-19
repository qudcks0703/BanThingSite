package bts.model.vo;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Bts_NoticeVO {

	String title;
	String content;
	String reg;
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getReg() {
		return reg;
	}
	public void setReg(String reg) {
		try {
			Date date = sdf.parse(reg);
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			//this.reg = (String)date;
		}
	}
	
}
