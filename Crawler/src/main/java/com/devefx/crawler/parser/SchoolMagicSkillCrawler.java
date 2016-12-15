package com.devefx.crawler.parser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;

import com.devefx.crawler.core.Crawler;
import com.devefx.crawler.core.Resolve;
import com.devefx.crawler.model.SchoolMagic;

public class SchoolMagicSkillCrawler extends Crawler<SchoolMagic> {

    private static final String css1 = ".l_table tbody tr";
    
    private static final String css2 = ".tablepad tbody tr";
    
    private static final Resolve<SchoolMagic> RESOLVE = new Resolve<SchoolMagic>() {
        @Override
        public SchoolMagic process(Element element) {
            if (element.children().size() == 2) {
                
                SchoolMagic schoolMagic = new SchoolMagic();
                schoolMagic.setName(element.child(0).text());
                
                if ("法术名称".equals(schoolMagic.getName())) {
                    return null;
                }
                
                Element ele = element.child(1);
                
                for (Node node : ele.childNodes()) {
                    if (node instanceof TextNode) {
                        String text = node.toString().trim().replaceAll("&nbsp;", "");
                        if (text.contains("功效")) {
                            schoolMagic.setDescription(text.replaceAll("功效：", ""));
                        } else if (text.contains("使用条件")) {
                            Pattern pattern = Pattern.compile("技能(等级)?达到 ?(\\d+) ?级");
                            Matcher matcher = pattern.matcher(text);
                            if (matcher.find()) {
                                schoolMagic.setSkillLevel(Integer.parseInt(matcher.group(2)));
                            } else {
                                System.err.println("==========================================================================");
                            }
                            
                            pattern = Pattern.compile("人物等级达到(\\d+)级");
                            matcher = pattern.matcher(text);
                            if (matcher.find()) {
                                schoolMagic.setRoleLevel(Integer.parseInt(matcher.group(1)));
                                schoolMagic.setRequireType(1);
                            } else {
                                if (text.contains("人物飞升")) {
                                    schoolMagic.setRequireType(2);
                                } else {
                                    if (text.contains("人物")) {
                                        System.err.println("==========================================================================");
                                    }
                                }
                            }
                            //System.out.println(text);
                        } else if (text.contains("消耗")) {
                            text = text.replaceAll("(消耗：| )", "");
                            schoolMagic.setCostDescription(text);
                            
                            Pattern pattern = Pattern.compile("(\\d+)点(魔法|MP)");
                            Matcher matcher = pattern.matcher(text);
                            if (matcher.find()) {
                                schoolMagic.setMpCost(Integer.parseInt(matcher.group(1)));
                            }
                            
                            pattern = Pattern.compile("(\\d+)点(气血|HP)");
                            matcher = pattern.matcher(text);
                            if (matcher.find()) {
                                schoolMagic.setHpCost(Integer.parseInt(matcher.group(1)));
                            }
                            
                            pattern = Pattern.compile("(\\d+)点(愤怒|SP)");
                            matcher = pattern.matcher(text);
                            if (matcher.find()) {
                                schoolMagic.setSpCost(Integer.parseInt(matcher.group(1)));
                            }
                            
                            if (Pattern.matches("^(\\d+)点(气血|HP)和\\d+点?(魔法|MP)", text) ||
                                    Pattern.matches("^(\\d+)点(气血|魔法|愤怒值|HP|MP|SP)。?", text)) {
                                schoolMagic.setCostType(0);
                                schoolMagic.setCostDescription("");
                            } else if (text.contains("非战斗时减半")) {
                                schoolMagic.setCostType(1);
                            } else if (text.contains("相同的活力")) {
                                schoolMagic.setCostType(2);
                            } else if (text.contains("/") || text.contains("作用人数") || text.contains("消耗增加")) {
                                schoolMagic.setCostType(3);
                            } else {
                                schoolMagic.setCostType(4);
                            }
                        }
                        //System.out.println(text);
                    }
                }
                //System.out.println();
                return schoolMagic;
            }
            return null;
        }
    };

    @Override
    public List<SchoolMagic> parse() throws IOException {
        List<SchoolMagic> magics = new ArrayList<SchoolMagic>();
        
        for (int i = 1; i <= 12; i++) {
            String url = String.format("http://xyq.163.com/mp0%02d.html", i);
            try {
                resolve(url, css1, magics, RESOLVE);
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println(url);
            }
        }
        
        for (int i = 13; i <= 15; i++) {
            String url = String.format("http://xyq.163.com/mp0%02d.html", i);
            try {
                resolve(url, css2, magics, RESOLVE);
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println(url);
            }
        }
        
        return magics;
    }
}
