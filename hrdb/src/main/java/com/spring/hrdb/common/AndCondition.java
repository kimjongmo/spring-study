package com.spring.hrdb.common;

public class AndCondition extends JunctionCondition {

	public AndCondition(Condition... conditions) {
		super(conditions);
	}

	@Override
	protected String getJunctionString() {
		return "and";
	}

}
