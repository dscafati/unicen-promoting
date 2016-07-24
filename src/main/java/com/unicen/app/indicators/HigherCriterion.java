package com.unicen.app.indicators;

public class HigherCriterion implements Criterion{
	public boolean isBetter(double value1, double value2) {
		return (value1>value2);
	}
}
