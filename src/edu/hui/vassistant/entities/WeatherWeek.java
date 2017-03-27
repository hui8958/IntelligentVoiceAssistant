package edu.hui.vassistant.entities;

import java.util.ArrayList;

public class WeatherWeek extends DetailEntity {
	ArrayList<String> days = new ArrayList<String>();
	ArrayList<String> icons = new ArrayList<String>();
	ArrayList<String> lts = new ArrayList<String>();
	ArrayList<String> hts = new ArrayList<String>();
	ArrayList<String> conditions = new ArrayList<String>();

	public WeatherWeek(ArrayList<String> days, ArrayList<String> icons,
			ArrayList<String> lts, ArrayList<String> hts,
			ArrayList<String> conditions, int layoutID) {
		// super();
		this.days = days;
		this.icons = icons;
		this.lts = lts;
		this.hts = hts;
		this.conditions = conditions;
		super.layoutID = layoutID;
	}

	public ArrayList<String> getDays() {
		return days;
	}

	public void setDays(ArrayList<String> days) {
		this.days = days;
	}

	public ArrayList<String> getIcons() {
		return icons;
	}

	public void setIcons(ArrayList<String> icons) {
		this.icons = icons;
	}

	public ArrayList<String> getLts() {
		return lts;
	}

	public void setLts(ArrayList<String> lts) {
		this.lts = lts;
	}

	public ArrayList<String> getHts() {
		return hts;
	}

	public void setHts(ArrayList<String> hts) {
		this.hts = hts;
	}

	public ArrayList<String> getConditions() {
		return conditions;
	}

	public void setConditions(ArrayList<String> conditions) {
		this.conditions = conditions;
	}

	public String getDetailName() {
		return "weatherweek";
	}

}
