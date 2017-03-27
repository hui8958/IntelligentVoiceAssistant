package edu.hui.vassistant.entities;

public class DetailWeather extends DetailEntity {
	private String Icon = "";
	private String Temperature = "";
	private String DayInfo = "";
	private String condition = "";
	private String wind = "";
	private String Humidity = "";
	private String cityName = "";

	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	public DetailWeather(String Icon, String Temperature, String DayInfo,
			String condition,String Humidity, String wind,String cityName,int layoutID) {
		this.Icon = Icon;
		this.Temperature = Temperature;
		this.DayInfo = DayInfo;
		this.condition = condition;
		this.wind = wind;
		this.Humidity = Humidity;
		super.layoutID = layoutID;
		this.cityName = cityName;
	}

	public String getWind() {
		return wind;
	}

	public void setWind(String wind) {
		this.wind = wind;
	}

	public String getHumidity() {
		return Humidity;
	}

	public void setHumidity(String humidity) {
		Humidity = humidity;
	}

	public String getIcon() {
		return Icon;
	}

	public void setIcon(String icon) {
		Icon = icon;
	}

	public String getTemperature() {
		return Temperature;
	}

	public void setTemperature(String temperature) {
		Temperature = temperature;
	}

	public String getDayInfo() {
		return DayInfo;
	}

	public void setDayInfo(String dayInfo) {
		DayInfo = dayInfo;
	}

	public String getCondition() {
		return condition;
	}

	public void setCondition(String condition) {
		this.condition = condition;
	}

	public String getDetailName() {
		// TODO Auto-generated method stub
		return "weather";
	}
	
}
