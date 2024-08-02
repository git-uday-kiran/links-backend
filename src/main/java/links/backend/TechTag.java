package links.backend;

import com.fasterxml.jackson.annotation.JsonValue;

enum TechTag {
	C, CPP,
	JAVA, SPRING, SPRING_BOOT,
	PYTHON,
	RUST,
	HTML, CSS, JAVASCRIPT, REACT,
	AI;

	@Override
	@JsonValue
	public String toString() {
		return name().toLowerCase();
	}

}
