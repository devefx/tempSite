package com.devefx.gamedata.mybatis;

import java.io.IOException;
import java.io.Reader;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

public class Mybatis {
	
	private SqlSessionFactory sqlSessionFactory;
	
	public boolean init(String configName) {
		Reader reader = null;
		try {
			reader = Resources.getResourceAsReader(configName);
			this.sqlSessionFactory = new SqlSessionFactoryBuilder().build(reader);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				reader.close();
			} catch (IOException e) {
			}
		}
		return false;
	}
	
	public SqlSessionFactory getSqlSessionFactory() {
		return sqlSessionFactory;
	}
}
