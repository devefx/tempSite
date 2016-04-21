package com.devefx.crawler.core;

import java.io.IOException;
import java.util.List;

import org.jsoup.Connection;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.jsoup.Jsoup;

public abstract class Crawler<T> {
	
	private static final String userAgent = "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:29.0) Gecko/20100101 Firefox/29.0";
	
	public List<T> resolve(String url, String cssQuery, List<T> list, Resolve<T> resolve) throws IOException {
		Connection connection = Jsoup.connect(url).userAgent(userAgent);
		Document document = connection.execute().parse();
		Elements elements = document.select(cssQuery);
		for (Element element : elements) {
			T t = resolve.process(element);
			if (list != null && t != null)
				list.add(t);
		}
		return list;
	}
	
	public abstract List<T> parse() throws IOException;
}
