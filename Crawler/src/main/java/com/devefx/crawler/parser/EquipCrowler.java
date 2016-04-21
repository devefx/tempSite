package com.devefx.crawler.parser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.nodes.Element;

import com.devefx.crawler.core.Crawler;
import com.devefx.crawler.core.Resolve;
import com.devefx.crawler.model.Equip;

public class EquipCrowler extends Crawler<Equip> {

	/**
	 * 武器
	 */
	private static final String qiangMao		= "http://xyq.163.com/introduce/dj004.html";
	private static final String fuYue			= "http://xyq.163.com/dj004_2.html";
	private static final String jian			= "http://xyq.163.com/dj004_3.html";
	private static final String shuangDuanjian	= "http://xyq.163.com/dj004_4.html";
	private static final String piaoDai			= "http://xyq.163.com/dj004_5.html";
	private static final String zhuaCi			= "http://xyq.163.com/dj004_6.html";
	private static final String shan			= "http://xyq.163.com/dj004_7.html";
	private static final String moBang			= "http://xyq.163.com/dj004_8.html";
	private static final String chui			= "http://xyq.163.com/dj004_9.html";
	private static final String bian			= "http://xyq.163.com/dj004_10.html";
	private static final String huanQuan		= "http://xyq.163.com/dj004_11.html";
	private static final String dao				= "http://xyq.163.com/dj004_12.html";
	private static final String faZhang			= "http://xyq.163.com/2013/8/22/4999_389401.html";
	private static final String baoZhu			= "http://xyq.163.com/2013/8/22/4999_389415.html";
	private static final String gongNu			= "http://xyq.163.com/2013/8/22/4999_389427.html";
	/**
	 * 防具
	 */
	private static final String touKui			= "http://xyq.163.com/introduce/dj005.html";
	private static final String kaiJia			= "http://xyq.163.com/dj005_2.html";
	private static final String xieZi			= "http://xyq.163.com/dj005_3.html";
	private static final String yaoDai			= "http://xyq.163.com/dj005_4.html";
	private static final String shiPin			= "http://xyq.163.com/dj005_5.html";
	
	private static final String cssQuery = ".l_table tbody tr";
	
	private static final Resolve<Equip> RESOLVE = new Resolve<Equip>() {
		@Override
		public Equip process(Element element) {
			String level = element.child(3).text();
			for (int i = level.indexOf("-"); i != -1;) {
				return new Equip(element.child(1).text(), element.child(2).text(),
						Integer.parseInt(level.substring(0, i)), null);
			}
			return new Equip(element.child(1).text(), element.child(2).text(),
					Integer.parseInt(element.child(3).text()), null);
		}
	};
	
	@Override
	public List<Equip> parse() throws IOException {
		List<Equip> equips = new ArrayList<Equip>();
		
		resolve(qiangMao, cssQuery, equips, RESOLVE);
		resolve(fuYue, cssQuery, equips, RESOLVE);
		resolve(jian, cssQuery, equips, RESOLVE);
		resolve(shuangDuanjian, cssQuery, equips, RESOLVE);
		resolve(piaoDai, cssQuery, equips, RESOLVE);
		resolve(zhuaCi, cssQuery, equips, RESOLVE);
		resolve(shan, cssQuery, equips, RESOLVE);
		resolve(moBang, cssQuery, equips, RESOLVE);
		resolve(chui, cssQuery, equips, RESOLVE);
		resolve(bian, cssQuery, equips, RESOLVE);
		resolve(huanQuan, cssQuery, equips, RESOLVE);
		resolve(dao, cssQuery, equips, RESOLVE);
		resolve(faZhang, cssQuery, equips, RESOLVE);
		resolve(baoZhu, cssQuery, equips, RESOLVE);
		resolve(gongNu, cssQuery, equips, RESOLVE);
		
		resolve(touKui, cssQuery, equips, RESOLVE);
		resolve(kaiJia, cssQuery, equips, RESOLVE);
		resolve(xieZi, cssQuery, equips, RESOLVE);
		resolve(yaoDai, cssQuery, equips, RESOLVE);
		resolve(shiPin, cssQuery, equips, RESOLVE);
		return equips;
	}

}
