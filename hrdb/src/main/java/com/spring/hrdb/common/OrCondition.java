package com.spring.hrdb.common;


public class OrCondition extends JunctionCondition {

	public OrCondition(Condition... conditions) {
		super(conditions);
	}

	@Override
	protected String getJunctionString() {
		return "or";
	}

}
