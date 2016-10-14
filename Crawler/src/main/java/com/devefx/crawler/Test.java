package com.devefx.crawler;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.devefx.crawler.model.*;
import com.devefx.crawler.parser.*;

public class Test {
	
	public static void main(String[] args) throws IOException {
	    /*
	    List<Monster> monsters = new MonsterCrawler().parse();
		for (Monster monster : monsters) {
		    String sql = String.format("insert into t01_monster(name, level_carry, level_war, `condition`)" +
		            " values('%s', %d, %d, %d);", monster.getName(), monster.getCarryLevel(),
		            monster.getUseLevel(), monster.getRequire());
			System.out.println(sql);
		}
		
		List<Equip> equips = new EquipCrowler().parse();
		for (Equip equip : equips) {
			System.out.println(equip.getName() + "|" + equip.getDescription() + "|" + equip.getLevel());
		}
		
		List<Item> items = new ItemCrawler().parse();
		for (Item item : items) {
			System.out.println(item.getName() + "|" + item.getDescription() + "|" + item.getEffect() + "|" + item.getPrice());
		}
		*/
	    
	    /*
		List<MonsterSkill> monsterSkills = new MonsterSkillCrawler().parse();
		for (MonsterSkill monsterSkill : monsterSkills) {
		    String sql = String.format("insert into t01_monster_skill(name, description) values('%s', '%s');",
		            monsterSkill.getName(), monsterSkill.getDescription());
		    System.out.println(sql);
        }
		*/
	    
	    List<Monster> list = new MonsterCrawlerEx().parse();
	    /*for (Monster monster : list) {
	        String sql = String.format("update t01_monster set description = '%s' where name = '%s';",
	                monster.getDescription(), monster.getName());
	        System.out.println(sql);
        }*/
	    /*
	    for (Monster monster : list) {
	        Integer[] aptitudes = monster.getAptitudes();
	        String sql = String.format("update t01_monster set aptitude_atk = %d, aptitude_def = %d, " +
	                "aptitude_con = %d, aptitude_mag = %d, aptitude_dex = %d, aptitude_eva = %d " +
	        		"where name = '%s';", aptitudes[0], aptitudes[1], aptitudes[2], aptitudes[3], aptitudes[4],
	        		aptitudes[5], monster.getName());
            System.out.println(sql);
	    }*/
	    
	    int i = 0;
	    for (Monster monster : list) {
	        List<String> skills = monster.getSkills();
	        for (String skill : skills) {
	            String sql = String.format("insert into t01_monster_own_skill(monster_id, monster_skill_id, required) " +
	            		"select a.id, b.id, 0 from t01_monster as a, t01_monster_skill as b where a.name = '%s' and b.name = '%s';",
	            		monster.getName(), skill);
	            
	            i++;
                System.out.println(sql);
            }
	        
	    }
	    System.out.println(i);
	    
	}
}
