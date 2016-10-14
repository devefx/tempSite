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
			    Monster monster = p(element, 0);
			    if (monster != null) {
			        monsters1.add(monster);
                }
			    monster = p(element, 3);
			    if (monster != null) {
			        monsters2.add(monster);
                }
			    monster = p(element, 6);
			    if (monster != null) {
			        monsters3.add(monster);
                }
				return null;
			}
		});
		monsters1.addAll(monsters2);
		monsters1.addAll(monsters3);
		return monsters1;
	}

	Monster p(Element element, int index) {
	    String name = element.child(index).text().trim();
	    String lv = element.child(index + 1).text().trim();
	    
	    
	    Monster monster = null;
	    try {
	        monster = new Monster();
	        monster.setName(name);
	        monster.setLevelCarry(Integer.parseInt(lv));
	        
	        int condition = Monster.LEVEL;
	        int levelWar = 0;
	        String str = element.child(index + 2).text();
	        if (str.contains("或飞升")) {
	            levelWar = Integer.parseInt(str.substring(0, str.length() - 3));
	            condition = Monster.LEVEL_OR_FLYUP;
	        } else if (str.contains("飞升后")) {
	            condition = Monster.FLYUP;
	        } else if (str.startsWith("飞升")) {
	            condition = Monster.LEVEL_AND_FLYUP;
	            levelWar = Integer.parseInt(str.substring(2));
	        } else if (str.contains("渡劫")) {
	            condition = Monster.KALPA;
	        } else {
	            levelWar = Integer.parseInt(str);
	        }
	        monster.setLevelWar(levelWar);
	        monster.setCondition(condition);
        } catch (Exception e) {
            //System.out.println(name + ":" + lv);
        }
	    return monster;
	}
	
}
