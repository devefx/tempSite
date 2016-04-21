package com.devefx.crawler;

import java.io.IOException;
import java.util.List;

import com.devefx.crawler.model.*;
import com.devefx.crawler.parser.*;

public class Test {
	
	public static void main(String[] args) throws IOException {
/*		List<Monster> monsters = new MonsterCrawler().parse();
		for (Monster monster : monsters) {
			System.out.println(monster.getName() + "|" + monster.getCarryLevel() + "|" + monster.getUseLevel());
		}
		
		List<Equip> equips = new EquipCrowler().parse();
		for (Equip equip : equips) {
			System.out.println(equip.getName() + "|" + equip.getDescription() + "|" + equip.getLevel());
		}
		*/
		List<Item> items = new ItemCrawler().parse();
		for (Item item : items) {
			System.out.println(item.getName() + "|" + item.getDescription() + "|" + item.getEffect() + "|" + item.getPrice());
		}
	}
}
