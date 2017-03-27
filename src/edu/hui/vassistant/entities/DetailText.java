package edu.hui.vassistant.entities;

public class DetailText extends DetailEntity {

	private String text;

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public DetailText(String name, String text, int layoutID) {

		super.name = name;
		this.text = text;
		super.layoutID = layoutID;
	}

	public String getDetailName() {
		return "text";
	}
}
