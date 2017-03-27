package edu.hui.vassistant.entities;

public class DetailEntity {
    protected String name;
    protected String date;
    protected int layoutID;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	
	public int getLayoutID() {
		return layoutID;
	}
	public void setLayoutID(int layoutID) {
		this.layoutID = layoutID;
	}
    
	public DetailEntity() {
	}
	
	public String getDetailName(){
		return "Main";
	}
	
    
}
