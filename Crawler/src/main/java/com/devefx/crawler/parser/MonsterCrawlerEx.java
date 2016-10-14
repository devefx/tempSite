package com.devefx.crawler.parser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.devefx.crawler.core.Crawler;
import com.devefx.crawler.core.Resolve;
import com.devefx.crawler.model.Monster;

public class MonsterCrawlerEx extends Crawler<Monster> {
    
    private static final String cssQuert = ".bd table";
            
    private static final Resolve<Monster> RESOLVE = new Resolve<Monster>() {
        @Override
        public Monster process(Element element) {
            String name = element.getElementsByClass("th1").text();
            String description = element.getElementsByClass("petInfo").text();
            
            if (name.endsWith("））") || (name.endsWith("）") && !name.contains("（"))) {
                name = name.substring(0, name.length() - 1);// 修正名称补“）”
            }
            if (name.endsWith("（兽）")) {
                name = name.replace("兽", "兽形");
            }
            if (name.endsWith("机关人（人）")) {
                name = name.replace("（人）", "");
            }
            if (name.endsWith("（人）")) {
                name = name.replace("人", "人型");
            }
            if (name.endsWith("狂豹（人型）")) {
                name = name.replace("人型", "仙族人型");
            }
            if (name.endsWith("蔓藤妖花")) {
                name = name.replace("蔓藤", "藤蔓");
            }
            if (name.endsWith("猫灵（兽形）") || name.endsWith("狂豹（兽形）")) {
                name = name.replace("兽形", "兽型");
            }
            
            Monster monster = new Monster();
            monster.setName(name.substring(2));
            monster.setDescription(description.substring(3));
            
            Elements elements = element.select(".petAbility ul li");
            List<String> skills = new ArrayList<String>(elements.size());
            for (Element e : elements) {
                String skill = e.text();
                if (skill.equals("高驱驱鬼")) {
                    skill = "高级驱鬼";
                } else if (skill.equals("水吸收")) {
                    skill = "水属性吸收";
                } else if (skill.equals("高级水性吸收")) {
                    skill = "高级水属性吸收";
                } else if (skill.equals("奔雷咒/li>")) {
                    skill = "奔雷咒";
                } else if (skill.equals("高级水系吸收")) {
                    skill = "高级水属性吸收";
                } else if (skill.equals("冥想")){
                    skill = "冥思";
                }
                skills.add(skill);
            }
            monster.setSkills(skills);
            
            try {
                List<Integer> aptitudes = new ArrayList<Integer>(6);
                elements = element.select(".petQuality ul li");
                for (Element e : elements) {
                    String value = e.text().substring(5);
                    aptitudes.add(Integer.parseInt(value));
                }
                monster.setAptitudes(aptitudes.toArray(new Integer[0]));
            } catch (Exception ex) {// 修正龟丞相数据
                monster.setAptitudes(new Integer[] { 1020, 1440, 5820, 1980, 900, 1140 });
            }
            return monster;
        }
    };
    
    @Override
    public List<Monster> parse() throws IOException {
        List<Monster> list = new ArrayList<Monster>();
        for (String url : new URLCrawler().parse()) {
            resolve(url, cssQuert, list, RESOLVE);
        }
        return list;
    }
    
    class URLCrawler extends Crawler<String> {
        
        private final String URL = "http://xyq.yzz.cn/special/tj/index.shtml";
        private final String cssQuert = "ul.clearfix li a";
        
        private final Resolve<String> RESOLVE = new Resolve<String>() {
            @Override
            public String process(Element element) {
                return "http://xyq.yzz.cn/special/tj/" + element.attr("href");
            }
        };
        
        @Override
        public List<String> parse() throws IOException {
            List<String> list = new ArrayList<String>();
            resolve(URL, cssQuert, list, RESOLVE);
            return list;
        }
    }
    
    
}
