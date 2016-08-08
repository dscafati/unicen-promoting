package com.unicen.app.ahp;

public interface Criterion {
	/*
	 * It must return true if value1 is better than value2
	 */
	public boolean isBetter (double value1, double value2);
}
