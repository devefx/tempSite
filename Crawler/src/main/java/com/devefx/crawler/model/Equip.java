package com.devefx.crawler.model;

public class Equip {
	private int id;
	private String name;
	private String description;
	private int level;
	private EquipType type;
	
	public Equip() {
	}
	public Equip(String name, String description, int level, EquipType type) {
		this.name = name;
		this.description = description;
		this.level = level;
		this.type = type;
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
	public int getLevel() {
		return level;
	}
	public void setLevel(int level) {
		this.level = level;
	}
	public EquipType getType() {
		return type;
	}
	public void setType(EquipType type) {
		this.type = type;
	}
}
