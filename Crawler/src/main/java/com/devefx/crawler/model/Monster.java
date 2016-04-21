package com.devefx.crawler.model;

public class Monster {
	private int id;
	private String name;
	private int carryLevel;
	private int useLevel;
	
	public Monster() {
	}
	public Monster(String name, int carryLevel) {
		this.name = name;
		this.carryLevel = carryLevel;
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
	public int getCarryLevel() {
		return carryLevel;
	}
	public void setCarryLevel(int carryLevel) {
		this.carryLevel = carryLevel;
	}
	public int getUseLevel() {
		return useLevel;
	}
	public void setUseLevel(int useLevel) {
		this.useLevel = useLevel;
	}
}
