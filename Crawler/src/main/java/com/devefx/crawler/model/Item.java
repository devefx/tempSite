package com.devefx.crawler.model;

public class Item {
	private int id;
	private String name;
	private String description;
	private String effect;
	private int price;
	private ItemType type;
	private int useTiming;	// 0 、无法使用 1、任何时候使用 2、平时状态下使用 3、平时状态下使用（召唤兽专用） 4、战斗时使用
	
	public Item() {
	}
	public Item(String name, String description, String effect, int price, ItemType type, int useTiming) {
		this.name = name;
		this.description = description;
		this.effect = effect;
		this.price = price;
		this.type = type;
		this.useTiming = useTiming;
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
	public String getEffect() {
		return effect;
	}
	public void setEffect(String effect) {
		this.effect = effect;
	}
	public int getPrice() {
		return price;
	}
	public void setPrice(int price) {
		this.price = price;
	}
	public ItemType getType() {
		return type;
	}
	public void setType(ItemType type) {
		this.type = type;
	}
	public int getUseTiming() {
		return useTiming;
	}
	public void setUseTiming(int useTiming) {
		this.useTiming = useTiming;
	}
}
