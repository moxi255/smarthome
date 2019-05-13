package com.smarthome.app.model;

public class Type 
{
	private String name;
	private int imageId;
	private String equipmentId;
	
	public String getEquipmentId() {
		return equipmentId;
	}

	public void setEquipmentId(String equipmentId) {
		this.equipmentId = equipmentId;
	}

	public Type(String name, int imageId) {
		this.name = name;
		this.imageId = imageId;
	}
	
	public Type(String name, int imageId, String equipmentId) {
		this.name = name;
		this.imageId = imageId;
		this.equipmentId = equipmentId;
	}
	
	public String getName() {
		return name;
	}
	
	public int getImageId() {
		return imageId;
	}
}
