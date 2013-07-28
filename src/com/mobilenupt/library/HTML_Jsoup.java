package com.mobilenupt.library;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

public class HTML_Jsoup {

	private static Document doc;
	private static String aString; // tag <a>
	private static String pspanString; // tag <span> from <p>
	private static String spanString; // tag <span>
	private static String h3String; // tag <h3>
	private static String pString;// tag <p>
	private static String hrefString;
	private static String n;// total num of book
	private static String[] type;
	private static String[] sysnumber;
	private static String[] bookname;
	private static String[] press;
	private static String[] lending;
	private static String[] totalstr;
	private static String[] hrefs;
	private static String msg;

	public static void connect(String postUrl, String data) throws IOException {
		String str = postUrl + "?" + data;
		System.out.println("HTML_Jsoup:" + str);
		doc = Jsoup.connect(str)
					.timeout(5000)
					.get();
	}

	public static String post(String postUrl, String data) throws IOException {
			msg = "";
			connect(postUrl, data);
			Elements topnews = doc.getElementsByClass("list_books");
			Elements links = topnews.select("div");
			int totalLinks = links.size();
			System.out.println("size = " + totalLinks);
			for (int i = 0; i < totalLinks; i++) {

				Elements h3 = links.get(i).select("h3");
				int totalH3 = h3.size();

				for (int ih3 = 0; ih3 < totalH3; ih3++) {
					h3String = h3.get(ih3).text();
					h3String.length();
					Elements span = h3.get(ih3).select("span");
					spanString = span.get(ih3).text();

					Elements a = h3.get(ih3).select("a[href]");
					hrefString = a.attr("abs:href");
					aString = a.get(ih3).text();
					String str = h3String.substring(spanString.length()
							+ aString.length(), h3String.length());
					
					type = new String[totalLinks];
					sysnumber = new String[totalLinks];
					bookname = new String[totalLinks];
					hrefs = new String[totalLinks];
					type[i] = spanString;
					sysnumber[i] = str;
					bookname[i] = aString;
					hrefs[i] = hrefString; 
				}
				Elements p = links.get(i).select("p");
				int totalP = p.size();
				for (int ip = 0; ip < totalP; ip++) {
					pString = p.get(ip).text();
					pString.length();
					Elements spanP = p.get(ip).select("span");
					pspanString = spanP.get(ip).text();
					String str1 = pString.substring(pspanString.length(),
							pString.length());
					press = new String[totalLinks];
					lending = new String[totalLinks];
					press[i] = str1;
					lending[i] = pspanString;
				}
				Elements topnews01 = doc.getElementsByClass("red");
				Elements links01 = topnews01.select("strong");
				
				n = links01.text();
				
				totalstr = new String[totalLinks];
				totalstr[i] = type[i] + "→" + sysnumber[i] + "→" + bookname[i]
						+ "→" + press[i] + "→" + lending[i]+ "→" + hrefs[i];
				msg += totalstr[i] + "→" + n + "!";
				msg += "\n";
				
			}
		return msg;
	}

}
