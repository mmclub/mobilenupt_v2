package com.mobilenupt.library;

public class ListBook {
	private String type;
	private String bookname;
	private String sysnumber;
	private String press;
	private String lending;
	private String n;// total num of book
	private String href;//link of each book
	
	public ListBook(String type, String sysnumber, String bookname,
			String press, String lending, String n,String href) {
		super();
		this.type = type;
		this.sysnumber = sysnumber;
		this.bookname = bookname;
		this.press = press;
		this.lending = lending;
		this.n = n;
		this.href = href;
	}
	
	public ListBook() {
		
	}
	
	public String getType() {
		return type;
	}
	
	public void setType(String type) {
		this.type = type;
	}
	
	public String getSysnubmer() {
		return sysnumber;
	}
	
	public void setSysnumber(String sysnumber) {
		this.sysnumber = sysnumber;
	}
	
	public String getBookname() {
		return bookname;
	}
	
	public void setBookname(String bookname) {
		this.bookname = bookname;
	}
	
	public String getPress() {
		return press;
	}
	
	public void setPress(String press) {
		this.press = press;
	}
	
	public String getLending() {
		return lending;
	}
	
	public void setLending(String lending) {
		this.lending = lending;
	}
	
	public String getN() {
		return n;
	}
	
	public void setN(String n) {
		this.n = n;
	}
	
	public String getHref() {
		return href;
	}
	
	public void setHref(String href) {
		this.href= href;
	}
}
