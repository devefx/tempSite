package com.devefx.crawler.parser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.jsoup.nodes.Element;

import com.devefx.crawler.core.Crawler;
import com.devefx.crawler.core.Resolve;
import com.devefx.crawler.model.SchoolSkill;

public class SchoolSkillCrawler extends Crawler<SchoolSkill> {

    private static final String css1 = ".l_table tbody tr";
    
    private static final String css2 = ".tablepad:gt(1) tbody tr";
    
    /*
     * 大唐官府1 方寸山2 化生寺3 女儿村4 天宫5
                    龙宫6  五庄观7 普陀山8 阴曹地府9 魔王寨10
                    狮驼岭11 盘丝洞12 凌波城14 无底洞15 神木林13
     */
    private static final int[] schools = {0,1,2,3,4,5,6,7,8,9,10,11,12,14,15,13};
    
    static int i = 1;
    
    private static final Resolve<SchoolSkill> RESOLVE = new Resolve<SchoolSkill>() {
        @Override
        public SchoolSkill process(Element element) {
            if (element.children().size() == 3) {
                SchoolSkill skill = new SchoolSkill();
                skill.setSchoolId(schools[i]);
                skill.setName(element.child(0).text().replaceAll("[　]", ""));
                
                if ("技能名称".equals(skill.getName())) {
                    return null;
                }
                
                skill.setDescription(element.child(1).text());
                
                String text = element.child(2).text();
                Set<String> skills = new HashSet<String>();
                
                if (text.length() > 0) {
                    text = text.replaceAll("[、， ]", ",");
                    String[] s = text.split(",");
                    for (String sk : s) {
                        skills.add(sk.replaceAll("[　 ]", ""));
                    }
                }
                skill.setIncludeSkills(skills);
                if (!skill.getName().isEmpty()) {
                    return skill;
                }
            }
            return null;
        }
    };
    
    @Override
    public List<SchoolSkill> parse() throws IOException {
        List<SchoolSkill> schoolSkills = new ArrayList<SchoolSkill>();
        for (i = 1; i <= 12; i++) {
            String url = String.format("http://xyq.163.com/mp0%02d.html", i);
            try {
                resolve(url, css1, schoolSkills, RESOLVE);
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println(url);
            }
        }
        
        for (i = 13; i <= 15; i++) {
            String url = String.format("http://xyq.163.com/mp0%02d.html", i);
            try {
                resolve(url, css2, schoolSkills, RESOLVE);
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println(url);
            }
        }
        
        return schoolSkills;
    }
    
}
