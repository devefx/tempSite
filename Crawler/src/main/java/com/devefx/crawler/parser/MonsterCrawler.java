package com.devefx.crawler.parser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.nodes.Element;

import com.devefx.crawler.core.Crawler;
import com.devefx.crawler.core.Resolve;
import com.devefx.crawler.model.Monster;

public class MonsterCrawler extends Crawler<Monster> {

	private static final String URL = "http://xyq.163.com/introduce/zhs003.html";
	private static final String cssQuery = ".l_table tbody tr";
	
	@Override
	public List<Monster> parse() throws IOException {
		final List<Monster> monsters1 = new ArrayList<Monster>();
		final List<Monster> monsters2 = new ArrayList<Monster>();
		final List<Monster> monsters3 = new ArrayList<Monster>();
		resolve(URL, cssQuery, null, new Resolve<Monster>() {
			@Override
			public Monster process(Element element) {
				try {
					monsters1.add(new Monster(element.child(0).text(), 
							Integer.parseInt(element.child(1).text().trim())));
				} catch (Exception e) {
				}
				try {
					monsters2.add(new Monster(element.child(3).text(), 
							Integer.parseInt(element.child(4).text().trim())));
				} catch (Exception e) {
				}
				try {
					monsters3.add(new Monster(element.child(6).text(), 
							Integer.parseInt(element.child(7).text().trim())));
				} catch (Exception e) {
				}
				return null;
			}
		});
		monsters1.addAll(monsters2);
		monsters1.addAll(monsters3);
		return monsters1;
	}

}
