package com.unicen.app.indicators;


public class Decision implements Comparable {
	private String school;
	private double probability;
	
	public Decision (String school, double probability) {
		this.setSchool(school);
		this.setProbability(probability);
	}

	public String getSchool() {
		return school;
	}

	public void setSchool(String school) {
		this.school = school;
	}

	public double getProbability() {
		return probability;
	}

	public void setProbability(double probability) {
		this.probability = probability;
	}

	@Override
	public int compareTo(Object o) {
		Decision other = (Decision)o;
		if (this.getProbability() < other.getProbability())
			return -1;
		if (this.getProbability() > other.getProbability())
			return 1;
		return 0;
	}
	
	
	
	
}
