package com.devefx.crawler.model;

public enum EquipType {
	
	枪矛(1, "枪矛"), 斧钺(2, "斧钺"), 剑(3, "剑"), 双短剑(4, "双短剑"), 飘带(5, "飘带"), 爪刺(6, "爪刺"),
	扇(7, "扇"), 魔棒(8, "魔棒"), 锤(9, "锤"), 鞭(10, "锤"), 环圈(11, "环圈"), 刀(12, "刀");
	
	private int id;
	private String name;
	
	private EquipType(int id, String name) {
		this.id = id;
		this.name = name;
	}
	public int getId() {
		return id;
	}
	public String getName() {
		return name;
	}
	public static EquipType valueOf(int id) {
		for (EquipType type : EquipType.values()) {
			if (type.id == id)
				return type;
		}
		return null;
	}
}
