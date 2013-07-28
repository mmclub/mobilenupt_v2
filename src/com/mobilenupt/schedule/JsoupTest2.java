package com.mobilenupt.schedule;

import java.util.ArrayList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import android.util.Log;

public class JsoupTest2 {
	
	public static String parse(String html){
		
		Document doc = null;
		try {
			doc = Jsoup.parse(html);
			// doc = Jsoup.connect("http://10.200.24.61/test.htm").get();
		}catch (Exception e){
			e.printStackTrace();
		}
		
		Elements trs = doc.getElementById("Table1").getElementsByTag("tr");		 
		
		ArrayList< ArrayList<String> > list = new ArrayList< ArrayList<String> >();
		ArrayList<String> list1 = new ArrayList<String>();
		ArrayList<String> list2 = new ArrayList<String>();
		ArrayList<String> list3 = new ArrayList<String>();
		ArrayList<String> list4 = new ArrayList<String>();
		ArrayList<String> list5 = new ArrayList<String>();
		ArrayList<String> list6 = new ArrayList<String>();
		
		list.add(list1);
		list.add(list2);
		list.add(list3);
		list.add(list4);
		list.add(list5);
		list.add(list6);
		
		// 标志变量，判断345和678的三种情形
		int[] flg = new int[6];
		for (int i=0; i<6; i++) flg[i] = 0;
		// 需要解析6个关键点
		int pos[] = {2, 4, 6, 7, 9 ,11};
		int seek[] = {2, 1, 1, 2, 1, 2};
		Elements tds;
		
		int reduce = 0;
		String temp;
		
		for (int i=0; i<6; i++){	// 第i节
			tds = trs.get( pos[i] ).getElementsByTag("td");
			// 判断第六节时重置偏移变量
			if (i==3) reduce = 0;
			for (int week=0; week+seek[i]<tds.size()-1 && week<6; week++) {	// 第i节的第week天				
				
				switch (i){
				case 0:		// 第一节
				case 5:		// 第十节
					temp = tds.get( week+seek[i] ).text();
					
					if (temp.length() < 5) list.get(week).add("#");
					else list.get(week).add(temp);
					break;
				case 1:	// 第三节
					temp = tds.get( week+seek[i] ).text();
					
					if ( temp.length() < 5 ) list.get(week).add("#");
					else {
						list.get(week).add(temp);
						// 当第三节或第6节出现3行时
						if (tds.get( week+seek[i] ).attr("rowspan").equals("3")){
							Log.v("tag","reduce");
							flg[week] = 1;
							list.get(week).add("同上");
						}
					}
					break;
				case 3:		// 第六节
					temp = tds.get( week+seek[i] ).text();
					
					if ( temp.length() < 5 ) list.get(week).add("#");
					else {
						list.get(week).add(temp);
						// 当第三节或第6节出现3行时
						if (tds.get( week+seek[i] ).attr("rowspan").equals("3")){
							Log.v("tag","reduce");
							flg[week] = 1;
							list.get(week).add("同上");
							// 当6,7,8连着显示时，第9节为空
							list.get(week).add("#");
						}
					}
					break;
				case 2:		// 第五节
					// 检查该节是否已处理
					if (flg[week+reduce]==1) {
						Log.i("week+reduce", String.valueOf(week+reduce));
						flg[week+reduce]=0;
						reduce++;
						week--;
					}else{						
						temp = tds.get( week+seek[i] ).text();
						if ( temp.length() < 5 )
							list.get(week+reduce).add("#");
						else list.get(week+reduce).add(temp);
					}
					
					break;
				case 4:		// 第八节		
					// 检查该节是否已处理
					if (flg[week+reduce]==1) {
						Log.i("week+reduce", String.valueOf(week+reduce));
						flg[week+reduce]=0;
						reduce++;
						week--;
					}else{						
						temp = tds.get( week+seek[i] ).text();
						if ( temp.length() < 5 ) {
							list.get(week+reduce).add("#");
							// 第8节为空，则第9节也为空
							list.get(week+reduce).add("#");
						}
						else {
							list.get(week+reduce).add(temp);
							// 第8节有内容，判断是否占两行
							if (tds.get(week+seek[i]).attr("rowspan").equals("2"))
								list.get(week+reduce).add("同上");
							else list.get(week+reduce).add("#");
						}
					}
					
					break;
				default:
					break;
				}
				
			}
		}

		// 处理成string返回
		String result="";
		for (int i=0; i<6; i++) {
			for (int j=0; j<7; j++) {
				result += list.get(i).get(j) + "@";
			}
		}
		
		return result;
		
	}
	
}
