package com.unicen.app.ahp;

import com.unicen.app.ahp.Criterion;

public class LowerCriterion implements Criterion {
	public boolean isBetter(double value1, double value2) {
		return (value1<value2);
	}
}
