package com.devefx.gamedata.parser;

import java.io.File;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.ibatis.session.SqlSession;

import com.devefx.gamedata.io.BufferReader;
import com.devefx.gamedata.model.Resource;
import com.devefx.gamedata.model.ResourceType;
import com.devefx.gamedata.mybatis.Mybatis;

public class WDFParser {
	
	private static int id = 1;
	private static Mybatis mybatis = new Mybatis();
	
	class Member {
		int id;
		int offset;
		int length;
		int space;
		ResourceType type;
		
		public Member(BufferReader input) throws IOException {
			this.id = input.readInt();
			this.offset = input.readInt();
			this.length = input.readInt();
			this.space = input.readInt();
			input.mark();
			input.seekg(offset, BufferReader.BEG);
			this.type = ResourceType.valueOf(assertType(input));
			input.reset();
		}
		
		// FFD8 FFE0 0010 4A46 4946 0001 ==> JPEG
		// FF2F ==> MP3
		// 5249 4646 ==> WAV
		// 5053 ==> WAS
		// 424D ==> BMP
		public String assertType(BufferReader input) throws IOException {
			short t = input.readShort();
			switch (t) {
			case 0x5053:
				return "WAS";
			case 0x4D42:
				return "BMP";
			case 0x4952:
				return "WAV";
			case 0xFFFFD8FF:
				return "JPEG";
			case 0xFFFFF2FF:
				return "MP3";
			}
			return "UNKNOWN";
		}
		
		@Override
		public String toString() {
			String hex = Integer.toHexString(id);
			for (int i = hex.length(); i < 8; i++)
				hex = "0" + hex;
			return type + ":" + hex;
		}
	}
	
	
	
	public void open(String filename) throws IOException {
		File file = new File(filename);
		InputStream is = new FileInputStream(file);
		BufferReader di = new BufferReader(is);

		int flag = di.readInt();
		if (flag == 1464092240) {
			int number = di.readInt();
			int offset = di.readInt();
			
			List<Member> members = new ArrayList<>();
			
			List<Resource> resources = new ArrayList<Resource>();
			
			di.seekg(offset, BufferReader.BEG);
			for (int i = 0; i < number; i++) {
				Member member = new Member(di);
				members.add(member);
				
				Resource resource = new Resource();
				resource.setFilename(file.getName());
				resource.setCode(member.id);
				resource.setType(member.type);
				if (!resources.contains(resource)) {
					resource.setId(id ++);
					resources.add(resource);
				}
			}
			
			/*SqlSession session = mybatis.getSqlSessionFactory().openSession();
			try {
				session.insert("insertResourceBatch", resources);
				session.commit();
			} catch (Exception e) {
				e.printStackTrace();
				session.rollback();
			} finally {
				session.close();
			}*/
			
			System.out.println(1);
		}
		
		di.close();
		is.close();
	}
	
	public static void main(String[] args) {
		//mybatis.init("mybatis-config.xml");
		
		final WDFParser parser = new WDFParser();
		
		File dir = new File("F:\\梦幻西游");
		if (dir.isDirectory()) {
			dir.list(new FilenameFilter() {
				@Override
				public boolean accept(File dir, String name) {
					if (Pattern.matches(".+\\.wd[a-z0-9]", name)) {
						try {
							parser.open(dir.getPath() + File.separator + name);
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
					return false;
				}
			});
		}
		System.out.println("over");
	}
	
}
