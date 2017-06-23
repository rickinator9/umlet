package com.baselet.element.old.relation;

import com.baselet.control.basics.geom.Rectangle;

public class TextRectangle extends Rectangle {
	String string;

	public String getString() {
		return string;
	}

	public TextRectangle(String s, int a, int b, int c, int d) {
		super(a, b, c, d);
		string = s;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + (string == null ? 0 : string.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!super.equals(obj)) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		TextRectangle other = (TextRectangle) obj;
		if (getString() == null) {
			if (other.getString() != null) {
				return false;
			}
		}
		else if (!getString().equals(other.getString())) {
			return false;
		}
		return true;
	}
}