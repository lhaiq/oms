package com.maxent.oms.utils;

import java.util.HashMap;

public class MapX extends HashMap<String, Object> {

	private static final long serialVersionUID = 1L;

	public String getString(String key) {
		Object val = this.get(key);
		if (val == null) {
			return null;
		}

		return String.valueOf(this.get(key));
	}

	public Integer getInteger(String key) {
		Object val = this.get(key);
		if (val == null) {
			return null;
		}

		if (val instanceof Number) {
			return ((Number) val).intValue();
		}

		return Integer.parseInt(val.toString());
	}

}
