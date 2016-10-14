package com.devefx.crawler.model;

import java.util.List;

public class Monster {
    
    public static final int LEVEL = 0;
    public static final int FLYUP = 1;
    public static final int LEVEL_OR_FLYUP = 2;
    public static final int LEVEL_AND_FLYUP = 3;
    public static final int KALPA = 4;
    
	private int id;
	private String name;
	private String description;
    private int levelCarry;
    private int levelWar;
    private int condition;
	private List<String> skills;
	private Integer[] aptitudes;
	
	public Monster() {
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
        return description;
    }
	public void setDescription(String description) {
        this.description = description;
    }
    public int getLevelCarry() {
        return levelCarry;
    }
    public void setLevelCarry(int levelCarry) {
        this.levelCarry = levelCarry;
    }
    public int getLevelWar() {
        return levelWar;
    }
    public void setLevelWar(int levelWar) {
        this.levelWar = levelWar;
    }
    public int getCondition() {
        return condition;
    }
    public void setCondition(int condition) {
        this.condition = condition;
    }
	public List<String> getSkills() {
        return skills;
    }
	public void setSkills(List<String> skills) {
        this.skills = skills;
    }
	public Integer[] getAptitudes() {
        return aptitudes;
    }
	public void setAptitudes(Integer[] aptitudes) {
        this.aptitudes = aptitudes;
    }
}
