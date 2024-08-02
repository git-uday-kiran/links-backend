package links.backend;

import com.fasterxml.jackson.annotation.JsonValue;

enum Importance {
	STRONGLY_RECOMMENDED, CHECKOUT, IGNORE;

	@Override
	@JsonValue
	public String toString() {
		return name().toLowerCase();
	}
}
