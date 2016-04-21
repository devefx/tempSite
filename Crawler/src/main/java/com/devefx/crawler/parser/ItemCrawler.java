package com.devefx.crawler.parser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.nodes.Element;

import com.devefx.crawler.core.Crawler;
import com.devefx.crawler.core.Resolve;
import com.devefx.crawler.model.Item;

public class ItemCrawler extends Crawler<Item> {

	private static final String yaoPin1 = "http://xyq.163.com/introduce/dj002.html";
	private static final String yaoPin2 = "http://xyq.163.com/dj002_2.html";
	private static final String yaoPin3 = "http://xyq.163.com/dj002_3.html";
	
	private static final String shiwu	= "http://xyq.163.com/introduce/dj003.html";
	
	private static final String juqing	= "http://xyq.163.com/introduce/dj006.html";
	
	private static final String cssQuery = ".l_table tbody tr";
	
	private static final Resolve<Item> YAOPIN_RESOLVE = new Resolve<Item>() {
		@Override
		public Item process(Element element) {
			if (element.select("td").size() == 5) {
				return new Item(element.child(1).text(), element.child(2).text(), 
						element.child(3).text(), Integer.parseInt(element.child(4).text()), null, 0);
			}
			return new Item(element.child(1).text(), element.child(2).text(), 
					element.child(3).text(), 0, null, 0);
		}
	};
	
	private static final Resolve<Item> SHIPIN_RESOLVE = new Resolve<Item>() {
		@Override
		public Item process(Element element) {
			if (element.select("td").size() == 5) {
				System.out.println("=================================" + element.child(3).text());
				return new Item(element.child(1).text(), element.child(2).text(), 
						element.child(4).text(), 0, null, 0);
			}
			return new Item(element.child(1).text(), element.child(2).text(), 
					element.child(3).text(), 0, null, 0);
		}
	};
	
	private static final Resolve<Item> JUQING_RESOLVE = new Resolve<Item>() {
		@Override
		public Item process(Element element) {
			return new Item(element.child(1).text(), element.child(2).text(), null, 0, null, 0);
		}
	};
	
	@Override
	public List<Item> parse() throws IOException {
		List<Item> items = new ArrayList<Item>();
		
		resolve(yaoPin1, cssQuery, items, YAOPIN_RESOLVE);
		resolve(yaoPin2, cssQuery, items, YAOPIN_RESOLVE);
		resolve(yaoPin3, cssQuery, items, YAOPIN_RESOLVE);
		
		resolve(shiwu, cssQuery, items, SHIPIN_RESOLVE);
		
		resolve(juqing, cssQuery, items, JUQING_RESOLVE);
		
		return items;
	}
}
