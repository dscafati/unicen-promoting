package com.unicen.app.ahp;


public class Decision implements Comparable<Decision> {
	private String schoolName;
	private String cityName;
	private Integer schoolId;
	private double probability;
	
	public Decision (Integer schoolId, String schoolName, String cityName, double probability) {
		this.setSchoolId(schoolId);
		this.setSchoolName(schoolName);
		this.setCityName(cityName);
		this.setProbability(probability);
	}

	public String getSchoolName() {
		return schoolName;
	}

	public void setSchoolName(String schoolName) {
		this.schoolName = schoolName;
	}

	public double getProbability() {
		return probability;
	}

	public void setProbability(double probability) {
		this.probability = probability;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}
	public String getCityName() {
		return this.cityName;
	}

	@Override
	public int compareTo(Decision o) {
		if (this.getProbability() < o.getProbability())
			return 1;
		if (this.getProbability() > o.getProbability())
			return -1;
		return 0;
	}

	public Integer getSchoolId() {
		return schoolId;
	}

	public void setSchoolId(Integer schoolId) {
		this.schoolId = schoolId;
	}
	
	
	
	
}
