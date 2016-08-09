package com.unicen.app.ahp;


public class Decision implements Comparable<Decision> {
	private String schoolName;
	private Integer schoolId;
	private double probability;
	
	public Decision (Integer schoolId, String schoolName,double probability) {
		this.setSchoolId(schoolId);
		this.setSchoolName(schoolName);
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

	@Override
	public int compareTo(Decision o) {
		if (this.getProbability() < o.getProbability())
			return -1;
		if (this.getProbability() > o.getProbability())
			return 1;
		return 0;
	}

	public Integer getSchoolId() {
		return schoolId;
	}

	public void setSchoolId(Integer schoolId) {
		this.schoolId = schoolId;
	}
	
	
	
	
}
