package com.devefx.crawler;

import java.io.IOException;
import java.util.List;

import com.devefx.crawler.model.*;
import com.devefx.crawler.parser.*;

public class Test {
	
	public static void main(String[] args) throws IOException {
	    
	    List<SchoolSkill> schoolSkills = new SchoolSkillCrawler().parse();
	    
	    int i = 1;
	    for (SchoolSkill schoolSkill : schoolSkills) {
	        schoolSkill.setId(i++);
	        
	        String sql = String.format("insert into t01_school_skill(id, school_id, name, description, icon) values(%d, %d, '%s', '%s', null);",
	                schoolSkill.getId(), schoolSkill.getSchoolId(), schoolSkill.getName(), schoolSkill.getDescription());
	        if ((i - 2) % 7 == 0) {
               // System.out.println();
            }
            //System.out.println(sql);
        }
	    
	    int j = 1;
	    int old_id = 0;
	    List<SchoolMagic> magics = new SchoolMagicSkillCrawler().parse();
	    for (SchoolMagic magic : magics) {
	        int skillId = 0;
	        for (SchoolSkill skill : schoolSkills) {
	            if (skill.getIncludeSkills().contains(magic.getName())) {
	                skillId = skill.getId();
	                break;
                }
	        }
	        if (skillId == 0) {
	            // 官网数据的坑
	            if ("掌心雷".equals(magic.getName())) {
	                for (SchoolSkill skill : schoolSkills) {
                        if ("乾坤塔".equals(skill.getName())) {
                            skillId = skill.getId();
                            break;
                        }
                    }
                }
            }
	        if (skillId == 0) {
	            System.err.println(magic.getName() + "==============================================================================================");
            }
	        magic.setSkillId(skillId);
	        
	        String sql = String.format("insert into t01_school_skill_magic(id, skill_id, name, description, cost_description, icon, icon_small, skill_level, role_level, require_type, hp_cost, mp_cost, sp_cost, cost_type)\n" +
	        		"values(%d, %d, '%s', '%s', '%s', null, null, %d, %d, %d, %d, %d, %d, %d);", 
	        		j++, magic.getSkillId(), magic.getName(), magic.getDescription(), magic.getCostDescription(), magic.getSkillLevel(), magic.getRoleLevel(), magic.getRequireType(), 
	        		magic.getHpCost(), magic.getMpCost(), magic.getSpCost(), magic.getCostType());
	        System.out.println(sql);
	        
	        if (skillId - old_id > 7) {
	            old_id = skillId;
	            System.out.println();
            }
        }
	    
	    /*
	    List<Monster> monsters = new MonsterCrawler().parse();
		for (Monster monster : monsters) {
		    String sql = String.format("insert into t01_monster(name, level_carry, level_war, `condition`)" +
		            " values('%s', %d, %d, %d);", monster.getName(), monster.getLevelCarry(),
		            monster.getLevelWar(), monster.getCondition());
			System.out.println(sql);
		}
		
		int id = 239;
		List<Equip> equips = new EquipCrowler().parse();
		for (Equip equip : equips) {
			String sql = String.format("insert into t01_item(id, name, description, icon, icon_big)\nvalues(%d, '%s', '%s', '', '');",
					id, equip.getName(), equip.getDescription(), "", "");
			
			String sql2 = String.format("insert into t01_item_weapon(item_id, kind_id, level) values(%d, %d, %d);",
					id, 15, equip.getLevel());
			
			System.out.println(sql);
			System.out.println(sql2);
			System.out.println();
			
			id ++;
			
			//System.out.println(equip.getName() + "|" + equip.getDescription() + "|" + equip.getLevel());
		}
		
		*/
		/*
		List<Item> items = new ItemCrawler().parse();
		for (Item item : items) {
			System.out.println(item.getName() + "|" + item.getDescription() + "|" + item.getEffect() + "|" + item.getPrice());
		}
		
	    
	    int i = 1;
		List<MonsterSkill> monsterSkills = new MonsterSkillCrawler().parse();
		for (MonsterSkill monsterSkill : monsterSkills) {
		    String sql = String.format("insert into t01_monster_skill(id, name, description, icon) values(%d, '%s', '%s', null);",
		            i, monsterSkill.getName(), monsterSkill.getDescription());
		    System.out.println(sql);
		    i++;
        }
		*/
		/*
	    List<Monster> list = new MonsterCrawlerEx().parse();
	    for (Monster monster : list) {
	        String sql = String.format("update t01_monster set description = '%s' where name = '%s';",
	                monster.getDescription(), monster.getName());
	        System.out.println(sql);
        }
	    
	    for (Monster monster : list) {
	        Integer[] aptitudes = monster.getAptitudes();
	        String sql = String.format("update t01_monster set aptitude_atk = %d, aptitude_def = %d, " +
	                "aptitude_con = %d, aptitude_mag = %d, aptitude_dex = %d, aptitude_eva = %d " +
	        		"where name = '%s';", aptitudes[0], aptitudes[1], aptitudes[2], aptitudes[3], aptitudes[4],
	        		aptitudes[5], monster.getName());
            System.out.println(sql);
	    }
	    
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
	    */
	}
}
