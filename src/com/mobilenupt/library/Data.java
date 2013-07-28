package com.mobilenupt.library;

import java.io.IOException;

public class Data {
    private DataProvider dataProvider = new DataProvider();
    private String postUrl = "";
    private String input = "";
    private String page = "";
    private String msg = "";

    public Data(String postUrl) {
	this.postUrl = postUrl;
    }

    public void setWord(String input) {
	this.input = input;
    }

    public String getWord() {
	return input;
    }

    public void setPage(String page) {
	this.page = page;
    }

    public String getPage() {
	return page;
    }

    public String post() throws IOException {
		dataProvider.setData(input, page);
		msg = HTML_Jsoup.post(postUrl, dataProvider.getData());
		return msg;
    }

}
