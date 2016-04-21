package com.devefx.crawler.parser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.nodes.Element;

import com.devefx.crawler.core.Crawler;
import com.devefx.crawler.core.Resolve;
import com.devefx.crawler.model.MonsterSkill;

public class MonsterSkillCrawler extends Crawler<MonsterSkill> {

	private static final String URL1 = "http://xyq.163.com/zhs006_2.html";
	private static final String URL2 = "http://xyq.163.com/zhs006_2_2.html";
	private static final String cssQuert = ".l_table tbody tr";
	
	private static final Resolve<MonsterSkill> RESOLVE = new Resolve<MonsterSkill>() {
		@Override
		public MonsterSkill process(Element element) {
			return new MonsterSkill(element.child(1).text(), element.child(2).text());
		}
	};
	
	@Override
	public List<MonsterSkill> parse() throws IOException {
		List<MonsterSkill> list = new ArrayList<MonsterSkill>();
		
		resolve(URL1, cssQuert, list, RESOLVE);
		resolve(URL2, cssQuert, list, RESOLVE);
		
		return list;
	}
}
