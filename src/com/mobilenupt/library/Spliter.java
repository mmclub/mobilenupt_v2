package com.mobilenupt.library;

import java.util.ArrayList;
import java.util.List;

public class Spliter {
	
	public static String parse(String resultStr) {
		return resultStr;
	}
	
	public static List<ListBook> getBookList(String data) {
		List<ListBook> bookArray=new ArrayList<ListBook>();
		String[] messages = data.split("!");
		for(int i=0;i<messages.length-1;i++){
			String message = messages[i];
			String[] items = message.split("");
			ListBook listbook=new ListBook();
			listbook.setType(items[0].trim());
			listbook.setSysnumber(items[1].trim());
			listbook.setBookname(items[2].trim());
			listbook.setPress(items[3].trim());
			listbook.setLending(items[4].trim());
			listbook.setHref(items[5].trim());
			listbook.setN(items[6].trim());
			bookArray.add(listbook);
		}
		
		return bookArray;
	}
}
